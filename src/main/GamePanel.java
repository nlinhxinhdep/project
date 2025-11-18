package main;

import javax.swing.JPanel;
import entity.Entity;
import entity.Player;
import tile.TileManager;
import tile_interactive.InteractiveTile;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.image.BufferedImage;
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
    public final int screenWidth = tileSize * maxScreenCol;  // 960 pixels
    public final int screenHeight = tileSize * maxScreenRow; // 576 pixels
    // World settings
    public final int maxWorldCol = 50;
    public final int maxWorldRow = 50;
    public final int maxMap =10;
    public int currentMap =1;
    // Full screen
    int screenWidth2 = screenWidth;
    int screenHeight2 = screenHeight;
    BufferedImage tempScreen;
    Graphics2D g2;
    public boolean fullScreenOn = false;
    

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
    Config config = new Config(this);
    Thread gameThread;
    
    // ENTITY AND OBJECT
    public Player player = new Player(this, keyH);
    public Entity obj[][] = new Entity[maxMap][20];
    public Entity npc[][] = new Entity[maxMap][20];
    public Entity monster[][] = new Entity[maxMap][20];
    public InteractiveTile iTile[][] = new InteractiveTile[maxMap][50];
    public ArrayList<Entity> projectileList = new ArrayList<>();
    public ArrayList<Entity> particleList = new ArrayList<>();
    ArrayList<Entity> entityList = new ArrayList<>();
    
    //Game State
    public int gameState;
    public final int titleState = 0;
    public final int playState = 1;
    public final int pauseState = 2;
    public final int dialogueState = 3;
    public final int characterState = 4;
    public final int optionsState = 5;
    public final int gameOverState = 6;


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
        aSetter.setInteractiveTile();
//        playMusic(0);
        gameState = titleState;

        tempScreen = new BufferedImage(screenWidth, screenHeight, BufferedImage.TYPE_INT_ARGB);
        g2 = (Graphics2D)tempScreen.getGraphics();


        if(fullScreenOn == true)
        {
            setFullScreen();
        }
        
    }
    // Retry game
    public void retry ()
    {
        player.setDefaultPositions();
        player.restoreLifeAndMana();
        aSetter.setNPC();
        aSetter.setMonster();
    }
    //RESTART game
    public void restart ()
    {
        player.setDefaultValues();
        player.setDefaultPositions();
        player.restoreLifeAndMana();
        player.setItems();
        aSetter.setObject();
        aSetter.setNPC();
        aSetter.setMonster();
        aSetter.setInteractiveTile();
    }

    public void setFullScreen() {
        // Lấy info màn hình đang sử dụng
         GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
         GraphicsDevice gd = ge.getDefaultScreenDevice();
         gd.setFullScreenWindow(Main.window);

         screenWidth2 = Main.window.getWidth();
         screenHeight2 = Main.window.getHeight();
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
                drawToTempScreen();                 // Draw vào ảnh đệm
                drawToScreen();                     // Draw ảnh đệm lên màn hình
                delta--;                            // Giảm delta xuống 1 để chuẩn bị cho khung tiếp theo
            } 
        }
    }


    public void update(){
    	if(gameState == playState) {
    		player.update();// Player
    		// npc
    		for(int i = 0; i < npc[1].length; i++) {
    			if(npc[currentMap][i] != null) {
    				npc[currentMap][i].update();
    			}
    		}
    		// monster
    		for(int i = 0; i < monster.length; i++) {
    			if(monster[currentMap][i] != null) {
    				if(monster[currentMap][i].alive == true && monster[currentMap][i].dying == false){
    					monster[currentMap][i].update();	
    				}
    				if(monster[currentMap][i].alive == false){
                        monster[currentMap][i].checkDrop();
    					monster[currentMap][i] = null;	
    				}
    			}
    		}
            // projectile
            for (int i = 0; i < projectileList.size(); i++) {
                if (projectileList.get(i) != null) {
                    if (projectileList.get(i).alive == true) {
                        projectileList.get(i).update();
                    }
                    if (projectileList.get(i).alive == false) {
                        projectileList.remove(i);
                    }
                }
            }
            for (int i = 0; i < particleList.size(); i++) {
                if (particleList.get(i) != null) {
                    if (particleList.get(i).alive == true) {
                        particleList.get(i).update();
                    }
                    if (particleList.get(i).alive == false) {
                        particleList.remove(i);
                    }
                }
            }
            for(int i = 0; i < iTile[1].length; i++) {
                if(iTile[currentMap][i] != null) {
                    iTile[currentMap][i].update();
                }
            }
    	}
    	if(gameState == pauseState) {
    		//nothing
    	}  
    }
    
    public void drawToTempScreen() {
               
        // TITLE SCREEN
        if(gameState == titleState) {
        	ui.draw(g2);
        }
        else {
            tileM.draw(g2); // Vẽ bản đồ (tile map)

            // Interactive tile
            for(int i = 0; i < iTile[1].length; i++) {
                if(iTile[currentMap][i] != null) {
                    iTile [currentMap][i].draw(g2);
                }
            }

            // add entity to the list
            entityList.add(player);
            for(int i = 0; i < npc[1].length; i++) {
                if(npc[currentMap][i] != null) {
                    entityList.add(npc[currentMap][i]);
                }
            }
            for(int i = 0; i < obj[1].length; i++) {
                if(obj[currentMap][i] != null) {
                    entityList.add(obj[currentMap][i]);
                }
            }
            for(int i = 0; i < monster[1].length; i++) {
                if(monster[currentMap][i] != null) {
                    entityList.add(monster[currentMap][i]);
                }
            }
            for(int i = 0; i < projectileList.size(); i++) {
                if(projectileList.get(i) != null) {
                    entityList.add(projectileList.get(i));
                }
            }
            for(int i = 0; i < particleList.size(); i++) {
                if(particleList.get(i) != null) {
                    entityList.add(particleList.get(i));
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
    }
    
    public void drawToScreen() {
        Graphics g = getGraphics();
        g.drawImage(tempScreen, 0, 0, screenWidth2, screenHeight2, null);
        g.dispose();
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
