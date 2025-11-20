package entity;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import main.GamePanel;
import main.UtilityTool;

public class Entity {
	GamePanel gp;
	public BufferedImage up1, up2, down1, down2, left1, left2, right1, right2;
	public BufferedImage attackUp1, attackUp2, attackDown1, attackDown2,
	        attackLeft1, attackLeft2, attackRight1, attackRight2;
	public BufferedImage image, image2, image3;
	public Rectangle solidArea = new Rectangle(0, 0, 48, 48);
	public Rectangle attackArea = new Rectangle(0 ,0, 0, 0);
	public int solidAreaDefaultX, solidAreaDefaultY;
	public boolean collision = false;
	String dialogues[] = new String[20];

	// STATE
	public int worldX, worldY;
	public String direction = "down";
	public int spriteNum = 1;
	int dialogueIndex = 0;
	public boolean collisionOn = false;
	public boolean invincible = false;
	boolean attacking = false;
	public boolean alive = true;
	public boolean dying = false;
	public boolean hpBarOn = false;
    public boolean onPath = false;
	
	// COUNTER
	public int spriteCounter = 0;
	public int actionLockCounter = 0;
	public int invincibleCounter = 0;
    public int shotAvailableCounter = 0;
	int dyingCounter = 0;
	int hpBarCounter = 0;

	// CHARACTER ATTRIBUTES
	public String name;
	public int speed;
	public int maxLife;
	public int life;
    public int maxMana;
    public int mana;
    public int ammo;
    public int level;
    public int strength;
    public int dexterity;
    public int attack;
    public int defense;
    public int exp;
    public int nextLevelExp;
    public int coin;
    public Entity currentWeapon;
    public Entity currentShield;
    public Entity currentLight;
    public Projectile projectile;

    //Item Attributes
    public ArrayList<Entity> inventory = new ArrayList<>();
    public final int maxInventorySize = 20;
    public int value;
    public int attackValue;
    public int defenseValue;
    public String description = "";
    public int useCost;
    public int price;
    
    public int lightRadius;

    // TYPE
    public int type; // 0 = player, 1 = npc, 2 = monster
    public final int type_player = 0;
    public final int type_npc = 1;
    public final int type_monster = 2;
    public final int type_sword = 3;
    public final int type_axe = 4;
    public final int type_shield = 5;
    public final int type_consumable = 6;
    public final int type_pickupOnly = 7;
    public final int type_light = 9;
    
    public Entity(GamePanel gp) {
    	this.gp = gp;
    }
    
    public void setAction() {}
    
    public void damageReaction(){
    	
    }
    public void speak() {
    	gp.ui.currentDialogue = dialogues[dialogueIndex];
        dialogueIndex++;
        // Nếu hết câu thoại, quay lại đầu
        if (dialogueIndex >= dialogues.length || dialogues[dialogueIndex] == null) {
            dialogueIndex = 0;
        }
        switch (gp.player.direction) {
        case "up":direction = "down"; // NPC nhìn xuống player
            break;
        case "down":direction = "up";   // NPC nhìn lên player
            break;
        case "left":direction = "right"; // NPC nhìn sang phải
            break;
        case "right":direction = "left";  // NPC nhìn sang trái
            break;
        }
    }

    public void use(Entity entity){}
    public void checkDrop() {}

    public void dropItem(Entity droppedItem) {
        for(int i = 0; i < gp.obj[1].length; i++) {
            if(gp.obj[gp.currentMap][i] == null) {
                gp.obj[gp.currentMap][i] = droppedItem;
                gp.obj[gp.currentMap][i].worldX = worldX; // droppedItem rơi tại vị trí monster
                gp.obj[gp.currentMap][i].worldY = worldY;
                break;
            }
        }
    }

    public Color getParticleColor() {
        Color color = null;
        return color;
    }
    public int getParticleSize() {
        int size = 0;
        return size;
    }
    public int getParticleSpeed() {
        int speed = 0;
        return speed;
    }
    public int getParticleMaxLife() {
        int maxLife = 0;
        return maxLife;
    }
    public void generatorParticle(Entity generator, Entity target) {
        Color color = generator.getParticleColor();
        int size = generator.getParticleSize();
        int speed = generator.getParticleSpeed();
        int maxLife = generator.getParticleMaxLife();

        Particle p1 = new Particle(gp, target, color, size, speed, maxLife, -2, -1);
        Particle p2 = new Particle(gp, target, color, size, speed, maxLife, 2, -1);
        Particle p3 = new Particle(gp, target, color, size, speed, maxLife, -2, 1);
        Particle p4 = new Particle(gp, target, color, size, speed, maxLife, 2, 1);
        gp.particleList.add(p1);
        gp.particleList.add(p2);
        gp.particleList.add(p3);
        gp.particleList.add(p4);
    }

