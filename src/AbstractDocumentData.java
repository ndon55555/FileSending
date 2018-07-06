public abstract class AbstractDocumentData extends AbstractFileData {
    AbstractDocumentData(String partialPathName, String pathName) {
        super(partialPathName, pathName);
    }

    @Override
    public String toString() {
        return "document " + this.getName();
    }
}
