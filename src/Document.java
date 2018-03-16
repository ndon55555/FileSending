import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class Document implements IFileTree {
    private File root;
    private byte[] data;

    // constructor
    Document(String rootPathName) throws IOException {
        this(new File(rootPathName));
    }

    // constructor
    Document(File root) throws IOException {
        this.root = root;
        this.data = Files.readAllBytes(this.root.toPath());
    }

    @Override
    public void copyTo(String destination) throws IOException {
        File destFile = new File(destination);

        if (destFile.isDirectory()) {
            Files.write(new File(destination + "/" + this.root.getName()).toPath(), this.data);
        } else {
            throw new IllegalArgumentException("Cannot place document into non-directory.");
        }
    }
}
