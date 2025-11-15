package main;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import object.OBJ_Heart;
import object.OBJ_Key;
import entity.Entity;

public class UI {

    GamePanel gp;        // tham chiếu đến GamePanel để vẽ thông tin trò chơi
    Font arial_40, arial_80B;       // font chữ để hiển thị thông tin
    Graphics2D g2;
    BufferedImage heart_full, heart_half, heart_blank;
    public boolean messageOn = false; // bật/tắt hiển thị thông báo tạm thời
    public String message = "";       // nội dung thông báo
    int messageCounter = 0;  // đếm thời gian hiển thị thông báo
    public boolean gameFinished = false;
    public String currentDialogue = "";
    public int commandNum = 0;
    

    public UI(GamePanel gp) {
        this.gp = gp;

        arial_40 = new Font("Arial", Font.PLAIN, 40); // tạo font 40px
        arial_80B = new Font("Arial", Font.BOLD, 80);
        
        // Create hud object
        Entity heart = new OBJ_Heart(gp);
        heart_full = heart.image;
        heart_half = heart.image2;
        heart_blank = heart.image3;
    }

    public void showMessage(String text) {
        message = text;      // đặt nội dung thông báo
        messageOn = true;    // bật hiển thị
    }

    public void draw(Graphics2D g2) {
        this.g2 = g2;
        g2.setFont(arial_40);
        g2.setColor(Color.white);
        
        // TITLE STATE
        if(gp.gameState == gp.titleState) {
        	drawTitleScreen();
        }

        // PLAY STATE
        if (gp.gameState == gp.playState) {
            // Không cần vẽ gì thêm ở đây (UI chỉ vẽ HUD, hoặc thông tin player)
            // Ví dụ: drawPlayerLife(); hoặc drawMessage();
        	drawPlayerLife();
        }

        // PAUSE STATE
        if (gp.gameState == gp.pauseState) {
        	drawPlayerLife();
            drawPauseScreen();
        }

        // DIALOGUE STATE
        if (gp.gameState == gp.dialogueState) {
        	drawPlayerLife();
            drawDialogueScreen();
        }
        // CHARACTER STATE
        if (gp.gameState == gp.characterState) {
        	drawCharacterScreen();
        }
    }
    
    public void drawPlayerLife() {

        // --- Thiết lập font chữ ---
        g2.setFont(g2.getFont().deriveFont(Font.BOLD, 24F));
        g2.setColor(Color.white);

        // --- Vẽ khung trái tim (Heart container) ---
        int x = gp.tileSize / 2;
        int y = gp.tileSize / 2;
        int i = 0;

        // Vẽ tất cả tim rỗng (khung)
        while (i < gp.player.maxLife / 2) {
            g2.drawImage(heart_blank, x, y, null);
            i++;
            x += gp.tileSize;
        }

        // --- Reset lại vị trí để vẽ tim đầy ---
        x = gp.tileSize / 2;
        y = gp.tileSize / 2;
        i = 0;

        // Vẽ tim nửa đầy (ví dụ 3.5 máu)
        while (i < gp.player.life) {
            g2.drawImage(heart_half, x, y, null);
            i++;
            if (i < gp.player.life) {
                g2.drawImage(heart_full, x, y, null);
                i++;
            }
            x += gp.tileSize;
        }
    }

    public void drawCharacterScreen() {
        // create a frame
        final int frameX = gp.tileSize * 2;
        final int frameY = gp.tileSize;
        final int frameWidth = gp.tileSize * 5;
        final int frameHeight = gp.tileSize * 10;
        drawSubWindow(frameX, frameY, frameWidth, frameHeight);
    	
    }

    
    public void drawTitleScreen() {

        // Màu nền
        g2.setColor(new Color(70, 120, 80));
        g2.fillRect(0, 0, gp.screenWidth, gp.screenHeight);

        // TÊN GAME
        g2.setFont(g2.getFont().deriveFont(Font.BOLD, 96F));
        String text = "My 2D Adventure";
        int x = getXforCenteredText(text);
        int y = gp.tileSize * 3;

        // Viền chữ
        g2.setColor(Color.black);
        g2.drawString(text, x + 5, y + 5);

        // Chữ chính
        g2.setColor(Color.white);
        g2.drawString(text, x, y);

        // VẼ NHÂN VẬT (logo hoặc hero đứng giữa màn hình)
        x = gp.screenWidth / 2 - (gp.tileSize * 2) / 2;
        y += gp.tileSize * 2;
        g2.drawImage(gp.player.down1, x, y, gp.tileSize * 2, gp.tileSize * 2, null);

        // MENU
        g2.setFont(g2.getFont().deriveFont(Font.BOLD, 48F));

        text = "NEW GAME";
        x = getXforCenteredText(text);
        y += gp.tileSize * 4;
        g2.drawString(text, x, y);
        if (commandNum == 0) {
            g2.drawString(">", x - gp.tileSize, y);
        }

        text = "LOAD GAME";
        x = getXforCenteredText(text);
        y += gp.tileSize;
        g2.drawString(text, x, y);
        if (commandNum == 1) {
            g2.drawString(">", x - gp.tileSize, y);
        }

        text = "QUIT";
        x = getXforCenteredText(text);
        y += gp.tileSize;
        g2.drawString(text, x, y);
        if (commandNum == 2) {
            g2.drawString(">", x - gp.tileSize, y);
        }
    }

    
    public void drawDialogueScreen() {
        // Hộp thoại nền
        int x = gp.tileSize * 2;
        int y = gp.tileSize / 2;
        int width = gp.screenWidth - (gp.tileSize * 4);
        int height = gp.tileSize * 4;

        drawSubWindow(x, y, width, height);

        g2.setFont(arial_40.deriveFont(Font.PLAIN, 28F));
        x += gp.tileSize;
        y += gp.tileSize;

        for (String line : currentDialogue.split("\n")) {
            g2.drawString(line, x, y);
            y += 40; // khoảng cách giữa các dòng
        }
    }
    
    public void drawSubWindow(int x, int y, int width, int height) {
        Color c = new Color(0, 0, 0, 200); // màu đen, độ trong suốt 200/255
        g2.setColor(c);
        g2.fillRoundRect(x, y, width, height, 35, 35); // bo tròn góc

        // Vẽ viền trắng quanh hộp
        c = new Color(255, 255, 255);
        g2.setStroke(new BasicStroke(5)); // độ dày của viền
        g2.setColor(c);
        g2.drawRoundRect(x + 5, y + 5, width - 10, height - 10, 25, 25);
    }

    public void drawPauseScreen() {
    	String text = "PAUSED";
    	
    	int x = getXforCenteredText(text);
    	int y = gp.screenHeight/2;
    	
    	g2.drawString(text,x,y);
    	
    }
    public int getXforCenteredText(String text) {
    	int length = (int)g2.getFontMetrics().getStringBounds(text, g2).getWidth();
    	int x = gp.screenWidth/2 - length/2;
    	return x;
    }

}
