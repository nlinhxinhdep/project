package main;

import javax.swing.JPanel;

import entity.Entity;
import entity.Player;
import tile.TileManager;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.awt.Color;

public class GamePanel extends JPanel implements Runnable {

    // SCREEN SETTINGS
    final int originalTileSize = 16; // 16x16 tile
    final int scale = 3;

    public final int tileSize = originalTileSize * scale; // 48x48 tile
    public final int maxScreenCol = 20;
    public final int maxScreenRow = 12;
    public final int screenWidth = tileSize * maxScreenCol;  // 768 pixels
    public final int screenHeight = tileSize * maxScreenRow; // 576 pixels
    // World settings
    public final int maxWorldCol = 50;
    public final int maxWorldRow = 50;
    
    
    

    // FPS
    int FPS = 60;
    
    // System
    TileManager tileM = new TileManager(this);
    public KeyHandler keyH = new KeyHandler(this);
    Sound music = new Sound();
    Sound se = new Sound();
    public CollisionChecker cChecker = new CollisionChecker(this); 
    public AssetSetter aSetter = new AssetSetter(this);
    public UI ui = new UI(this);
    public EventHandler eHandler = new EventHandler(this);
    Thread gameThread;
    
    // ENTITY AND OBJECT
    public Player player = new Player(this, keyH);
    public Entity obj[] = new Entity[20];
    public Entity npc[] = new Entity[20];
    public Entity monster[] = new Entity[20];
    
    ArrayList<Entity> entityList = new ArrayList<>();
    
    //Game State
    public int gameState;
    public final int titleState = 0;
    public final int playState = 1;
    public final int pauseState = 2;
    public final int dialogueState = 3;
    


    public GamePanel() {
        this.setPreferredSize(new Dimension(screenWidth, screenHeight)); // Đặt kích thước ưa thích cho GamePanel (rộng x cao)
        this.setBackground(Color.black);                                 // Đặt màu nền là màu đen
        this.setDoubleBuffered(true);                                    // Bật chế độ double buffering để chống nhấp nháy khi vẽ
        this.addKeyListener(keyH);                                       // Gắn KeyListener để bắt sự kiện bàn phím
        this.setFocusable(true);                                         // Cho phép GamePanel nhận focus để điều khiển bằng phím
    }

    
    public void setupGame() {
        aSetter.setObject();
        aSetter.setNPC();
        aSetter.setMonster();
//        playMusic(0);
        gameState = titleState;
    }

    public void startGameThread(){
        gameThread = new Thread(this);
        gameThread.start();
        this.requestFocusInWindow();
    }

    @Override
    public void run() {
        long lastTime = System.nanoTime();             // Lấy thời điểm hiện tại (tính bằng nano giây)
        double drawInterval = 1000000000 / FPS;        // Thời gian giữa mỗi khung hình (1 giây / số khung hình/giây)
        double delta = 0;                              // Biến đếm để kiểm soát khi nào cần cập nhật khung hình

        while (gameThread != null) {                   // Vòng lặp chính của game (game loop), chạy liên tục khi gameThread tồn tại
            long currentTime = System.nanoTime();      // Lấy thời điểm hiện tại (nano giây)
            delta += (currentTime - lastTime) / drawInterval; 	// Tính xem đã trôi qua bao nhiêu phần của 1 khung hình
            lastTime = currentTime;                    			// Cập nhật lại mốc thời gian để tính lần sau

            if (delta >= 1) {                       // Khi đủ thời gian cho 1 khung hình (delta >= 1)
                update();                           // Cập nhật trạng thái game (vị trí nhân vật, va chạm, logic, v.v.)
                repaint();                          // Vẽ lại màn hình game (gọi paintComponent)
                delta--;                            // Giảm delta xuống 1 để chuẩn bị cho khung tiếp theo
            } 
        }
    }


    public void update(){
    	if(gameState == playState) {
    		// player
    		player.update();
    		// npc
    		for(int i = 0; i < npc.length; i++) {
    			if(npc[i] != null) {
    				npc[i].update();
    			}
    		}
    		// monster
    		for(int i = 0; i < monster.length; i++) {
    			if(monster[i] != null) {
    				if(monster[i].alive == true && monster[i].dying == false){
    					monster[i].update();	
    				}
    				if(monster[i].alive == false){
    					monster[i] = null;	
    				}
    			}
    		}
    		
    		
    	}
    	if(gameState == pauseState) {
    		//nothing
    	}  
    }
    
// @Override
    public void paintComponent(Graphics g){
        super.paintComponent(g);                // Gọi hàm paintComponent của lớp cha để xóa nền cũ
        Graphics2D g2 = (Graphics2D)g;          // Ép kiểu Graphics sang Graphics2D để dùng các hàm vẽ nâng cao
        
        // TITLE SCREEN
        if(gameState == titleState) {
        	ui.draw(g2);
        }
        else {
        	 tileM.draw(g2); // Vẽ bản đồ (tile map)
        	 
        	 // add entity to the list
        	 entityList.add(player);
        	 for(int i = 0; i < npc.length; i++) {
        		 if(npc[i] != null) {
        			 entityList.add(npc[i]);
        		 }
        	 }
        	 for(int i = 0; i < obj.length; i++) {
        		 if(obj[i] != null) {
        			 entityList.add(obj[i]);
        		 }
        	 }
        	 for(int i = 0; i < monster.length; i++) {
        		 if(monster[i] != null) {
        			 entityList.add(monster[i]);
        		 }
        	 }
        	 
        	 // sort
        	 Collections.sort(entityList, new Comparator<Entity>(){
        		  @Override
        		  public int compare(Entity e1, Entity e2) {
        			  int result = Integer.compare(e1.worldY, e2.worldY);
        			  return result;
        		  }
        	});
        	 
        	 for(int i = 0; i < entityList.size(); i++) {
        		 entityList.get(i).draw(g2);
        	 }
        	 
        	 // empty
        		 entityList.clear();
        	 
             
             ui.draw(g2);
             	
        }
        g2.dispose();          
    }
    
    public void playMusic(int i) {
    	music.setFile(i);
    	music.play();
    	music.loop();
    }
    
    public void stopMusic() {
    	music.stop();
    }
    
    public void playSE(int i) {
    	se.setFile(i);
    	se.play();
    }

}
