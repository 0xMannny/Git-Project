import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Index {
    ABlob blob;
    private ArrayList<String> filesToHash;
    private HashMap<String, File> hashToFile;

    public Index() {
        blob = new ABlob();
        filesToHash = new ArrayList<>();
        hashToFile = new HashMap<>();
    }

    public void init() throws IOException {
        new File("./objects").mkdir();
        File index = new File("./index.txt");
        index.createNewFile();
    }

    public void updateIndex() throws IOException {
        String output = "";
        for (String s : filesToHash) {
            output += s + "\n";
        }
        FileWriter writer = new FileWriter("./index.txt");
        writer.write(output);
        writer.flush();
        writer.close();
    }

    public void add(File file) throws FileNotFoundException, NoSuchAlgorithmException, IOException {
        String hash = blob.sha1(file.getName());
        if (!(filesToHash.contains(file.getName() + " : " + hash + ".txt"))) {
            File temp = blob.blobFile(file);
            hashToFile.put(hash, temp);
        }
        filesToHash.add(file.getName() + " : " + hash + ".txt");
        updateIndex();
    }

    public void remove(File file) throws IOException, NoSuchAlgorithmException {
        String hash = blob.sha1(file.getName());
        filesToHash.remove(file.getName() + " : " + hash + ".txt");
        if (!(filesToHash.contains(file.getName() + " : " + hash + ".txt"))) {
            hashToFile.get(hash).delete();
            hashToFile.remove(hash);
        }
        updateIndex();
    }
}
