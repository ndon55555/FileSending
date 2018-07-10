import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Scanner;

public class ReceivingClient {
    public static void main(String[] args) {
        final int PORT = 10001;
        final String HOSTNAME = "donraspberrypi.ddns.net";
        final String DOWNLOAD_PATH = "C:/Users/Don/Desktop";

        Scanner usrInput = new Scanner(System.in);
        System.out.println("Connecting to server...");

        try (Socket server = new Socket(HOSTNAME, PORT);
             ObjectOutputStream toServer = new ObjectOutputStream(server.getOutputStream());
             ObjectInputStream fromServer = new ObjectInputStream(server.getInputStream())) {

            System.out.println("Connected to server.");
            toServer.writeObject(ClientType.RECEIVER);
            toServer.flush();
            System.out.println("Sent client type to server.");
            FileDataRequest request = getUserRequest(usrInput);
            Utilities.writeFlushResetObject(toServer, request);
            System.out.println("Sent user request.");

            boolean continueReceiving = true;

            while (continueReceiving) {
                Object o = fromServer.readObject();

                if (o instanceof IFileData) {
                    IFileData fileData = (IFileData) o;
                    fileData.writeTo(DOWNLOAD_PATH);
                    System.out.println("Received " + fileData.toString());
                } else { // o instanceof FileSendingTerminator
                    continueReceiving = false;
                }
            }

            System.out.println("No more data from sending client.");
            System.out.println("Terminating session.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static FileDataRequest getUserRequest(Scanner usrInput) {
        String targetPath = getUserTargetPath(usrInput);
        int maxDepth = getUserMaxDepth(usrInput);
        long maxFileByteSize = getUserMaxFileByteSize(usrInput);

        return new FileDataRequest(targetPath, maxDepth, maxFileByteSize);
    }

    private static String getUserTargetPath(Scanner usrInput) {
        System.out.print("Enter target path: ");

        return usrInput.nextLine();
    }

    private static int getUserMaxDepth(Scanner usrInput) {
        System.out.print("Limit depth? (Y/N): ");
        String response = usrInput.nextLine().toUpperCase();

        while (!response.equals("Y") && !response.equals("N")) {
            System.out.print("Try again. Limit depth? (Y/N): ");
            response = usrInput.nextLine().toUpperCase();
        }

        if (response.equals("Y")) {
            System.out.print("Set depth limit: ");

            return Integer.parseInt(usrInput.nextLine());
        } else {
            return Integer.MAX_VALUE;
        }
    }

    private static long getUserMaxFileByteSize(Scanner usrInput) {
        System.out.print("Limit file byte size? (Y/N): ");
        String response = usrInput.nextLine().toUpperCase();

        while (!response.equals("Y") && !response.equals("N")) {
            System.out.print("Try again. Limit file byte size? (Y/N): ");
            response = usrInput.nextLine().toUpperCase();
        }

        if (response.equals("Y")) {
            System.out.print("Set file byte size limit: ");

            return Long.parseLong(usrInput.nextLine());
        } else {
            return Long.MAX_VALUE;
        }
    }
}
