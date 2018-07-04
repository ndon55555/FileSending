import java.io.File;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.file.Files;

public class ReceivingClient {
    private static final Object LOCK = new Object();

    public static void main(String[] args) {
        final int PORT = 10001;
        final String HOSTNAME = "donraspberrypi.ddns.net";
        final String DOWNLOAD_PATH = "C:/Users/Don/Desktop";

        try (Socket server = new Socket(HOSTNAME, PORT);
             ObjectInputStream fromServer = new ObjectInputStream(server.getInputStream())) {

            System.out.println("Connected to server.");
            new PrintWriter(server.getOutputStream(), true).println("Downloads");
            System.out.println("Sent desired pathname.");

            while (true) {
                IFileData fileData = (IFileData) fromServer.readObject();
                File target = new File(DOWNLOAD_PATH + "/" + fileData.getPartialPathName());

                if (fileData.getFile().isDirectory()) {
                    Files.createDirectory(target.toPath());
                } else { // otherwise, it's a document
                    FileOutputStream toDoc = new FileOutputStream(target, true);

                    synchronized (LOCK) {
                        toDoc.write(((DocumentData) fileData).getData());
                    }
                }

                System.out.println("Received file: " + fileData.getFile().getName() + ".");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
