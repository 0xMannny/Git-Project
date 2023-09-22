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

    public Commit (String author, String summary) throws Exception
    {
        lastCommit = "";
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

    public void writeToFile () throws Exception 
    {
        //first add all the data to a stringbuilder
        StringBuilder sb = new StringBuilder();

        sb.append(tree+"\n");
        sb.append(lastCommit + "\n");
        sb.append(nextCommit + "\n");
        sb.append(author + "\n");
        sb.append(date + "\n");
        sb.append(summary);

        //then print the data to the SHA1 of the contents 
        
        String hash = ABlob.sha1(sb.toString());
        PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter("./objects/" + hash)));

        pw.print(sb.toString());

        pw.close();

    }
}
