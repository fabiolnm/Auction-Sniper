package auctionsniper;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.SwingUtilities;

import auctionsniper.ui.MainWindow;
import auctionsniper.ui.SnipersTableModel;
import auctionsniper.xmpp.XmppAuctionHouse;

public class Main {
	private static final int ARG_HOSTNAME = 0;
	private static final int ARG_USERNAME = 1;
	private static final int ARG_PASSWORD = 2;
	
	public static void main(String... args) throws Exception {
		String host = args[ARG_HOSTNAME], user = args[ARG_USERNAME], password = args[ARG_PASSWORD];
		XmppAuctionHouse auctionHouse = XmppAuctionHouse.connectTo(host, user, password);

		Main main = new Main();
		main.disconnectWhenUiCloses(auctionHouse);
		main.addRequestListenerFor(auctionHouse);
	}
	
	private MainWindow ui;
	private SnipersTableModel snipers = new SnipersTableModel();
	
	public Main() throws Exception {
		startUserInterface();
	}

	private void startUserInterface() throws Exception {
		SwingUtilities.invokeAndWait(new Runnable() {
			public void run() {
				ui = new MainWindow(snipers);
			}
		});
	}
	
	private void addRequestListenerFor(XmppAuctionHouse auctionHouse) {
		ui.addUserRequestListener(new SniperLauncher(auctionHouse, snipers));
	}
	
	private void disconnectWhenUiCloses(final XmppAuctionHouse auctionHouse) {
		ui.addWindowListener(new WindowAdapter() {
		   @Override 
		   public void windowClosed(WindowEvent e) {
			   auctionHouse.disconnect();
		   }
		});
	}
}