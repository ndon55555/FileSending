import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.LinkedList;
import java.util.List;

// Represents a directory.
class DirectoryData extends AbstractFileData {
    // constructor
    DirectoryData(String partialPathName, String pathName) {
        super(partialPathName, pathName);
    }

    // Tries to create this directory at the given path.
    @Override
    public void writeTo(String destination) throws IOException {
        Files.createDirectory(new File(destination + "/" + this.getPartialPathName()).toPath());
    }

    @Override
    public boolean isDirectory() {
        return true;
    }

    @Override
    public List<IFileData> getSubFiles() {
        List<IFileData> result = new LinkedList<>();
        File[] subFiles = this.listFiles();

        if (subFiles != null) {
            for (File subFile : subFiles) {
                String childPartialPathName = this.getPartialPathName() + "/" + subFile.getName();

                try {
                    IFileData f = FileDataFactory.getFileData(childPartialPathName, subFile.getCanonicalPath());
                    result.add(f);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return result;
    }

    @Override
    public String toString() {
        return "directory " + this.getName();
    }
}
