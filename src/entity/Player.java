package entity;

import main.KeyHandler;
import object.OBJ_Shield_Wood;
import object.OBJ_Sword_Normal;

import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import main.GamePanel;
import java.awt.image.BufferedImage;

public class Player extends Entity {
	
    KeyHandler keyH;
    public final int screenX;
    public final int screenY;
    public int hasKey = 0;
    public boolean attackCanceled = false;
    
    public Player(GamePanel gp, KeyHandler keyH){	
    	super(gp);
        this.keyH = keyH;
        screenX = gp.screenWidth / 2 - gp.tileSize/2;
        screenY = gp.screenHeight / 2 - gp.tileSize/2;
        // set solid area
        solidArea = new Rectangle();
        
        solidArea.x = 8;
        solidArea.y = 16;
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;
        solidArea.width = 26;
        solidArea.height = 26;
        
        attackArea.width = 36;
        attackArea.height= 36;
        
        setDefaultValues();
        getPlayerImage();
        getPlayerAttackImage();
    }

    public void setDefaultValues() {
        worldX = gp.tileSize * 23;
        worldY = gp.tileSize * 21;
        speed = 4;
        direction = "down";
        level = 1;
        strength = 1; // the more strength, the more attack power
        dexterity = 1; // the more dexterity, the more defense power
        maxLife = 20;
        life = maxLife;
        exp = 0;
        nextLevelExp = 5;
        coin = 0;
        currentWeapon = new OBJ_Sword_Normal(gp);
        currentShield = new OBJ_Shield_Wood(gp);
        attack = getAttack(); // calculate attack value
        defense = getDefense(); // calculate defense value  
    }
    public int getAttack() {
    	return attack = strength *currentWeapon.attackValue;
    }

    public int getDefense() {
    	return defense = dexterity * currentShield.defenseValue;
    }

    public void getPlayerImage() {
        up1 = setup("/player/boy_up_1", gp.tileSize, gp.tileSize);
        up2 = setup("/player/boy_up_2", gp.tileSize, gp.tileSize);
        down1 = setup("/player/boy_down_1", gp.tileSize, gp.tileSize);
        down2 = setup("/player/boy_down_2", gp.tileSize, gp.tileSize);
        left1 = setup("/player/boy_left_1", gp.tileSize, gp.tileSize);
        left2 = setup("/player/boy_left_2", gp.tileSize, gp.tileSize);
        right1 = setup("/player/boy_right_1", gp.tileSize, gp.tileSize);
        right2 = setup("/player/boy_right_2", gp.tileSize, gp.tileSize);
    }

    public void getPlayerAttackImage() {
        int w = gp.tileSize;
        int h = gp.tileSize;
        attackUp1 = setup("/player/boy_attack_up_1",  w, h * 2);
        attackUp2 = setup("/player/boy_attack_up_2",  w, h * 2);
        attackDown1 = setup("/player/boy_attack_down_1",  w, h * 2);
        attackDown2 = setup("/player/boy_attack_down_2",  w, h * 2);
        attackLeft1 = setup("/player/boy_attack_left_1",  w * 2, h);
        attackLeft2 = setup("/player/boy_attack_left_2",  w * 2, h);
        attackRight1 = setup("/player/boy_attack_right_1",  w * 2, h);
        attackRight2 = setup("/player/boy_attack_right_2",  w * 2, h);
    }

