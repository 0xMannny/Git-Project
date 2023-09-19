import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;

public class GitTester {
    public static void main(String[] args) throws FileNotFoundException, NoSuchAlgorithmException, IOException {
        Index index = new Index();
        index.init();
        File file = new File("try.txt");
        index.add(file);
        File file2 = new File("try2.txt");
        index.add(file2);
        Tree t = new Tree();
        t.initialize();
        t.addToTree("blob : b444ac06613fc8d63795be9ad0beaf55011936ac : try.txt");
        t.addToTree("hello");
        t.save();

    }
}
