import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.nio.file.Files;
import java.util.Stack;

public class SendingClient {
    public static void main(String[] args) {
        final int PORT = 10001;
        final String HOSTNAME = "donraspberrypi.ddns.net";

        boolean shouldContinue = true;

        while (shouldContinue) {
            try (Socket server = new Socket(HOSTNAME, PORT);
                 ObjectOutputStream toServer = new ObjectOutputStream(server.getOutputStream());
                 ObjectInputStream fromServer = new ObjectInputStream(server.getInputStream())) {

                System.out.println("Connected to server.");
                toServer.writeObject(ClientType.SENDER);
                toServer.flush();
                System.out.println("Sent client type to server.");
                String userPath = fromServer.readUTF();
                System.out.println("Received desired path: " + userPath);
                sendAllInPath(userPath, toServer);
                System.out.println("Sent copied file(s).");
            } catch (Exception e) {
                e.printStackTrace();
                shouldContinue = false;
            }
        }
    }

    // Sends all the files in the given user path through the given output stream.
    private static void sendAllInPath(String userPath, ObjectOutputStream toServer) throws IOException {
        String absPath = "C:/Users/" + System.getProperty("user.name") + "/" + userPath;
        File root = new File(absPath);

        if (Files.exists(root.toPath())) {
            Stack<DirectoryData> toSend = new Stack<>();
            handleFile(root.getName(), root, toSend, toServer);

            while (!toSend.isEmpty()) {
                DirectoryData f = toSend.pop();
                toServer.writeObject(f);
                toServer.flush();
                toServer.reset();
                File[] subFiles = f.listFiles();

                if (subFiles != null) {
                    for (File subFile : subFiles) {
                        String partialPathName = f.getPartialPathName() + "/" + subFile.getName();
                        handleFile(partialPathName, subFile, toSend, toServer);
                    }
                }
            }
        }
    }

    // Determines if a given file should be added to the stack or immediately sent through the stream.
    private static void handleFile(String partialPathName, File f, Stack<DirectoryData> toSend, ObjectOutputStream toServer) throws IOException {
        if (f.isDirectory()) {
            toSend.push(new DirectoryData(partialPathName, f.getCanonicalPath()));
        } else {
            DocumentData doc = new DocumentData(partialPathName, f.getCanonicalPath());

            // sending the document in pieces
            for (DocumentPiece piece : doc.getPieces()) {
                toServer.writeObject(piece);
                toServer.flush();
                toServer.reset();
            }
        }
    }
}