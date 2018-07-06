import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
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
        String canonPathName = "C:/Users/" + System.getProperty("user.name") + "/" + userPath;
        IFileData root;

        try {
            root = FileDataFactory.getFileData(canonPathName);
        } catch (FileNotFoundException e) {
            return;
        }

        Stack<DirectoryData> toSend = new Stack<>();
        handleFile(root, toSend, toServer);

        while (!toSend.isEmpty()) {
            DirectoryData f = toSend.pop();
            toServer.writeObject(f);
            toServer.flush();
            toServer.reset();

            for (IFileData subFile : f.getSubFiles()) {
                handleFile(subFile, toSend, toServer);
            }
        }
    }

    // Determines if a given file should be added to the stack or immediately sent through the stream.
    private static void handleFile(IFileData f, Stack<DirectoryData> toSend, ObjectOutputStream toServer) throws IOException {
        if (f.isDirectory()) {
            DirectoryData dir = (DirectoryData) f;
            toSend.push(dir);
        } else {
            DocumentData doc = (DocumentData) f;
            // sending the document in pieces
            for (DocumentDataPiece piece : doc.getPieces()) {
                toServer.writeObject(piece);
                toServer.flush();
                toServer.reset();
            }
        }
    }
}