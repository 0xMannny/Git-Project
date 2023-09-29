import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.NoSuchAlgorithmException;
import java.sql.Blob;
import java.util.ArrayList;
import java.util.Collection;

public class Tree {
    String oldName;
    String oldFileContent;
    Boolean firstFile;
    boolean ifFirstToAdd;

    ArrayList<String> values;

    public Tree() {
        oldName = "";
        oldFileContent = "";
        firstFile = true;
        ifFirstToAdd = true;
        values = new ArrayList<String>();
    }

    private String getShaOfFileContent(String fileInfo) throws IOException, NoSuchAlgorithmException {
        return ABlob.sha1(fileInfo);
    }
    // need to fix

    public String addDirectory(File directory) throws Exception {
        if (!directory.exists()) {
            throw new Exception("Invalid Directory Path");
        }
        File[] arr = directory.listFiles();
        for (File file : arr) {
            if (file.isDirectory()) {
                Tree childTree = new Tree();
                String hash = childTree.addDirectory(file);
                addToTree("tree : " + hash + " : " + file.getName());
            } else {
                Path path = file.toPath();
                String content = Files.readString(path);
                String hash = getShaOfFileContent(content);
                addToTree("blob : " + hash + " : " + file.getName());
            }
        }
        return save();
    }

    public void addWithToObjects(String newFileForTree) throws IOException, NoSuchAlgorithmException {
        String sha = "";
        if (firstFile) {
            firstFile = false;
            StringBuilder fileInfo = new StringBuilder();
            fileInfo.append(newFileForTree);
            sha = getShaOfFileContent(fileInfo.toString());
            oldName = sha;
        } else {

            BufferedReader br = new BufferedReader(new FileReader("./objects/" + oldName));
            StringBuilder sb = new StringBuilder();

            while (br.ready()) {
                sb.append(br.readLine() + "\n");
            }
            oldFileContent = sb.toString();
            sb.append(newFileForTree);

            sha = getShaOfFileContent(sb.toString());
            br.close();
        }

        File newFile = new File("./objects/" + sha);
        newFile.createNewFile();
        oldName = sha;
        BufferedWriter bw = new BufferedWriter(new FileWriter("./objects/" + sha));
        bw.write(oldFileContent);
        bw.write(newFileForTree);
        bw.close();
    }

    public String save() throws IOException, NoSuchAlgorithmException {
        StringBuilder sb = new StringBuilder();
        for (String value : values) {
            sb.append(value + "\n");
        }
        String output = "";
        if (sb.length() > 0) {
            output = sb.substring(0, sb.length() - 1);
        }
        String sha = getShaOfFileContent(output);
        File newFile = new File("./objects/" + sha);
        newFile.createNewFile();
        BufferedWriter bw = new BufferedWriter(new FileWriter("./objects/" + sha));
        bw.write(output);
        bw.close();

        return sha;
    }

    // checks if the file is already in the Index list
    public Boolean alrInTree(String shaOfFile) throws IOException {
        return values.contains(shaOfFile);
    }

    // adds the file to the index if it doesn't already exist
    public void addToTree(String fileName) throws IOException {
        values.add(fileName);
    }

    public void removeFromTree(String fileName) throws IOException {
        if (alrInTree(fileName)) {
            values.remove(fileName);
        }
    }

    public void initialize() throws IOException {
        new File("objects").mkdirs();
    }
}
