import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

// Represents an entire document.
public class DocumentData extends AbstractDocumentData {
    // constructor
    DocumentData(String partialPathName, String pathName) {
        super(partialPathName, pathName);
    }

    // Returns a list of all DocumentPieces used to represent this document.
    public List<DocumentDataPiece> getPieces() throws IOException {
        List<byte[]> bytes = Utilities.obtainBytes(new File(this.getCanonicalPath()));
        List<DocumentDataPiece> result = new LinkedList<>();

        for (byte[] arr : bytes) {
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
