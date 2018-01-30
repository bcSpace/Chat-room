package server.main;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Client extends Thread{
	
	private Chatter chat;
	
	private Socket socket;
	private BufferedReader in;
	private PrintWriter out;
	
	private int id; 
	private String username;

	private boolean running = false;
	private boolean waitingForUsername = true;
	
	
	public void create(Chatter c,Socket socket, int id, String username) {
		this.chat = c;
		this.id = id;
		this.username = username;
		this.socket = socket;
		try {
			in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			out = new PrintWriter(socket.getOutputStream(), true);
			out.println("CONNECTION_ACCEPTED");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void run() {
		try {
			//loop until it has a user name
			while(waitingForUsername) {
				String line = in.readLine();
				if(line.startsWith("SUBMITNAME")) {
					username = line.substring(11); 
					waitingForUsername = !chat.usernameGood(username, id);
					if(waitingForUsername) out("NAMEBAD");
					else out("NAMEGOOD");
				}
				chat.addUser(username, id);
			}
			running = true;
			while(running) {
				String line = in.readLine();
				if(line.startsWith("MESSAGE")) {
					chat.sendMessage(username + ": " +line.substring(8));
				} else if(line.startsWith("DISCONNECTING")) {
					connectionLost();
				}
			}
		} catch(Exception e) {
//			System.out.println("Server client error: " + e.getMessage());
			connectionLost();
		}
		
	}
	
	//closing out if the server made it happen
	public void close() {
		try {
			out.println("SERVERCLOSING");
			socket.close();
			in.close();
			out.close();
			running = false;
		} catch(Exception e) {
			
		}
	}
	
	//closing out if the client made it happen
	public void connectionLost() {
		try {
			chat.removeUser(username, id);
			socket.close();
			in.close();
			out.close();
			running = false;
		} catch(Exception e) {
			
		}
	}
	
	public void kick() {
		try {
			out.println("KICKED");
			running = false;
			socket.close();
			in.close();
			out.close();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	//shortcut for using the outstream
	public void out(String s) {
		out.println(s);
	}
	
	public int getClientId() {return id;}
	public String getUsername() {return username;}
	
}
