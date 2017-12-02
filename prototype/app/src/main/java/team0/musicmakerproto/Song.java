package team0.musicmakerproto;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.HashMap;

public class Song implements Parcelable {
	String title;
	String artist;
	String path;
	String displayName;
	String songDuration;
	//int id;
	static Resources resources;
	
	String notes;
	
	//Copy constructor
	public Song(Song s){
		title = s.title;
		artist = s.artist;
		path = s.path;
		displayName = s.displayName;
		songDuration = s.songDuration;
	}

	//Constructor for quick song creation when searching for songs
	public Song(String inTitle, String inArtist, String inPath, String inDisplayName, String inSongDuration, int inID){
		title = inTitle;
		artist = inArtist;
		path = inPath;
		displayName = inDisplayName;
		songDuration = inSongDuration;
		//id = inID;
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
	public void setNote(String newNote) { notes = newNote;}
	public static void setResources(Resources r) {resources = r;}
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

	//Get the image art based on the metadata of the song.
	public Bitmap getImage() {
		MediaMetadataRetriever mmr = new MediaMetadataRetriever();
		mmr.setDataSource(path);
		byte [] data = mmr.getEmbeddedPicture();

		//If there is no album art for the song, use a default image.
		if(data == null)
			return BitmapFactory.decodeResource(resources, R.drawable.ic_default_albumart2);
		return BitmapFactory.decodeByteArray(data, 0, data.length);
	}

	// Method for parcel
	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel parcel, int i) {
		parcel.writeString(title);
		parcel.writeString(artist);
		parcel.writeString(path);
		parcel.writeString(displayName);
		parcel.writeString(songDuration);
		//parcel.writeInt(id);
		parcel.writeString(notes);
	}

	public static final Parcelable.Creator<Song> CREATOR = new Parcelable.Creator<Song>() {
		public Song createFromParcel(Parcel in) {
			return new Song(in);
		}

		public Song[] newArray(int size) {
			return new Song[size];
		}
	};

	private Song(Parcel in)
	{
		title = in.readString();
		artist = in.readString();
		path = in.readString();
		displayName = in.readString();
		songDuration = in.readString();
		//id = in.readInt();
		notes = in.readString();

	}
}