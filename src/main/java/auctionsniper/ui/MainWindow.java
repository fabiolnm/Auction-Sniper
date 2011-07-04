package auctionsniper.ui;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;

import auctionsniper.UserRequestListener;

public class MainWindow extends JFrame {
	public static final String NAME = "Auction Sniper Main";
	public static final String TITLE = "Auction Sniper";
	
	public static final String SNIPER_TABLE_NAME = "sniper_table";
	public static final String NEW_ITEM_ID_NAME = "new_item_input";
	public static final String JOIN_BUTTON_NAME = "join_button";
	
	public static final String STATUS_JOINING = "Joining";
	public static final String STATUS_LOST = "Lost";
	public static final String STATUS_BIDDING = "Bidding";
	public static final String STATUS_WINNING = "Winning";
	public static final String STATUS_WON = "Won";
	
	public MainWindow(SnipersTableModel snipers) {
		super(TITLE);
		setName(NAME);
		
		fillContentPane(makeControls(), createSniperTable(snipers));
		
		pack(); // fit to prefered size
		setVisible(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
	private void fillContentPane(JPanel controls, JTable snipersTable) {
	    final Container contentPane = getContentPane(); 
	    contentPane.setLayout(new BorderLayout()); 
	    contentPane.add(controls, BorderLayout.NORTH); 
	    contentPane.add(new JScrollPane(snipersTable), BorderLayout.CENTER); 
	}

	private JPanel makeControls() {
		JPanel controls = new JPanel(new FlowLayout());
		final JTextField itemIdField = new JTextField();
		itemIdField.setColumns(25);
		itemIdField.setName(NEW_ITEM_ID_NAME);
		controls.add(itemIdField);
		
		JButton joinAuctionButton = new JButton("Join Auction");
		joinAuctionButton.setName(JOIN_BUTTON_NAME);
		controls.add(joinAuctionButton);
		return controls;
	}
	
	private JTable createSniperTable(SnipersTableModel snipers) {
		JTable table = new JTable(snipers);
		table.setName(SNIPER_TABLE_NAME);
		return table;
	}

	public void addUserRequestListener(UserRequestListener userRequestListener) {
		// TODO Auto-generated method stub
		
	}
}