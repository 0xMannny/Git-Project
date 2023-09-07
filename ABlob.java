import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.MessageDigest;

public class ABlob {
    public ABlob() {

    }

    public String sha1(String input) throws NoSuchAlgorithmException {
        MessageDigest mDigest = MessageDigest.getInstance("SHA1");
        byte[] result = mDigest.digest(input.getBytes());
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < result.length; i++) {
            sb.append(Integer.toString((result[i] & 0xff) + 0x100, 16).substring(1));
        }
        return sb.toString();
    }

    public File blobFile(File fileR) throws FileNotFoundException, IOException, NoSuchAlgorithmException {
        String temp = "";
        try (BufferedReader reader = new BufferedReader(new FileReader(fileR))) {
            int i = 0;
            while (true) {
                String line = reader.readLine();
                if (line == null) {
                    break;
                }
                if (i == 1) {
                    temp += "\n";
                }
                temp += line;
                i = 1;
            }
        }
        String result = sha1(temp);
        File file = new File("./objects/" + result + ".txt");
        file.createNewFile();
        FileWriter writer = new FileWriter(file);
        writer.write(temp);
        writer.flush();
        writer.close();
        return file;
    }
}