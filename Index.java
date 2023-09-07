import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

public class Index {
    ABlob blob;
    private HashMap<String, String> filesToHash;

    public Index() {
        blob = new ABlob();
        filesToHash = new HashMap<>();
    }

    public void init() throws IOException {
        new File("./objects").mkdir();
        File index = new File("./index.txt");
        index.createNewFile();
    }

    public void updateIndex() throws IOException {
        String output = "";
        for (Map.Entry<String, String> entry : filesToHash.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            output += key + " : " + value + "\n";
        }
        FileWriter writer = new FileWriter("./index.txt");
        writer.write(output);
        writer.flush();
        writer.close();
    }

    public void add(File file) throws FileNotFoundException, NoSuchAlgorithmException, IOException {
        String hash = blob.blobFile(file);
        filesToHash.put(file.getName(), hash);
        updateIndex();
    }

    public void remove(File file) {
    }
}
