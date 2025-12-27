package main;
import ai.PathFinder;
import data.SaveLoad;
import entity.Entity;
import entity.Player;
import environment.EnvironmentManager;
import tile.Map;
import tile.TileManager;
import tile_interactive.InteractiveTile;

import javax.swing.JPanel;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class GamePanel extends JPanel implements Runnable {

    // SCREEN SETTINGSX
    final int originalTileSize = 16; // 16x16 tile
    final int scale = 3;

    public final int tileSize = originalTileSize * scale; // 48x48 tile
    public final int maxScreenCol = 20;
    public final int maxScreenRow = 12;
    public final int screenWidth = tileSize * maxScreenCol;  // 960 pixels
    public final int screenHeight = tileSize * maxScreenRow; // 576 pixels
    // World settings
    public int maxWorldCol;
    public int maxWorldRow;
    public final int maxMap = 20;
    public int currentMap = 0;
    // Full screen
    int screenWidth2 = screenWidth;
    int screenHeight2 = screenHeight;
    BufferedImage tempScreen;
    Graphics2D g2;
    public boolean fullScreenOn = false;
    public boolean resetTimer = false; //reset frame khi chuyển cảnh
    

    // FPS
    int FPS = 60;
    
    // System
    public TileManager tileM = new TileManager(this);
    public KeyHandler keyH = new KeyHandler(this);
    Sound music = new Sound();
    Sound se = new Sound();
    public CollisionChecker cChecker = new CollisionChecker(this); 
    public AssetSetter aSetter = new AssetSetter(this);
    public EnvironmentManager eManager = new EnvironmentManager(this);
    public UI ui = new UI(this);
    public EventHandler eHandler = new EventHandler(this);
    Config config = new Config(this);
    public PathFinder pFinder = new PathFinder(this);
    Map map = new Map(this) ;
    SaveLoad saveLoad = new SaveLoad(this);
    public EntityGenerator eGenerator = new EntityGenerator(this);
    public CutsceneManager csManager = new CutsceneManager(this);
    Thread gameThread;
    
    // ENTITY AND OBJECT
    public Player player = new Player(this, keyH);
    public Entity obj[][] = new Entity[maxMap][20];
    public Entity npc[][] = new Entity[maxMap][20];
    public Entity monster[][] = new Entity[maxMap][20];
    public InteractiveTile iTile[][] = new InteractiveTile[maxMap][50];
    public Entity projectile[][] = new Entity[maxMap][20];
    // public ArrayList<Entity> projectileList = new ArrayList<>();
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
    public final int transitionState = 7;
    public final int tradeState = 8;
    public final int sleepState = 9;
    public final int mapState = 10;
    public final int cutsceneState = 11;
    public Object projectileList; 

    // OTHERS
    public boolean bossBattleOn = false;

    // AREA
    public int currentArea;
    public int nextArea;
    public final int outside = 50;
    public final int indoor = 51;
    public final int dungeon = 52;

    // MUSIC
    int currentMusicId = -1;


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

        eManager.setup();
        gameState = titleState;
        currentArea = outside;

        playMusic(23); // Bật nhạc Menu (BIYTheme.mp3) ngay khi vào game

        tempScreen = new BufferedImage(screenWidth, screenHeight, BufferedImage.TYPE_INT_ARGB);
        g2 = (Graphics2D)tempScreen.getGraphics();

        if(fullScreenOn == true){
            setFullScreen();
        }
    }

    public void resetGame (boolean restart) {
        stopMusic();
        currentArea = outside;
        removeTempEnity();
        bossBattleOn = false;
        player.setDefaultPositions();
        player.restoreStatus();
        player.resetCounter();
        aSetter.setNPC();
        aSetter.setMonster();

        if (restart == true) {
            player.setDefaultValues();
            aSetter.setObject();
            aSetter.setInteractiveTile();
            eManager.lighting.resetDay();
        }
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
            if (resetTimer == true) {
                lastTime = currentTime;                 // Đặt lại mốc thời gian bằng hiện tại
                delta = 0;                              // Xóa hết "nợ" khung hình cũ
                resetTimer = false;                     // Tắt cờ đi
            }
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
    		for(int i = 0; i < monster[1].length; i++) {
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
            for (int i = 0; i < projectile[1].length; i++) {
                if (projectile[currentMap][i] != null) {
                    if (projectile[currentMap][i].alive == true) {
                        projectile[currentMap][i].update();
                    }
                    if (projectile[currentMap][i].alive == false) {
                        projectile[currentMap][i] = null;
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
            eManager.update();
    	}
    	if(gameState == pauseState) {
    		//nothing
    	}  
    }
    
    public void drawToTempScreen() {
               
        long drawStart = 0;
        if(keyH.showDebugText == true)
        {
            drawStart = System.nanoTime();
        }
        // TITLE SCREEN
        if(gameState == titleState) {
        	ui.draw(g2);
        }

        // MAP SCREEN 
        else if(gameState == mapState){
            map.drawFullMapScreen(g2);
        }
        // OTHERS
        else {
            tileM.draw(g2); // Vẽ bản đồ (tile map)

            // Interactive tile
            for(int i = 0; i < iTile[1].length; i++) {
                if(iTile[currentMap][i] != null) {
                    iTile[currentMap][i].draw(g2);
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
            for(int i = 0; i < projectile[1].length; i++) {
                if(projectile[currentMap][i] != null) {
                    entityList.add(projectile[currentMap][i]);
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

            //ENVIRONMENT
            eManager.draw(g2);

            // MINI MAP
            map.drawMiniMap(g2);

            // CUTSCENE
            csManager.draw(g2);

            //UI
            ui.draw(g2);
            
            //DEBUG

            if(keyH.showDebugText == true)
            {
                long drawEnd = System.nanoTime();
                long passed = drawEnd - drawStart;

                g2.setFont(new Font("Arial", Font.PLAIN,20));
                g2.setColor(Color.white);
                int x = 10;
                int y = 400;
                int lineHeight = 20;

                g2.drawString("WorldX " + player.worldX,x,y);
                y+= lineHeight;
                g2.drawString("WorldY " + player.worldY,x,y);
                y+= lineHeight;
                g2.drawString("Col " + (player.worldX + player.solidArea.x) / tileSize,x,y);
                y+= lineHeight;
                g2.drawString("Row " + (player.worldY + player.solidArea.y) / tileSize,x,y);
                y+= lineHeight;
                g2.drawString("Map " + currentMap,x,y);
                y+= lineHeight;
                g2.drawString("Draw time: " + passed,x,y);
                y+= lineHeight;
                g2.drawString("God Mode: " + keyH.godModeOn, x, y);

            }
        }
    }
    
    public void drawToScreen() {
        Graphics g = getGraphics();
        g.drawImage(tempScreen, 0, 0, screenWidth2, screenHeight2, null);
        g.dispose();
    }
    public void playMusic(int i) {
    	// 1. Cập nhật bài nhạc mà game "muốn" phát hiện tại
        currentMusicId = i;
        
        // 2. Tắt ngay bài nhạc đang phát (nếu có)
        music.stop(); 

        // 3. Chạy luồng tải nhạc (để giảm lag)
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    // Tải file (nặng)
                    music.setFile(i); 
                    
                    // --- ĐOẠN QUAN TRỌNG NHẤT ---
                    // Khi tải xong, kiểm tra xem ID này còn khớp với mong muốn hiện tại không?
                    // Nếu bạn bấm New Game nhanh quá, currentMusicId đã bị đổi thành bài khác (ví dụ 0)
                    // nên bài nhạc Menu (23) sẽ không được phép chạy lệnh play().
                    if (currentMusicId == i) {
                        music.play();
                        music.loop();
                    }
                    // -----------------------------
                    
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
    public void stopMusic() {
    	music.stop();
    }
    public void playSE(int i) {
    	new Thread(new Runnable() {
            @Override
            public void run() {
                se.setFile(i);
                se.play();
            }
        }).start();
    }
    public void changeArea() {

        if (nextArea != currentArea) {

            stopMusic();

            if (nextArea == outside) {
                playMusic(0);
            }
            if (nextArea == indoor) {
                playMusic(18);
            }
            if (nextArea == dungeon) {
                playMusic(19);
            }

            aSetter.setNPC();
        }

        currentArea = nextArea;
        aSetter.setMonster();// making monsters respawn

        resetTimer = true; // Báo hiệu cho GameLoop reset lại thời gian
    }

    public void removeTempEnity(){

        for(int mapNum = 0; mapNum < maxMap; mapNum++){
            for (int i = 0; i < obj[1].length; i++) {
                if(obj[mapNum][i] != null && obj[mapNum][i].temp == true) {
                    obj[mapNum][i] = null;
                }
            }
        }
    }
}