    public void update() {
    	if(attacking == true) {
    		attacking();
    	}
    	else if (keyH.upPressed || keyH.downPressed || keyH.leftPressed || keyH.rightPressed || keyH.enterPressed) {
            if (keyH.upPressed) {
                direction = "up";
            } else if (keyH.downPressed) {
                direction = "down";
            } else if (keyH.leftPressed) {
                direction = "left";
            } else if (keyH.rightPressed) {
                direction = "right";
            }
            // CHECK TILE COLLISION
            collisionOn = false;
            gp.cChecker.checkTile(this);

            //CHECK OBJ COLLISION
            int objIndex = gp.cChecker.checkObject(this, true);
            pickUpObject(objIndex); 

            // CHECK NPC COLLISION
            int npcIndex = gp.cChecker.checkEntity(this, gp.npc);
            interactNPC(npcIndex);  

            //CHECK MONSTER COLLISION
            int monsterIndex = gp.cChecker.checkEntity(this, gp.monster);
            contactMonster(monsterIndex);
            
            //CHECK EVENT
            gp.eHandler.checkEvent();
            gp.keyH.enterPressed = false;
            
            // IF COLLISION IS FALSE, PLAYER CAN MOVE
            if (collisionOn == false && keyH.enterPressed == false) {
                switch (direction) {
                    case "up": worldY -= speed; break;
                    case "down": worldY += speed; break;
                    case "left": worldX -= speed; break;
                    case "right": worldX += speed; break;
                }
            }

            if(keyH.enterPressed == true && attackCanceled == false) {
                attacking = true;
                spriteCounter = 0;
            }
            attackCanceled = false;

            gp.keyH.enterPressed = false;
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
        }
        
        // this needs to be...
        if(invincible == true) {
        	invincibleCounter++;
        	if(invincibleCounter > 60) {
        		invincible = false;
        		invincibleCounter = 0;	
        	}        	
        }
    }
    public void attacking() {
    	spriteCounter++;
    	if(spriteCounter <= 5) {
    	    spriteNum = 1;
    	}
    	if(spriteCounter > 5 && spriteCounter <= 25) {
    	    spriteNum = 2;
    	    int currentWorldX = worldX;
    	    int currentWorldY = worldY;
    	    int solidAreaWidth = solidArea.width;
    	    int solidAreaHeight = solidArea.height;
    	    
    	    // Adjust player's worldX/Y for the attackArea
    	    switch (direction) {
    	        case "up": worldY -= attackArea.height; break;
    	        case "down": worldY += attackArea.height; break;
    	        case "left": worldX -= attackArea.width; break;
    	        case "right": worldX += attackArea.width; break;
    	    }

    	    // attackArea becomes solidArea
    	    solidArea.width = attackArea.width;
    	    solidArea.height = attackArea.height;

    	    // Check monster collision with the updated worldX, worldY and solidArea
    	    int monsterIndex = gp.cChecker.checkEntity(this, gp.monster);
    	    damageMonster(monsterIndex);

    	    // After checking collision, resotre the original data
    	    worldX = currentWorldX;
    	    worldY = currentWorldY;
    	    solidArea.width = solidAreaWidth;
    	    solidArea.height = solidAreaHeight;
    	}
    	if(spriteCounter > 25) {
    	    spriteNum = 1;
    	    spriteCounter = 0;
    	    attacking = false;
    	}
        
    }

    public void pickUpObject(int i) {
        if (i != 999) {

        }
    }

    public void interactNPC(int i) {
    	if(gp.keyH.enterPressed == true){
    		if (i != 999) {      
                attackCanceled = true;  	
            	gp.gameState = gp.dialogueState;          
                gp.npc[i].speak();           	
    		}
    	}  
    }
    
    public void contactMonster(int i) {
    	if(i != 999) {
    		if(invincible == false){
    			gp.playSE(6);
    			life -= 1;
    			invincible = true;
    		}
    		
    	}
    }
    
    public void damageMonster(int i) {
    	if (i != 999) {
    	    if (gp.monster[i].invincible == false) {
    	    	gp.playSE(5);
    	        gp.monster[i].life -= 1;
    	        gp.monster[i].invincible = true;
    	        gp.monster[i].damageReaction();
    	        if (gp.monster[i].life <= 0) {
    	            gp.monster[i].dying = true;
    	        }
    	    }
    	}
        
    }
    

    public void draw(Graphics2D g2) {
        BufferedImage image = null;
        int tempScreenX = screenX;
        int tempScreenY = screenY;
        int drawWidth = gp.tileSize;
        int drawHeight = gp.tileSize;

        switch (direction) {
            case "up":
                if (attacking) {
                    tempScreenY = screenY - gp.tileSize; // vung kiếm lên trên
                    drawHeight = gp.tileSize * 2;
                    if (spriteNum == 1) image = attackUp1;
                    if (spriteNum == 2) image = attackUp2;
                } else {
                    if (spriteNum == 1) image = up1;
                    if (spriteNum == 2) image = up2;
                }
                break;

            case "down":
                if (attacking) {
                    drawHeight = gp.tileSize * 2;
                    if (spriteNum == 1) image = attackDown1;
                    if (spriteNum == 2) image = attackDown2;
                } else {
                    if (spriteNum == 1) image = down1;
                    if (spriteNum == 2) image = down2;
                }
                break;

            case "left":
                if (attacking) {
                    tempScreenX = screenX - gp.tileSize; // vung kiếm sang trái
                    drawWidth = gp.tileSize * 2;
                    if (spriteNum == 1) image = attackLeft1;
                    if (spriteNum == 2) image = attackLeft2;
                } else {
                    if (spriteNum == 1) image = left1;
                    if (spriteNum == 2) image = left2;
                }
                break;

            case "right":
                if (attacking) {
                    drawWidth = gp.tileSize * 2;
                    if (spriteNum == 1) image = attackRight1;
                    if (spriteNum == 2) image = attackRight2;
                } else {
                    if (spriteNum == 1) image = right1;
                    if (spriteNum == 2) image = right2;
                }
                break;
        }

        if (invincible) {
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.4f));
        }
        g2.drawImage(image, tempScreenX, tempScreenY, drawWidth, drawHeight, null);
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
    }
}
