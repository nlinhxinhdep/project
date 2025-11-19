package main;

import javax.swing.JFrame;

public class Main {

    public static JFrame window;
    public static void main(String[] args) {
        window = new JFrame();                         // Tạo cửa sổ mới (JFrame)
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);// Đóng cửa sổ sẽ thoát chương trình
        window.setResizable(false);                // Không cho phép thay đổi kích thước cửa sổ
        window.setTitle("My 2D Game");                 // Đặt tiêu đề cho cửa sổ
        // window.setUndecorated(true);             // Fullscreen

        GamePanel gamePanel = new GamePanel();                // Tạo đối tượng GamePanel (màn hình game)
        window.add(gamePanel);                                // Thêm GamePanel vào trong cửa sổ

        gamePanel.config.loadConfig();                             // Load cấu hình từ file
        if(gamePanel.fullScreenOn == true) {
            window.setUndecorated(true);
        }

        window.pack();                                        // Tự căn kích thước cửa sổ theo GamePanel
        window.setLocationRelativeTo(null);                 // Đặt cửa sổ ra giữa màn hình
        window.setVisible(true);                            // Hiển thị cửa sổ lên màn hình

        gamePanel.requestFocusInWindow();                     // Cho phép GamePanel nhận phím từ bàn phím
        gamePanel.setupGame();                                // Chuẩn bị dữ liệu, bản đồ, nhân vật, v.v.
        gamePanel.startGameThread();                          // Bắt đầu vòng lặp game (cập nhật + vẽ liên tục)
    }
}


