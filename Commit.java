import java.util.*;

import org.junit.experimental.theories.Theories.TheoryAnchor;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.format.DateTimeFormatter;  
import java.time.LocalDateTime;    

public class Commit {
    private String lastCommit;
    private String nextCommit;
    private String tree;

    private String summary; 
    private String author; 
    private String date;

    private String hash;

    public Commit (String author, String summary, String lastCommit) throws Exception
    {
        this.lastCommit = lastCommit;
        this.tree = createTree();
        this.nextCommit = "";
        this.summary = summary;
        this.author = author; 
        this.date = getDate();

        writeToFile();

        if (lastCommit != "") {
            File file = new File("objects/" + lastCommit);
            String output = "";
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                int i = 0;
                while (true) {
                    String line = reader.readLine();
                    if (line == null) {
                        break;
                    }
                    if (i > 0) {
                        output += "\n";
                    }
                    if (i == 2) {
                        output += this.hash;
                    } else {
                        output += line;
                    }
                    i++;
                }
            }
            FileWriter writer = new FileWriter(file);
            writer.write(output);
            writer.flush();
            writer.close();
        }
    }

    public Commit (String author, String summary) throws Exception
    {
        this(author, summary, "");
    }

    public static String getDate() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd");  
        LocalDateTime now = LocalDateTime.now();  
        return dtf.format(now); 
    }

    public String getOldDate() {
        return this.date;
    }

    public String createTree () throws Exception 
    { 
        //now add every file to the tree
        //NB: tree can just be blank right now
        Tree tree = new Tree();

        if (lastCommit != "") {
            tree.addToTree("tree : " + getTreeHashFromCommitHash(lastCommit));
        }

        BufferedReader reader = new BufferedReader(new FileReader("./index"));
		String line = reader.readLine();
        ArrayList<String> toDeleteOrEdit = new ArrayList<String>();
        ArrayList<String> newEditedFiles = new ArrayList<String>();
		while (line != null) {
            if (line.contains("*deleted*") || line.contains("*edited*")) {
                toDeleteOrEdit.add(line.substring(line.indexOf(" ") + 1));
                if (line.contains("*edited*")) {
                    String hash = getSHA1(readFile(new File(line.substring(9))));
                    tree.addToTree("blob : " + hash + " : " + line.substring(9));
                }
            } else {
                tree.addToTree(line);
            }
			// read next line
			line = reader.readLine();
		}
		reader.close();

        if (toDeleteOrEdit.size() > 0) {
            String toPoint = tree.deleteOrEditTreesAndTraverse(toDeleteOrEdit, getTreeHashFromCommitHash(lastCommit));
            if (toPoint != "") {
                tree.changeTreeToPoint(toPoint);
            } else {
                tree.removeFrontOfTree();
            }
        }

        Index index = new Index();
        index.init();

        return tree.save();
    }

    public String getSHA1() {
        return this.hash;
    }

    public String getSHA1(String content) throws Exception
    {
        return ABlob.sha1(content);
    }

    public void writeToFile () throws Exception 
    {
        ArrayList<String> values = new ArrayList<>();

        values.add(this.tree);
        values.add(this.lastCommit);
        values.add(this.nextCommit);
        values.add(this.author);
        values.add(this.date);
        values.add(this.summary);

        StringBuilder sb = new StringBuilder();

        for(int i = 0; i < values.size(); i++) {
            sb.append(values.get(i));
            if (i < values.size() - 1) {
                sb.append("\n");
            }
        }

        StringBuilder content = new StringBuilder();

        for(int i = 0; i < values.size(); i++) {
            if (i != 2) {
                content.append(values.get(i));
            }
            if (i < values.size() - 1) {
                content.append("\n");
            }
        }

        String hash = getSHA1(content.toString());

        this.hash = hash;

        PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter("./objects/" + hash)));

        pw.print(sb.toString());

        pw.close();
    }

    public void checkout(String sha1OfCommit) throws Exception {
        String treeHashOfCommit = getTreeHashFromCommitHash(sha1OfCommit);
        File fileOfTree = new File("objects/" + treeHashOfCommit);
        BufferedReader br = new BufferedReader(new FileReader(fileOfTree));

        String lineOfFile = br.readLine();
        if (lineOfFile != null && lineOfFile.length() == 47) {
            lineOfFile = br.readLine();
        }
        while (lineOfFile != null) {
            int indexOfFileNameInLine = lineOfFile.lastIndexOf(" ");
            String nameOfFile = lineOfFile.substring(indexOfFileNameInLine + 1);
            File fileToChange = new File(nameOfFile);
            if (!fileToChange.exists()) {
                fileToChange.createNewFile();
            }
            String contentOfOldFile = readFile(new File("objects/" + lineOfFile.substring(7, 47)));

            FileWriter writer = new FileWriter(fileToChange);
            writer.write(contentOfOldFile);
            writer.flush();
            writer.close();

            lineOfFile = br.readLine();
        }
        br.close();
    }

    public String getTreeHashFromCommitHash(String commitHash) throws Exception{
        File commit = new File("objects/" + commitHash);
        BufferedReader br = new BufferedReader(new FileReader(commit));
        String text = br.readLine();
        br.close();
        return text;
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
}