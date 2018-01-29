package server.main;

public abstract class Chatter {
	
	public abstract boolean usernameGood(String s, int id); //used by client to check if name avaible
	public abstract void sendMessage(String message); //sending a message
	public abstract void start(); //starting
	public abstract void stop(); //ending
	public abstract void addUser(String name, int id); //adding a user after a good username is made
	public abstract void removeUser(String name, int id); //removing a user
	
}
