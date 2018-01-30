# Chat-room

# Goal 
The goal of the project was to create a multi-user chat room, where users could connect with a custom username and chat to the other users.

# What it does
## Client
Prompts the user to connect to the server using an IP address, hard coded port is 25565

Once connection is accepted user needs to enter a unique username

Once a unique name is entered they have access to the chatroom and can send messages or disconnect back to the ip prompt

## Server
The server handles connections from the clients, up to a max amount of clients, which is hard coded at 10

Server displays what clients are connected and their name/client id

Server also has a command line and start/stop button

The command line currently has two commands, /kickall and /kick "client id"

# How it works
Sockets.
