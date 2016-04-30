

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Playlist {
	public ERR_MSG errMsg;
	public enum ERR_MSG {INVALID, NOT_FOUND, FOUND};
	public String address; 
	public String name;
	public String creater;
	public String photoAddress;
	public String numOfSongs;
	public List<Song> listOfSongs;
	
	public Playlist(String addr) {
		
		this.address = addr;
		if (!address.matches("http://api.haochang.tv/user/playlist\\?playlistId=[0-9]{8}"))
			errMsg = ERR_MSG.INVALID;
		else {
			String content = Crawler.getSource(address);
			if (content.contains("<meta name=\"keywords\" content=\"404\" />"))
				errMsg = ERR_MSG.NOT_FOUND;
			else {
				errMsg = ERR_MSG.FOUND;
				this.name = Crawler.searchPattern(content, "<title>(.+?)</title>", 1)[0];
				this.photoAddress = Crawler.searchPattern(content, "background: url\\((.+?)\\) center", 1)[0];
				this.numOfSongs = Crawler.searchPattern(content, "([0-9]+)สื", 1)[0];
				this.creater = Crawler.searchPattern(content, "[0-9]+สื.+?<p>(.+?)</p>", 1)[0];
				listOfSongs = new ArrayList<Song>();
			}
		}
	}
	
	public List<Song> listSongs() {
		String content = Crawler.getSource(address);
		Pattern pattern = Pattern.compile("<li data-src=\"(.+?)\" data-username=\"(.+?)\".+?<p>(.+?)</p>");
		Matcher matcher = pattern.matcher(content);
		while(matcher.find()) {
			String result[] = new String[3];
			for (int i = 1; i <= 3; i++) 
				result[i-1] = matcher.group(i);
			String date = getDate(result[0]);
			Song song = new Song(result[2], result[1], date, result[0]);
			listOfSongs.add(song);
		}
		return listOfSongs;
	}
	
	private String getDate(String link) {
		Pattern pattern1 = Pattern.compile("http://audio-qn.okchang.com/tingting/music/(.+?)/.+?.m4a");
		Pattern pattern2 = Pattern.compile("http://files.haochang.tv/file/song/(.+?)[0-9]{2}/.+?.m4a");
	    Matcher dateMatcher1 = pattern1.matcher(link);
	    Matcher dateMatcher2 = pattern2.matcher(link);
	    if (dateMatcher1.find())
	    	return dateMatcher1.group(1);
	    else if (dateMatcher2.find()){
	    	return dateMatcher2.group(1);
	    }
	    else
	    	return null;
	}
	
	@Override
	public String toString() {
		return "Name: "+this.name+
				"\nPhotoAddress: "+ this.photoAddress+
				"\nCreater: "+ this.creater +
				"\nNumber of songs: "+ this.numOfSongs;
	}
}
