import java.util.*;
import java.io.*; 

import java.time.format.DateTimeFormatter;  
import java.time.LocalDateTime;    

public class Commit {
    private Commit lastCommit;
    private Commit nextCommit;

    private String summary; 
    private String author; 
    private String date; 

    public Commit (String author, String summary)
    {
        lastCommit = null;
        nextCommit = null;

        date = getDate();
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

    }
}
