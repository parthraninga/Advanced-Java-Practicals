# UDPServer - Complete Architecture & Dependencies

## 📊 Class Dependency Graph

```mermaid
graph TB
    UDPServer[UDPServer Class]
    Main[main method]
    
    %% Java Standard Library Classes
    DatagramSocket[DatagramSocket<br/>java.net]
    DatagramPacket[DatagramPacket<br/>java.net]
    FileOutputStream[FileOutputStream<br/>java.io]
    IOException[IOException<br/>java.io]
    String[String<br/>java.lang]
    
    %% Dependencies
    UDPServer --> Main
    Main --> DatagramSocket
    Main --> DatagramPacket
    Main --> FileOutputStream
    Main --> String
    Main --> IOException
    
    %% Object creation flows
    DatagramSocket -.creates.-> SocketObject[socket object]
    DatagramPacket -.creates.-> PacketObject[receivePacket object]
    FileOutputStream -.creates.-> FileObject[fos object]
    
    style UDPServer fill:#e1f5ff
    style DatagramSocket fill:#fff4e1
    style DatagramPacket fill:#fff4e1
    style FileOutputStream fill:#fff4e1
    style IOException fill:#ffe1e1
```

## 🔗 Object Interdependency Flow

```mermaid
sequenceDiagram
    participant Main as main()
    participant DS as DatagramSocket
    participant DP as DatagramPacket
    participant FOS as FileOutputStream
    participant BA as byte[]
    
    Main->>DS: new DatagramSocket(port)
    DS-->>Main: socket object
    
    Main->>BA: new byte[packetSize]
    BA-->>Main: receiveData array
    
    Main->>DP: new DatagramPacket(receiveData, length)
    DP-->>Main: receivePacket object
    
    Main->>DS: socket.receive(receivePacket)
    Note over DS,DP: Waits for packet
    DS-->>DP: Fills packet with data
    
    Main->>DP: receivePacket.getData()
    DP-->>Main: byte[] data
    
    Main->>Main: Convert to String
    
    Main->>FOS: new FileOutputStream(filename)
    FOS-->>Main: fos object
    
    loop For each packet
        Main->>DS: socket.receive(receivePacket)
        DS-->>DP: Fill with data
        Main->>DP: getData(), getLength()
        Main->>FOS: fos.write(data)
    end
    
    Main->>FOS: fos.close()
    Main->>DS: socket.close()
```

## 📦 Complete Class & Package Hierarchy

```
java.lang (Implicit - no import needed)
├── Object (root of all classes)
│   ├── String
│   ├── System
│   └── ... all classes inherit from Object

java.io
├── OutputStream (abstract)
│   └── FileOutputStream
├── IOException
└── Closeable (interface)
    └── FileOutputStream implements this

java.net
├── DatagramSocket
│   ├── implements Closeable
│   └── Methods: receive(), send(), close(), etc.
└── DatagramPacket
    ├── Methods: getData(), getLength(), setData()
    └── Represents UDP packet
```

## 🎯 Object Creation & Dependency Chain

```mermaid
flowchart LR
    A[main method starts] --> B{Create DatagramSocket}
    B --> C[socket = new DatagramSocket 9876]
    
    C --> D{Create byte array}
    D --> E[receiveData = new byte 1024]
    
    E --> F{Create DatagramPacket}
    F --> G[receivePacket = new DatagramPacket]
    G -.depends on.-> E
    
    G --> H{Receive first packet}
    H --> I[socket.receive]
    I -.uses.-> G
    
    I --> J{Extract filename}
    J --> K[new String from getData]
    K -.uses.-> G
    
    K --> L{Create FileOutputStream}
    L --> M[fos = new FileOutputStream]
    
    M --> N{Loop: Receive packets}
    N --> O[socket.receive]
    O -.uses.-> G
    
    O --> P{Check if END}
    P -->|Yes| Q[Close resources]
    P -->|No| R[Write to file]
    R -.uses.-> M
    R --> N
    
    Q --> S[fos.close & socket.close]
```

## 🔍 Method Call Hierarchy

```mermaid
graph TD
    A[main] --> B[DatagramSocket constructor]
    A --> C[new byte array]
    A --> D[DatagramPacket constructor]
    A --> E[socket.receive]
    A --> F[receivePacket.getData]
    A --> G[receivePacket.getLength]
    A --> H[String constructor]
    A --> I[String.trim]
    A --> J[FileOutputStream constructor]
    A --> K[String.equals]
    A --> L[fos.write]
    A --> M[fos.close]
    A --> N[socket.close]
    A --> O[System.out.println]
    A --> P[System.out.print]
    
    style A fill:#e1f5ff
    style B fill:#fff4e1
    style D fill:#fff4e1
    style E fill:#ffe1f5
    style J fill:#e1ffe1
```

