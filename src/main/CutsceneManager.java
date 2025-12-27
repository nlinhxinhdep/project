package main;
import java.awt.AlphaComposite;
import java.awt.Graphics2D;

import entity.PlayerDummy;
import monster.MON_SkeletonLord;
import object.OBJ_BlueHeart;
import object.OBJ_Door_Iron;
import java.awt.Color;

public class CutsceneManager {
    
    GamePanel gp;
    Graphics2D g2;
    public int sceneNum;
    public int scenePhase;
    int counter = 0;
    float alpha = 0f;
    int y;
    String endCredit;

    //Scene Number
    public final int NA = 0;
    public final int skeletonLord = 1;
    public final int ending = 2;

    public CutsceneManager(GamePanel gp) {
        this.gp = gp;

        // Cập nhật thông tin nhóm 6 người
        endCredit = "DEVELOPED BY GROUP 6\n"
                  + "--------------------------\n\n"
                  + "--- Project Lead & Testing ---\n"
                  + "Hoang Phuc Hung\n\n"
                  + "--- Graphics & Resources ---\n"
                  + "Dang Trung Hieu\n\n"
                  + "--- Core Programming Team ---\n"
                  + "Mai Huy Hoang\n"
                  + "Do Trang Minh Quan\n"
                  + "Nguyen Duc Thien\n"
                  + "Le Tran Manh Tien\n"
                  + "Hoang Phuc Hung\n"
                  + "Dang Trung Hieu\n\n\n\n\n\n\n\n\n\n\n"
                  + "Thank you for playing!";
    }
    public void draw(Graphics2D g2) {
        this.g2 = g2;

        switch(sceneNum) {
            case skeletonLord: scene_skeletonLord(); break;
            case ending: scene_ending(); break;
        }
    }
    public void scene_skeletonLord() {
        if(scenePhase == 0) {
            gp.bossBattleOn = true;
            
            // Shut the iron door
            for(int i = 0; i < gp.obj[1].length; i++) {

                if(gp.obj[gp.currentMap][i] == null) {
                    gp.obj[gp.currentMap][i] = new OBJ_Door_Iron(gp);
                    gp.obj[gp.currentMap][i].worldX = gp.tileSize * 25;
                    gp.obj[gp.currentMap][i].worldY = gp.tileSize * 28;
                    gp.obj[gp.currentMap][i].temp = true;
                    break;
                }
            }
            
            //Search a vacant slot for the dummy
            for(int i = 0; i < gp.npc[1].length; i++) {
                if(gp.npc[gp.currentMap][i] == null) {
                    gp.npc[gp.currentMap][i] = new PlayerDummy(gp);
                    gp.npc[gp.currentMap][i].worldX = gp.player.worldX;
                    gp.npc[gp.currentMap][i].worldY = gp.player.worldY;
                    gp.npc[gp.currentMap][i].direction = gp.player.direction;
                    break;
                }
            }

            gp.player.drawing = false;

            scenePhase++;
            
        }
        if(scenePhase == 1) {
            
            gp.player.worldY -= 2;

            if(gp.player.worldY < gp.tileSize * 16) {
                scenePhase++;
            }
        }
        if(scenePhase == 2) {
            
            //Search the boss
            for(int i = 0; i < gp.monster[1].length; i++) {
                if(gp.monster[gp.currentMap][i] != null && gp.monster[gp.currentMap][i].name == MON_SkeletonLord.monName) {
                    gp.monster[gp.currentMap][i].sleep = false;
                    gp.ui.npc = gp.monster[gp.currentMap][i];
                    scenePhase++;
                    break;
                }
            }
        }
        if(scenePhase == 3) {

            //The boss speaks
            gp.ui.drawDialogueScreen();
        }
        if(scenePhase == 4) {
            
            // Return to the player

            // Search the dummy
            for(int i = 0; i < gp.npc[1].length; i++) {
                
                if(gp.npc[gp.currentMap][i] != null && gp.npc[gp.currentMap][i].name.equals(PlayerDummy.npcName)) {
                    //Restore player position
                    gp.player.worldX = gp.npc[gp.currentMap][i].worldX;
                    gp.player.worldY = gp.npc[gp.currentMap][i].worldY;
                    // Delete the dummy
                    gp.npc[gp.currentMap][i] = null;
                    break;
                }
            }

            // Start drawing the player
            gp.player.drawing = true;

            // Reset
            sceneNum = NA;
            scenePhase = 0;
            gp.gameState = gp.playState;

            // Boss battle music
            gp.stopMusic();
            gp.playMusic(22);

            gp.resetTimer = true;
        }
    }
    public void scene_ending() {
        
        if(scenePhase == 0){
            gp.stopMusic();
            gp.ui.npc = new OBJ_BlueHeart(gp);
            scenePhase++;
        }
        if(scenePhase == 1){
            // Display dialogues
            gp.ui.drawDialogueScreen();
        }
        if(scenePhase == 2){
            //Play the fanfare
            gp.playSE(4);
            scenePhase++;
        }
        if(scenePhase == 3){
            // Wait until the sound effect finishes
            if(counterReached(300) == true){
                scenePhase++;
            }
        }
        if(scenePhase == 4){
            // The screen gets darker
            alpha += 0.005f;
            if(alpha > 1f){
                alpha = 1f;
            }
            drawBlackBackground(alpha);

            if(alpha == 1f){
                alpha = 0;
                scenePhase++;
            }
        }
        if(scenePhase == 5){
            drawBlackBackground(1f);
            alpha += 0.005f;
            if(alpha > 1f){
                alpha = 1f;
            }

            String text = "Congratulations!\nYou have defeated the Skeleton Lord\nand brought peace back to the kingdom!";
            drawString(alpha, 38f, 200, text, 70);

            if(counterReached(600) == true){
                gp.playMusic(0);
                scenePhase++;
            }
        }
        if(scenePhase == 6){
            drawBlackBackground(1f);
            
            // Hiện tên game
            drawString(1f, 120f, gp.screenHeight / 2, "Huster Adventure", 40);
            
            // Đợi 5 giây (300 frames) rồi mới bắt đầu chạy Credit
            if(counterReached(300) == true){
                scenePhase++;
                // Reset vị trí Y xuống dưới đáy màn hình để chuẩn bị chạy lên
                y = gp.screenHeight + 100; 
            }
        }
        if(scenePhase == 7){
            drawBlackBackground(1f);

            // Chạy chữ từ dưới lên
            y--; 
            drawString(1f, 38f, y, endCredit, 40);

            // LOGIC DỪNG:
            // Tính toán chiều cao của cả đoạn văn bản
            int lineHeight = 40;
            int textHeight = endCredit.split("\n").length * lineHeight;
            
            // Khi dòng cuối cùng ("Thank you...") chạy đến giữa màn hình thì dừng lại
            // Công thức: y (đỉnh text) + textHeight (độ cao text) = vị trí đáy text
            // Ta muốn đáy text nằm ở giữa màn hình (gp.screenHeight/2)
            if(y + textHeight < gp.screenHeight / 2 + 130) { // +130 để nó nằm thấp xuống một chút cho đẹp
                scenePhase++;
            }
        }
        if(scenePhase == 8){
            drawBlackBackground(1f);
            
            // Vẽ chữ đứng yên tại vị trí y hiện tại
            drawString(1f, 38f, y, endCredit, 40);

            // Đợi 5 giây (300 frames) để người chơi đọc dòng Thank You
            if(counterReached(480) == true){
                scenePhase++;
            }
        }
        if(scenePhase == 9){
            // Reset game và trở về màn hình chính
            gp.stopMusic();
            gp.gameState = gp.titleState;
            sceneNum = NA;
            scenePhase = 0;
            gp.playMusic(23);
        }
    }
    public boolean counterReached(int target){
        boolean counterReached = false;

        counter++;
        if(counter > target){
            counterReached = true;
            counter = 0;
        }
        
        return counterReached;
    }
    public void drawBlackBackground(float alpha){
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
        g2.setColor(Color.black);
        g2.fillRect(0,0,gp.screenWidth, gp.screenHeight);
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
    }
    public void drawString(float alpha, float fontSize, int y, String text, int lineHeight){

        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
        g2.setColor(Color.white);
        g2.setFont(g2.getFont().deriveFont(fontSize));

        for(String line: text.split("\n")){
            int x = gp.ui.getXforCenteredText(line);
            g2.drawString(line,x,y);
            y += lineHeight;
        }
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
    }
}