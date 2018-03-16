import java.io.ObjectInputStream;
import java.io.PrintWriter;
import java.net.Socket;

public class ReceivingClient {
    public static void main(String[] args) {
        final int PORT = 10001;
        final String HOSTNAME = "donraspberrypi.ddns.net";

        try (Socket server = new Socket(HOSTNAME, PORT)) {
            System.out.println("Connected to server.");
            new PrintWriter(server.getOutputStream(), true).println("Pictures");
            System.out.println("Sent desired pathname.");
            IFileTree copiedFiles = (Directory) new ObjectInputStream(server.getInputStream()).readObject();
            System.out.println("Received copies of files.");
            copiedFiles.copyTo("C:/Users/Don/Desktop");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
