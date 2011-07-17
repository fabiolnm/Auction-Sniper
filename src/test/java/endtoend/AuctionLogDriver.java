package endtoend;

import static org.junit.Assert.assertThat;

import java.io.File;
import java.io.IOException;
import java.util.logging.LogManager;

import org.apache.commons.io.FileUtils;
import org.hamcrest.Matcher;

import auctionsniper.xmpp.XmppAuctionHouse;

public class AuctionLogDriver {
	private final File logFile = new File(XmppAuctionHouse.LOG_FILE_NAME);

	public void hasEntry(Matcher<String> matcher) throws IOException {
		assertThat(FileUtils.readFileToString(logFile), matcher);
	}

	public void clearLog() {
		LogManager.getLogManager().reset();
		logFile.delete();
	}
}
