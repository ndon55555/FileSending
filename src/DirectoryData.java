import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class DirectoryData extends AbstractFileData {
    // constructor
    DirectoryData(String partialPathName, File root) {
        super(partialPathName, root);
    }

    // Creates a copy of this directory at the given path string.
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