    public void checkCollision(){
        collisionOn = false;
        gp.cChecker.checkTile(this);
        gp.cChecker.checkObject(this, false);
        gp.cChecker.checkEntity(this, gp.npc);
        gp.cChecker.checkEntity(this, gp.monster);
        gp.cChecker.checkEntity(this, gp.iTile);
        boolean contactPlayer = gp.cChecker.checkPlayer(this);
        
        if(this.type == type_monster && contactPlayer == true) {
            damagePlayer(attack);
        	
        }
    }

    public void update() {
        setAction();
        checkCollision();

        // IF COLLISION IS FALSE, ENTITY CAN MOVE
        if (collisionOn == false) { 
        	switch (direction) {
                case "up": worldY -= speed; break;
                case "down": worldY += speed; break;
                case "left": worldX -= speed; break;
                case "right": worldX += speed; break;
            }
        }
        // ANIMATION
        spriteCounter++;
        if (spriteCounter > 12) {
            if (spriteNum == 1) {
                spriteNum = 2;
            } else if (spriteNum == 2) {
                spriteNum = 1;
            }
            spriteCounter = 0;
        }

        if (invincible == true) {
            invincibleCounter++;
            if (invincibleCounter > 40) {
                invincible = false;
                invincibleCounter = 0;
            }
        }
        if(shotAvailableCounter < 30){
            shotAvailableCounter++;
        }
    }

    public void damagePlayer(int attack){
        if(gp.player.invincible == false) {
            gp.playSE(6);

            int damage = attack - gp.player.defense;
            if(damage < 0) {
                damage = 0; // Prevent negative damage
            }
            gp.player.life -= damage;
            gp.player.invincible = true;
        }
    }
    
    
    public void draw(Graphics2D g2) {
    	BufferedImage image = null;
        int screenX = worldX - gp.player.worldX + gp.player.screenX;
        int screenY = worldY - gp.player.worldY + gp.player.screenY;
        if (worldX + gp.tileSize > gp.player.worldX - gp.player.screenX &&
            worldX - gp.tileSize < gp.player.worldX + gp.player.screenX &&
            worldY + gp.tileSize > gp.player.worldY - gp.player.screenY &&
            worldY - gp.tileSize < gp.player.worldY + gp.player.screenY) {
        	switch (direction) {
            case "up":
                if (spriteNum == 1) {image = up1;}
                if (spriteNum == 2) {image = up2;}
                break;
            case "down":
                if (spriteNum == 1) {image = down1;}
                if (spriteNum == 2) {image = down2;}
                break;
            case "left":
                if (spriteNum == 1) {image = left1;}
                if (spriteNum == 2) {image = left2;}
                break;
            case "right":
                if (spriteNum == 1) {image = right1;}
                if (spriteNum == 2) {image = right2;}
                break;
        	}
        	// Monster HP bar
        	if (type == 2 && hpBarOn == true) {
        	    double oneScale = (double)gp.tileSize/maxLife;
        	    double hpBarValue = oneScale*life;

        	    g2.setColor(new Color(35, 35, 35));
        	    g2.fillRect(screenX-1, screenY-16, gp.tileSize+2, 12);

        	    g2.setColor(new Color(255, 0, 30));
        	    g2.fillRect(screenX, screenY - 15, (int)hpBarValue, 10);
        	    
        	    hpBarCounter++;
        	    if(hpBarCounter > 600) {
        	    	hpBarCounter = 0;
        	    	hpBarOn = false;
        	    }
        	}
        	if (invincible == true) {
        		hpBarOn = true;
        		hpBarCounter = 0;
        		changeAlpha(g2, 0.4f);
            }
        	if(dying == true) {
        		dyingAnimation(g2);
        	}
	        g2.drawImage(image, screenX, screenY, null);
	        changeAlpha(g2, 1f);
        }
    }
    
