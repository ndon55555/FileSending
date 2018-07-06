import java.io.File;
import java.io.FileNotFoundException;

public class FileDataFactory {
    // Returns the appropriate implementation of IFileData based on the file at the given path.
    public static IFileData getFileData(String canonPathName) throws FileNotFoundException {
        File f = new File(canonPathName);

        return getFileData(f.getName(), canonPathName);
    }

    public static IFileData getFileData(String partialPathName, String canonPathName) throws FileNotFoundException {
        File f = new File(canonPathName);

        if (f.exists()) {
            if (f.isDirectory()) {
                return new DirectoryData(partialPathName, canonPathName);
            } else {
                return new DocumentData(partialPathName, canonPathName);
            }
        } else {
            throw new FileNotFoundException();
        }
    }
}
