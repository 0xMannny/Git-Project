import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

public class Index {
    ABlob blob;
    private ArrayList<String> filesToHash;
    
    public Index() {
        blob = new ABlob();
        filesToHash = new ArrayList<>();
    }

    public void init() throws IOException {
        new File("./objects").mkdir();
        File index = new File("./index.txt");
        index.createNewFile();
    }

    public void updateIndex() throws IOException {
        String output = "";
        for (int i = 0; i < filesToHash.size(); i++) {
            output += filesToHash.get(i) + "\n";
        }
        FileWriter writer = new FileWriter("./index.txt");
        writer.write(output);
        writer.flush();
        writer.close();
    }

    public String readFile(File file) throws FileNotFoundException, IOException {
        String output = "";
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            int i = 0;
            while (true) {
                String line = reader.readLine();
                if (line == null) {
                    break;
                }
                if (i == 1) {
                    output += "\n";
                }
                output += line;
                i = 1;
            }
        }
        return output;
    }

    public void add(File file) throws FileNotFoundException, NoSuchAlgorithmException, IOException {
        String hash = blob.sha1(readFile(file));
        if (!(filesToHash.contains(file.getName() + " : " + hash))) {
            blob.blobFile(file);
            filesToHash.add(file.getName() + " : " + hash);
        }
        updateIndex();
    }

    public void remove(File file) throws IOException, NoSuchAlgorithmException {
        String hash = blob.sha1(readFile(file));
        if (filesToHash.contains(file.getName() + " : " + hash)) {
            filesToHash.remove(file.getName() + " : " + hash);
        }
        updateIndex();
    }
}