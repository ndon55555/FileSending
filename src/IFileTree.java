import java.io.IOException;
import java.io.Serializable;

public interface IFileTree extends Serializable {
    void copyTo(String destination) throws IOException;
}
