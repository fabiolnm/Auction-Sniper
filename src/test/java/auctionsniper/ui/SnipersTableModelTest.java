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
	public void setsSniperValuesInColumns() {
		context.checking(new Expectations() {{
			one(listener).tableChanged(with(aRowChangedEvent()));
		}});
		model.updateSnapshot(SniperSnapshot.joining(ITEM_ID).bidding(555, 666));
		
		assertColumnEquals(0, ITEM_ID);
		assertColumnEquals(1, 555);
		assertColumnEquals(2, 666);
		assertColumnEquals(3, MainWindow.STATUS_BIDDING);
	}
	
	private void assertColumnEquals(int columnIndex, Object expected) {
		final int rowIndex = 0;
		assertEquals(expected, model.getValueAt(rowIndex, columnIndex));
	}
	
	private Matcher<TableModelEvent> aRowChangedEvent() {
		return samePropertyValuesAs(new TableModelEvent(model, 0));
	}	
}