import java.io.FileOutputStream;
import java.io.IOException;

// Represents a contiguous subset of a document.
public class DocumentPiece extends AbstractFileData {
    private static final Object LOCK = new Object();

    private byte[] data;

    DocumentPiece(String partialPathName, String pathName, byte[] data) {
        super(partialPathName, pathName);
        this.data = data;
    }

    // Writes/appends this file's bytes to the given path.
    @Override
    public void writeTo(String destination) throws IOException {
        FileOutputStream toDoc = new FileOutputStream(destination + "/" + this.getPartialPathName(), true);

        synchronized (LOCK) {
            toDoc.write(this.data);
        }
    }

    @Override
    public boolean isDirectory() {
        return false;
    }
}
