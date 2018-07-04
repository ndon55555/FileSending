import java.io.File;

public class DocumentData extends AbstractFileData {
    private byte[] data;

    // constructor
    DocumentData(String partialPathName, File file, byte[] data) {
        super(partialPathName, file);
        this.data = data;
    }

    // Returns the bytes of this document.
    public byte[] getData() {
        return this.data;
    }
}
