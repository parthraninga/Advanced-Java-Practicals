import java.io.*;
import java.net.*;
import java.util.Scanner;

/**
 * TCP Client that sends a message to server and receives character count
 */
public class Client {
    public static void main(String[] args) {
        // Server details - where to connect
        // "localhost" means same computer, change to server's IP if on different computer
        String serverAddress = "localhost";
        int port = 8080;  // Must match the server's port number
        
        // For taking user input from keyboard
        Scanner scanner = new Scanner(System.in);
        
        try {
            // Connect to the server - like making a phone call
            // Why Socket? To establish connection with server
            Socket socket = new Socket(serverAddress, port);
            System.out.println("✓ Connected to server at " + serverAddress + ":" + port);
            
            // Create output stream to SEND data to server
            // Why PrintWriter? To send text messages easily
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            
            // Create input stream to READ response from server
            // Why BufferedReader? To read the response line by line
            BufferedReader in = new BufferedReader(
                new InputStreamReader(socket.getInputStream())
            );
            
            // Ask user to type a message
            System.out.print("\nEnter a message to send to server: ");
            String message = scanner.nextLine();
            
            // Send message to server
            // Why println? Sends message with newline so server's readLine() can read it
            out.println(message);
            System.out.println("✓ Message sent to server");
            
            // Wait for and read server's response
            // Why readLine()? To wait for server's response (blocks until data arrives)
            String response = in.readLine();
            System.out.println("\n→ Server response: " + response);
            
            // Close all connections - clean up resources
            // Why close? To free up system resources and properly end connection
            in.close();
            out.close();
            socket.close();
            scanner.close();
            System.out.println("✓ Connection closed");
            
        } catch (UnknownHostException e) {
            // This error happens if server address is wrong
            System.out.println("✗ Error: Cannot find server at " + serverAddress);
            System.out.println("  Make sure server address is correct!");
        } catch (IOException e) {
            // This error happens if server is not running or network problem
            System.out.println("✗ Error: Cannot connect to server");
            System.out.println("  Make sure server is running on port " + port);
            e.printStackTrace();
        }
    }
}
