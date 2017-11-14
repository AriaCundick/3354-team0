package team0.musicmakerproto;

import android.media.Image;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class Playlist implements Parcelable{
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

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel parcel, int i) {
		parcel.writeList(songs);
		parcel.writeString(playlistName);

	}

	public static final Parcelable.Creator<Playlist> CREATOR = new Parcelable.Creator<Playlist>() {
		public Playlist createFromParcel(Parcel in) {
			return new Playlist(in);
		}

		public Playlist[] newArray(int size) {
			return new Playlist[size];
		}
	};

	//Constructor for handling parcelable data.
	private Playlist(Parcel in) {
		songs = new ArrayList<Song>();
		songs = in.readArrayList(Song.class.getClassLoader());
		playlistName = in.readString();
	}
}