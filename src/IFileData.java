import java.io.File;
import java.io.IOException;
import java.io.Serializable;

public interface IFileData extends Serializable {
    String getPartialPathName();
    File getFile();
    void writeTo(String destination) throws IOException;
}
