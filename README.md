
# Ứng dụng Chat với Hỗ trợ Phòng Chat

Dự án này triển khai một ứng dụng chat cơ bản gồm server và client, hỗ trợ nhiều phòng chat. Người dùng có thể tham gia các phòng chat cụ thể, gửi tin nhắn, và nhận tin nhắn từ các người dùng khác trong cùng phòng.

## Tính năng

### Server
- Xử lý nhiều client cùng lúc.
- Hỗ trợ nhiều phòng chat.
- Tự động xóa phòng trống.
- Xử lý ngắt kết nối của client một cách an toàn.
- Chỉ gửi tin nhắn đến các client trong cùng phòng.

### Client
- Ứng dụng chat với giao diện GUI sử dụng Swing.
- Cho phép người dùng nhập tên và chọn phòng chat.
- Hiển thị tin nhắn từ người dùng khác trong cùng phòng.
- Tự động cuộn xuống cuối khi có tin nhắn mới.
- Phân biệt tin nhắn của chính mình và của người khác (căn chỉnh và màu nền).

## Yêu cầu Môi trường

### Công cụ cần thiết
1. **Java Development Kit (JDK)**: Cài đặt JDK 8 hoặc cao hơn.
2. **VS Code**: Cài đặt Visual Studio Code.
3. **Java Extension Pack**: Cài đặt extension này từ Marketplace của VS Code để hỗ trợ phát triển Java.

### Cài đặt Môi trường
1. Tải và cài đặt JDK từ [Oracle](https://www.oracle.com/java/technologies/javase-downloads.html) hoặc [OpenJDK](https://openjdk.org/).
2. Cài đặt Visual Studio Code từ [VS Code](https://code.visualstudio.com/).
3. Mở VS Code và cài đặt **Java Extension Pack** từ Marketplace.
4. Tạo một thư mục cho dự án và thêm hai file: `ChatServer.java` và `ChatClientGUI.java`.

## Cách thức hoạt động

### Mô tả
1. Server chạy trên một cổng cố định (mặc định là `12345`).
2. Client kết nối đến server, nhập tên người dùng và tên phòng chat.
3. Các client trong cùng phòng có thể gửi và nhận tin nhắn của nhau.
4. Khi một client rời khỏi phòng, server tự động xóa client đó.
5. Nếu một phòng không còn người, server sẽ xóa phòng đó.

### Chức năng chính
- **Server**:
  - Quản lý nhiều phòng chat.
  - Xử lý kết nối và ngắt kết nối của client.
  - Gửi tin nhắn đến đúng phòng chat.
- **Client**:
  - Kết nối tới server, chọn phòng chat.
  - Hiển thị tin nhắn từ các người dùng khác.
  - Phân biệt tin nhắn của mình và của người khác.

## Hướng dẫn chạy

### Bước 1: Chạy Server
1. Mở VS Code.
2. Tạo file `ChatServer.java` với mã nguồn của server.
3. Biên dịch server bằng cách mở terminal trong VS Code:
   ```bash
   javac ChatServer.java
   ```
4. Chạy server:
   ```bash
   java ChatServer
   ```
5. Server sẽ bắt đầu lắng nghe trên cổng `12345`.

### Bước 2: Chạy Client
1. Tạo file `ChatClientGUI.java` với mã nguồn của client.
2. Biên dịch client:
   ```bash
   javac ChatClientGUI.java
   ```
3. Chạy client:
   ```bash
   java ChatClientGUI
   ```
4. Nhập tên người dùng và tên phòng chat để tham gia.

## Ví dụ Sử dụng

### Kịch bản
1. Chạy server.
2. Mở hai client.
3. **Client 1**:
   - Nhập tên: `Alice`
   - Nhập phòng: `Love`
4. **Client 2**:
   - Nhập tên: `Bob`
   - Nhập phòng: `Love`
5. Cả hai client có thể gửi và nhận tin nhắn trong phòng `Love`.

## Tổng quan Mã

### ChatServer
Class `ChatServer` xử lý tất cả các chức năng server, bao gồm:
- Quản lý phòng chat và các client trong phòng.
- Gửi tin nhắn đến tất cả client trong một phòng.
- Loại bỏ client và dọn dẹp phòng trống.

**Các thành phần chính:**
- **`Map<String, Set<Socket>> chatRooms`**: Lưu trữ các phòng chat và các client trong phòng.
- **`Map<Socket, String> clientRoomMap`**: Ánh xạ mỗi client tới phòng của họ.

#### Chức năng chính
- `handleClient(Socket clientSocket)`: Xử lý một kết nối client, nhận và xử lý tin nhắn.
- `broadcast(String roomName, String message, Socket sender)`: Gửi tin nhắn đến tất cả các client trong phòng, trừ người gửi.
- `removeClient(Socket clientSocket)`: Xóa client khỏi server và phòng.

### ChatClientGUI
Class `ChatClientGUI` triển khai chức năng client với giao diện Swing.

**Các tính năng:**
- Nhập tên người dùng và phòng chat để tham gia.
- Hiển thị tin nhắn từ người dùng khác với căn chỉnh và màu sắc phù hợp.
- Tự động cuộn xuống khi có tin nhắn mới.

#### Chức năng chính
- `addMessage(JPanel chatPanel, String message, boolean isMine)`: Thêm tin nhắn vào giao diện, căn chỉnh tùy thuộc vào người gửi.

## Các cải tiến

### Hiện tại
- Chỉ hỗ trợ gửi tin nhắn văn bản.
- Server sử dụng `Socket` và `PrintWriter` cơ bản.

### Đề xuất nâng cấp
1. **Chia sẻ tệp**: Cho phép người dùng gửi và nhận tệp.
2. **Xác thực**: Thêm hệ thống đăng nhập.
3. **Mã hóa**: Mã hóa tin nhắn để bảo mật.
4. **Lịch sử tin nhắn**: Lưu trữ lịch sử tin nhắn vào cơ sở dữ liệu.
5. **Giao diện hiện đại**: Nâng cấp giao diện sử dụng JavaFX.

## Khắc phục sự cố

### Các vấn đề thường gặp

#### 1. **Không kết nối được tới server**
- Kiểm tra server có đang chạy không.
- Đảm bảo server đang lắng nghe trên đúng cổng (`12345`).
- Kiểm tra địa chỉ IP nếu chạy trên các máy khác nhau.

#### 2. **Client bị ngắt kết nối**
- Kiểm tra kết nối mạng.
- Đảm bảo client và server sử dụng cùng giao thức và cổng.

#### 3. **Tin nhắn không hiển thị**
- Đảm bảo các client ở trong cùng một phòng chat.
- Kiểm tra server hoạt động ổn định.

## Giấy phép
Dự án này là mã nguồn mở và được sử dụng cho mục đích giáo dục. Bạn có thể tự do chỉnh sửa và sử dụng trong các dự án của riêng mình.
