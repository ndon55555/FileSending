import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

// Represents a contiguous subset of a file's bytes.
public class DocumentData extends AbstractFileData {
    private static final Object LOCK = new Object();

    private byte[] data;

    // constructor
    DocumentData(String partialPathName, File file, byte[] data) {
        super(partialPathName, file);
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
