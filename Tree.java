import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.sql.Blob;

public class Tree 
{
    String oldName;
    Boolean firstFile;

    public Tree()
    {
        oldName = "";
        firstFile = true;
    }

    private String getShaOfFileContent(String fileName) throws IOException, NoSuchAlgorithmException
    {
        String sha = "";
        ABlob blob = new ABlob();
        BufferedReader br = new BufferedReader(new FileReader(fileName));
        StringBuilder sb = new StringBuilder();
        while(br.ready())
        {
            sb.append(br.readLine());
        }
        br.close();
        sha = blob.sha1(sb.toString());
        return sha;
    }
    public void addFile(String info) throws IOException, NoSuchAlgorithmException
    {
        ABlob b = new ABlob();
        String sha = "";
        if(firstFile)
        {
            firstFile = false;
            sha = getShaOfFileContent(info);
            oldName = sha;
        }
        else
        {

            BufferedReader br = new BufferedReader(new FileReader(oldName));
            BufferedWriter bw = new BufferedWriter(new FileWriter("filler", false));

            while(br.ready())
            {
                bw.write(br.readLine()+"\n");
            }
            bw.write(info);
            sha = getShaOfFileContent("filler");
            br.close();
            bw.close();
        }

        File newFile = new File("./"+sha+".txt");
        newFile.createNewFile();
        BufferedReader br2 = new BufferedReader(new FileReader(oldName));
        BufferedWriter bw2 = new BufferedWriter(new FileWriter(newFile, false));

        while(br2.ready())
        {
            bw2.write(br2.readLine() + "\n");
        }

        br2.close();
        bw2.close();

        
    }

    public void removeFile(String file)
    {

    }
}
