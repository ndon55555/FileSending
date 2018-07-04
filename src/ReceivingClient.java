import java.io.EOFException;
import java.io.ObjectInputStream;
import java.io.PrintWriter;
import java.net.Socket;

public class ReceivingClient {
    public static void main(String[] args) {
        final int PORT = 10001;
        final String HOSTNAME = "donraspberrypi.ddns.net";
        final String DOWNLOAD_PATH = "C:/Users/Don/Desktop";

        try (Socket server = new Socket(HOSTNAME, PORT);
             ObjectInputStream fromServer = new ObjectInputStream(server.getInputStream())) {

            System.out.println("Connected to server.");
            new PrintWriter(server.getOutputStream(), true).println("Pictures");
            System.out.println("Sent desired pathname.");

            while (true) {
                IFileData fileData = (IFileData) fromServer.readObject();
                fileData.writeTo(DOWNLOAD_PATH);
                System.out.println("Received " + fileData.toString());
            }
        } catch (EOFException eofe) {
            System.out.println("Disconnected from server.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
