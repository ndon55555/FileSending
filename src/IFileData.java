import java.io.IOException;
import java.io.Serializable;
import java.util.List;

public interface IFileData extends Serializable {
    String getPartialPathName();
    void writeTo(String destination) throws IOException;
    List<IFileData> getSubFiles();
    boolean isDirectory();
}
