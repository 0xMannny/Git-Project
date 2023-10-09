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
    private ArrayList<String> namesAndHashes;
    
    public Index() {
        blob = new ABlob();
        namesAndHashes = new ArrayList<>();
    }

    public void init() throws IOException {
        new File("./objects").mkdir();
        File index = new File("./index");
        if (index.exists()) {
            index.delete();
        }
        index.createNewFile();
        namesAndHashes = new ArrayList<>();
    }

    public String sha1(String FiletoSha1) throws IOException, NoSuchAlgorithmException
    {
        ABlob b = new ABlob();
        BufferedReader br = new BufferedReader(new FileReader(FiletoSha1));
        StringBuilder sb = new StringBuilder();
        while(br.ready())
        {
            sb.append(br.readLine() + "\n");
        }
        if (sb.length() > 0) {
            sb.setLength(sb.length() - 1);
        }
        return b.sha1(sb.toString());
    } 

    public void updateIndex() throws IOException {
        String output = "";
        for (int i = 0; i < namesAndHashes.size(); i++) {
            output += namesAndHashes.get(i);
            if (i < namesAndHashes.size() - 1) {
                output += "\n";
            }
        }
        FileWriter writer = new FileWriter("./index");
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

    public void add(File file) throws Exception {
        String hash = blob.sha1(readFile(file));
        if (!(namesAndHashes.contains("blob : " + hash + " : " + file.getName()))) {
            blob.blobFile(file);
            namesAndHashes.add("blob : " + hash + " : " + file.getName());
        }
        updateIndex();
    }

    public void addDirectory(File directory) throws Exception {
        Tree tree = new Tree();
        String hash = tree.addDirectory(directory);
        if (!(namesAndHashes.contains("tree : " + hash + " : " + directory.getName()))) {
            namesAndHashes.add("tree : " + hash + " : " + directory.getName());
        }
        updateIndex();
    }

    public void remove(File file) throws Exception {
        String hash = blob.sha1(readFile(file));
        if (namesAndHashes.contains("tree : " + hash + " : " + file.getName()) || namesAndHashes.contains("blob : " + hash + " : " + file.getName())) {
            namesAndHashes.remove(file.getName() + " : " + hash);
        }
        updateIndex();
    }
}