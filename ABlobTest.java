import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class ABlobTest {

    private static String testFile = "testfile.txt";


    @Test
    void testBlobFile() {



    }

    @Test
    void testSha1() throws Exception
    {
        ABlob b = new ABlob();
        String sha1OfInfo = b.sha1("hello");

        assertTrue(sha1OfInfo.equals("aaf4c61ddcc5e8a2dabede0f3b482cd9aea9434d"));

    }
}
