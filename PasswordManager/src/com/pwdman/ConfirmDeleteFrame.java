package com.pwdman;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.WindowConstants;

public class ConfirmDeleteFrame extends JFrame implements ActionListener{
	private JLabel label;
	private JButton yes, no;
	private JPanel labelPanel, buttonsPanel;
	private int row;
	private Manager man;

	
	public ConfirmDeleteFrame(int row, Manager man){
		this.row = row;
		this.man = man;
		this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		
		BoxLayout box = new BoxLayout(this.getContentPane(), BoxLayout.Y_AXIS);
		this.setLayout(box);
		labelPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
		label = new JLabel("Are you sure you want to delete row " + (row + 1) + "?");
		labelPanel.add(label);
		
		yes = new JButton("Yes");
		no = new JButton("No");
		yes.addActionListener(this);
		no.addActionListener(this);
		
		JPanel buttonsPanel = new JPanel();
		buttonsPanel.add(yes);
		buttonsPanel.add(no);
		
		this.add(labelPanel);
		this.add(buttonsPanel);
		this.pack();
		this.setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == yes){
			Logger.info("Deleting row " + row);
			man.deleteRow(row);
		}
		this.dispose();
	}
}
