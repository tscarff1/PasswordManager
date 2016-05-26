package com.pwdman;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ScrollPaneConstants;
import javax.swing.WindowConstants;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;

public class MainFrame extends JFrame implements TableModelListener, ActionListener{
	private Manager man;
	private JMenuBar menuBar;
	private JMenu menu, subMenu;
	private JMenuItem addItem, changeItem;
	
	private JTableHeader header;
	private JScrollPane scrollPane;
	String[] headers = {"Account", "Username", "Password", "Delete"};
	private DefaultTableModel dm;
	private JTable table;
	private final int NUMCOLS = 4;
	
	
	
	public MainFrame(Manager man){
		this.man = man;
		this.setTitle("Password Manager");
		this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		this.setMinimumSize(new Dimension(700,500));
		
		menu = new JMenu("File");
		menuBar = new JMenuBar();
		
		
		addItem = new JMenuItem("Add Account");
		addItem.addActionListener(this);
		menu.add(addItem);
		
		changeItem = new JMenuItem("Change Admin Password");
		changeItem.addActionListener(this);
		menu.add(changeItem);
		
		menuBar.add(menu);
		this.setJMenuBar(menuBar);
		dm = new DefaultTableModel(10,4);
		table = new JTable(dm);
		table.setRowHeight(25);
		for(int i = 0; i < NUMCOLS; i++){
			TableColumn col = table.getColumnModel().getColumn(i);
			col.setHeaderValue(headers[i]);;
			if(i==3){
				col.setCellEditor(null);
				col.setMaxWidth(50);
			}
		}
		table.putClientProperty("terminateEditOnFocusLost", true);
		table.getModel().addTableModelListener(this);
		
		
		scrollPane = new JScrollPane(table);
		scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		table.setFillsViewportHeight(true);
		
		this.add(scrollPane);
		
		//man.writeToFile();
	}
	
	
	
	public String[] getAccounts(){
		String[] accounts = new String[table.getModel().getRowCount()];
		for(int i = 0; i< table.getModel().getRowCount(); i++){
			accounts[i] = (String) table.getModel().getValueAt(i, 0);
		}
		return accounts;
	}
	
	public String[] getUsers(){
		String[] users = new String[table.getModel().getRowCount()];
		for(int i = 0; i< table.getModel().getRowCount(); i++){
			users[i] = (String) table.getModel().getValueAt(i, 1);
		}
		return users;
	}
	
	public String[] getPasswords(){
		String[] passwords = new String[table.getModel().getRowCount()];
		for(int i = 0; i< table.getModel().getRowCount(); i++){
			passwords[i] = (String) table.getModel().getValueAt(i, 2);
		}
		return passwords;
	}
	
	public JTable getTable(){
		return table;
	}

	@Override
	public void tableChanged(TableModelEvent arg0) {
		if(man.isInit() && man.fileCreated()){
			man.setAccounts(getAccounts(), getUsers(), getPasswords());
			man.writeToFile();
		}
		
	}



	@Override
	public void actionPerformed(ActionEvent e) {
		Logger.info(e.getActionCommand());
		if(e.getSource() == addItem){
			new AddAccountFrame(man);
		}
	}
}
