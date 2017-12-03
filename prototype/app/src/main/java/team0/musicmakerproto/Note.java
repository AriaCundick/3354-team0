package team0.musicmakerproto;

/**
 * Created by Aria on 12/2/17.
 */

public class Note {
    String name;
    String contents;
    String path;

    public Note()
    {
        name = path = contents = "";
    }

    public Note(String n, String c, String p)
    {
        name = n;
        contents = c;
        path = p;
    }

    //Getter methods
    public String getName() {return name;}
    public String getContents() {return contents;}
    public String getPath() {return path;}

    //Setter methods
    public void setName(String s) {name = s;}
    public void setContents(String s) {contents = s;}
    public void setPath(String s) {path = s;}


}
