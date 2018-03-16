import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.LinkedList;
import java.util.List;

public class Directory implements IFileTree {
    private File root;
    private List<IFileTree> children;

    // constructor
    Directory(String rootPathName) throws IOException {
        this(new File(rootPathName));
    }

    // constructor
    Directory(File root) throws IOException {
        this.root = root;
        this.children = new LinkedList<>();

        if (this.root.listFiles() != null) {
            for (File f : this.root.listFiles()) {
                if (f.isFile()) {
                    this.children.add(new Document(f));
                } else {
                    this.children.add(new Directory(f));
                }
            }
        }
    }

    @Override
    public void copyTo(String destination) throws IOException {
        File destFile = new File(destination);

        if (destFile.isDirectory()) {
            String target = destination + "/" + this.root.getName();
            Files.createDirectory(new File(target).toPath());

            for (IFileTree fileTree : this.children) {
                fileTree.copyTo(target);
            }
        } else {
            System.out.println(destination);
            throw new IllegalArgumentException("Cannot place directory into non-directory.");
        }
    }
}
