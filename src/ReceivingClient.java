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
            new PrintWriter(server.getOutputStream(), true).println("Downloads");
            System.out.println("Sent desired pathname.");

            while (true) {
                IFileData fileData = (IFileData) fromServer.readObject();
                fileData.writeTo(DOWNLOAD_PATH);
                System.out.println("Received file: " + fileData.getName());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
