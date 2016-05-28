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
	private JPanel accountPanel, userPanel, passwordPanel, confirmPanel, buttonPanel, statusPanel;
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
		
		statusPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
		status = new JLabel("<html><div style='text-align: center;'><br/><br/><br/><br/></html>");
		statusPanel.add(status);
		
		this.add(accountPanel);
		this.add(userPanel);
		this.add(passwordPanel);
		this.add(confirmPanel);
		this.add(buttonPanel);
		this.add(statusPanel);
		this.pack();
		this.setResizable(false);
		this.setVisible(true);
	}
	
	//verify and add acount
	public boolean submitAccount(){
		String error = verifyAccount();
		if(error.equals("")){
			Logger.info("Adding new account");
			man.addAccount(accountField.getText(), userField.getText(), passwordField.getPassword());
			return true;
		}
		else{
			status.setText(error);
			return false;
		}
	}
	
	//Check for any errors in the new account
	public String verifyAccount(){
		String error = "<html><div style='text-align: center;'>";
		if(accountField.getText().isEmpty()){
			error += "Account name is empty<br/>";
		}
		if(userField.getText().isEmpty()){
			error+="Username is empty<br/>";
		}
		if(passwordField.getPassword().length == 0){
			error += "Password is empty<br/>";
		}
		if(!Arrays.equals(passwordField.getPassword(), confirmField.getPassword())){
			error += "Passwords do not match!";
		}
		error+="</html>";
		Logger.info("New account verification resulted in the following errors: " + error);
		return error;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		boolean shouldClose = true;
		if(e.getSource() == submit){
			shouldClose = submitAccount();
		}
		if(shouldClose){
			man.enableMainFrame();
			this.setVisible(false);
			this.dispose();
		}
	}
}
