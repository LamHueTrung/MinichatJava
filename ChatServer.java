import java.io.*;
import java.net.*;
import java.util.*;

public class ChatServer {
    private static final Map<String, Set<Socket>> chatRooms = new HashMap<>();
    private static final Map<Socket, String> clientRoomMap = new HashMap<>();

    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(12345)) {
            System.out.println("Server is running...");

            while (true) {
                Socket clientSocket = serverSocket.accept();
                new Thread(() -> handleClient(clientSocket)).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void handleClient(Socket clientSocket) {
        String username = null;
        String roomName = null;

        try (BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true)) {

            // Nhận thông tin username
            username = in.readLine();

            // Nhận thông tin phòng
            roomName = in.readLine();

            // Thêm client vào phòng
            synchronized (chatRooms) {
                chatRooms.putIfAbsent(roomName, new HashSet<>());
                chatRooms.get(roomName).add(clientSocket);
                clientRoomMap.put(clientSocket, roomName);
            }

            System.out.println(username + " joined room: " + roomName);

            // Gửi tin nhắn trong phòng
            String message;
            while ((message = in.readLine()) != null) {
                String fullMessage = username + ": " + message;
                broadcast(roomName, fullMessage, clientSocket);
            }
        } catch (IOException e) {
            System.out.println("Client disconnected: " + username + " from room: " + roomName);
        } finally {
            removeClient(clientSocket, username, roomName);
        }
    }

    private static void broadcast(String roomName, String message, Socket sender) {
        synchronized (chatRooms) {
            Set<Socket> roomClients = chatRooms.getOrDefault(roomName, new HashSet<>());

            Iterator<Socket> iterator = roomClients.iterator();
            while (iterator.hasNext()) {
                Socket socket = iterator.next();
                if (socket != sender) {
                    try {
                        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                        out.println(message);
                    } catch (IOException e) {
                        System.out.println("Failed to send message to a client in room: " + roomName);
                        iterator.remove(); // Loại bỏ client không còn hợp lệ
                    }
                }
            }
        }
    }

    private static void removeClient(Socket clientSocket, String username, String roomName) {
        if (roomName != null) {
            synchronized (chatRooms) {
                Set<Socket> roomClients = chatRooms.get(roomName);
                if (roomClients != null) {
                    roomClients.remove(clientSocket);
                    System.out.println(username + " left room: " + roomName);

                    if (roomClients.isEmpty()) {
                        chatRooms.remove(roomName);
                        System.out.println("Room " + roomName + " is now empty and has been removed.");
                    }
                }
            }
        }
        clientRoomMap.remove(clientSocket);
        try {
            clientSocket.close();
        } catch (IOException e) {
            System.out.println("Error closing client socket for: " + username);
        }
    }
}
