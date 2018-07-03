import java.io.File;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.nio.file.Files;
import java.util.Scanner;
import java.util.Stack;

public class SendingClient {
    public static void main(String[] args) {
        final int PORT = 10001;
        final String HOSTNAME = "donraspberrypi.ddns.net";

        try (Socket server = new Socket(HOSTNAME, PORT);
             ObjectOutputStream toServer = new ObjectOutputStream(server.getOutputStream())) {

            System.out.println("Connected to server.");
            String userPath = new Scanner(server.getInputStream()).nextLine();
            System.out.println("Received desired path.");
            sendAllInPath(userPath, toServer);
            System.out.println("Sent copied file(s).");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Sends all the files in the given user path through the given output stream.
    private static void sendAllInPath(String userPath, ObjectOutputStream toServer) throws IOException {
        String absPath = "C:/Users/" + System.getProperty("user.name") + "/" + userPath;
        File root = new File(absPath);

        if (Files.exists(root.toPath())) {
            Stack<IFileData> toSend = new Stack<>();
            handleFile(root.getName(), root, toSend, toServer);

            while (!toSend.isEmpty()) {
                IFileData f = toSend.pop();
                toServer.writeObject(f);
                File[] subFiles = f.getFile().listFiles();

                if (subFiles != null) {
                    for (File subFile : subFiles) {
                        String pathName = f.getPartialPathName() + "/" + subFile.getName();
                        handleFile(pathName, subFile, toSend, toServer);
                    }
                }
            }
        }
    }

    // Determines if a given file should be added to the stack or immediately sent through the stream.
    private static void handleFile(String pathName, File f, Stack<IFileData> toSend, ObjectOutputStream toServer) {
        if (f.isDirectory()) {
            toSend.push(new DirectoryData(pathName, f));
        } else {
            try {
                toServer.writeObject(new DocumentData(pathName, f));
            } catch (IOException e) {
                e.printStackTrace();
            } catch (OutOfMemoryError ooe) {
                System.err.println("Out of memory. Unable to process document: " + f.getName() + ".");
            }
        }
    }
}
