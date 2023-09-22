import java.util.*;
import java.io.*; 

import java.time.format.DateTimeFormatter;  
import java.time.LocalDateTime;    

public class Commit {
    private String lastCommit;
    private String nextCommit;
    private String tree;

    private String summary; 
    private String author; 
    private String date; 

    private StringBuilder contents;

    public Commit (String author, String summary) throws Exception
    {
        contents = new StringBuilder();
        lastCommit = "";
        nextCommit = "";

        date = getDate();

        this.tree = createTree();
        this.author = author; 
        this.summary = summary; 

        contents.append(this.tree+"\n");
        contents.append(this.lastCommit + "\n");
        contents.append(this.nextCommit + "\n");
        contents.append(this.author + "\n");
        contents.append(this.date + "\n");
        contents.append(this.summary);
    }

    public Commit (String author, String summary, String lastCommit) throws Exception
    {
        this.lastCommit = lastCommit;
        nextCommit = "";

        date = getDate();

        this.tree = createTree();
        this.author = author; 
        this.summary = summary; 
    }

    public String getDate ()
    {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd");  
        LocalDateTime now = LocalDateTime.now();  
        return dtf.format(now); 
    }

    public String createTree () throws Exception 
    { 
        //now add every file to the tree
        //NB: tree can just be blank right now
        Tree t = new Tree();

        return t.save();
    }

    public String getSHA1 () throws Exception
    {
        return ABlob.sha1(contents.toString());
    }

    public void writeToFile () throws Exception 
    {
        String hash = getSHA1();
        PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter("./objects/" + hash)));

        pw.print(contents.toString());

        pw.close();

    }
}
