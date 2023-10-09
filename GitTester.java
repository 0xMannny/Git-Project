import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;

public class GitTester {
    public static void main(String[] args) throws Exception {
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
    }

    public static File createFile(String fileName, String content) throws Exception {
        File file1 = new File(fileName);
        file1.createNewFile();
        PrintWriter pw1 = new PrintWriter(new BufferedWriter(new FileWriter(file1)));
        pw1.print(content);
        pw1.close();
        return file1;
    }
}
