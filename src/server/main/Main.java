package server.main;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Main extends Chatter {
	
	//SERVER
	private final int port = 25565; 
	private ServerSocket ss;
	
	private int clientId = 0;
	private int maxClient = 10;
	private ArrayList<Client> clientList = new ArrayList<Client>(10);
	private ArrayList<String> nameList = new ArrayList<String>(10); 
	
	boolean running = false;
	
	Main() {
		startServer();
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
			} else {
				//add the client
				System.out.println("Added a client: " + clientId);
				Client client = new Client();
				client.create(this, socket, clientId, "User_"+clientId++);
				client.start();
				this.addClient(client);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	private void startServer() {
		running = true;
		try {
			ss = new ServerSocket(port);
			new Thread(() -> {
	            while (running) {
	            	waitForClients();
	            }
	        }).start();
		} catch(Exception e) {
			e.printStackTrace();
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
	
	public void sendMessage(String s) {
		for(int i = 0; i < clientList.size(); i++) {
			clientList.get(i).out("MESSAGE " + s);
		}
	}
	
	public boolean usernameGood(String s, int clientId) {
		for(int i = 0; i < clientList.size(); i++) {
			if(clientList.get(i).getClientId() != clientId) if(s.equals(clientList.get(i).getName())) return false;
		}
		return true;
	}
	
	public static void main(String[] args) {
		new Main();
	}


}
