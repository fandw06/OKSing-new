import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TestDrive {

	public static void main(String[] args) {
		
		String bk = "http://api.haochang.tv/user/playlist?playlistId=10005603";
		Playlist p = new Playlist(bk);
	//	System.out.println(p.errMsg);
		System.out.println(p.toString());
	//	System.out.println(p.listSongs());
		/*
		String song = "http://audio-qn.okchang.com/tingting/music/201511/gyh_1446819119_10815163.m4a";
		String date = Crawler.searchPattern(song, 
	    		"http://audio-qn.okchang.com/tingting/music/(.+?)/.+?.m4a", 
	    		1)[0];		
		System.out.println(date);
		*/
		/*
		String test = "http://files.haochang.tv/file/song/20160415/IM314607281052725981.m4a";
		Pattern pattern = Pattern.compile("http://files.haochang.tv/file/song/(.+?)[0-9]{2}/.+?.m4a");
		Matcher m = pattern.matcher(test);
		System.out.println(m.find());
		System.out.println(m.group(1));
		*/
	}

}
