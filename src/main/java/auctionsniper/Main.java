package auctionsniper;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.SwingUtilities;

import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.MessageListener;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.Message;

import auctionsniper.ui.MainWindow;

public class Main {
	private static final int ARG_HOSTNAME = 0;
	private static final int ARG_USERNAME = 1;
	private static final int ARG_PASSWORD = 2;
	private static final int ARG_ITEM_ID = 3;
	
	public static final String AUCTION_RESOURCE = "Auction";
	public static final String ITEM_ID_AS_LOGIN = "auction-%s";
	public static final String AUCTION_ID_FORMAT =
		ITEM_ID_AS_LOGIN + "@%s/" + AUCTION_RESOURCE;
	
	public static final String JOIN_COMMAND_FORMAT = "SOLVersion: 1.1; Command: JOIN;";
	
	public static void main(String... args) throws Exception {
		String host = args[ARG_HOSTNAME], 
			user = args[ARG_USERNAME], 
			password = args[ARG_PASSWORD],
			itemId = args[ARG_ITEM_ID];

		XMPPConnection connection = connectTo(host, user, password);
		
		Main main = new Main();
		main.joinAuction(connection, itemId);
	}
	
	private MainWindow ui;
	
	public Main() throws Exception {
		startUserInterface();
	}

	private void startUserInterface() throws Exception {
		SwingUtilities.invokeAndWait(new Runnable() {
			public void run() {
				ui = new MainWindow();
			}
		});
	}

	private void joinAuction(XMPPConnection connection, String itemId) throws XMPPException {
		disconnectWhenUiCloses(connection);
		String auctionId = String.format(AUCTION_ID_FORMAT, itemId, connection.getServiceName());
		Chat chat = connection.getChatManager().createChat(auctionId, new MessageListener() {
			public void processMessage(Chat aChat, Message message) {
				// this method sshould be used when an 
				// application thread needs to update the GUI
				SwingUtilities.invokeLater(new Runnable() {
					public void run() {
						ui.showStatusLost();
					}
				});
			}
		});
		chat.sendMessage(JOIN_COMMAND_FORMAT);
	}

	private void disconnectWhenUiCloses(final XMPPConnection connection) {
		ui.addWindowListener(new WindowAdapter() {
		   @Override 
		   public void windowClosed(WindowEvent e) {
		      connection.disconnect();
		   }
		});
	}
	
	private static XMPPConnection connectTo(String hostname, String username, String password)
	throws XMPPException {
		XMPPConnection connection = new XMPPConnection(hostname);
		connection.connect();
		connection.login(username, password, AUCTION_RESOURCE);
		return connection;
	}	
}