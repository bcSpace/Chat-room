package server.main;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import server.gui.ServerGui;

public class Main extends Chatter {
	
	//SERVER
	private final int port = 25565; 
	private ServerSocket ss;
	
	private int clientId = 0;
	private int maxClient = 10;
	private ArrayList<Client> clientList = new ArrayList<Client>(10);

	private ServerGui gui;
	private boolean running = false;
	
	Main() {
		gui = new ServerGui(this);
	}
	
	public void waitForClients() {
		Socket socket;
		try {
			socket = ss.accept();
			if(clientList.size() >= maxClient) {
				//send a rejection message
				PrintWriter pw = new PrintWriter(socket.getOutputStream(), true);
				pw.println("Rejected");
				socket.close();
				pw.close();
			} else {
				//add the client
				System.out.println("Added a client: " + clientId);
				Client client = new Client();
				client.create(this, socket, clientId, "User_"+clientId++);
				client.start();
				this.addClient(client);
			}
		} catch (IOException e) {
			gui.log("SERVER ERROR: " + e.getMessage());
		}
	}
	
	private synchronized void addClient(Client client) {
		clientList.add(client);
	}
	
	private synchronized void removeClient(int clientId) {
		for(int i = 0; i < clientList.size(); i++) {
			if(clientList.get(i).getClientId() == clientId) {
				clientList.remove(i);
				return;
			}
		}
	}
	
	//starting
	public void start() {
		running = true;
		try {
			ss = new ServerSocket(port);
			gui.start();
			new Thread(() -> {
	            while (running) {
	            	waitForClients();
	            }
	        }).start();
		} catch(Exception e) {
			e.printStackTrace();
			gui.failStart(e.getMessage());
		}
	}
	
	//close out all clients, then end
	public void stop() {
		for(int i = 0; i < clientList.size(); i++) {
			clientList.get(i).close();
		}
		clientList.clear();
		running = false;
		try {ss.close();} catch (IOException e) {}
	}
	
	//send messages to all clients then update server gui
	public void sendMessage(String s) {
		for(int i = 0; i < clientList.size(); i++) {
			clientList.get(i).out("MESSAGE " + s);
		}
		gui.log(s);
	}
	
	//check entire list of clients and make sure name is not used
	public boolean usernameGood(String s, int clientId) {
		for(int i = 0; i < clientList.size(); i++) {
			if(clientList.get(i).getClientId() != clientId) if(s.equals(clientList.get(i).getName())) return false;
		}
		return true;
	}
	
	//add the user to the gui
	public void addUser(String name, int id) {
		gui.addUser(name, id);
		gui.log(name+" joined");
		this.broadcast("MESSAGE "+name+" joined");
	}
	
	//remove the user from the gui
	public void removeUser(String name, int id) {
		gui.removeUser(id);
		gui.log(name+" disconnected");
		this.broadcast("MESSAGE "+name+" disconnected");
		this.removeClient(id);
	}
	
	//send a string to all users
	public void broadcast(String s) {
		for(int i = 0; i < clientList.size(); i++) {
			clientList.get(i).out(s);
		}
	}
	
	public static void main(String[] args) {
		new Main();
	}


}
