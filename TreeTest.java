import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.io.*;
import java.security.NoSuchAlgorithmException;

public class TreeTest {

    private Tree tree;

    @BeforeEach
    public void setUp() throws IOException {

        tree = new Tree();
        tree.initialize();
    }

    @Test
    public void testAddWithToObjects() throws IOException, NoSuchAlgorithmException {
        // Test adding a file to the Tree
        String newFileContent = "This is a test file content.";
        tree.addWithToObjects(newFileContent);

        // Verify that the file was added to the objects directory
        File newFile = new File("./objects/" + tree.oldName);
        assertTrue(newFile.exists());

        // Verify that the file content is as expected
        String fileContent = readFileContent(newFile);
        assertEquals(newFileContent, fileContent);
    }

    @Test
    public void testSave() throws IOException, NoSuchAlgorithmException {
        // Test saving the Tree
        String treeContent = "File1\nFile2\nFile3";
        writeFileContent("tree", treeContent);

        tree.save();

        // Verify that a new file was created in the objects directory
        String fileName = ("./objects/" + tree.oldName);
        File newFile = new File("./objects/" + tree.oldName);
        assertTrue(newFile.exists());

        // Verify that the file content is as expectedBufferedReader br = new BufferedReader(new FileReader(testFile));
        String fileContent = readFileContent(fileName);
        assertEquals(treeContent, fileContent);
    }

    @Test
    public void testAlrInTree() throws IOException {
        // Test checking if a file is already in the Tree
        String fileName = "File1";
        String treeContent = "File1\nFile2\nFile3";
        writeFileContent("tree", treeContent);

        assertTrue(tree.alrInTree(fileName));
        assertFalse(tree.alrInTree("NonExistentFile"));
    }

    @Test
    public void testAddToTree() throws IOException {
        // Test adding a file to the Tree
        String fileName = "NewFile";
        tree.addToTree(fileName);

        // Verify that the file was added to the tree
        String treeContent = readFileContent(fileName);
        assertTrue(treeContent.contains(fileName));
    }

    @Test
    public void testRemoveFromTree() throws IOException {
        // Test removing a file from the Tree
        String fileNameToRemove = "File2";
        String treeContent = "File1\nFile2\nFile3";
        writeFileContent("tree", treeContent);

        tree.removeFromTree(fileNameToRemove);

        // Verify that the file was removed from the tree
        String RemoveTreeContent = readFileContent(fileNameToRemove);
        assertFalse(RemoveTreeContent.contains(fileNameToRemove));
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
