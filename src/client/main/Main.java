package client.main;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

import client.gui.ClientGui;

public class Main extends Chatter{
	
	//CLIENT
	private int port = 25565;
	private String ip = "localHost";
	
	private Socket socket;
	private BufferedReader in;
	private PrintWriter out;
	
	private boolean running = false;
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
			if(allowed.startsWith("CONNECTION_ACCEPTED")) {
				//allow
			} else {
				System.out.print("Connection rejected");
				socket.close();
				System.exit(0);
				failed = true;
				s="Server full";
			}
			
		} catch (Exception e) {
			failed = true;
			s = e.getMessage();
		}
		
		if(failed) {
			gui.connectionFailed(s);
		} else {
			gui.connectionAccepted();
			new Thread(() -> {
        		run();
	        }).start();
		}
		
	}
	
	private void run() {
		boolean running = true;
		while(running) {
			try {
				String line = in.readLine();
				System.out.println(line);
				if(line.startsWith("NAMEGOOD")) {
					gui.nameGood();
				} else if(line.startsWith("NAMEBAD")) {
					gui.nameBad();
				} else if(line.startsWith("MESSAGE")) {
					gui.message(line.substring(8));
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				System.out.println("Error: " + e.getMessage());
			} 
			try {Thread.sleep(100);} catch (InterruptedException e) {}
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
		out.println("SUBMITNAME " + username);
	}

	public void sendMessage(String s) {
		System.out.println("Sending message: " + s);
		out.println("MESSAGE " + s);
	}
	
	public void exit() {
		
	}

	

}
