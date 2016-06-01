package com.pwdman;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ScrollPaneConstants;
import javax.swing.UIManager;
import javax.swing.WindowConstants;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
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
		
		menuBar.add(menu);
		this.setJMenuBar(menuBar);
		dm = new DefaultTableModel(0,4);
		table = new JTable(dm);
		table.setRowHeight(25);
		for(int i = 0; i < NUMCOLS; i++){
			TableColumn col = table.getColumnModel().getColumn(i);
			col.setHeaderValue(headers[i]);
			if(i==2){
				col.setCellRenderer(new PasswordCellRenderer());
			}
			if(i==3){ //Delete button column
				col.setCellRenderer( new ButtonRenderer() );
				col.setCellEditor(new ButtonEditor(new JCheckBox()));
				col.setMaxWidth(50);
			}
		}
		table.putClientProperty("terminateEditOnFocusLost", true);
		table.getModel().addTableModelListener(this);
		table.setAutoCreateRowSorter(true);
		
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

	public void addAccount(String account, String user, char[] password){
		DefaultTableModel model = (DefaultTableModel) table.getModel();
		model.addRow(new Object[]{account, user, new String(password)});
	}
	
	public void deleteRow(int row){
		DefaultTableModel model = (DefaultTableModel) table.getModel();
		model.removeRow(row);
	}
	
	@Override
	public void tableChanged(TableModelEvent arg0) {
			man.setAccounts(getAccounts(), getUsers(), getPasswords());
			man.writeAccountsToFile();
	}



	@Override
	public void actionPerformed(ActionEvent e) {
		Logger.info(e.getActionCommand());
		if(e.getSource() == addItem){
			this.setEnabled(false);
			new AddAccountFrame(man);
		}
	}
	
	class ButtonRenderer extends JButton implements TableCellRenderer {

		  public ButtonRenderer() {
		    setOpaque(true);
		  }

		  public Component getTableCellRendererComponent(JTable table, Object value,
		      boolean isSelected, boolean hasFocus, int row, int column) {
		    if (isSelected) {
		      setForeground(table.getSelectionForeground());
		      setBackground(table.getSelectionBackground());
		    } else {
		      setForeground(table.getForeground());
		      setBackground(UIManager.getColor("Button.background"));
		    }
		    setText("X");
		    return this;
		  }
		}

		/**
		 * @version 1.0 11/09/98
		 */

		class ButtonEditor extends DefaultCellEditor {
		  protected JButton button;

		  private String label;

		  private boolean isPushed;
		  private int row;

		  public ButtonEditor(JCheckBox checkBox) {
		    super(checkBox);
		    button = new JButton();
		    button.setOpaque(true);
		    button.addActionListener(new ActionListener() {
		      public void actionPerformed(ActionEvent e) {
		        fireEditingStopped();
		      }
		    });
		  }

		  public Component getTableCellEditorComponent(JTable table, Object value,
		      boolean isSelected, int row, int column) {
			  button.setText("X");
		    if (isSelected) {
		      button.setForeground(table.getSelectionForeground());
		      button.setBackground(table.getSelectionBackground());
		    } else {
		      button.setForeground(table.getForeground());
		      button.setBackground(table.getBackground());
		    }
		    label = (value == null) ? "" : value.toString();
		    Logger.info("Delete button pressed. " + label);
		    
		    isPushed = true;
		    this.row = row;
		    return button;
		  }

		  public Object getCellEditorValue() {
		    isPushed = false;
		    return new String(label);
		  }

		  public boolean stopCellEditing() {
		    isPushed = false;
		    return super.stopCellEditing();
		  }

		  protected void fireEditingStopped() {
		    super.fireEditingStopped();
		    setEnabled(false);
		    new ConfirmDeleteFrame(row, man);
		  }
		}

}

