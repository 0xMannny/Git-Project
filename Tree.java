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
    String oldFileContent;
    Boolean firstFile;

    public Tree()
    {
        oldName = "";
        oldFileContent = "";
        firstFile = true;
    }

    private String getShaOfFileContent(String fileInfo) throws IOException, NoSuchAlgorithmException
    {
        String sha = "";
        ABlob blob = new ABlob();
        sha = blob.sha1(fileInfo);
        return sha;
    }


    public void addFile(String newFileForTree) throws IOException, NoSuchAlgorithmException
    {
        String sha = "";
        if(firstFile)
        {
            firstFile = false;
            StringBuilder fileInfo = new StringBuilder();
            fileInfo.append(newFileForTree);
            sha = getShaOfFileContent(fileInfo.toString());
            oldName = sha;
        }
        else
        {

            BufferedReader br = new BufferedReader(new FileReader("./objects/"+oldName));
            StringBuilder sb = new StringBuilder();

            while(br.ready())
            {
                sb.append(br.readLine() + "\n");
            }
            oldFileContent = sb.toString();
            sb.append(newFileForTree);
            
            sha = getShaOfFileContent(sb.toString());
            br.close();
        }

        File newFile = new File("./objects/"+sha);
        newFile.createNewFile();
        oldName = sha;
        BufferedWriter bw = new BufferedWriter(new FileWriter("./objects/"+sha));
        bw.write(oldFileContent);
        bw.write(newFileForTree);
        bw.close();   
    }

    public void removeFile(String file)
    {
        
    }
}
