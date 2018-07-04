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
             ObjectInputStream fromSender = new ObjectInputStream(sendingClient.getInputStream());

             Scanner receiverTarget = new Scanner(receivingClient.getInputStream());
             PrintWriter targetToReceiver = new PrintWriter(sendingClient.getOutputStream(), true)) {

            System.out.println("Both clients connected.");
            String targetUserPath = receiverTarget.nextLine();
            targetToReceiver.println(targetUserPath);
            //receiverTarget.close();
            //targetToReceiver.close();

            while (true) {
                toReceiver.writeObject(fromSender.readObject());
                toReceiver.flush();
                toReceiver.reset();
                System.out.println(Runtime.getRuntime().freeMemory());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
