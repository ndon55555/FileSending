import java.io.File;
import java.io.Serializable;

public interface IFileData extends Serializable {
    String getPartialPathName();
    File getFile();
}
