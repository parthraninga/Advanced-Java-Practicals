import java.io.*;
import java.net.*; // I can use class DatagramSocket, DatagramPacket, InetAddress from this package also ServerSocket and Socket are from java.net package but they are used in TCP not UDP.

/**
 * UDP Server - Receives a text file from client and saves it
 * UDP = User Datagram Protocol (connectionless, sends packets independently)
 */
public class UDPServer {
    public static void main(String[] args) {
        // Port where server will listen for data packets
        int port = 9876;
        
        // Size of each packet - UDP has size limit, we use 1024 bytes (1KB)
        // Why 1024? It's safe size that works well for most networks
        int packetSize = 1024; //If we want to send more than this we may need to re package the data
        
        try {
            // Create DatagramSocket - this is like a mailbox for receiving packets
            // Why DatagramSocket? In UDP, we use DatagramSocket (not ServerSocket like TCP)
            DatagramSocket socket = new DatagramSocket(port);// I can also write DatagramSocket serverSocket = new DatagramSocket(port);
            System.out.println("✓ UDP Server started on port " + port);
            System.out.println("✓ Waiting to receive file...\n");
            
            // Create a byte array to hold incoming data
            byte[] receiveData = new byte[packetSize];//I can also write byte[] buffer = new byte[packetSize];
            
            // Create DatagramPacket - like an envelope to receive data
            // Why DatagramPacket? UDP data comes in packets, not streams
            DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);//DatagramPacket is the class that allows us to receive the data in the form of packets. It takes the byte array and its length as parameters.
            
            // Receive the first packet (contains filename)
            // Why receive()? To wait for and catch the incoming packet
            socket.receive(receivePacket);// DatagramSocket's recieve() is null method and returns nothing. It will store the data in 
            
            // Extract filename from the packet
            // Why new String()? To convert bytes back to readable text
            String filename = new String(receivePacket.getData(), 0, receivePacket.getLength()).trim();
            System.out.println("→ Receiving file: " + filename);
            
            // Create file to save received data
            // Why FileOutputStream? To write bytes to a file
            FileOutputStream fos = new FileOutputStream("received_" + filename);
            
            // Keep receiving packets until we get "END" signal
            // Why loop? File is sent in multiple small packets
            while (true) {
                // Create new packet for receiving next chunk
                receiveData = new byte[packetSize];
                receivePacket = new DatagramPacket(receiveData, receiveData.length);
                
                // Wait for next packet
                socket.receive(receivePacket);
                
                // Convert packet data to string to check if it's end signal
                String message = new String(receivePacket.getData(), 0, receivePacket.getLength());
                
                // Check if transfer is complete
                // Why "END"? We use this as signal that file transfer is done
                if (message.equals("END")) {
                    System.out.println("✓ File transfer complete!");
                    break;  // Exit loop
                }
                
                // Write received data to file
                // Why write()? To save the packet's data to our file
                fos.write(receivePacket.getData(), 0, receivePacket.getLength());
                System.out.print(".");  // Show progress
            }
            
            // Close file and socket
            // Why close? To save file properly and free resources
            fos.close();
            socket.close();
            
            System.out.println("\n✓ File saved as: received_" + filename);
            System.out.println("✓ Server stopped");
            
        } catch (IOException e) {
            System.out.println("✗ Server error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
