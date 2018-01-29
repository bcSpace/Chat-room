package server.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

import server.main.Chatter;

public class ServerGui {
	
	private Chatter chat; 
	
	private JFrame frame;
	
	private JPanel display; 
	private JScrollPane tablePane;
	private JTable userTable;
	private DefaultTableModel table; 
	
	private JPanel controlPanel;
	private JScrollPane scroll;
	private JTextArea area;
	private JTextField commandLine;
	private JButton start;
	
	private boolean serverRunning;
	
	
	public ServerGui(Chatter chat) {
		this.chat = chat; 
		createGui();
		serverRunning = false;
	}
	
	public void createGui() {
		frame = new JFrame();
		frame.setTitle("Chat Server");
		frame.setSize(500,250);
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(false);
		
		display = new JPanel();
		controlPanel = new JPanel(); 
		
		table = new DefaultTableModel(0,0);
		table.setColumnIdentifiers(new String[] {"Username", "Client id"});
		userTable = new JTable(table);
		tablePane = new JScrollPane(userTable);
		tablePane.setPreferredSize(new Dimension(200, 200));
		display.setPreferredSize(new Dimension(200,200));
		display.add(tablePane);
		
		area = new JTextArea(10,20);
		area.setEditable(false);
		area.setLineWrap(true);
		scroll = new JScrollPane(area);
		commandLine = new JTextField(19);
		start = new JButton("Start");
		
		controlPanel.setLayout(new FlowLayout());
		controlPanel.add(scroll);
		controlPanel.add(commandLine);
		controlPanel.add(start);
			
		frame.setLayout(new BorderLayout());
		frame.add(controlPanel, "Center");
		frame.add(display, "East");
		frame.setVisible(true);
		
		
		start.addActionListener(new ActionListener() { 
			  public void actionPerformed(ActionEvent e) { 
				  if(!serverRunning) {
					  //attempt to start the server
					  chat.start();
					  //the two outcomes are this.failstart and this.start
				  } else {
					  stop();
				  }
			  } 
		});
		
	}
	
	//adding a new user
	public void addUser(String username, int id) {
		table.addRow(new Object[] {username,id});
		table.fireTableDataChanged();
	}
	
	//removing user
	public void removeUser(int id) {
		for(int i = 0; i < table.getRowCount(); i++) {
			if(id == (int)table.getValueAt(i, 1)) {
				table.removeRow(i);
				table.fireTableDataChanged();
				return; 
			}
		}
	}
	
	//used for parsing command line and executing commands
	public void sendCommand() {
		String command = commandLine.getText();
		commandLine.setText("");
	}
	
	//if the server socket failed
	public void failStart(String s) {
		area.append(s+"\n");
		start.setEnabled(true);
	}
	
	//starting the server
	public void start() {
		area.setText("");
		serverRunning = true;
		start.setEnabled(true);
		start.setText("Stop");
		log("SERVER: Server Started");
		JScrollBar bar = scroll.getVerticalScrollBar();
		bar.setValue(bar.getMaximum());
	}
	
	public void stop() {
		serverRunning = false;
		chat.stop();
		log("SERVER: Server Closed");
		start.setEnabled(true);
		start.setText("Start");
		//create a log file
	}
	
	//logging
	public void log(String s) {
		if(serverRunning) area.append(s+"\n");
		JScrollBar bar = scroll.getVerticalScrollBar();
		bar.setValue(bar.getMaximum());
	}
	

}
