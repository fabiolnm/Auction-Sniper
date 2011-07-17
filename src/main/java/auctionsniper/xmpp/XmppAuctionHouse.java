package auctionsniper.xmpp;

import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import org.jivesoftware.smack.XMPPConnection;

import auctionsniper.Auction;
import auctionsniper.AuctionHouse;

public class XmppAuctionHouse implements AuctionHouse {
	public static final String ITEM_ID_AS_LOGIN = "auction-%s";
	public static final String AUCTION_RESOURCE = "Auction";
	public static final String AUCTION_ID_FORMAT =
		ITEM_ID_AS_LOGIN + "@%s/" + AUCTION_RESOURCE;

	private final XMPPConnection connection;
	
	private final LoggingXmppFailureReporter failureReporter;
	public static final String LOG_FILE_NAME = "auction-sniper.log"; 
	private FileHandler loggingFileHandler;

	private XmppAuctionHouse(XMPPConnection connection) throws XmppAuctionException {
		this.connection = connection;
		this.failureReporter = new LoggingXmppFailureReporter(makeLogger());
	}

	public static XmppAuctionHouse connectTo(String hostname, String username, String password)
	throws XmppAuctionException {
		try {
			XMPPConnection connection = new XMPPConnection(hostname);
			connection.connect();
			connection.login(username, password, AUCTION_RESOURCE);
			return new XmppAuctionHouse(connection);
		} catch (Exception e) {
			throw new XmppAuctionException(e);
		}
	}

	public void disconnect() {
		connection.disconnect();
		loggingFileHandler.close();
	}

	public Auction auctionFor(String itemId) {
		return new XMPPAuction(connection, itemId, failureReporter);
	}
	
	private Logger makeLogger() throws XmppAuctionException {
		Logger logger = Logger.getLogger(XmppAuctionHouse.class.getName()); 
		logger.setUseParentHandlers(false); 
		logger.addHandler(simpleFileHandler());
		return logger;
	}
	
	private FileHandler simpleFileHandler() throws XmppAuctionException {
		try { 
			loggingFileHandler = new FileHandler(LOG_FILE_NAME);
			loggingFileHandler.setFormatter(new SimpleFormatter());
			return loggingFileHandler;
		} catch (Exception e) { 
			throw new XmppAuctionException("Could not create logger FileHandler");
		} 
	}
}