## 📋 Complete Object Inventory

| Variable Name | Type | Package | Purpose | Dependencies |
|--------------|------|---------|---------|--------------|
| `port` | `int` | java.lang | Port number (9876) | None |
| `packetSize` | `int` | java.lang | Buffer size (1024) | None |
| `socket` | `DatagramSocket` | java.net | UDP socket for communication | port |
| `receiveData` | `byte[]` | java.lang | Buffer for incoming data | packetSize |
| `receivePacket` | `DatagramPacket` | java.net | Packet wrapper for data | receiveData |
| `filename` | `String` | java.lang | Name of file being received | receivePacket |
| `fos` | `FileOutputStream` | java.io | Writes data to file | filename |
| `message` | `String` | java.lang | Temporary string for checking "END" | receivePacket |
| `e` | `IOException` | java.io | Exception handler | N/A |

## 🎨 Data Flow Diagram

```mermaid
flowchart TB
    subgraph Input
        A[Client sends UDP packets]
    end
    
    subgraph Network Layer
        B[DatagramSocket<br/>receives packets]
    end
    
    subgraph Buffer Layer
        C[byte array<br/>receiveData]
        D[DatagramPacket<br/>receivePacket]
    end
    
    subgraph Processing Layer
        E{Check packet type}
        F[Extract filename]
        G[Extract file data]
        H{Is END signal?}
    end
    
    subgraph Output Layer
        I[FileOutputStream]
        J[Write to disk]
        K[received_filename.txt]
    end
    
    A --> B
    B --> C
    C --> D
    D --> E
    
    E -->|First packet| F
    E -->|Data packet| G
    E -->|Last packet| H
    
    F --> I
    G --> I
    I --> J
    J --> K
    
    H -->|Yes| L[Close & Stop]
    H -->|No| G
```

## 🔄 Method Execution Order

```
1. main() starts
   ├── 2. new DatagramSocket(port)
   │      └── Opens UDP socket on port 9876
   │
   ├── 3. new byte[packetSize]
   │      └── Creates 1024-byte buffer
   │
   ├── 4. new DatagramPacket(receiveData, length)
   │      └── Wraps buffer in packet structure
   │
   ├── 5. socket.receive(receivePacket)  [BLOCKS]
   │      └── Waits for first packet
   │
   ├── 6. receivePacket.getData()
   │      ├── 7. receivePacket.getLength()
   │      └── 8. new String(data, offset, length)
   │             └── 9. trim()
   │
   ├── 10. new FileOutputStream(filename)
   │       └── Creates/opens file for writing
   │
   ├── 11. LOOP: while(true)
   │       ├── 12. new byte[packetSize]  [fresh buffer]
   │       ├── 13. new DatagramPacket(receiveData, length)
   │       ├── 14. socket.receive(receivePacket)  [BLOCKS]
   │       ├── 15. new String(data, offset, length)
   │       ├── 16. message.equals("END")
   │       │      ├── TRUE → break (go to 21)
   │       │      └── FALSE → continue
   │       ├── 17. fos.write(data, offset, length)
   │       └── 18. System.out.print(".")
   │           └── Loop back to 12
   │
   ├── 21. fos.close()
   ├── 22. socket.close()
   └── 23. System.out.println(messages)
```

## 🧩 Class Method Usage Matrix

| Class | Method Used | Called By | Returns | Purpose |
|-------|-------------|-----------|---------|---------|
| **DatagramSocket** | `DatagramSocket(int port)` | main | DatagramSocket | Constructor - creates socket |
| | `receive(DatagramPacket)` | main | void | Receives incoming packet |
| | `close()` | main | void | Closes socket |
| **DatagramPacket** | `DatagramPacket(byte[], int)` | main | DatagramPacket | Constructor - wraps buffer |
| | `getData()` | main | byte[] | Gets packet data |
| | `getLength()` | main | int | Gets actual data length |
| **FileOutputStream** | `FileOutputStream(String)` | main | FileOutputStream | Constructor - opens file |
| | `write(byte[], int, int)` | main | void | Writes bytes to file |
| | `close()` | main | void | Closes file stream |
| **String** | `String(byte[], int, int)` | main | String | Constructor - bytes to string |
| | `trim()` | main | String | Removes whitespace |
| | `equals(String)` | main | boolean | Compares strings |
| **System.out** | `println(String)` | main | void | Print with newline |
| | `print(String)` | main | void | Print without newline |

