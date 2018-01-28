package client.gui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import client.main.Chatter;

public class MainChat {
	
	private Chatter chat; 
	
	private JFrame frame; 
	
	private JScrollPane scroll;
	private JTextArea area; 
	
	private JPanel input;
	private JTextField message; 
	private JButton send; 
	
	
	public MainChat(Chatter chat) {
		this.chat = chat;
		createGui();
	}
	
	private void createGui() {
		frame = new JFrame();
		frame = new JFrame("Chat Room");
		frame.setSize(400, 400);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocationRelativeTo(null);
		frame.setLayout(new BorderLayout());
		frame.setResizable(false);

		area = new JTextArea(20,20);
		area.setEditable(false);
		scroll = new JScrollPane(area);
		
		input = new JPanel();
		message =  new JTextField(15);
		send = new JButton("Send");
		input.setLayout(new FlowLayout());
		input.add(message);
		input.add(send);
		
		frame.add(scroll, "Center");
		frame.add(input, "South");
		
		send.addActionListener(new ActionListener() { 
			  public void actionPerformed(ActionEvent e) { 
				  sendMessage();
			  } 
		});
		
		message.addActionListener(new ActionListener() { 
			  public void actionPerformed(ActionEvent e) { 
				  sendMessage();
			  } 
		});
		
	}
	
	public void start() {
		frame.setVisible(true);
	}
	
	private void sendMessage() {
		String mess = message.getText();
		if(mess.length() != 0) {
			chat.sendMessage(mess);
			message.setText("");
			message.requestFocus();
		}
	}
	
	public void message(String s) {
		area.append(s + "\n");
	}

}
