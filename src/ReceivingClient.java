import java.io.EOFException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Scanner;

public class ReceivingClient {
    public static void main(String[] args) {
        final int PORT = 10001;
        final String HOSTNAME = "donraspberrypi.ddns.net";
        final String DOWNLOAD_PATH = "C:/Users/Don/Desktop";

        boolean shouldContinue = true;
        Scanner usrInput = new Scanner(System.in);

        while (shouldContinue) {
            try (Socket server = new Socket(HOSTNAME, PORT);
                 ObjectOutputStream toServer = new ObjectOutputStream(server.getOutputStream());
                 ObjectInputStream fromServer = new ObjectInputStream(server.getInputStream())) {

                System.out.println("Connected to server.");
                toServer.writeObject(ClientType.RECEIVER);
                toServer.flush();
                System.out.println("Sent client type to server.");
                System.out.print("Enter user path for file extraction: ");
                String targetPath = usrInput.nextLine();
                toServer.writeUTF(targetPath);
                toServer.flush();
                System.out.println("Sent desired pathname: " + targetPath);

                while (true) {
                    IFileData fileData = (IFileData) fromServer.readObject();
                    fileData.writeTo(DOWNLOAD_PATH);
                    System.out.println("Received " + fileData.toString());
                }
            } catch (EOFException eofe) {
                System.out.println("No more data from sending client.");
            } catch (Exception e) {
                e.printStackTrace();
                shouldContinue = false;
            }
        }
    }
}
