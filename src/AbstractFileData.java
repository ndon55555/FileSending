import java.io.File;

public abstract class AbstractFileData implements IFileData {
    private String partialPathName;
    private File root;

    // constructor
    AbstractFileData(String partialPathName, File root) {
        this.partialPathName = partialPathName;
        this.root = root;
    }

    @Override
    public String getPartialPathName() {
        return this.partialPathName;
    }

    @Override
    public File getFile() {
        return this.root;
    }
}
