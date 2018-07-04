import java.io.File;
import java.util.List;

// Represents an entire document (contains all disjoint subsets of its bytes).
public class DocumentPieces {
    private List<byte[]> pieces;

    // constructor
    DocumentPieces(File f) {
        this.pieces = Utilities.obtainBytes(f);
    }

    // Returns a list of all byte arrays needed to represent this document.
    public List<byte[]> getPieces() {
        return this.pieces;
    }
}
