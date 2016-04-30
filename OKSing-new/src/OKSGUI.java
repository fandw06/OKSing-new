
import javax.swing.JPanel;
import javax.swing.JProgressBar;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingWorker;
import javax.swing.JCheckBox;
import javax.swing.JFrame;

public class OKSGUI{

	/**
	 * The top-level panel to add into the frame.
	 */
	private JPanel OKSPanel;
	/**
	 * The menu bar to add into the frame.
	 */
	private JMenuBar OKSMenubar;
	
	/*
	 * GUI components for panel. 
	 */
	private JLabel lPhoto;
	private JLabel lName;
	private JLabel lCreater;
	private JCheckBox cb[];
	private JCheckBox bListSongs;
	private JButton bDownload;
	private JLabel lSongs;
	private JLabel lNumber;	
	private JProgressBar pbProgress;
	private JPanel container;
	
	/*
	 * GUI components for menu. 
	 */
	private JMenu mFile;
	private JMenuItem miChange;
	private JMenuItem miQuit;
	private JMenu mHelp;
	private JMenuItem miAbout;
	
	/*
	 * Non-GUI data members.
	 */
	private Playlist playlist;
	private List<Song> songList;

	private final String DEFAULT_PAGE = "http://api.haochang.tv/user/playlist?playlistId=10005603";
	private String page;
	private class DlInfo {
		boolean completed;
		int number;
		int progress;
		DlInfo (boolean c, int n, int p) {
			this.completed = c;
			this.number = n;
			this.progress = p;
		}
	}
	
	/**
	 * Create the panel.
	 */
	public OKSGUI() {		
		initializePanel();
		initializeMenu();
		displayPage(DEFAULT_PAGE);
	}
	
	/**
	 * This is for initializing components which are irrelevant with person.
	 */
	public void initializePanel() {
		
		OKSPanel = new JPanel();
		OKSPanel.setLayout(null);
		OKSPanel.setSize(600, 550);
		OKSPanel.setLocation(0, 0);

		lPhoto = new JLabel();
		lPhoto.setSize(new Dimension(160, 160));
		lPhoto.setLocation(10, 10);
		lPhoto.setVisible(true);
		OKSPanel.add(lPhoto);
		
		lName = new JLabel();
		lName.setFont(new Font("Serif", Font.PLAIN, 35));
		lName.setLocation(180, 15);
		lName.setSize(260, 40);
		OKSPanel.add(lName);
		
		lCreater = new JLabel();
		lCreater.setFont(new Font("Serif", Font.PLAIN, 25));
		lCreater.setLocation(180, 60);
		lCreater.setSize(260, 40);
		OKSPanel.add(lCreater);
		
		lSongs = new JLabel();
		lSongs.setFont(new Font("Serif", Font.PLAIN, 25));
		lSongs.setLocation(180, 100);
		lSongs.setSize(150, 28);
		OKSPanel.add(lSongs);
		
		bListSongs = new JCheckBox();
		bListSongs.setFont(new Font("Serif", Font.PLAIN, 25));
		bListSongs.setLocation(340, 100);
		bListSongs.setSize(150, 28);
		bListSongs.addActionListener( e -> {
			if (bListSongs.isSelected()) {
				if (Integer.parseInt(playlist.numOfSongs) == 0)
					JOptionPane.showMessageDialog(null, 
							"No songs to list.",
							"Info",
							JOptionPane.INFORMATION_MESSAGE);	
				else {
					ListSongs listSongs = new ListSongs();
					listSongs.execute();
				}
				
			}					
		});
		OKSPanel.add(bListSongs);
		
		bDownload = new JButton();
		bDownload.setFont(new Font("Serif", Font.PLAIN, 20));
		bDownload.setLocation(180, 135);
		bDownload.setSize(150, 28);
		bDownload.setEnabled(false);
		bDownload.addActionListener( e -> {
			Download download = new Download();
			download.execute();	
			
		});
		OKSPanel.add(bDownload);
		
		lNumber = new JLabel();
		lNumber.setFont(new Font("Serif", Font.PLAIN, 22));
		lNumber.setLocation(345, 127);
		lNumber.setSize(40, 40);
		lNumber.setVisible(false);
		OKSPanel.add(lNumber);
		
		pbProgress = new JProgressBar(0, 100);
	    pbProgress.setValue(0);
	    pbProgress.setStringPainted(true);
	    pbProgress.setSize(new Dimension(160, 25));
	    pbProgress.setLocation(380, 136);
	    pbProgress.setVisible(false);
	    OKSPanel.add(pbProgress);
	    
	    container = new JPanel(null);
	    container.setSize(570, 350);
	    container.setLocation(10, 180);
	    container.setVisible(true);
	    OKSPanel.add(container);
	}

