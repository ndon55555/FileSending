import java.io.File;

public abstract class AbstractFileData extends File implements IFileData {
    private String partialPathName;

    // constructor
    AbstractFileData(String partialPathName, String pathName) {
        super(pathName);
        this.partialPathName = partialPathName;
    }

    // Returns a pathname that may be anywhere from the absolute path name to simply the file name.
    @Override
    public String getPartialPathName() {
        return this.partialPathName;
    }
}
