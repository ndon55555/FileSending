import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class FileSendingServer {
    public static void main(String[] args) {
        final int PORT = 10001;

        try (ServerSocket server = new ServerSocket(PORT)) {
            Socket receivingClient = server.accept();
            String userFolder = new Scanner(receivingClient.getInputStream()).nextLine();
            System.out.println(userFolder);

            Socket sendingClient = server.accept();
            System.out.println("Both clients connected.");
            new PrintWriter(sendingClient.getOutputStream(), true).println(userFolder);
            Object copiedFiles = new ObjectInputStream(sendingClient.getInputStream()).readObject();
            new ObjectOutputStream(receivingClient.getOutputStream()).writeObject(copiedFiles);
            System.out.println("Done");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
