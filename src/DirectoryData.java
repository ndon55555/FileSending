import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

// Represents a directory.
class DirectoryData extends AbstractFileData {
    // constructor
    DirectoryData(String partialPathName, File root) {
        super(partialPathName, root);
    }

    // Tries to create this directory at the given path.
    @Override
    public void writeTo(String destination) throws IOException {
        Files.createDirectory(new File(destination + "/" + this.getPartialPathName()).toPath());
    }
}
