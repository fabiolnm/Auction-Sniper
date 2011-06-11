package auctionsniper.ui;

import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertEquals;
import static org.hamcrest.Matchers.equalTo;
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
			one(listener).tableChanged(with(aRowChangedEvent()));
		}});
		model.updateSnapshot(SniperSnapshot.joining(ITEM_ID).bidding(555, 666));
		
		assertColumnEquals(Column.ITEM_IDENTIFIER, ITEM_ID);
		assertColumnEquals(Column.LAST_PRICE, 555);
		assertColumnEquals(Column.LAST_BID, 666);
		assertColumnEquals(Column.SNIPER_STATE, MainWindow.STATUS_BIDDING);
	}
	
	@Test
	public void addSniperAndNotifyTableListener() {
		context.checking(new Expectations() {{
			one(listener).tableChanged(with(anInsertionAtRow(0)));
		}});
		
		assertEquals(0, model.getRowCount());
		
		model.addSniper(SniperSnapshot.joining(ITEM_ID));

		assertEquals(1, model.getRowCount());

		assertColumnEquals(Column.ITEM_IDENTIFIER, ITEM_ID);
		assertColumnEquals(Column.LAST_PRICE, 0);
		assertColumnEquals(Column.LAST_BID, 0);
		assertColumnEquals(Column.SNIPER_STATE, MainWindow.STATUS_JOINING);
	}
	
	private void assertColumnEquals(Column column, Object expected) {
		final int rowIndex = 0, columnIndex = column.ordinal();
		assertEquals(expected, model.getValueAt(rowIndex, columnIndex));
	}
	
	private Matcher<TableModelEvent> aRowChangedEvent() {
		return samePropertyValuesAs(new TableModelEvent(model, 0));
	}
	
	private Matcher<TableModelEvent> anInsertionAtRow(int row) {
		return samePropertyValuesAs(
			new TableModelEvent(model, row, row, 
					TableModelEvent.ALL_COLUMNS, 
					TableModelEvent.INSERT));
	}	
}