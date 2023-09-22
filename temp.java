public class temp {
   public static void main (String[] args)  throws Exception
   {
    //     Commit c = new Commit ("luke", "bleh");
        
    //     StringBuilder contents = new StringBuilder("");
    //    contents.append("f3f6a1540a168e8c332ef06679ccb57649b957b1\n"); //the sha1 for an empty tree ig

    //    //the two empty commits
    //    contents.append("\n");
    //    contents.append("\n");

    //    contents.append("luke\n");
    //    contents.append(Commit.getDate()+"\n");
    //    contents.append("bleh");

    //    System.out.println(contents.toString());
    //    System.out.println("_______________________-");
    //    System.out.println(c.getContents());

    //    System.out.println("_______________________-");
    //    System.out.println( ABlob.sha1(contents.toString()).equals(c.getSHA1()));

        Tree t = new Tree();
        System.out.println ( t.save() );
   }
}
