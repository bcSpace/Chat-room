package client.main;

public abstract class Chatter {
	
	public abstract void connect(String ip, int port);
	public abstract void testUsername(String username);
	public abstract void sendMessage(String s); 
	public abstract void exit(); 

}
