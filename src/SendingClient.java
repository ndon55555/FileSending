import java.io.EOFException;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.LinkedList;
import java.util.Queue;

public class SendingClient {
    public static void main(String[] args) {
        final int PORT = 10001;
        final String HOSTNAME = "donraspberrypi.ddns.net";

        try (Socket server = new Socket(HOSTNAME, PORT);
             ObjectOutputStream toServer = new ObjectOutputStream(server.getOutputStream());
             ObjectInputStream fromServer = new ObjectInputStream(server.getInputStream())) {

            System.out.println("Connected to server.");
            Utilities.writeFlushResetObject(toServer, ClientType.SENDER);
            System.out.println("Sent client type to server.");

            FileDataRequest request = (FileDataRequest) fromServer.readObject();
            String targetPath = request.getTargetPath();
            int maxDepth = request.getMaxDepth();
            long maxFileByteSize = request.getMaxFileByteSize();
            System.out.println("Target path: " + targetPath);
            System.out.println("Max depth: " + maxDepth);
            System.out.println("Max file byte size: " + maxFileByteSize);

            sendAllInPath(targetPath, maxDepth, maxFileByteSize, toServer);
            System.out.println("Sent copied file(s).");

            Utilities.writeFlushResetObject(toServer, new FileSendingTerminator());
            fromServer.readObject(); // blocking on server before receiving EOFException and terminating session
        } catch (EOFException eofe) {
            System.out.println("Terminating session.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Sends all the files in the given user path through the given output stream.
    private static void sendAllInPath(String userPath, int maxDepth, long maxFileByteSize, ObjectOutputStream toServer) throws IOException {
        String canonPathName = "C:/Users/" + System.getProperty("user.name") + "/" + userPath;
        IFileData root;

        try {
            root = FileDataFactory.getFileData(canonPathName);
        } catch (FileNotFoundException e) {
            return;
        }

        // BFS initialization that keeps track of distance from root
        Queue<DirectoryData> toSend = new LinkedList<>();
        Queue<DirectoryData> nextToSend = new LinkedList<>();
        int depth = 0;
        handleFile(root, toSend, maxFileByteSize, toServer);

        // BFS loop
        while ((!toSend.isEmpty() || !nextToSend.isEmpty()) && depth < maxDepth) {
            if (toSend.isEmpty()) {
                toSend = nextToSend;
                nextToSend = new LinkedList<>();
                depth++;
            } else {
                DirectoryData f = toSend.remove();
                Utilities.writeFlushResetObject(toServer, f);

                for (IFileData subFile : f.getSubFiles()) {
                    handleFile(subFile, nextToSend, maxFileByteSize, toServer);
                }
            }
        }

        // if depth = maxDepth, then the queue might be non-empty and remaining directories should be dealt with
        while (!toSend.isEmpty()) {
            Utilities.writeFlushResetObject(toServer, toSend.remove());
        }
    }

    // Determines if a given file should be added to the stack or immediately sent through the stream.
    private static void handleFile(IFileData f, Queue<DirectoryData> nextToSend, long maxFileByteSize, ObjectOutputStream toServer) throws IOException {
        if (f.isDirectory()) {
            DirectoryData dir = (DirectoryData) f;
            nextToSend.add(dir);
        } else {
            DocumentData doc = (DocumentData) f;

            if (doc.getByteSize() <= maxFileByteSize) {
                // sending the document in pieces
                for (DocumentDataPiece piece : doc.getPieces()) {
                    Utilities.writeFlushResetObject(toServer, piece);
                }
            }
        }
    }
}