package com.pwdman;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.WindowConstants;

public class StartFrame extends JFrame implements ActionListener{
	private JButton startBut, cancelBut;
	private Manager man;
	private JLabel statusLabel;
	private JPasswordField passwordField;
	
	public StartFrame(Manager m){
		man = m;
		this.setTitle("Password Manager");
		this.setSize(300, 200);
		
		this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		
		BoxLayout box = new BoxLayout(this.getContentPane(), BoxLayout.Y_AXIS);
		this.setLayout(box);
		JLabel startLabel = new JLabel("Please enter admin password: ");
		startLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		passwordField = new JPasswordField(10);
		passwordField.setMaximumSize(new Dimension(250,20));
		passwordField.setAlignmentX(Component.CENTER_ALIGNMENT);
		
		startBut = new JButton("Access Accounts");
		cancelBut = new JButton("Cancel");
		startBut.addActionListener(this);
		cancelBut.addActionListener(this);
		
		JPanel buttonsPanel = new JPanel();
		buttonsPanel.add(startBut);
		buttonsPanel.add(cancelBut);
		
		statusLabel = new JLabel("");
		statusLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		
		this.add(Box.createVerticalGlue());
		this.add(startLabel);
		this.add(passwordField);
		this.add(buttonsPanel);
		this.add(statusLabel);
		this.add(Box.createVerticalGlue());
		this.setVisible(true);
	}
	
		@Override
		public void actionPerformed(ActionEvent e) {
			JButton source =(JButton) e.getSource();
			if(source == startBut){
				if(man.attemptLogin(new String(passwordField.getPassword()).getBytes())){
					statusLabel.setText("Correct Password");
					this.dispose();
				}
				else{
					statusLabel.setText("Incorrect password");
				}
			}
			if(source == cancelBut){
				this.setDefaultCloseOperation(EXIT_ON_CLOSE);
				System.exit(0);
			}
			
		}
}
