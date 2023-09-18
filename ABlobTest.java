import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.security.NoSuchAlgorithmException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ABlobTest {

    private ABlob aBlob;

    @TempDir
    File tempDir;

    @BeforeEach
    public void setUp() {
        aBlob = new ABlob();
    }

    @Test
    public void testSha1() throws NoSuchAlgorithmException {
        String input = "hello this is a test";
        String expectedHash = "fa26be19de6bff93f70bc2308434e4a440bbad02"; // This is the SHA1 hash of "hello this is a test"
        assertEquals(expectedHash, aBlob.sha1(input));
    }

    @Test
    public void testBlobFile() throws IOException, NoSuchAlgorithmException {
        File testFile = new File(tempDir, "test.txt");
        try (FileWriter writer = new FileWriter(testFile)) {
            writer.write("hello this is a test");
        }

        File resultFile = aBlob.blobFile(testFile);
        assertTrue(resultFile.exists());

        String fileContent = new String(Files.readAllBytes(resultFile.toPath()));
        assertEquals("hello this is a test", fileContent);
    }

    // Additional tests can be added, e.g., for edge cases or invalid input.
}
