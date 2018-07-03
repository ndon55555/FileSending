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

    private static void sendAllInPath(String userPath, ObjectOutputStream toServer) throws IOException {
        String absPath = "C:/Users/" + System.getProperty("user.name") + "/" + userPath;
        File root = new File(absPath);

        if (Files.exists(root.toPath())) {
            Stack<IFileData> toSend = new Stack<>();

            if (root.isDirectory()) {
                toSend.push(new Directory(root.getName(), root));
            } else {
                try {
                    toServer.writeObject(new Document(root.getName(), root));
                } catch (OutOfMemoryError e) {
                    System.err.println("Unable to process document: " + root.getName() + ".");
                }
            }

            while (!toSend.empty()) {
                IFileData f = toSend.pop();
                toServer.writeObject(f);
                File[] subFiles = f.getFile().listFiles();

                if (subFiles != null) {
                    for (File subFile : subFiles) {
                        String pathName = f.getPartialPathName() + "/" + subFile.getName();

                        if (subFile.isDirectory()) {
                            toSend.push(new Directory(pathName, subFile));
                        } else {
                            try {
                                toServer.writeObject(new Document(pathName, subFile));
                            } catch (OutOfMemoryError e) {
                                System.err.println("Out of memory. Unable to process document: " + subFile.getName() + ".");
                            }
                        }
                    }
                }
            }
        }
    }
}
