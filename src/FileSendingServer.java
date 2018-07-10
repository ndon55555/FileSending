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
            Socket receivingClient = null;
            Socket sendingClient = null;
            ObjectOutputStream toReceiver = null;
            ObjectOutputStream toSender = null;
            ObjectInputStream fromReceiver = null;
            ObjectInputStream fromSender = null;

            try {
                server = new ServerSocket(PORT);

                while (receivingClient == null || sendingClient == null) {
                    Socket client = server.accept();
                    ObjectOutputStream toClient = new ObjectOutputStream(client.getOutputStream());
                    toClient.flush();
                    ObjectInputStream fromClient = new ObjectInputStream(client.getInputStream());
                    ClientType type = (ClientType) fromClient.readObject();

                    switch (type) {
                        case SENDER:
                            if (sendingClient != null) {
                                System.out.println("Cannot have two sending clients.");
                                Utilities.closeAll(client, toClient, fromClient);
                            } else {
                                sendingClient = client;
                                toSender = toClient;
                                fromSender = fromClient;
                            }

                            break;
                        case RECEIVER:
                            if (receivingClient != null) {
                                System.out.println("Cannot have two receiving clients.");
                                Utilities.closeAll(client, toClient, fromClient);
                            } else {
                                receivingClient = client;
                                toReceiver = toClient;
                                fromReceiver = fromClient;
                            }

                            break;
                    }
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
                Utilities.closeAll(server, receivingClient, sendingClient, fromReceiver, toReceiver, fromSender, toSender);
            }
        }
    }
}
