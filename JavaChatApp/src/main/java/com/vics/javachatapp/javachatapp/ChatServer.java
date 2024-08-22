package com.vics.javachatapp.javachatapp;
import java.io.*;
import java.net.*;
import java.util.*;

public class ChatServer {
    // vreates a list for the clients who are going to instancciate
  private static List<ClientHandler> clients = new ArrayList<>();

  public static void main(String[] args) throws IOException {
      // sets up the server to be connected to
      ServerSocket serverSocket = new ServerSocket(5000);
      System.out.println("Server started. Waiting for clients...");

      //creates a while loop to brroadcast the message to the stored list of connected clients
      while (true) {
          // allows clients to connect to the server
          Socket clientSocket = serverSocket.accept();
          System.out.println("Client connected: " + clientSocket);

          ClientHandler clientThread = new ClientHandler(clientSocket, clients);
          clients.add(clientThread);
          new Thread(clientThread).start();
      }
  }
}

class ClientHandler implements Runnable {
  private Socket clientSocket;
  private List<ClientHandler> clients;
  private PrintWriter out;
  private BufferedReader in;

  public ClientHandler(Socket socket, List<ClientHandler> clients) throws IOException {
      this.clientSocket = socket;
      this.clients = clients;
      this.out = new PrintWriter(clientSocket.getOutputStream(), true);
      this.in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
  }

  public void run() {
      try {
          String inputLine;
          while ((inputLine = in.readLine()) != null) {
              for (ClientHandler aClient : clients) {
                  aClient.out.println(inputLine);
              }
          }
      } catch (IOException e) {
          System.out.println("An error occurred: " + e.getMessage());
      } finally {
          try {
              in.close();
              out.close();
              clientSocket.close();
          } catch (IOException e) {
              e.printStackTrace();
          }
      }
  }
}