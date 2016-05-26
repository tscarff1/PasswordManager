package com.pwdman;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.WindowConstants;

public class AddAccountFrame extends JFrame implements ActionListener{
	private JTextField accountField, userField;
	private JPasswordField passwordField, confirmField;
	private JButton submit, cancel;
	private JLabel accountLabel, userLabel, passwordLabel, confirmLabel, status;
	private JPanel accountPanel, userPanel, passwordPanel, confirmPanel, buttonPanel;
	private Manager man;
	
	public AddAccountFrame(Manager man){
		this.setTitle("Add New Account");
		this.man = man;
		
		this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		BoxLayout box = new BoxLayout(this.getContentPane(), BoxLayout.Y_AXIS);
		this.setLayout(box);
		accountLabel = new JLabel("Account name: ");
		accountField = new JTextField(20);
		accountPanel = new JPanel(new FlowLayout(FlowLayout.TRAILING));
		accountPanel.add(accountLabel);
		accountPanel.add(accountField);
		
		userLabel = new JLabel("Username: ");
		userField = new JTextField(20);
		userPanel = new JPanel(new FlowLayout(FlowLayout.TRAILING));
		userPanel.add(userLabel);
		userPanel.add(userField);
		
		passwordLabel = new JLabel("Password: ");
		passwordField = new JPasswordField(20);
		passwordPanel = new JPanel(new FlowLayout(FlowLayout.TRAILING));
		passwordPanel.add(passwordLabel);
		passwordPanel.add(passwordField);
		
		confirmLabel = new JLabel("confirm: ");
		confirmField = new JPasswordField(20);
		confirmPanel = new JPanel(new FlowLayout(FlowLayout.TRAILING));
		confirmPanel.add(confirmLabel);
		confirmPanel.add(confirmField);
		
		submit = new JButton("SUBMIT");
		submit.addActionListener(this);
		cancel = new JButton("CANCEL");
		cancel.addActionListener(this);
		buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
		buttonPanel.add(submit);
		buttonPanel.add(cancel);
		
		status = new JLabel("");
		
		this.add(accountPanel);
		this.add(userPanel);
		this.add(passwordPanel);
		this.add(confirmPanel);
		this.add(buttonPanel);
		this.add(status);
		this.pack();
		this.setResizable(false);
		this.setVisible(true);
	}
	
	//verify and add acount
	public void submitAccount(){
		String error = verifyAccount();
		if(error.equals("")){
			man.addAccount(accountField.getText(), userField.getText(), passwordField.getPassword());
		}
		else{
			status.setText("");
		}
	}
	
	//Check for any errors in the new account
	public String verifyAccount(){
		String error = "";
		if(accountField.getText().isEmpty()){
			error += "Account name is empty ";
		}
		if(userField.getText().isEmpty()){
			error+="Username is empty ";
		}
		if(passwordField.getPassword().length == 0){
			error += "Password is empty ";
		}
		if(!Arrays.equals(passwordField.getPassword(), confirmField.getPassword())){
			error += "Passwords do not match!";
		}
		return error;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == submit){
			submitAccount();
		}
		this.setVisible(false);
		this.dispose();
	}
}