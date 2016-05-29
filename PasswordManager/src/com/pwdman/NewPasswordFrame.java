package com.pwdman;

import java.awt.Component;
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
import javax.swing.WindowConstants;

public class NewPasswordFrame extends JFrame implements ActionListener{
	private JLabel desc, status, passwordLabel, confirmLabel;
	private JPasswordField passwordField, confirmField;
	private JPanel descPanel, statusPanel, passwordPanel, confirmPanel, buttonsPanel;
	private JButton submit, cancel;
	private Manager man;
	public NewPasswordFrame(Manager m){
		man = m;
		this.setTitle("Set an Admin Password");
		this.setSize(300, 200);
		
		this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		BoxLayout box = new BoxLayout(this.getContentPane(), BoxLayout.Y_AXIS);
		this.setLayout(box);
		
		
		desc = new JLabel("<html><div style='text-align: center;'>Please type an admin password to use : <br/>" +  
				"(This cannot be changed or recovered!)</html>");
		descPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
		descPanel.add(desc);
		
		passwordLabel = new JLabel("Password: ");
		passwordField = new JPasswordField(20);
		passwordPanel = new JPanel(new FlowLayout(FlowLayout.TRAILING));
		passwordPanel.add(passwordLabel);
		passwordPanel.add(passwordField);
		
		confirmLabel = new JLabel("Confirm: ");
		confirmField = new JPasswordField(20);
		confirmPanel = new JPanel(new FlowLayout(FlowLayout.TRAILING));
		confirmPanel.add(confirmLabel);
		confirmPanel.add(confirmField);
		
		submit = new JButton("SUBMIT");
		submit.addActionListener(this);
		cancel = new JButton("CLOSE");
		cancel.addActionListener(this);
		buttonsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
		buttonsPanel.add(submit);
		buttonsPanel.add(cancel);
		
		status = new JLabel("<html><br/><br/></html>");
		statusPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
		statusPanel.add(status);
		
		this.add(descPanel);
		this.add(passwordPanel);
		this.add(confirmPanel);
		this.add(statusPanel);
		this.add(buttonsPanel);
		
		this.pack();
		this.setVisible(true);
		
	}
	
	//Check for any errors in the new account
		public String verifyAccount(){
			String error = "<html><div style='text-align: center;'>";
			
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
		if(e.getSource() == submit){
			String errors = verifyAccount();
			status.setText(errors);
			if(errors.equals("<html><div style='text-align: center;'></html>")){
				man.init(passwordField.getPassword());
				this.dispose();
				
			}
			
		}
		else if(e.getSource() == cancel){
			Logger.info("Closing program...");
			System.exit(0);
		}
		
	}
}
