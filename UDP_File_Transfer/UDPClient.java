import java.io.*;
import java.net.*;

/**
 * UDP Client - Sends a text file to server
 * Breaks file into small packets and sends them one by one
 */
public class UDPClient {
    public static void main(String[] args) {
        // Server details - where to send the file
        String serverAddress = "localhost";  // Change to server's IP if on different computer
        int port = 9876;  // Must match server's port
        
        // File to send
        String filename = "sample.txt";
        
        // Packet size - same as server (1KB)
        int packetSize = 1024;
        
        try {
            // Create DatagramSocket for sending packets
            // Why DatagramSocket? All UDP communication uses DatagramSocket
            // Note: We don't "connect" in UDP, we just send packets to an address
            DatagramSocket socket = new DatagramSocket();
            
            // Get server's address
            // Why InetAddress? To convert "localhost" to actual IP address
            InetAddress serverIP = InetAddress.getByName(serverAddress);
            
            System.out.println("✓ Connected to server at " + serverAddress + ":" + port);
            
            // Check if file exists
            File file = new File(filename);
            if (!file.exists()) {
                System.out.println("✗ Error: File '" + filename + "' not found!");
                System.out.println("  Please create a sample.txt file first.");
                socket.close();
                return;
            }
            
            // First, send the filename to server
            // Why send filename first? So server knows what to name the received file
            byte[] filenameBytes = filename.getBytes();
            DatagramPacket filenamePacket = new DatagramPacket(
                filenameBytes, 
                filenameBytes.length, 
                serverIP, 
                port
            );
            socket.send(filenamePacket);
            
            System.out.println("→ Sending file: " + filename);
            System.out.println("→ File size: " + file.length() + " bytes\n");
            
            // Open file for reading
            // Why FileInputStream? To read file as bytes
            FileInputStream fis = new FileInputStream(file);
            
            // Buffer to hold file chunks
            byte[] buffer = new byte[packetSize];
            int bytesRead;
            
            // Read and send file in chunks
            // Why loop? To read file piece by piece and send each piece
            while ((bytesRead = fis.read(buffer)) != -1) {
                // Create packet with file data
                // Why DatagramPacket? UDP sends data in packets (like individual letters)
                DatagramPacket sendPacket = new DatagramPacket(
                    buffer,           // The data to send
                    bytesRead,        // How many bytes to send
                    serverIP,         // Where to send
                    port              // Which port
                );
                
                // Send the packet
                // Why send()? To transmit packet to server
                socket.send(sendPacket);
                System.out.print(".");  // Show progress
            }
            
            // Send "END" signal to tell server we're done
            // Why END signal? Server needs to know when file transfer is complete
            byte[] endSignal = "END".getBytes();
            DatagramPacket endPacket = new DatagramPacket(
                endSignal, 
                endSignal.length, 
                serverIP, 
                port
            );
            socket.send(endPacket);
            
            System.out.println("\n✓ File sent successfully!");
            
            // Close resources
            fis.close();
            socket.close();
            
        } catch (UnknownHostException e) {
            System.out.println("✗ Error: Cannot find server at " + serverAddress);
        } catch (IOException e) {
            System.out.println("✗ Error sending file: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
