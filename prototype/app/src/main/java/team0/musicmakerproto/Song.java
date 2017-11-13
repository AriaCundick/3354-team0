package team0.musicmakerproto;

import java.util.HashMap;

public class Song{
	String title;
	String artist;
	String path;
	String displayName;
	String songDuration;
	int id;
	
	//Holds Notes, referenced by time(String)
	//Change second String to Note newNote when Note.java available
	HashMap<String, String> notes;
	
	//Default constructor
	public Song(){
		notes = new HashMap<String, String>();
	}

	//Constructor for quick song creation when searching for songs
	public Song(String inTitle, String inArtist, String inPath, String inDisplayName, String inSongDuration, int inID){
		title = inTitle;
		artist = inArtist;
		path = inPath;
		displayName = inDisplayName;
		songDuration = inSongDuration;
		id = inID;
		notes = new HashMap<String, String>();
	}
	
	//Set methods
	public void setTitle(String newTitle){
		title = newTitle;
	}
	public void setArtist(String newArtist){
		artist = newArtist;
	}
	public void setPath(String newPath){
		path = newPath;
	}
	public void setDisplayName(String newDisplayName){
		displayName = newDisplayName;
	}
	public void setSongDuration(String newSongDuration){
		songDuration = newSongDuration;
	}
	
	//Get methods
	public String getTitle(){
		return title;
	}
	public String getArtist(){
		return artist;
	}
	public String getPath(){
		return path;
	}
	public String getDisplayName(){
		return displayName;
	}
	public String getSongDuration(){
		return songDuration;
	}
	
	//Adds a note
	//Change String newNote to Note newNote when Note.java available
	public void addNote(String time, String newNote){
		notes.put(time, newNote);
	}
}