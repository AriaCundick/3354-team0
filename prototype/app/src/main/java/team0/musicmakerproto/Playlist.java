package team0.musicmakerproto;

import android.media.Image;

import java.util.ArrayList;

public class Playlist{
	private ArrayList<Song> songs;
	private String playlistName;
	private Image playlistIMG;
	
	//Default Constructor
	public Playlist(){
		songs = new ArrayList<Song>();
		playlistName = "untitled";
		playlistIMG = null;
	}

	public Playlist(String name){
		songs = new ArrayList<Song>();
		playlistName = name;
		playlistIMG = null;
	}

	public Playlist(String name, ArrayList<Song> s)
	{
		songs = s;
		playlistName = name;
		playlistIMG = null;
	}
	
	//Adds a song
	public void addSong(Song newSong){
		songs.add(newSong);
	}

	//Overwrites the current playlist with an existing arraylist of songs.
	public void readExistingPlaylist(ArrayList<Song> songs)
	{
		this.songs = songs;
	}
	
	//Returns arrayList of songs
	public ArrayList<Song> getSongs(){ return songs;}
	public String getPlaylistName() { return playlistName;}
	public Image getPlaylistIMG() { return playlistIMG;}
}