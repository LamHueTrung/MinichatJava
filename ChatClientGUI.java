import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.net.*;

public class ChatClientGUI {
    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;
    private String username;
    private String roomName;

    public ChatClientGUI() {
        JFrame frame = new JFrame("Chat LHT");
        frame.setSize(400, 500);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel chatPanel = new JPanel();
        chatPanel.setLayout(new BoxLayout(chatPanel, BoxLayout.Y_AXIS)); // Sử dụng BoxLayout để sắp xếp các tin nhắn
        JScrollPane chatScrollPane = new JScrollPane(chatPanel);

        JTextField inputField = new JTextField();
        JButton sendButton = new JButton("Gởi");

        JPanel panel = new JPanel(new BorderLayout());
        panel.add(inputField, BorderLayout.CENTER);
        panel.add(sendButton, BorderLayout.EAST);

        frame.add(chatScrollPane, BorderLayout.CENTER);
        frame.add(panel, BorderLayout.SOUTH);

        frame.setVisible(true);

        // Kết nối tới server
        try {
            socket = new Socket("localhost", 12345);
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            // Nhập username
            username = JOptionPane.showInputDialog(frame, "Nhập tên của bạn:");
            out.println(username);

            // Nhập tên phòng
            roomName = JOptionPane.showInputDialog(frame, "Nhập tên phòng chat:");
            out.println(roomName);

            // Thread để nhận tin nhắn
            new Thread(() -> {
                try {
                    String message;
                    while ((message = in.readLine()) != null) {
                        // Bỏ qua các dòng thông báo không cần thiết
                        if (message.startsWith("Enter your username:") || message.startsWith("Enter chat room")) {
                            continue; // Không hiển thị dòng này
                        }

                        if (message.startsWith(username + ":")) {
                            addMessage(chatPanel, message, true); // Tin nhắn của chính mình (căn phải)
                        } else {
                            addMessage(chatPanel, message, false); // Tin nhắn của đối phương (căn trái)
                        }
                    }
                } catch (IOException e) {
                    addMessage(chatPanel, "Connection lost.", false);
                }
            }).start();

            // Xử lý gửi tin nhắn
            sendButton.addActionListener(e -> {
                String message = inputField.getText();
                if (!message.isEmpty()) {
                    String formattedMessage = message;
                    addMessage(chatPanel, formattedMessage, true); // Hiển thị tin nhắn của mình
                    out.println(formattedMessage); // Gửi tin nhắn tới server
                    inputField.setText("");
                }
            });

            inputField.addActionListener(e -> sendButton.doClick());

        } catch (IOException e) {
            JOptionPane.showMessageDialog(frame, "Unable to connect to server.");
            System.exit(0);
        }
    }

    // Thêm tin nhắn vào giao diện
    private void addMessage(JPanel chatPanel, String message, boolean isMine) {
        JPanel messagePanel = new JPanel();
        messagePanel.setLayout(new BorderLayout());
        JLabel messageLabel = new JLabel(message);

        if (isMine) {
            messagePanel.add(messageLabel, BorderLayout.EAST); // Căn phải
            messageLabel.setBackground(new Color(173, 216, 230)); // Màu nền cho tin nhắn của mình
        } else {
            messagePanel.add(messageLabel, BorderLayout.WEST); // Căn trái
            messageLabel.setBackground(new Color(240, 240, 240)); // Màu nền cho tin nhắn của đối phương
        }

        messageLabel.setOpaque(true);
        messageLabel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));

        chatPanel.add(messagePanel);
        chatPanel.revalidate(); // Cập nhật giao diện
        chatPanel.repaint();
    }

    public static void main(String[] args) {
        new ChatClientGUI();
    }
}