    public void dyingAnimation(Graphics2D g2) {
    	dyingCounter++;
    	int i = 10;
    	if (dyingCounter <= i) {changeAlpha(g2, 0f);}
    	if (dyingCounter > i && dyingCounter <= i * 2) {changeAlpha(g2, 1f);}
    	if (dyingCounter > i * 2 && dyingCounter <= i * 3) {changeAlpha(g2, 0f);}
    	if (dyingCounter > i * 3 && dyingCounter <= i * 4) {changeAlpha(g2, 1f);}
    	if (dyingCounter > i * 4 && dyingCounter <= i * 5) {changeAlpha(g2, 0f);}
    	if (dyingCounter > i * 5 && dyingCounter <= i * 6) {changeAlpha(g2, 1f);}
    	if (dyingCounter > i * 6 && dyingCounter <= i * 7) {changeAlpha(g2, 0f);}
    	if (dyingCounter > i * 7 && dyingCounter <= i * 8) {changeAlpha(g2, 1f);}
    	if (dyingCounter > i * 8) {
    	    alive = false;
    	}
    }
    public void changeAlpha(Graphics2D g2, float alphaValue){
    	g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alphaValue));
    }

    public BufferedImage setup(String imagePath, int width, int height) {
        UtilityTool uTool = new UtilityTool();
        BufferedImage image = null;
        try {
            image = ImageIO.read(getClass().getResourceAsStream(imagePath + ".png"));
            image = uTool.scaleImage(image, width, height);   
        } catch (IOException e) {
            e.printStackTrace();
        }
        return image;
    }

    public void searchPath(int goalCol, int goalRow) {

        int startCol = (worldX + solidArea.x) / gp.tileSize;
        int startRow = (worldY + solidArea.y) / gp.tileSize;

        gp.pFinder.setNodes(startCol, startRow, goalCol, goalRow);

        // 1. CHỈ TÌM ĐƯỜNG NẾU TÌM THẤY (Tránh lỗi null path)
        if (gp.pFinder.search() == true) {

            // Lấy bước đi tiếp theo trong danh sách
            // A* thường trả về pathList[0] là đích đến ngay cạnh nút start
            if(gp.pFinder.pathList.size() > 0) {
                
                int nextX = gp.pFinder.pathList.get(0).col * gp.tileSize;
                int nextY = gp.pFinder.pathList.get(0).row * gp.tileSize;

                // Lấy tọa độ TRUNG TÂM của Entity
                int enLeftX = worldX + solidArea.x;
                int enRightX = worldX + solidArea.x + solidArea.width;
                int enTopY = worldY + solidArea.y;
                int enBottomY = worldY + solidArea.y + solidArea.height;
                
                // Tọa độ trung tâm thực tế của Entity (quan trọng để di chuyển mượt)
                int enMidX = enLeftX + (solidArea.width / 2);
                int enMidY = enTopY + (solidArea.height / 2);
                
                // Tọa độ trung tâm của ô Next
                int nextMidX = nextX + (gp.tileSize / 2);
                int nextMidY = nextY + (gp.tileSize / 2);

                // --- LOGIC DI CHUYỂN ĐƠN GIẢN HÓA ---
                
                // Ưu tiên di chuyển theo trục nào xa hơn để trông tự nhiên
                // Hoặc đơn giản là kiểm tra vị trí tương đối
                
                if (enTopY > nextY && enLeftX >= nextX && enRightX < nextX + gp.tileSize) {
                    // Trường hợp 1: Chỉ cần đi lên
                    direction = "up";
                }
                else if (enTopY < nextY && enLeftX >= nextX && enRightX < nextX + gp.tileSize) {
                    // Trường hợp 2: Chỉ cần đi xuống
                    direction = "down";
                }
                else if (enTopY >= nextY && enBottomY < nextY + gp.tileSize) {
                    // Trường hợp 3: Chỉ cần đi ngang (Trái/Phải)
                    if (enLeftX > nextX) direction = "left";
                    if (enLeftX < nextX) direction = "right";
                }
                else {
                    // Trường hợp 4: Cần đi chéo hoặc tìm đường né vật cản
                    // Logic: So sánh trung tâm để quyết định hướng chính
                    // Nếu khoảng cách dọc lớn hơn ngang -> ưu tiên đi dọc
                    
                    if(enTopY > nextY && enLeftX > nextX) { // Cần đi Up hoặc Left
                        direction = "up";
                        checkCollision();
                        if(collisionOn) direction = "left";
                    }
                    else if(enTopY > nextY && enLeftX < nextX) { // Cần đi Up hoặc Right
                        direction = "up";
                        checkCollision();
                        if(collisionOn) direction = "right";
                    }
                    else if(enTopY < nextY && enLeftX > nextX) { // Cần đi Down hoặc Left
                        direction = "down";
                        checkCollision();
                        if(collisionOn) direction = "left";
                    }
                    else if(enTopY < nextY && enLeftX < nextX) { // Cần đi Down hoặc Right
                        direction = "down";
                        checkCollision();
                        if(collisionOn) direction = "right";
                    }
                }
                
                // Điều kiện dừng: Khi đã đến rất gần đích cuối cùng
                // int nextCol = gp.pFinder.pathList.get(0).col;
                // int nextRow = gp.pFinder.pathList.get(0).row;
                
                // if (nextCol == goalCol && nextRow == goalRow) {
                //     // Bạn có thể muốn check kỹ hơn (vd: khoảng cách < 10 pixel) thì mới dừng
                //     onPath = false; 
                // }
            }
        }
    }
}

