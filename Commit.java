import java.util.*;

import org.hamcrest.SelfDescribing;

import java.io.*; 

public class Commit {
    private Commit lastCommit;
    private Commit nextCommit;

    private String summary; 
    private String author; 
    private String date; 

    private StringBuilder contents;

    public Commit ()
    {
        lastCommit = null;
        nextCommit = null;

        contents = new StringBuilder();

        Tree t = new Tree();
        
    }

    public String getDate ()
    {
        return date; 
    }

    public void createTree ()
    {

    }

    public void writeToFile () throws Exception 
    {

    }
}