## 🏗️ Object Lifecycle Diagram

```mermaid
stateDiagram-v2
    [*] --> SocketCreated: new DatagramSocket(port)
    
    SocketCreated --> BufferCreated: new byte[1024]
    BufferCreated --> PacketCreated: new DatagramPacket(buffer, length)
    
    PacketCreated --> WaitingData: socket.receive()
    WaitingData --> DataReceived: Packet arrives
    
    DataReceived --> FilenameExtracted: First packet?
    FilenameExtracted --> FileOpened: new FileOutputStream()
    
    FileOpened --> ReceiveLoop: Enter loop
    ReceiveLoop --> WaitingPacket: socket.receive()
    WaitingPacket --> PacketArrived: Data arrives
    
    PacketArrived --> CheckEnd: Is "END"?
    CheckEnd --> WriteFile: No - write data
    WriteFile --> ReceiveLoop: Continue
    
    CheckEnd --> CloseFile: Yes - fos.close()
    CloseFile --> CloseSocket: socket.close()
    CloseSocket --> [*]: Program ends
```

## 🔐 Resource Dependencies

```mermaid
graph LR
    A[Operating System] --> B[Network Stack]
    A --> C[File System]
    
    B --> D[Port 9876]
    D --> E[DatagramSocket]
    
    C --> F[File Handle]
    F --> G[FileOutputStream]
    
    H[JVM Memory] --> I[Heap]
    I --> J[byte arrays]
    I --> K[Objects]
    
    E -.requires.-> D
    G -.requires.-> F
    J -.stored in.-> I
    K -.stored in.-> I
    
    style A fill:#ffe1e1
    style H fill:#e1ffe1
```

## 📊 Memory Allocation Map

```
STACK MEMORY (Local Variables):
├── port (int, 4 bytes)
├── packetSize (int, 4 bytes)
├── socket (reference, 8 bytes) ────┐
├── receiveData (reference, 8 bytes)─┼─┐
├── receivePacket (reference, 8 bytes)┼┼┐
├── filename (reference, 8 bytes)────┼┼┼┐
├── fos (reference, 8 bytes)─────────┼┼┼┼┐
└── message (reference, 8 bytes)─────┼┼┼┼┼┐
                                     │││││││
HEAP MEMORY (Objects):               │││││││
├── DatagramSocket object ←──────────┘││││││
├── byte[1024] array ←─────────────────┘│││││
├── DatagramPacket object ←─────────────┘││││
├── String object (filename) ←───────────┘│││
├── FileOutputStream object ←─────────────┘││
└── String object (message) ←──────────────┘│
                                            │
DISK (File System):                         │
└── received_[filename].txt ←───────────────┘
```

## 🎓 Key Takeaways

### 1. **Class Dependencies**
- UDPServer depends on 3 main classes from Java libraries
- No custom classes created (simple, single-file program)
- All dependencies are standard Java library classes

### 2. **Object Relationships**
- `DatagramSocket` → manages network communication
- `DatagramPacket` → wraps data in UDP packet format
- `FileOutputStream` → handles file writing
- All three are independent but work together

### 3. **Method Calls**
- Most methods are called directly from `main()`
- Linear execution flow with one loop
- Two blocking calls: `socket.receive()` (waits for data)

### 4. **Resource Management**
- Three resources must be closed: socket, file stream, (byte arrays auto-managed)
- Closing happens in `finally` would be best practice
- Current code closes in try block (works if no exceptions)

### 5. **Data Transformation Chain**
```
Network → byte[] → DatagramPacket → byte[] → String (for END check)
                                   → byte[] → File (for data)
```

## 🚀 Complete Dependency Summary

**External Dependencies:**
- `java.net.DatagramSocket` - UDP socket
- `java.net.DatagramPacket` - Packet wrapper
- `java.io.FileOutputStream` - File writing
- `java.io.IOException` - Error handling
- `java.lang.String` - Text processing

**Internal Dependencies:**
- `socket` depends on: port number
- `receivePacket` depends on: receiveData array
- `fos` depends on: filename string
- Loop depends on: "END" signal detection

**No Circular Dependencies:** Clean, linear flow ✅
