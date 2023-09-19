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
    boolean ifFirstToAdd;


    public Tree()
    {
        oldName = "";
        oldFileContent = "";
        firstFile = true;
        ifFirstToAdd = true;
    }

    private String getShaOfFileContent(String fileInfo) throws IOException, NoSuchAlgorithmException
    {
        String sha = "";
        ABlob blob = new ABlob();
        sha = blob.sha1(fileInfo);
        return sha;
    }
    //need to fix


    public void addWithToObjects(String newFileForTree) throws IOException, NoSuchAlgorithmException
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

    File f;

    public void save()throws IOException, NoSuchAlgorithmException
    {
        BufferedReader br = new BufferedReader(new FileReader("tree"));
        StringBuilder sb = new StringBuilder();
        while(br.ready())
        {
            sb.append(br.readLine()+ "\n");
        }
        sb.deleteCharAt(sb.length()-1);

        String sha = getShaOfFileContent(sb.toString());
        File newFile = new File("./objects/"+sha);
        newFile.createNewFile();
        BufferedWriter bw = new BufferedWriter(new FileWriter("./objects/"+sha));
        bw.write(sb.toString());
        bw.close();

    }

    //checks if the file is already in the Index list
    public Boolean alrInTree(String shaOfFile) throws IOException
    {
        BufferedReader br = new BufferedReader(new FileReader("tree"));
        StringBuilder sb = new StringBuilder();
        while(br.ready())
        {
            sb.append(br.readLine() + "\n");
        }
        if(sb.indexOf(shaOfFile) > -1)
        {
            return true;
        }
        br.close();
        return false;
    }
    
    //adds the file to the index if it doesn't already exist
    public void addToTree(String fileName) throws IOException
    {
        BufferedWriter bw = new BufferedWriter(new FileWriter("tree", true));
        if(ifFirstToAdd)
        {
            ifFirstToAdd = false;
            bw.write(fileName);
        }
        else
        {
            if(!alrInTree(fileName))
            {
                bw.write("\n" + fileName);
            }
        }
        bw.close();
    }

    public void removeFromTree(String fileName) throws IOException
    {
        if(alrInTree(fileName))
        {
            BufferedReader br = new BufferedReader(new FileReader("tree"));
            StringBuilder sb = new StringBuilder();
            while(br.ready())
            {
                StringBuilder currentString = new StringBuilder();
                currentString.append(br.readLine());
                if(currentString.indexOf(fileName) == -1 ) //this doesnt deal w edge cases
                {
                    sb.append(currentString + "\n");
                }
                
            }
            br.close();
            sb.deleteCharAt(sb.length()-1);
            BufferedWriter bw = new BufferedWriter(new FileWriter("tree", false));
            bw.write(sb.toString());
            bw.close();
            
        }
    }

    public void initialize() throws IOException
    {
        new File("objects").mkdirs();
        f = new File("tree");
        f.createNewFile();
        BufferedWriter bw = new BufferedWriter(new FileWriter("tree", false));
        bw.close();
    }
}