	public void initializeMenu() {
		
		OKSMenubar = new JMenuBar(); 
		OKSMenubar.setOpaque(true);
		/*
		 * Initialize menu and menu items.
		 */
		/**
		 * mFile menu includes "Change person" and "Quit".
		 */
		mFile = new JMenu("File"); 
		mFile.setMnemonic(KeyEvent.VK_F); 
	
		miChange = new JMenuItem("Change person");
		miChange.setMnemonic(KeyEvent.VK_Q); 
		miChange.addActionListener( e -> {
			page = JOptionPane.showInputDialog(null,  
					"The homepage : ",
					"Input a person's homepage",
				    JOptionPane.OK_CANCEL_OPTION);	
			displayPage(page);					
		});
		mFile.add(miChange); 
		
		miQuit = new JMenuItem("Quit"); 
		miQuit.setMnemonic(KeyEvent.VK_Q); 
		miQuit.addActionListener( e -> {
			System.exit(0);
		});
		mFile.add(miQuit); 

		OKSMenubar.add(mFile);
		
		mHelp = new JMenu("Help"); 
		mHelp.setMnemonic(KeyEvent.VK_H); 
		
		miAbout = new JMenuItem("About");	
		miAbout.setMnemonic(KeyEvent.VK_A);
		miAbout.addActionListener( e -> {
			JOptionPane.showMessageDialog(null, 
					"OK Sing v2.0\n"+ "Date: 04/30/2016\n"+ "Author: Dawei Fan",
					"About",
					JOptionPane.INFORMATION_MESSAGE);				
		});
		mHelp.add(miAbout);
		
		OKSMenubar.add(mHelp); 	
	}
	
	public void displayPage(String page) {
		
		URL url;
		playlist = new Playlist(page);	
		if (playlist.errMsg == Playlist.ERR_MSG.INVALID) {
			JOptionPane.showMessageDialog(null, 
					"Invalid URL!" ,
					"Warning",
					JOptionPane.WARNING_MESSAGE);	
			return;
		}
		
		if (playlist.errMsg == Playlist.ERR_MSG.NOT_FOUND) {
			JOptionPane.showMessageDialog(null, 
					"Playlist not found!" ,
					"Warning",
					JOptionPane.WARNING_MESSAGE);	
			return;
		}
		
		System.out.println(playlist.toString());
		
		BufferedImage c = null;
		try {
			url = new URL(playlist.photoAddress);
			c = ImageIO.read(url);
		} catch (IOException e) {
			e.printStackTrace();
		}	

		lPhoto.setIcon(new ImageIcon(c.getScaledInstance(160, 160,  java.awt.Image.SCALE_SMOOTH)));	
		lName.setText(playlist.name);	
		lCreater.setText("Creater: "+playlist.creater);
		lSongs.setText("Songs: "+ playlist.numOfSongs);
		bListSongs.setText("List songs");
		bDownload.setText("Download");
		lNumber.setText("No.");
		bListSongs.setEnabled(true);
		bListSongs.setSelected(false);
		pbProgress.setValue(0);
		pbProgress.setVisible(false);
		
		container.removeAll();
		container.validate();		
		container.repaint();
		
	}
	
	public JPanel getOKSPanel() {
		return OKSPanel;
	}

	public JMenuBar getOKSMenubar() {
		return OKSMenubar;
	}

	private class Download extends SwingWorker<Void, DlInfo> {

