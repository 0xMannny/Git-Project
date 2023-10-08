import static org.junit.Assert.*;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.LinkedHashMap;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class TreeTest {

    private Tree tree;
    private static String fileToTest = "fileToTest.txt";

    @BeforeEach
    public void setUp() throws IOException {
        File file = new File(fileToTest);
        file.createNewFile();

        BufferedWriter bw = new BufferedWriter(new FileWriter(fileToTest));

        bw.write("testing");
        bw.close();

        File objects = new File ("./objects");
        objects.mkdirs();

        tree = new Tree();
        tree.initialize();


    }

    @Test
    public void testAddWithToObjects() throws IOException, NoSuchAlgorithmException {
        // Test adding a file to the Tree
        String newFileContent = "testing";
        tree.addWithToObjects(newFileContent);

        // Verify that the file was added to the objects directory
        String newFileName = ("./objects/" + tree.oldName);
        File newFile = new File("./objects/" + tree.oldName);
        assertTrue(newFile.exists());

        // Verify that the file content is as expected
        String fileContent = readFileContent(newFileName);
        assertEquals(newFileContent, fileContent);
    }

    @Test
    public void testSave() throws IOException, NoSuchAlgorithmException {
        // Test saving the Tree
        String firstEntry = "tree : bd1ccec139dead5ee0d8c3a0499b42a7d43ac44b";
        String second = "blob : 81e0268c84067377a0a1fdfb5cc996c93f6dcf9f : file1.txt";
        tree.addToTree(firstEntry);
        tree.addToTree(second);
        tree.save();
        ABlob b = new ABlob();
        String hash = b.sha1("tree : bd1ccec139dead5ee0d8c3a0499b42a7d43ac44b\nblob : 81e0268c84067377a0a1fdfb5cc996c93f6dcf9f : file1.txt");
        File treeFile = new File ("./objects/" + hash);
        assertTrue(treeFile.exists());
    }

    @Test
    public void testAlrInTree() throws IOException {
        // Test checking if a file is already in the Tree
        Tree tree = new Tree();
        tree.addToTree("Bob");

        assertTrue(tree.alrInTree("ob"));
        assertFalse(tree.alrInTree("NonExistentFile"));
    }

    @Test
    public void testAddDirectory() throws Exception {
        File directory = Paths.get("./advancedTest").toFile();
        directory.mkdir();

        assertTrue(directory.exists());

        Tree tree = new Tree();
        tree.addDirectory(directory);

        File objects = new File("./objects");

        File[] files = objects.listFiles();

        ArrayList<String> ar = new ArrayList<>();

        for (File file : files) {
            ar.add(file.getName());
        }
        assertTrue(ar.contains("62fab9fe58f9ef1a83825775b02d2466ca183786") &&
                   ar.contains("032544217e9609e54d911df3963bdefc53b3fdda") &&
                   ar.contains("da39a3ee5e6b4b0d3255bfef95601890afd80709"));
    }

    @Test
    public void testAddToTree() throws IOException, NoSuchAlgorithmException {
        // Test adding a file to the Tree
        String firstEntry = "tree : bd1ccec139dead5ee0d8c3a0499b42a7d43ac44b";
        String second = "blob : 81e0268c84067377a0a1fdfb5cc996c93f6dcf9f : file1.txt";
        tree.addToTree(firstEntry);
        tree.addToTree(second);

        // Verify that the file was added to the tree
        assertTrue(tree.alrInTree("tree : bd1ccec139dead5ee0d8c3a0499b42a7d43ac44b"));
        assertTrue(tree.alrInTree("blob : 81e0268c84067377a0a1fdfb5cc996c93f6dcf9f : file1.txt"));
    }

    @Test
    public void testRemoveFromTree() throws IOException {
        // Test removing a file from the Tree

        String firstEntry = "tree : bd1ccec139dead5ee0d8c3a0499b42a7d43ac44b";
        String second = "blob : 81e0268c84067377a0a1fdfb5cc996c93f6dcf9f : file1.txt";
        tree.addToTree(firstEntry);
        tree.addToTree(second);

        String treeContent = readFileContent("tree");

        tree.removeFromTree(firstEntry);

        // Verify that the file was removed from the tree
        String RemoveTreeContent = readFileContent("tree");
        assertFalse(RemoveTreeContent.contains(firstEntry));
    }

    private String readFileContent(String newFile) throws IOException {
        StringBuilder sb = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new FileReader(newFile))) {
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line).append("\n");
            }
        }
        if (sb.length() > 0) {
            sb.setLength(sb.length() - 1); // Remove the trailing newline
        }
        return sb.toString();
    }

    private void writeFileContent(String filePath, String content) throws IOException {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filePath))) {
            bw.write(content);
        }
    }
}
