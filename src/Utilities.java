import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

public class Utilities {
    public static int ARRAY_SIZE_UPPER_LIMIT = 2000000;

    /*
    Returns a list of the byte arrays of the given file. The list has more than
    one element if the file's size exceeds the ARRAY_SIZE_UPPER_LIMIT. No elements
    if the file is empty.
     */
    public static List<byte[]> obtainBytes(File f) {
        List<byte[]> result = new LinkedList<>();
        long remainingBytes = f.length();

        try (FileInputStream in = new FileInputStream(f)) {
            while (remainingBytes > ARRAY_SIZE_UPPER_LIMIT) {
                byte[] b = new byte[ARRAY_SIZE_UPPER_LIMIT];
                in.read(b);
                result.add(b);
                remainingBytes -= ARRAY_SIZE_UPPER_LIMIT;
            }

            byte[] b = new byte[(int) remainingBytes];
            in.read(b);
            result.add(b);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return result;
    }
}
