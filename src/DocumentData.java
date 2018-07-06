import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

// Represents an entire document (contains all disjoint subsets of its bytes).
public class DocumentData extends AbstractDocumentData {
    private List<byte[]> pieces;

    // constructor
    DocumentData(String partialPathName, String pathName) {
        super(partialPathName, pathName);
        this.pieces = Utilities.obtainBytes(new File(pathName));
    }

    // Returns a list of all DocumentPieces used to represent this document.
    public List<DocumentDataPiece> getPieces() throws IOException {
        List<DocumentDataPiece> result = new LinkedList<>();

        for (byte[] arr : pieces) {
            result.add(new DocumentDataPiece(this.getPartialPathName(), this.getCanonicalPath(), arr));
        }

        return result;
    }

    @Override
    public void writeTo(String destination) throws IOException {
        for (DocumentDataPiece piece : this.getPieces()) {
            piece.writeTo(destination);
        }
    }
}
