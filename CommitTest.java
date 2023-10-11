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

import javax.swing.text.AbstractDocument.BranchElement;

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
        Commit c = new Commit ("luke", "bleh");
        String commitHash = c.getSHA1();

        assertTrue("the commit hash did not match the tree hash", c.getTreeHashFromCommitHash(commitHash).equals("da39a3ee5e6b4b0d3255bfef95601890afd80709"));
    }


    @Test
    void testOneCommit() throws Exception {
        File file1 = createFile("file1.txt", "foobar");
        File file2 = createFile("file2.txt", "foobar");

        Index index = new Index();
        index.add(file1);
        index.add(file2);

        Commit c = new Commit("Manny", "hi");
        String hash = c.getSHA1();

        String line1 = "blob : " + c.getSHA1("foobar") + " : " + file1.getName();
        String line2 = "blob : " + c.getSHA1("foobar") + " : " + file2.getName();
        String hash3 = c.getSHA1(line1 + "\n" + line2);

        assertTrue("Tree Hash is incorrect", c.getTreeHashFromCommitHash(hash).equals(hash3));

        BufferedReader br = new BufferedReader(new FileReader("objects/" + hash));
        String text = br.readLine();
        text = br.readLine();

        assertTrue("Prev Hash is incorrect", text.equals(""));

        text = br.readLine();
        br.close();

        assertTrue("Next Hash is incorrect", text.equals(""));
    }


    @Test
    void testTwoCommits() throws Exception {
        // First Commit

        File file1 = createFile("file1.txt", "foobar");
        File file2 = createFile("file2.txt", "foobar");

        Index indexA = new Index();
        indexA.add(file1);
        indexA.add(file2);

        Commit cA = new Commit("Manny", "hi");
        String hashA = cA.getSHA1();

        String lineA1 = "blob : " + cA.getSHA1("foobar") + " : " + file1.getName();
        String lineA2 = "blob : " + cA.getSHA1("foobar") + " : " + file2.getName();
        String hashA3 = cA.getSHA1(lineA1 + "\n" + lineA2);

        assertTrue("Tree Hash is incorrect", cA.getTreeHashFromCommitHash(hashA).equals(hashA3) &&
                                             hashA3.equals("8cbfd0351e88eeb05a6b7c8f4a0ee79d1bf9c5d1"));

        BufferedReader brA = new BufferedReader(new FileReader("objects/" + hashA));
        String textA = brA.readLine();
        textA = brA.readLine();
        brA.close();

        assertTrue("Prev Hash is incorrect", textA.equals(""));

        // Second Commit

        Index indexB = new Index();

        indexB.add(file1);
        File dir = new File("./advancedTest");
        indexB.addDirectory(dir);

        Commit cB = new Commit("Manny", "hi", hashA);
        String hashB = cB.getSHA1();

        String lineB0 = "tree : " + cB.getTreeHashFromCommitHash(hashA);
        String lineB1 = "blob : " + cB.getSHA1("foobar") + " : " + file1.getName();
        String lineB2 = "tree : " + "62fab9fe58f9ef1a83825775b02d2466ca183786" + " : " + dir.getName();
        String hashB3 = cB.getSHA1(lineB0 + "\n" + lineB1 + "\n" + lineB2);

        // Also Tests Contents, otherwise hash would be different
        assertTrue("Tree Hash is incorrect", cB.getTreeHashFromCommitHash(hashB).equals(hashB3));;

        BufferedReader brB = new BufferedReader(new FileReader("objects/" + hashB));
        String textB = brB.readLine();
        textB = brB.readLine();

        assertTrue("Prev Hash is incorrect", textB.equals(hashA));

        textB = brB.readLine();
        brB.close();

        brA = new BufferedReader(new FileReader("objects/" + hashA));
        textA = brA.readLine();
        textA = brA.readLine();
        textA = brA.readLine();
        brA.close();

        assertTrue("Next Hash is incorrect", textA.equals(hashB));
        assertTrue("Next Hash is incorrect", textB.equals(""));
    }


    @Test
    void testFourCommits() throws Exception {

        // Setup Files, Directories, Index, and Commit

        File dir1 = new File("./advancedTest");
        File dir2 = new File("./advancedTest/test3");
        for (int i = 0; i < 8; i++) {
            createFile("file" + i + ".txt", "content" + i);
        }
        Index index1 = new Index();
        index1.init();
        Index index2 = new Index();
        index2.init();
        Index index3 = new Index();
        index3.init();
        Index index4 = new Index();
        index4.init();

        index1.add(new File("file0.txt"));
        index1.add(new File("file1.txt"));
        index1.addDirectory(dir1);

        Commit commit1 = new Commit("Manny", "First Commit");
        String commit1Hash = commit1.getSHA1();

        index2.add(new File("file2.txt"));
        index2.add(new File("file3.txt"));
        index2.addDirectory(dir2);

        Commit commit2 = new Commit("Manny", "Second Commit", commit1Hash);
        String commit2Hash = commit2.getSHA1();

        index3.add(new File("file4.txt"));
        index3.add(new File("file5.txt"));

        Commit commit3 = new Commit("Manny", "Third Commit", commit2Hash);
        String commit3Hash = commit3.getSHA1();

        index4.add(new File("file6.txt"));
        index4.add(new File("file7.txt"));

        Commit commit4 = new Commit("Manny", "Fourth Commit", commit3Hash);
        String commit4Hash = commit4.getSHA1();

        // Testing if Tree Hash is Correct also verifies the content is correct, otherwise it would be the wrong

        // First Commit

        assertTrue("Tree Hash in Commit 1 is Wrong", getTreeHash(new File("objects/" + commit1Hash)).equals("2c89ff8aeb5fe85c1c5c516551a7fabf3ed202d9"));
        assertTrue("Prev Hash in Commit 1 is Wrong", getPrevHash(new File("objects/" + commit1Hash)).equals(""));
        assertTrue("Next Hash in Commit 1 is Wrong", getNextHash(new File("objects/" + commit1Hash)).equals(commit2Hash));

        // Second Commit

        assertTrue("Tree Hash in Commit 2 is Wrong", getTreeHash(new File("objects/" + commit2Hash)).equals("a5b52dd73bc6f226a2551c862174ade52e7db2cb"));
        assertTrue("Prev Hash in Commit 2 is Wrong", getPrevHash(new File("objects/" + commit2Hash)).equals(commit1Hash));
        assertTrue("Next Hash in Commit 2 is Wrong", getNextHash(new File("objects/" + commit2Hash)).equals(commit3Hash));

        // Third Commit

        assertTrue("Tree Hash in Commit 3 is Wrong", getTreeHash(new File("objects/" + commit3Hash)).equals("f00d5c87abe5c86d69359c34a4f0f41bffc80740"));
        assertTrue("Prev Hash in Commit 3 is Wrong", getPrevHash(new File("objects/" + commit3Hash)).equals(commit2Hash));
        assertTrue("Next Hash in Commit 3 is Wrong", getNextHash(new File("objects/" + commit3Hash)).equals(commit4Hash));

        // Fourth Commit

        assertTrue("Tree Hash in Commit 4 is Wrong", getTreeHash(new File("objects/" + commit4Hash)).equals("0efe6262efec8b453552b836640afdc07a0c75a4"));
        assertTrue("Prev Hash in Commit 4 is Wrong", getPrevHash(new File("objects/" + commit4Hash)).equals(commit3Hash));
        assertTrue("Next Hash in Commit 4 is Wrong", getNextHash(new File("objects/" + commit4Hash)).equals(""));

    }


    @Test
    void testFiveCommits() throws Exception {

        // Setup Files, Directories, Index, and Commit

        File dir1 = new File("./advancedTest");
        File dir2 = new File("./advancedTest/test3");
        for (int i = 0; i < 8; i++) {
            createFile("file" + i + ".txt", "content" + i);
        }
        Index index1 = new Index();
        index1.init();
        Index index2 = new Index();
        index2.init();
        Index index3 = new Index();
        index3.init();
        Index index4 = new Index();
        index4.init();
        Index index5 = new Index();
        index5.init();

        index1.add(new File("file0.txt"));
        index1.add(new File("file1.txt"));
        index1.addDirectory(dir1);

        Commit commit1 = new Commit("Manny", "First Commit");
        String commit1Hash = commit1.getSHA1();

        index2.add(new File("file2.txt"));
        index2.add(new File("file3.txt"));
        index2.addDirectory(dir2);

        Commit commit2 = new Commit("Manny", "Second Commit", commit1Hash);
        String commit2Hash = commit2.getSHA1();

        index3.add(new File("file4.txt"));
        index3.add(new File("file5.txt"));
        index3.deleteOrEditFile("*deleted* file2.txt");

        Commit commit3 = new Commit("Manny", "Third Commit", commit2Hash);
        String commit3Hash = commit3.getSHA1();

        index4.add(new File("file6.txt"));
        index4.add(new File("file7.txt"));

        Commit commit4 = new Commit("Manny", "Fourth Commit", commit3Hash);
        String commit4Hash = commit4.getSHA1();

        index5.deleteOrEditFile("*edited* file6.txt");
        index5.deleteOrEditFile("*deleted* file0.txt");

        Commit commit5 = new Commit("Manny", "Fifth Commit", commit4Hash);
        String commit5Hash = commit5.getSHA1();

        // Testing if Tree Hash is Correct also verifies the content is correct, otherwise it would be the wrong

        // First Commit

        assertTrue("Tree Hash in Commit 1 is Wrong", getTreeHash(new File("objects/" + commit1Hash)).equals("2c89ff8aeb5fe85c1c5c516551a7fabf3ed202d9"));
        assertTrue("Previous Tree Hash in Commit 1 is incorrect", getFirstLineOfFile(new File("objects/" + getTreeHash(new File("objects/" + commit1Hash)))).equals("blob : e28d139734a42a269c4becdbbfb528f1f51d2739 : file0.txt"));

        // Second Commit

        assertTrue("Tree Hash in Commit 2 is Wrong", getTreeHash(new File("objects/" + commit2Hash)).equals("a5b52dd73bc6f226a2551c862174ade52e7db2cb"));
        assertTrue("Previous Tree Hash in Commit 2 is incorrect", getFirstLineOfFile(new File("objects/" + getTreeHash(new File("objects/" + commit2Hash)))).equals("tree : 2c89ff8aeb5fe85c1c5c516551a7fabf3ed202d9"));

        // Third Commit

        assertTrue("Tree Hash in Commit 3 is Wrong", getTreeHash(new File("objects/" + commit3Hash)).equals("dd100a538dbeaeb44c4ebe47d5902d6b70b3a0f9"));
        assertTrue("Previous Tree Hash in Commit 3 is incorrect", getFirstLineOfFile(new File("objects/" + getTreeHash(new File("objects/" + commit3Hash)))).equals("tree : 2c89ff8aeb5fe85c1c5c516551a7fabf3ed202d9"));

        // Fourth Commit

        assertTrue("Tree Hash in Commit 4 is Wrong", getTreeHash(new File("objects/" + commit4Hash)).equals("428b9bc0db2380fcacf9206aa1c02736174e53f9"));
        assertTrue("Previous Tree Hash in Commit 4 is incorrect", getFirstLineOfFile(new File("objects/" + getTreeHash(new File("objects/" + commit4Hash)))).equals("tree : dd100a538dbeaeb44c4ebe47d5902d6b70b3a0f9"));

        // Fifth Commit

        assertTrue("Tree Hash in Commit 5 is Wrong", getTreeHash(new File("objects/" + commit5Hash)).equals("cd8da74f4e907a2e35a9bf9512dbf397b992b9b1"));
        assertTrue("Previous Tree Hash in Commit 5 is incorrect", getFirstLineOfFile(new File("objects/" + getTreeHash(new File("objects/" + commit5Hash)))).equals("blob : d50f83291ceabd6e2cbf70c0312e58ab296476df : file6.txt"));
    }


    @Test
    void testGetDate() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd");  
        LocalDateTime now = LocalDateTime.now();  
        String curDate = dtf.format(now);

        assertTrue ("date was not correct", curDate.equals(Commit.getDate()));
    }


    //NOTE: BOTH OF THESE TESTS DON'T WORK. I'm ngl the tree class is broken and requires the existence of some weird files to work, so it doesn't work in the tester. 
    //and also the Commit class doesn't correctly create the tree yet, since it's just blank now. 
    @Test
    void testGetSHA1() throws Exception {
        Commit c = new Commit ("luke", "bleh");
        
        StringBuilder contents = new StringBuilder("");
       contents.append("da39a3ee5e6b4b0d3255bfef95601890afd80709\n"); //the sha1 for an empty tree ig

       //the two empty commits
       contents.append("\n");
       contents.append("\n");

       contents.append("luke\n");
       contents.append(c.getOldDate()+"\n");
       contents.append("bleh");

       String c1 = contents.toString().substring(contents.toString().indexOf("\n"));
       assertTrue("The sha does not match the expected value", ABlob.sha1(contents.toString()).equals(c.getSHA1()));
    }

    @Test
    void testWriteToFile() throws Exception {
        Commit c = new Commit ("luke", "bleh");

        c.writeToFile(); 

        StringBuilder contents = new StringBuilder();

       contents.append("da39a3ee5e6b4b0d3255bfef95601890afd80709\n"); //the sha1 for an empty tree ig

       //the two empty commits
       contents.append("\n");
       contents.append("\n");

       contents.append("luke\n");
       contents.append(c.getOldDate()+"\n");
       contents.append("bleh");

       //now just read in the file that commit makes
       BufferedReader br = new BufferedReader(new FileReader("objects/" + c.getSHA1()));
       StringBuilder commitContents = new StringBuilder();
       while (br.ready())
       {
        commitContents.append((char) br.read());
       }

       assertTrue ("File info did not match the expected one", contents.toString().equals(commitContents.toString()));

    }

    public File createFile(String fileName, String content) throws Exception {
        File file1 = new File(fileName);
        file1.createNewFile();
        PrintWriter pw1 = new PrintWriter(new BufferedWriter(new FileWriter(file1)));
        pw1.print(content); 
        pw1.close();
        return file1;
    }

    public String getFirstLineOfFile(File file) throws Exception {
        BufferedReader br = new BufferedReader(new FileReader(file));
        String text = br.readLine();
        br.close();
        return text;
    }

    public String getTreeHash(File file) throws Exception {
        BufferedReader br = new BufferedReader(new FileReader(file));
        String text = br.readLine();
        br.close();
        return text;
    }

    public String getPrevHash(File file) throws Exception{
        BufferedReader br = new BufferedReader(new FileReader(file));
        String text = br.readLine();
        text = br.readLine();
        br.close();
        return text;
    }

    public String getNextHash(File file) throws Exception {
        BufferedReader br = new BufferedReader(new FileReader(file));
        String text = br.readLine();
        text = br.readLine();
        text = br.readLine();
        br.close();
        return text;
    }
}
