import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class Document extends AbstractFileData {
    private byte[] data;

    // constructor
    Document(String partialPathName, String rootPathName) throws IOException {
        this(partialPathName, new File(rootPathName));
    }

    // constructor
    Document(String partialPathName, File root) throws IOException{
        super(partialPathName, root);
        this.data = Files.readAllBytes(root.toPath());
    }

    @Override
    public void copyTo(String destination) throws IOException {
        File destFile = new File(destination);

        if (destFile.isDirectory()) {
            Files.write(new File(destination + "/" + this.getPartialPathName()).toPath(), this.data);
        } else {
            throw new IllegalArgumentException("Cannot place document into non-directory.");
        }
    }
}
