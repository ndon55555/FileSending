import java.io.File;

// Represents a file's information, including the file itself.
public abstract class AbstractFileData implements IFileData {
    private String partialPathName;
    private File file;

    // constructor
    AbstractFileData(String partialPathName, File file) {
        this.partialPathName = partialPathName;
        this.file = file;
    }

    // Returns a pathname that may be anywhere from the absolute path name to simply the file name.
    @Override
    public String getPartialPathName() {
        return this.partialPathName;
    }

    // Returns the File representation of this file data.
    @Override
    public File getFile() {
        return this.file;
    }
}
