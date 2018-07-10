import java.io.IOException;
import java.net.ServerSocket;

public class FileSendingServer {
    public static void main(String[] args) {
        final int PORT = 10001;

        boolean shouldContinue = true;

        while (shouldContinue) {
            try (ServerSocket server = new ServerSocket(PORT)){
                FileSendingProtocol fsp = new FileSendingProtocol();

                while (!fsp.hasReceiver() || !fsp.hasSender()) {
                    fsp.assignToEitherRole(server.accept());
                }

                System.out.println("Both clients connected.");
                System.out.println("Running file sending protocol.");
                fsp.run();
            } catch (IOException e) {
                e.printStackTrace();
                shouldContinue = false;
            }
        }
    }
}
