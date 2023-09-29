import java.io.File;
import java.nio.file.Paths;

public class GitTester {
    public static void main(String[] args) throws Exception {
        File directory = Paths.get("./advancedTest").toFile();

        Tree tree = new Tree();
        tree.initialize();

        tree.addDirectory(directory);
    }
}
