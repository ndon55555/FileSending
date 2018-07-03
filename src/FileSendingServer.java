import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class FileSendingServer {
    public static void main(String[] args) {
        final int PORT = 10001;

        try (ServerSocket server = new ServerSocket(PORT);
             Socket receivingClient = server.accept();
             Socket sendingClient = server.accept();

             ObjectOutputStream toReceiver = new ObjectOutputStream(receivingClient.getOutputStream());
             ObjectInputStream fromSender = new ObjectInputStream(sendingClient.getInputStream())) {

            System.out.println("Both clients connected.");
            String targetUserPath = new Scanner(receivingClient.getInputStream()).nextLine();
            new PrintWriter(sendingClient.getOutputStream(), true).println(targetUserPath);

            boolean isReading = true;

            while (isReading) {
                try {
                    toReceiver.writeObject(fromSender.readObject());
                    System.out.println("Received a file and forwarded it.");
                } catch (Exception e) {
                    isReading = false;
                }
            }

            System.out.println("Requested file(s) sent.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
