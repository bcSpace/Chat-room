package client.gui;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import client.main.Chatter;

public class ClientGui {
	
	private Chatter chat;
	
	private JFrame frame;

	private JPanel startPanel; 
	private JLabel ipLabel = new JLabel("IP: ");
	private JTextField ipField; 
	private JButton connectButton; 
	
	//the chat frame to go to when connected
	private MainChat mainChat;
	
	public ClientGui(Chatter chat) {
		this.chat = chat;
		mainChat = new MainChat(chat);
		
		createGui();
	}
	
	private void createGui() {
		ipField = new JTextField("localhost",10);
		connectButton = new JButton("Connect");
		frame = new JFrame("Chat");
		frame.setSize(300, 150);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocationRelativeTo(null);
		
		startPanel = new JPanel();
		startPanel.setPreferredSize(new Dimension(300,150));
		
		startPanel.setLayout(new FlowLayout());
		startPanel.add(ipLabel);
		startPanel.add(ipField);
		startPanel.add(connectButton);
		frame.add(startPanel);
		connectButton.addActionListener(new ActionListener() { 
			  public void actionPerformed(ActionEvent e) { 
				  connect();
			  } 
		});
	}
	
	public void start() {
		frame.setVisible(true);
	}
	
	public void restart() {
		mainChat.restart();
		frame.setVisible(true);
		connectButton.setEnabled(true);
	}
	
	public void connect() {
		connectButton.setEnabled(false);
		chat.connect(ipField.getText(), 25565);
	}
	
	public void connectionFailed(String s) {
		JOptionPane.showMessageDialog(frame, s, "Failed", JOptionPane.WARNING_MESSAGE);
		connectButton.setEnabled(true);
	}
	
	public void connectionAccepted() {
		chat.testUsername(JOptionPane.showInputDialog(frame, "Enter username"));
	}
	
	public void nameGood() {
		frame.setVisible(false);
		mainChat.start(frame);
		frame.dispose();
	}
	
	public void nameBad() {
		chat.testUsername(JOptionPane.showInputDialog(frame, "Enter a different username"));
	}
	
	public void message(String s) {
		mainChat.message(s);
	}
	
}
