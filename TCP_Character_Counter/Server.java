import java.io.*;
import java.net.*; // Provided the ServerSocket Class which allows to create obejct with port.

/**
 * TCP Server that counts characters in messages from clients
 * This server can handle multiple clients at the same time using threads
 */
public class Server {
    public static void main(String[] args) {
        // Port number - like a door number where server will listen for clients
        // We use 8080, but you can use any number between 1024-65535
        int port = 8080;
        
        try {
            // Create a ServerSocket - this is like opening a shop that waits for customers
            // Why? Because we need something that listens for incoming client connections
            ServerSocket serverSocket = new ServerSocket(port);
            System.out.println("✓ Server started on port " + port);
            System.out.println("✓ Waiting for clients to connect...\n");
            
            // Keep the server running forever to accept multiple clients
            // Why infinite loop? So server doesn't stop after serving one client
            while (true) {
                // Wait for a client to connect (this line blocks/waits until someone connects)
                // Why accept()? To establish a connection with the client
                Socket clientSocket = serverSocket.accept();
                
                // When a client connects, print their information
                System.out.println("→ New client connected: " + clientSocket.getInetAddress());
                
                // Create a new thread to handle this client
                // Why new thread? So this client gets served while server can accept more clients
                // Without threads, server would handle one client at a time (like single queue at bank)
                ClientHandler clientHandler = new ClientHandler(clientSocket);
                Thread thread = new Thread(clientHandler);
                thread.start();  // Start the thread to serve this client
            }
            
        } catch (IOException e) {
            System.out.println("✗ Server error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}

/**
 * ClientHandler - A separate class that handles each client
 * Why separate class? To handle multiple clients simultaneously, each in their own thread
 */
class ClientHandler implements Runnable {
    private Socket clientSocket;
    
    // Constructor receives the client's socket connection
    public ClientHandler(Socket socket) {
        this.clientSocket = socket;
    }
    
    @Override
    public void run() {
        // This method runs in a separate thread for each client
        BufferedReader in = null;
        PrintWriter out = null;
        
        try {
            // Create input stream to READ data from client
            // Why BufferedReader? To read text line by line efficiently
            in = new BufferedReader(
                new InputStreamReader(clientSocket.getInputStream())
            );
            
            // Create output stream to SEND data to client
            // Why PrintWriter? To write text easily with println()
            out = new PrintWriter(clientSocket.getOutputStream(), true);
            
            // Read the message sent by client
            // Why readLine()? To read complete message until newline character
            String message = in.readLine();
            System.out.println("  Received from " + clientSocket.getInetAddress() + ": " + message);
            
            // Count characters in the message
            // Why length()? To get total number of characters in the string
            int charCount = message.length();
            System.out.println("  Character count: " + charCount);
            
            // Send the count back to client
            // Why send back? Client needs to know the result
            out.println("Character count: " + charCount);
            System.out.println("  Response sent to client\n");
            
        } catch (IOException e) {
            System.out.println("✗ Error handling client: " + e.getMessage());
        } finally {
            // Close all connections - like cleaning up after customer leaves
            // Why finally? To ensure resources are closed even if error occurs
            try {
                if (in != null) in.close();
                if (out != null) out.close();
                if (clientSocket != null) clientSocket.close();
                System.out.println("← Client disconnected: " + clientSocket.getInetAddress() + "\n");
            } catch (IOException e) {
                System.out.println("✗ Error closing connection: " + e.getMessage());
            }
        }
    }
}
