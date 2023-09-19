import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;

public class IndexTest {
    private Index index;

    @Before
    public void setUp() {
        index = new Index();
    }

    @Test
    public void testInit() throws IOException {
        // Test if the init method creates the necessary directories and files
        index.init();
        File objectsDir = new File("./objects");
        File indexFile = new File("./index.txt");

        assertTrue(objectsDir.exists() && objectsDir.isDirectory());
        assertTrue(indexFile.exists() && indexFile.isFile());
    }

    @Test
    public void testAdd() throws IOException, NoSuchAlgorithmException {
        // Test adding and removing files from the index
        String testFileName = "./test.txt";
        File testFile = new File("./test.txt");
        testFile.createNewFile();

        // Add the file to the index
        index.add(testFile);
        BufferedReader br = new BufferedReader(new FileReader(testFile));
        StringBuilder sb = new StringBuilder();
        while(br.ready())
        {
            sb.append(br.readLine()+"\n");
        }
        String sbAsString = sb.toString();
        assertTrue(sbAsString.contains(testFile.getName() + " : " + index.sha1(testFileName)));
    }

     @Test
    public void testRemove() throws IOException, NoSuchAlgorithmException {
        // Test adding and removing files from the index
        File testFile = new File("./test.txt");
        testFile.createNewFile();

        // Remove the file from the index
        index.remove(testFile);
        BufferedReader br = new BufferedReader(new FileReader(testFile));
        StringBuilder sb = new StringBuilder();
        while(br.ready())
        {
            sb.append(br.readLine()+"\n");
        }
        String sbAsString = sb.toString();
        assertFalse(sbAsString.contains(testFile.getName() + " : " + index.blob.sha1(index.readFile(testFile))));
    }

    @Test
    public void testReadFile() throws IOException {
        // Test the readFile method
        File testFile = new File("./test.txt");
        String content = "Test file content";
        FileWriter fileWriter = new FileWriter(testFile);
        fileWriter.write(content);
        fileWriter.close();

        String readContent = index.readFile(testFile);
        assertEquals(content, readContent);
    }

    @Test
    public void testUpdateIndex() throws IOException, NoSuchAlgorithmException {
        // Test the updateIndex method
        File testFile = new File("./test.txt");
        testFile.createNewFile();

        // Add the file to the index
        index.add(testFile);

        // Update the index and check if the file is present in the index file
        index.updateIndex();
        String indexContent = index.readFile(new File("./index.txt"));
        assertTrue(indexContent.contains(testFile.getName()));
    }
}
