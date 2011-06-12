package auctionsniper.ui;

import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertEquals;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.samePropertyValuesAs;

import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;

import org.hamcrest.Matcher;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import auctionsniper.SniperSnapshot;
import auctionsniper.util.Defect;

@RunWith(JMock.class)
public class SnipersTableModelTest {
	private final String ITEM_ID = "item id";
	private final Mockery context = new Mockery();
	private final SnipersTableModel model = new SnipersTableModel();
	private TableModelListener listener = context.mock(TableModelListener.class);
	
	@Before 
	public void attachModelListener() {
		model.addTableModelListener(listener);
	}
	
	@Test 
	public void hasEnoughColumns() {
		assertThat(model.getColumnCount(), equalTo(4));
	}
	
	@Test 
	public void	setsUpColumnHeadings() {
		for (Column column: Column.values())
			assertEquals(column.header(), model.getColumnName(column.ordinal()));
	}
	
	@Test
	public void setsSniperValuesInColumns() {
		context.checking(new Expectations() {{
			allowing(listener).tableChanged(with(anyInsertionEvent()));
			one(listener).tableChanged(with(aChangeAtRow(0)));
		}});
		
		SniperSnapshot joining = SniperSnapshot.joining(ITEM_ID);
		SniperSnapshot bidding = joining.bidding(555, 666);
		model.addSniper(joining);
		
		model.updateSnapshot(bidding);
		assertRowMatchesSnapshot(0, bidding);
	}
	
	@Test
	public void addSniperAndNotifyTableListener() {
		context.checking(new Expectations() {{
			one(listener).tableChanged(with(anInsertionAtRow(0)));
		}});
		
		SniperSnapshot joining = SniperSnapshot.joining(ITEM_ID);
		
		assertEquals(0, model.getRowCount());
		model.addSniper(joining);
		assertEquals(1, model.getRowCount());

		assertRowMatchesSnapshot(0, joining);
	}
	
	@Test 
	public void holdsSnipersInAdditionOrder() {
		context.checking(new Expectations() {{
			ignoring(listener);
		}});
		
		model.addSniper(SniperSnapshot.joining("item 0"));
		model.addSniper(SniperSnapshot.joining("item 1"));
		
		assertEquals("item 0", cellValue(0, Column.ITEM_IDENTIFIER));
		assertEquals("item 1", cellValue(1, Column.ITEM_IDENTIFIER));
	}

	@Test
	public void updatesCorrectRowForSniper() {
		context.checking(new Expectations() {{
			ignoring(listener);
		}});
		
		model.addSniper(SniperSnapshot.joining("item 0"));
		
		SniperSnapshot snapshot = SniperSnapshot.joining("item 1");
		model.addSniper(snapshot);

		SniperSnapshot winning = snapshot.winning(1000);
		model.sniperStateChanged(winning);
		
		assertRowMatchesSnapshot(1, winning);
	}
	
	@Test(expected=Defect.class)
	public void throwsDefectIfNoExistingSniperForAnUpdate() {
		model.sniperStateChanged(SniperSnapshot.joining("item 0"));
	}
	
	private void assertRowMatchesSnapshot(int row, SniperSnapshot snapshot) {
		assertColumnEquals(row, Column.ITEM_IDENTIFIER, snapshot.itemId);
		assertColumnEquals(row, Column.LAST_PRICE, snapshot.lastPrice);
		assertColumnEquals(row, Column.LAST_BID, snapshot.lastBid);
		assertColumnEquals(row, Column.SNIPER_STATE, SnipersTableModel.textFor(snapshot.status));
	}
	
	private void assertColumnEquals(int rowIndex, Column column, Object expected) {
		assertEquals(expected, cellValue(rowIndex, column));
	}
	
	private Object cellValue(int rowIndex, Column column) {
		final int columnIndex = column.ordinal();
		return model.getValueAt(rowIndex, columnIndex);
	}
	
	private Matcher<TableModelEvent> aChangeAtRow(int row) {
		return samePropertyValuesAs(new TableModelEvent(model, row));
	}
	
	private Matcher<TableModelEvent> anyInsertionEvent() {
	    return hasProperty("type", equalTo(TableModelEvent.INSERT));
	}
	
	private Matcher<TableModelEvent> anInsertionAtRow(int row) {
		return samePropertyValuesAs(
			new TableModelEvent(model, row, row, 
					TableModelEvent.ALL_COLUMNS, 
					TableModelEvent.INSERT));
	}	
}