package auctionsniper.xmpp;

import java.util.HashMap;

import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.MessageListener;
import org.jivesoftware.smack.packet.Message;

import auctionsniper.AuctionEventListener;
import auctionsniper.AuctionEventListener.PriceSource;
import auctionsniper.util.Defect;

public class AuctionMessageTranslator implements MessageListener {
	private final String sniperId;
	private AuctionEventListener listener;
	private final XmppFailureReporter failureReporter;

	public AuctionMessageTranslator(String sniperId, XmppFailureReporter failureReporter) {
		this.sniperId = sniperId;
		this.failureReporter = failureReporter;
	}

	public void setListener(AuctionEventListener listener) {
		this.listener = listener;
	}

	public void processMessage(Chat chat, Message message) {
		if (listener == null)
			throw new Defect("AuctionEventListener not set");
		try {
			translate(message.getBody());
		} catch (Exception e) {
			listener.auctionFailed();
		}
	}
	
	private void translate(String messageBody) throws MissingValueException {
		AuctionEvent event = AuctionEvent.from(messageBody);
		String eventType = event.type();
		if ("CLOSE".equals(eventType))
			listener.auctionClosed();
		else if ("PRICE".equals(eventType))
			listener.currentPrice(event.currentPrice(), event.increment(), event.isFrom(sniperId));
	}

	private static class AuctionEvent {
		static AuctionEvent from(String messageBody) {
			AuctionEvent event = new AuctionEvent();
			for (String field : fieldsIn(messageBody))
				event.addField(field);
			return event;
		}

		static String[] fieldsIn(String messageBody) {
			return messageBody.split(";");
		}

		private final HashMap<String, String> fields = new HashMap<String, String>();

		public String type() throws MissingValueException {
			return get("Event");
		}

		public int currentPrice() throws MissingValueException {
			return getInt("CurrentPrice");
		}

		public int increment() throws MissingValueException {
			return getInt("Increment");
		}

		public String bidder() throws MissingValueException {
			return get("Bidder");
		}

		public PriceSource isFrom(String sniperId) throws MissingValueException {
			return bidder().equals(sniperId) ? PriceSource.FromSniper :	PriceSource.FromOtherBidder;
		}

		private int getInt(String fieldName) throws MissingValueException {
			return Integer.parseInt(get(fieldName));
		}

		private String get(String fieldName) throws MissingValueException {
			String value = fields.get(fieldName);
			if (value == null)
				throw new MissingValueException(fieldName);
			return value;
		}

		private void addField(String field) {
			String[] pair = field.split(":");
			fields.put(pair[0].trim(), pair[1].trim());
		}
	}
}