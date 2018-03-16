import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Scanner;

public class SendingClient {
    public static void main(String[] args) {
        final int PORT = 10001;
        final String HOSTNAME = "donraspberrypi.ddns.net";

        try (Socket server = new Socket(HOSTNAME, PORT)) {
            System.out.println("Connected to server.");
            String userFolder = new Scanner(server.getInputStream()).nextLine();
            System.out.println("Received desired path.");
            IFileTree copiedFiles = new Directory("C:/Users/" + System.getProperty("user.name") + "/" + userFolder);
            new ObjectOutputStream(server.getOutputStream()).writeObject(copiedFiles);
            System.out.println("Sent copied files.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
