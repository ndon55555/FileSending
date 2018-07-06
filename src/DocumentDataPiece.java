import java.io.FileOutputStream;
import java.io.IOException;

// Represents a contiguous subset of a document (a piece of some DocumentData).
public class DocumentDataPiece extends AbstractDocumentData {
    private static final Object LOCK = new Object();

    private byte[] data;

    DocumentDataPiece(String partialPathName, String pathName, byte[] data) {
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
}
