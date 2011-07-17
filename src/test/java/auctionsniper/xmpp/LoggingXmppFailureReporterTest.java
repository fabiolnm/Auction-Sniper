package auctionsniper.xmpp;

import static auctionsniper.xmpp.LoggingXmppFailureReporter.COULD_NOT_TRANSLATE_MESSAGE_FORMAT;

import java.util.logging.LogManager;
import java.util.logging.Logger;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.jmock.lib.legacy.ClassImposteriser;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(JMock.class)
public class LoggingXmppFailureReporterTest {
	private final Mockery context = new Mockery() {{
		setImposteriser(ClassImposteriser.INSTANCE);
	}};
	private final Logger logger = context.mock(Logger.class);
	private final LoggingXmppFailureReporter reporter = new LoggingXmppFailureReporter(logger);
	
	@Test 
	public void writesMessageTranslationFailureToLog() {
		final String sniperId = "a sniper", badMessage = "bad message";
		final Exception badException = new Exception("bad");
		context.checking(new Expectations() {{
			oneOf(logger).severe(String.format(COULD_NOT_TRANSLATE_MESSAGE_FORMAT, sniperId, badMessage, badException));
		}}); 
		reporter.cannotTranslateMessage(sniperId, badMessage, badException);
	}
	
	@After
	public void resetLogging() {
		LogManager.getLogManager().reset();
	}
}
