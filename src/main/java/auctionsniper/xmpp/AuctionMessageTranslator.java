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

	public AuctionMessageTranslator(String sniperId) {
		this.sniperId = sniperId;
	}

	public void setListener(AuctionEventListener listener) {
		this.listener = listener;
	}

	public void processMessage(Chat chat, Message message) {
		if (listener == null)
			throw new Defect("AuctionEventListener not set");
			
		AuctionEvent event = AuctionEvent.from(message.getBody());
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

		public String type() {
			return get("Event");
		}

		public int currentPrice() {
			return getInt("CurrentPrice");
		}

		public int increment() {
			return getInt("Increment");
		}

		public String bidder() {
			return get("Bidder");
		}

		public PriceSource isFrom(String sniperId) {
			return bidder().equals(sniperId) ? PriceSource.FromSniper :	PriceSource.FromOtherBidder;
		}

		private int getInt(String fieldName) {
			return Integer.parseInt(get(fieldName));
		}

		private String get(String fieldName) {
			return fields.get(fieldName);
		}

		private void addField(String field) {
			String[] pair = field.split(":");
			fields.put(pair[0].trim(), pair[1].trim());
		}
	}
}