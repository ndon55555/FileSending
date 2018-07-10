import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class FileSendingProtocol implements Runnable {
    private Socket receivingClient;
    private Socket sendingClient;
    private ObjectOutputStream toReceiver;
    private ObjectOutputStream toSender;
    private ObjectInputStream fromReceiver;
    private ObjectInputStream fromSender;

    // constructor
    FileSendingProtocol() {
        this.receivingClient = null;
        this.sendingClient = null;
        this.toReceiver = null;
        this.toSender = null;
        this.fromReceiver = null;
        this.fromSender = null;
    }

    boolean hasReceiver() {
        return this.receivingClient != null;
    }

    boolean hasSender() {
        return this.sendingClient != null;
    }

    void assignToEitherRole(Socket client) {
        ObjectOutputStream toClient;
        ObjectInputStream fromClient;

        // not using try-with-resources since streams shouldn't close unless client is of a duplicate type
        try {
            toClient = new ObjectOutputStream(client.getOutputStream());
            fromClient = new ObjectInputStream(client.getInputStream());
            // for some reason, if output stream to client isn't flushed, then program gets stuck reading from client
            toClient.flush();
            ClientType type = (ClientType) fromClient.readObject();

            switch (type) {
                case SENDER:
                    if (this.hasSender()) {
                        System.out.println("Cannot have two sending clients.");
                        Utilities.closeAll(client, fromClient, toClient);
                    } else {
                        this.sendingClient = client;
                        this.toSender = toClient;
                        this.fromSender = fromClient;
                    }

                    break;
                case RECEIVER:
                    if (this.hasReceiver()) {
                        System.out.println("Cannot have two receiving clients.");
                        Utilities.closeAll(client, fromClient, toClient);
                    } else {
                        this.receivingClient = client;
                        this.toReceiver = toClient;
                        this.fromReceiver = fromClient;
                    }

                    break;
            }
        } catch (ClassNotFoundException cnfe) {
            System.err.println("Make sure classes used are up to date.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        try {
            FileDataRequest request = (FileDataRequest) fromReceiver.readObject();
            System.out.println("Received request from receiver.");
            Utilities.writeFlushResetObject(toSender, request);
            System.out.println("Forwarded request to sender.");

            boolean continueSending = true;

            while (continueSending) {
                Object o = fromSender.readObject();
                Utilities.writeFlushResetObject(toReceiver, o);
                continueSending = !(o instanceof FileSendingTerminator);
            }

            fromReceiver.readObject(); // blocking on receiver before receiving EOFException and terminating session
        } catch (EOFException eofe) {
            System.out.println("Ending session.");
        } catch (ClassNotFoundException cnfe) {
            System.err.println("Make sure classes used are up to date.");
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            Utilities.closeAll(
                    this.receivingClient, this.sendingClient, this.fromReceiver,
                    this.fromSender, this.toReceiver, this.toSender
            );
        }
    }
}
