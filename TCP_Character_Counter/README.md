# TCP Character Counter - Client-Server Application

## What This Program Does
This is a simple client-server application where:
1. **Client** sends a text message to the server
2. **Server** counts how many characters are in the message
3. **Server** sends the count back to the client
4. **Server** can handle multiple clients at the same time (multi-threaded)

## How It Works (Simple Explanation)

### 🖥️ Server (Server.java)
Think of the server as a shop that's always open:
- It **opens a door** (creates ServerSocket on port 8080)
- It **waits for customers** (waits for clients to connect)
- When a client arrives, it **assigns a helper** (creates new thread)
- The **helper serves that client** while the shop continues accepting more customers
- Each helper:
  - Reads the message from client
  - Counts the characters
  - Sends the count back
  - Says goodbye and cleans up

### 💻 Client (Client.java)
Think of the client as a customer visiting the shop:
- It **finds the shop** (connects to server at localhost:8080)
- It **gives a message** to the server
- It **waits for response** (character count)
- It **receives the result** and displays it
- It **leaves** (closes connection)

## Key Concepts Explained

### Why TCP Sockets?
- **TCP** = Transmission Control Protocol
- It's **reliable** - messages are guaranteed to arrive in order
- Like a **phone call** - direct connection between client and server
- Perfect for applications where data must not be lost

### Why Multi-Threading?
Without threads:
```
Client1 connects → Server serves Client1 → Client1 disconnects
                → Only now Client2 can connect
```

With threads:
```
Client1 connects → Thread1 serves Client1
Client2 connects → Thread2 serves Client2  } At the same time!
Client3 connects → Thread3 serves Client3
```

Each client gets their own thread, like each customer gets their own cashier at a bank.

### Why These Classes?

1. **ServerSocket** - Opens a "listening port" to accept connections
2. **Socket** - Represents the actual connection between client and server
3. **BufferedReader** - Reads text data efficiently line by line
4. **PrintWriter** - Sends text data easily
5. **Thread** - Allows handling multiple clients simultaneously

## How to Run

### Step 1: Compile the programs
```bash
cd "Advanced Java Practicals/TCP_Character_Counter"
javac Server.java
javac Client.java
```

### Step 2: Start the Server (in one terminal)
```bash
java Server
```
You should see:
```
✓ Server started on port 8080
✓ Waiting for clients to connect...
```

### Step 3: Run Client(s) (in another terminal)
```bash
java Client
```
You can run multiple clients to test multi-threading!

### Step 4: Test It
1. When client runs, it asks for a message
2. Type something like: `Hello World`
3. Client sends it to server
4. Server counts characters (11 characters including space)
5. Server sends back: `Character count: 11`
6. Client displays the response

## Example Run

**Server Output:**
```
✓ Server started on port 8080
✓ Waiting for clients to connect...

→ New client connected: /127.0.0.1
  Received from /127.0.0.1: Hello World
  Character count: 11
  Response sent to client

← Client disconnected: /127.0.0.1
```

**Client Output:**
```
✓ Connected to server at localhost:8080

Enter a message to send to server: Hello World
✓ Message sent to server

→ Server response: Character count: 11
✓ Connection closed
```

## Common Issues & Solutions

### ❌ "Address already in use"
- **Problem:** Another program is using port 8080
- **Solution:** Change port number in both Server.java and Client.java

### ❌ "Connection refused"
- **Problem:** Server is not running
- **Solution:** Start the server first, then run client

### ❌ Client hangs/freezes
- **Problem:** Server not sending newline, or client not reading properly
- **Solution:** Make sure server uses `println()` and client uses `readLine()`

## Learning Objectives Achieved ✓

1. ✅ Understanding TCP socket programming
2. ✅ Client-Server architecture
3. ✅ Multi-threaded server implementation
4. ✅ Stream-based communication (BufferedReader, PrintWriter)
5. ✅ Resource management (closing connections)
6. ✅ Exception handling in network programming

## Next Steps to Enhance

1. Add error handling for empty messages
2. Keep connection alive for multiple messages
3. Add a GUI using Java Swing
4. Implement more operations (word count, vowel count, etc.)
5. Add encryption for secure communication
