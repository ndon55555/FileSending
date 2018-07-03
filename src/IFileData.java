import java.io.File;
import java.io.IOException;
import java.io.Serializable;

public interface IFileData extends Serializable {
    void copyTo(String destination) throws IOException;
    String getPartialPathName();
    File getFile();
}
