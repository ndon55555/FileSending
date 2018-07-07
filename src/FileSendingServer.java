import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class FileSendingServer {
    public static void main(String[] args) throws IOException {
        final int PORT = 10001;

        boolean shouldContinue = true;

        while (shouldContinue) {
            ServerSocket server = null;
            Socket client1 = null;
            Socket client2 = null;
            ObjectOutputStream oos1 = null;
            ObjectOutputStream oos2 = null;
            ObjectInputStream ois1 = null;
            ObjectInputStream ois2 = null;

            try {
                server = new ServerSocket(PORT);

                // Establish connections with first client
                client1 = server.accept();
                oos1 = new ObjectOutputStream(client1.getOutputStream());
                oos1.flush();
                ois1 = new ObjectInputStream(client1.getInputStream());

                // Establish connections with second client
                client2 = server.accept();
                oos2 = new ObjectOutputStream(client2.getOutputStream());
                oos2.flush();
                ois2 = new ObjectInputStream(client2.getInputStream());

                ClientType type1 = (ClientType) ois1.readObject();
                ClientType type2 = (ClientType) ois2.readObject();
                Socket receivingClient = null;
                Socket sendingClient = null;
                ObjectOutputStream toReceiver = null;
                ObjectOutputStream toSender = null;
                ObjectInputStream fromReceiver = null;
                ObjectInputStream fromSender = null;

                switch (type1) {
                    case SENDER:
                        sendingClient = client1;
                        toSender = oos1;
                        fromSender = ois1;
                        break;
                    case RECEIVER:
                        receivingClient = client1;
                        toReceiver = oos1;
                        fromReceiver = ois1;
                        break;
                }

                switch (type2) {
                    case SENDER:
                        if (sendingClient != null) {
                            throw new RuntimeException("Cannot have two sending clients.");
                        } else {
                            sendingClient = client2;
                            toSender = oos2;
                            fromSender = ois2;
                        }

                        break;
                    case RECEIVER:
                        if (receivingClient != null) {
                            throw new RuntimeException("Cannot have two receiving clients.");
                        } else {
                            receivingClient = client2;
                            toReceiver = oos2;
                            fromReceiver = ois2;
                        }

                        break;
                }

                System.out.println("Both clients connected.");
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
                System.out.println("Terminating session.");
            } catch (Exception e) {
                e.printStackTrace();
                shouldContinue = false;
            } finally {
                Utilities.closeAll(server, client1, client2, oos1, oos2, ois1, ois2);
            }
        }
    }
}
