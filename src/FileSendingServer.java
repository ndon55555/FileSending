import java.io.EOFException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class FileSendingServer {
    public static void main(String[] args) {
        final int PORT = 10001;

        boolean shouldContinue = true;

        while (shouldContinue) {
            try (ServerSocket server = new ServerSocket(PORT);
                 Socket client1 = server.accept();
                 Socket client2 = server.accept();
                 ObjectOutputStream oos1 = new ObjectOutputStream(client1.getOutputStream());
                 ObjectOutputStream oos2 = new ObjectOutputStream(client2.getOutputStream());
                 ObjectInputStream ois1 = new ObjectInputStream(client1.getInputStream());
                 ObjectInputStream ois2 = new ObjectInputStream(client2.getInputStream())
            ) {

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
                String targetUserPath = fromReceiver.readUTF();
                System.out.println("Receiver wants " + targetUserPath);
                toSender.writeUTF(targetUserPath);
                toSender.flush();

                while (true) {
                    toReceiver.writeObject(fromSender.readObject());
                    toReceiver.flush();
                    toReceiver.reset();
                }
            } catch (EOFException eofe) {
                System.out.println("One of the clients has disconnected.");
            } catch (Exception e) {
                e.printStackTrace();
                shouldContinue = false;
            }
        }
    }
}
