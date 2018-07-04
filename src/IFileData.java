import java.io.IOException;
import java.io.Serializable;

public interface IFileData extends Serializable {
    String getPartialPathName();
    void writeTo(String destination) throws IOException;
}
