import java.io.Serializable;

class FileDataRequest implements Serializable {
    private String targetPath;
    private int maxDepth;
    private long maxFileByteSize;

    FileDataRequest(String targetPath, int maxDepth, long maxFileByteSize) {
        this.targetPath = targetPath;
        this.maxDepth = maxDepth;
        this.maxFileByteSize = maxFileByteSize;
    }

    String getTargetPath() {
        return this.targetPath;
    }

    int getMaxDepth() {
        return this.maxDepth;
    }

    long getMaxFileByteSize() {
        return this.maxFileByteSize;
    }
}
