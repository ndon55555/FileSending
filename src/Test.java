import java.io.File;
import java.io.IOException;

public class Test {
    public static void main(String[] args) throws IOException {
        IFileTree fileTree = new Directory("C:/Users/Don/Pictures");
        fileTree.copyTo("C:/Users/Don/Desktop");
    }
}
