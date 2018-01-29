package client.main;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

import javax.swing.JOptionPane;

import client.gui.ClientGui;

public class Main extends Chatter {
	
	//CLIENT
	private int port = 25565;
	private String ip = "localHost";
	
	private Socket socket;
	private BufferedReader in;
	private PrintWriter out;
	
	private boolean running = false; //listening for the in
	private ClientGui gui;
	
	
	Main() {
		gui = new ClientGui(this);
		gui.start();
	}
	
	private void connect() {
		boolean failed = false;
		String s = "";
		try {
			socket = new Socket(ip, port);
			in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			out = new PrintWriter(socket.getOutputStream(), true);
			
			String allowed = in.readLine();
			System.out.print(allowed);
			if(allowed.startsWith("CONNECTION_ACCEPTED")) {
				//allow
			} else if(allowed.startsWith("Rejected")) {
				System.out.print("Connection rejected");
				in.close();
				out.close();
				socket.close();
				failed = true;
				s="Server full";
			} else {
				failed = true;
				s = "Unknown error";
			}
			
		} catch (Exception e) {
			failed = true;
			s = e.getMessage();
		}
		
		if(failed) {
			gui.connectionFailed(s);
		} else {
			gui.connectionAccepted();
			running = true;
			//start a new thread to not take up the event listener thread
			new Thread(() -> {
        		run();
	        }).start();
		}
		
	}
	
	private void run() {
		while(running) {
			try {
				String line = in.readLine();
				if(line.startsWith("NAMEGOOD")) {
					gui.nameGood();
				} else if(line.startsWith("NAMEBAD")) {
					gui.nameBad();
				} else if(line.startsWith("MESSAGE")) {
					gui.message(line.substring(8));
				} else if(line.startsWith("SERVERCLOSING")) {
					exit("Server closing");
				}
			} catch (IOException e) {
				if(e.getMessage().equals("Connection reset")) exit("Unexpected Server Error");
			} 
		}
	}
	
	
	public static void main(String[] args) {
		new Main();
	}
	
	public void connect(String ip, int port) {
		this.ip = ip;
		this.port = port;
		this.connect();
	}
	
	public void testUsername(String username) {
		this.username = username;
		out.println("SUBMITNAME " + username);
	}

	public void sendMessage(String s) {
		out.println("MESSAGE " + s);
	}
	
	//user disconnecting
	public void disconnect() {
		running = false;
		try {
			out.println("DISCONNECTING");
			out.close();
			in.close();
			socket.close();
			gui.restart();
		} catch(Exception e) {
			System.out.println("Disconnect error: " + e.getMessage());
		}
	}
	
	//server forcing a disconnect, or an arror
	public void exit(String s) {
		try {
			running = false;
			in.close();
			out.close();
			socket.close();
			JOptionPane.showMessageDialog(null, s, "Server", JOptionPane.WARNING_MESSAGE);
			gui.restart();
		} catch(Exception e) {
			System.out.println(e.getMessage());
		}
	}

	

}
