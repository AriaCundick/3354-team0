package team0.musicmakerproto;

import java.util.ArrayList;

public class Playlist(){
	ArrayList<Song> songs;
	String playlistName;
	
	//Default Constructor
	public Playlist(){
		songs = new ArrayList<Song>();
		playlistName = "untitled";
	}
	public Playlist(String name){
		songs = new ArrayList<Song>();
		playlistName = name;
	}
	
	//Adds a song
	public void addSong(Song newSong){
		songs.add(newSong);
	}
	
	//Returns arrayList of songs
	public ArrayList<Song> getSongs(){
		return songs;
	}
}