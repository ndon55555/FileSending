import java.util.LinkedList;
import java.util.List;

public abstract class AbstractDocumentData extends AbstractFileData {
    AbstractDocumentData(String partialPathName, String pathName) {
        super(partialPathName, pathName);
    }

    @Override
    public boolean isDirectory() {
        return false;
    }

    @Override
    public List<IFileData> getSubFiles() {
        return new LinkedList<>();
    }

    public long getByteSize() {
        return this.length();
    }

    @Override
    public String toString() {
        return "document " + this.getName();
    }
}
