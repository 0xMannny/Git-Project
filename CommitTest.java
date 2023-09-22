import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.Assert.*;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedHashMap;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.format.DateTimeFormatter;  
import java.time.LocalDateTime;  

public class CommitTest {

    private static String testFile = "testfile.txt";

    @BeforeAll
    static void runBeforeAll () throws Exception {
        //make a test file
        File f = new File(testFile);
        f.createNewFile();

        PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(testFile)));

        pw.print("foobar"); 
        pw.close();

        //make objects file
        File objects = new File ("./objects");
        objects.mkdirs();

    }


    //NOTE: this won't work right now because createTree just creates an empty tree
    @Test
    void testCreateTree() throws Exception {
        Tree t = new Tree();
        t.addWithToObjects(testFile);

        String correctHash = t.save();

        Commit c = new Commit ("luke", "bleh");
        String commitHash = c.createTree();

        assertTrue("the commit hash did not match the tree hash", correctHash.equals(commitHash));
    }

    @Test
    void testGetDate() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd");  
        LocalDateTime now = LocalDateTime.now();  
        String curDate = dtf.format(now);

        assertTrue ("date was not correct", curDate.equals(Commit.getDate()));
    }

    @Test
    void testGetSHA1() throws Exception{
        //FIXME: this eventually won't work when we update Commits to actually have full functionality

        Commit c = new Commit ("luke", "bleh");
        
        StringBuilder contents = new StringBuilder("");
       contents.append("f3f6a1540a168e8c332ef06679ccb57649b957b1\n"); //the sha1 for an empty tree ig

       //the two empty commits
       contents.append("\n");
       contents.append("\n");

       contents.append("luke\n");
       contents.append(Commit.getDate()+"\n");
       contents.append("bleh\n");

       assertTrue("The sha does not match the expected value", ABlob.sha1(contents.toString()).equals(c.getSHA1()));
    }

    @Test
    void testWriteToFile() {

    }
}
