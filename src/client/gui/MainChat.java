package client.gui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import client.main.Chatter;

public class MainChat {
	
	private Chatter chat; 
	
	private JFrame frame; 
	
	//chat
	private JScrollPane scroll;
	private JTextArea area; 
	
	//messaging and controls
	private JPanel input;
	private JTextField message; 
	private JButton send; 
	private JButton disconnect; 
	
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
		area.setLineWrap(true);
		scroll = new JScrollPane(area);
		
		input = new JPanel();
		message =  new JTextField(15);
		send = new JButton("Send");
		disconnect = new JButton("Disconnect");
		input.setLayout(new FlowLayout());
		input.add(message);
		input.add(send);
		input.add(disconnect);
		
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
		
		disconnect.addActionListener(new ActionListener() { 
			  public void actionPerformed(ActionEvent e) { 
				  disconnect();
			  } 
		});
		
		
	}
	
	public void start(JFrame f) {
		frame.setLocationRelativeTo(f);
		frame.setVisible(true);
	}
	
	public void disconnect() {
		chat.disconnect();
	}
	
	public void restart() {
		area.setText("");
		message.setText("");
		frame.setVisible(false);
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
		JScrollBar bar = scroll.getVerticalScrollBar();
		bar.setValue(bar.getMaximum());
	}

}
