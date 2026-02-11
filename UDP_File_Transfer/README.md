# UDP File Transfer - Client-Server Application

## What This Program Does
This program transfers a text file from one computer to another using UDP protocol:
1. **Client** reads a text file and breaks it into small packets
2. **Client** sends each packet to the server
3. **Server** receives all packets and reconstructs the file
4. **Server** saves the file with "received_" prefix

## UDP vs TCP - Simple Explanation

### 🚂 TCP (Previous program)
- Like sending a **package via courier**
- Connection is established first (handshake)
- Guaranteed delivery in correct order
- Slower but more reliable
- Good for: File downloads, emails, web pages

### 📮 UDP (This program)
- Like sending **postcards via mail**
- No connection needed (just send!)
- Faster but packets can get lost or arrive out of order
- Lighter weight
- Good for: Video streaming, online games, voice calls

## How It Works (Simple Explanation)

### 📡 Server (UDPServer.java)
Think of the server as a **mailbox** waiting for letters:

1. **Opens mailbox** - Creates DatagramSocket on port 9876
2. **Waits for first letter** - Receives filename
3. **Collects all letters** - Receives file data in packets
4. **Stops when sees "END"** - Knows transfer is complete
5. **Assembles letters into book** - Saves all data as a file

### 📤 Client (UDPClient.java)
Think of the client as someone **mailing a book page by page**:

1. **Opens the book** - Opens the file to read
2. **Sends book title** - Sends filename first
3. **Tears pages** - Breaks file into 1KB chunks
4. **Mails each page** - Sends each packet to server
5. **Sends "THE END" card** - Signals completion
6. **Closes book** - Closes file and socket

## Key Differences from TCP Program

| Aspect | TCP (Previous) | UDP (This) |
|--------|---------------|------------|
| **Connection** | Yes (3-way handshake) | No (just send) |
| **Socket Type** | ServerSocket, Socket | DatagramSocket |
| **Data Type** | Streams (BufferedReader) | Packets (DatagramPacket) |
| **Reliability** | Guaranteed delivery | Best effort (may lose packets) |
| **Order** | Always in order | May arrive out of order |
| **Speed** | Slower | Faster |

## Main Classes Explained

### 1. **DatagramSocket**
- Like a **mailbox** for sending/receiving packets
- Used by both client and server
- No connection needed - just create and use!

```java
DatagramSocket socket = new DatagramSocket(port); // Server - opens mailbox at specific address
DatagramSocket socket = new DatagramSocket();      // Client - opens any available mailbox
```

### 2. **DatagramPacket**
- Like an **envelope** containing data
- Has: data, destination address, port
- Each packet is independent

```java
// Creating a packet to send
DatagramPacket packet = new DatagramPacket(
    data,      // What to send (byte array)
    length,    // How much data
    address,   // Where to send (IP)
    port       // Which port
);

// Creating a packet to receive
DatagramPacket packet = new DatagramPacket(
    buffer,    // Where to store received data
    length     // Maximum size
);
```

### 3. **InetAddress**
- Represents an **IP address**
- Converts "localhost" to actual IP (127.0.0.1)

```java
InetAddress serverIP = InetAddress.getByName("localhost");
```

## How to Run

### Step 1: Compile the programs
```bash
cd "Advanced Java Practicals/UDP_File_Transfer"
javac UDPServer.java
javac UDPClient.java
```

### Step 2: Start the Server (Terminal 1)
```bash
java UDPServer
```
You should see:
```
✓ UDP Server started on port 9876
✓ Waiting to receive file...
```

### Step 3: Run the Client (Terminal 2)
```bash
java UDPClient
```

### Step 4: Check Results
The server will save the file as `received_sample.txt` in the same folder!

## Example Run

**Server Output:**
```
✓ UDP Server started on port 9876
✓ Waiting to receive file...

→ Receiving file: sample.txt
.........
✓ File transfer complete!
✓ File saved as: received_sample.txt
✓ Server stopped
```

**Client Output:**
```
✓ Connected to server at localhost:9876
→ Sending file: sample.txt
→ File size: 623 bytes

.........
✓ File sent successfully!
```

## Code Flow Diagram

```
CLIENT                          SERVER
------                          ------
1. Open sample.txt
2. Create DatagramSocket    →   1. Create DatagramSocket (port 9876)
3. Send filename packet     →   2. Receive filename packet
4. Read file chunk 1            3. Create output file
5. Send packet 1            →   4. Receive packet 1
6. Read file chunk 2            5. Write to file
7. Send packet 2            →   6. Receive packet 2
...                             7. Write to file
8. Send "END" packet        →   8. Receive "END" signal
9. Close socket                 9. Close file & socket
```

## Why This Code is Simple

✅ **No threads needed** - UDP is simple, one-way transfer  
✅ **No complex protocols** - Just send and receive packets  
✅ **Clear structure** - Filename first, data second, END signal last  
✅ **Good comments** - Every line explained with "what" and "why"  
✅ **Progress indicators** - Dots show transfer happening  

## Common Issues & Solutions

### ❌ "File not found"
- **Problem:** sample.txt doesn't exist
- **Solution:** Make sure sample.txt is in the UDP_File_Transfer folder

### ❌ "Address already in use"
- **Problem:** Port 9876 is being used by another program
- **Solution:** Change port in both UDPServer.java and UDPClient.java

### ❌ "Received file is corrupted or incomplete"
- **Problem:** UDP packets were lost (normal UDP behavior)
- **Solution:** This is why UDP isn't used for critical file transfers! For important files, use TCP

### ❌ Client runs but server receives nothing
- **Problem:** Server not started before client
- **Solution:** Always start server first, then client

## Learning Objectives Achieved ✓

1. ✅ Understanding UDP socket programming
2. ✅ Difference between TCP and UDP
3. ✅ DatagramSocket and DatagramPacket usage
4. ✅ File I/O with byte streams
5. ✅ Breaking data into packets
6. ✅ Simple protocol design (filename → data → END)

## Try These Experiments

1. **Test with larger files** - Create a bigger text file
2. **Change packet size** - Try 512 bytes or 2048 bytes
3. **Send different file** - Create your own text file
4. **Network transfer** - Run client and server on different computers
5. **Add confirmation** - Make server send "OK" after each packet (like a simple reliability layer)

## Real-World UDP Usage

🎮 **Online Gaming** - Player position updates (lost packet = minor lag)  
📹 **Video Streaming** - Netflix, YouTube (few lost frames = no problem)  
📞 **Voice Calls** - WhatsApp, Zoom (small audio loss = acceptable)  
🌐 **DNS Queries** - Looking up website addresses (fast matters)  

## Next Steps to Enhance

1. Add packet numbering for proper ordering
2. Implement acknowledgment system (basic reliability)
3. Add file integrity check (checksum)
4. Support binary files (images, PDFs)
5. Add compression before sending
6. Build a two-way transfer system
