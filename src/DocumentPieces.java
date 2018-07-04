import java.io.File;
import java.util.List;

public class DocumentPieces {
    private List<byte[]> pieces;

    // constructor
    DocumentPieces(File f) {
        this.pieces = Utilities.obtainBytes(f);
    }

    public List<byte[]> getPieces() {
        return this.pieces;
    }
}
