import java.io.ObjectInputStream;
import java.io.PrintWriter;
import java.net.Socket;

public class ReceivingClient {
    public static void main(String[] args) {
        final int PORT = 10001;
        final String HOSTNAME = "donraspberrypi.ddns.net";

        try (Socket server = new Socket(HOSTNAME, PORT);
             ObjectInputStream fromServer = new ObjectInputStream(server.getInputStream())) {

            System.out.println("Connected to server.");
            new PrintWriter(server.getOutputStream(), true).println("Pictures/Huh");
            System.out.println("Sent desired pathname.");

            boolean isReading = true;

            while (isReading) {
                try {
                    IFileData fileData = (IFileData) fromServer.readObject();
                    fileData.copyTo("C:/Users/Don/Desktop");
                    System.out.println("File received.");
                } catch (Exception e) {
                    isReading = false;
                }
            }

            System.out.println("Received copies of files.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
