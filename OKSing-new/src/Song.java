


import java.net.MalformedURLException;
import java.net.URL;
  
public class Song {  
	
    public String title;   
    public String author;
    public String uploadTime;
    public int size;
    public URL mediaUrl;  
    public String address; 
   
    public Song(String title, String author, String uploadTime, String address) {   
        this.title = title;
        this.author = author;
        this.uploadTime = uploadTime;
        this.address = address;
        try {
			this.mediaUrl = new URL(address);
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        this.size = 0;
    }  
    
    public URL getMediaAddress() {
    	URL url = null;
        try {
			url =  new URL(address);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
        return url;
    }

    @Override  
    public String toString() {  
        return "Title: " + title + 
        		"\nAuthor: " + author +
        		"\nAddress: " + address + 
        		"\nUpload time: " + uploadTime +
        		"\nMedia address: "+ mediaUrl+"\n";  
    }  
}  