		@Override
		protected Void doInBackground() throws Exception {
			
			bDownload.setText("Downloading");
			bDownload.setEnabled(false);
			lNumber.setVisible(true);
			pbProgress.setVisible(true);
			miChange.setEnabled(false);
			
			
			for (int i = 0; i< cb.length; i++)
				cb[i].setEnabled(false);
			
			for (int i = 0; i< cb.length; i++) {
				if (cb[i].isSelected()) {
					Song song = songList.get(i);
					URL website = song.getMediaAddress();
					int fileSize = Crawler.getFileSize(website);
					try {
						ReadableByteChannel rbc = Channels.newChannel(website.openStream());
						FileOutputStream fos = new FileOutputStream(song.title+".m4a");
						int bufferSize = 60000;
						ByteBuffer buffer = ByteBuffer.allocate(bufferSize);
						int bytesRead = rbc.read(buffer);
						int loop = 0;
						int percent = 0;
						long currentSize = 0;
						while(bytesRead != -1 ) {
							
							buffer.flip();
							while(buffer.hasRemaining())
								fos.write(buffer.get());
							loop++;
							if (loop % 10 == 0) {
								currentSize = fos.getChannel().size();
								percent = (int) ((double)currentSize/(double)fileSize*100);
								publish(new DlInfo(false, i+1, percent));
							}
							
							buffer.clear();
						    bytesRead = rbc.read(buffer);
						}

						if (fileSize == fos.getChannel().size())
							publish(new DlInfo(true, i+1, 100));
						else 
							publish(new DlInfo(true, i+1, 0));
						fos.close();
					} catch (IOException e1) {
						e1.printStackTrace();
					}
				}
			}
			return null;
		}
		
		@Override
		protected void done() {
			bDownload.setText("Download");
			bDownload.setEnabled(true);
			miChange.setEnabled(true);
			for (int i = 0; i< cb.length; i++) {
				cb[i].setSelected(false);
				cb[i].setEnabled(true);
			}
			JOptionPane.showMessageDialog(null, 
					"Downloaded successfully!" ,
					"Done",
					JOptionPane.INFORMATION_MESSAGE);	
		}
		
		@Override
		protected void process(List<DlInfo> p) {
			boolean comp = p.get(p.size()-1).completed;
			int current = p.get(p.size()-1).number;
			int percent = p.get(p.size()-1).progress;
			
			lNumber.setText(Integer.toString(current));
			pbProgress.setValue(percent);
			pbProgress.validate();

			/**
			 * If the file is incomplete, display an error message.
			 */
			if (comp && (percent == 0)) {
				this.cancel(true);
				JOptionPane.showMessageDialog(null, 
						"Download failed due to the network, please retry downloading songs from current song.",
						"Warning",
						JOptionPane.WARNING_MESSAGE);
				
			}
		}
		
	}
	
	private class ListSongs extends SwingWorker<Void, Void> {

		@Override
		protected Void doInBackground() throws Exception {
			
			miChange.setEnabled(false);
			lSongs.setText("Listing...");
			playlist.listSongs();
			songList = playlist.listOfSongs;
		//	System.out.println(playlist.listOfSongs);
			return null;
		}
		
		@Override
		protected void done() {
			lSongs.setText("Songs: "+ playlist.numOfSongs);
			
			JPanel display = new JPanel();
			display.setPreferredSize(new Dimension(600, Integer.parseInt(playlist.numOfSongs)*30+30));
			display.setLayout(new GridLayout(Integer.parseInt(playlist.numOfSongs)+1, 1));
			display.setLocation(10, 180);
			
			cb = new JCheckBox[Math.min(Integer.parseInt(playlist.numOfSongs), 10)];
			
			JPanel labelRow = new JPanel(new GridLayout(1, 3));
			labelRow.setVisible(true);
			labelRow.add(new JLabel("    Title"));
			labelRow.add(new JLabel("Singer"));
			labelRow.add(new JLabel("Upload Time"));
			display.add(labelRow);
			
			int number = Math.min(songList.size(), 10);
			for (int i = 0; i< number; i++) {
				Song song = songList.get(i);
				JPanel row = new JPanel(new GridLayout(1, 3));
				row.setVisible(true);
				cb[i] = new JCheckBox(song.title+"   ");
				row.add(cb[i]);
				row.add(new JLabel(song.author));
				row.add(new JLabel(song.uploadTime));
				display.add(row);
			}	
			
			JScrollPane spSongsList = new JScrollPane(display, 
					ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
                    ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		//	int height = (Integer.parseInt(playlist.numOfSongs) < 10)? Integer.parseInt(playlist.numOfSongs)*30+30: 330;
			int height = 330;
			spSongsList.setSize(550, height);
			spSongsList.setLocation(10, 0);
			spSongsList.setVisible(true);
			container.add(spSongsList);
			
			container.validate();
			bListSongs.setEnabled(false);
			bDownload.setEnabled(true);
			miChange.setEnabled(true);
		}
		
	}
}
