import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;

public class GitTester {
    public static void main(String[] args) throws FileNotFoundException, NoSuchAlgorithmException, IOException {
        ABlob blob = new ABlob();
        File file = new File("try.txt");
        blob.blobFile(file);
    }
}
