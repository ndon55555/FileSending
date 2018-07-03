import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class Directory extends AbstractFileData {
    // constructor
    Directory(String partialPathName, String rootPathName) {
        this(partialPathName, new File(rootPathName));
    }

    // constructor
    Directory(String partialPathName, File root) {
        super(partialPathName, root);
    }

    @Override
    public void copyTo(String destination) throws IOException {
        File destFile = new File(destination);

        if (destFile.isDirectory()) {
            String target = destination + "/" + this.getPartialPathName();
            Files.createDirectory(new File(target).toPath());
        } else {
            System.out.println(destination);
            throw new IllegalArgumentException("Cannot place directory into non-directory.");
        }
    }
}
