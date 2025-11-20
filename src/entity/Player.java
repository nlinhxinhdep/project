package entity;

import main.KeyHandler;
import object.OBJ_Fireball;
import object.OBJ_Key;
import object.OBJ_Rock;
import object.OBJ_Shield_Wood;
import object.OBJ_Sword_Normal;
import object.OBJ_Axe;
import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import main.GamePanel;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class Player extends Entity {
	
    KeyHandler keyH;
    public final int screenX;
    public final int screenY;
    public int standCounter = 0;
    public int hasKey = 0;
    public boolean attackCanceled = false;
    public ArrayList<Entity> inventory = new ArrayList<>();
    public final int maxInventorySize = 20;
    public boolean lightUpdated = false;
    
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
        
        // attackArea.width = 36;
        // attackArea.height= 36;
        
        setDefaultValues();
        getPlayerImage();
        getPlayerAttackImage();
        setItems();
    }

    public void setDefaultValues() {
        worldX = gp.tileSize * 23;
        worldY = gp.tileSize * 21;
        // worldX = gp.tileSize * 12;
        // worldY = gp.tileSize * 13;
        speed = 4;
        direction = "down";

        maxMana = 4;
        mana = maxMana;
        ammo = 10;
        level = 1;
        strength = 1; 
        dexterity = 1; 
        maxLife = 6;
        life = maxLife;
        exp = 0;
        nextLevelExp = 5;
        coin = 300;
//        currentWeapon = new OBJ_Sword_Normal(gp);
        currentWeapon = new OBJ_Axe(gp);
        currentShield = new OBJ_Shield_Wood(gp);
        projectile = new OBJ_Fireball(gp);  
        // projectile = new OBJ_Rock(gp);      
        attack = getAttack(); // calculate attack value
        defense = getDefense(); // calculate defense value  
    }

    public void setDefaultPositions() {
        worldX = gp.tileSize * 23;
        worldY = gp.tileSize * 21;
        direction = "down";
    }
    public void restoreLifeAndMan() {
        life = maxLife;
        mana = maxMana;
        invincible = false;
    }
    public void setItems() {
        inventory.clear();
        inventory.add(currentWeapon);
        inventory.add(currentShield);
        inventory.add(new OBJ_Key(gp));
    }
    public int getAttack() {
        attackArea = currentWeapon.attackArea;
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
        if(currentWeapon.type == type_sword){
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
        if (currentWeapon.type == type_axe) {
            attackUp1 = setup("/player/boy_axe_up_1", gp.tileSize, gp.tileSize*2);
            attackUp2 = setup("/player/boy_axe_up_2", gp.tileSize, gp.tileSize*2);
            attackDown1 = setup("/player/boy_axe_down_1", gp.tileSize, gp.tileSize*2);
            attackDown2 = setup("/player/boy_axe_down_2", gp.tileSize, gp.tileSize*2);
            attackLeft1 = setup("/player/boy_axe_left_1", gp.tileSize*2, gp.tileSize);
            attackLeft2 = setup("/player/boy_axe_left_2", gp.tileSize*2, gp.tileSize);
            attackRight1 = setup("/player/boy_axe_right_1", gp.tileSize*2, gp.tileSize);
            attackRight2 = setup("/player/boy_axe_right_2", gp.tileSize*2, gp.tileSize);
        }

        
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

            // CHECK INTERACTIVE TILE COLLISION
            int iTileIndex = gp.cChecker.checkEntity(this, gp.iTile);
            
            //CHECK EVENT teleport, trap, healing pool
            gp.eHandler.checkEvent();
            
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
                gp.playSE(7);
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
            else{
                standCounter++;
                if(standCounter == 20){
                    standCounter = 0;
                    spriteNum = 1;
                }
            }
        }

        if(gp.keyH.shotKeyPressed == true && projectile.alive == false 
        && shotAvailableCounter == 30 && projectile.haveResource(this) == true){
            projectile.set(worldX, worldY, direction, true, this);

            projectile.subtractResource(this);           
            gp.projectileList.add(projectile);

            shotAvailableCounter = 0;
            gp.keyH.shotKeyPressed = false;
            gp.playSE(10);
        }

        // INVINCIBILITY TIMER
        if(invincible == true) {
        	invincibleCounter++;
        	if(invincibleCounter > 60) {
        		invincible = false;
        		invincibleCounter = 0;	
        	}        	
        }
        if(shotAvailableCounter < 30){
            shotAvailableCounter++;
        }
        if(life > maxLife) {
            life = maxLife;
        }
        if(mana > maxMana) {
            mana = maxMana;
        }
        if(life <= 0){
            gp.gameState = gp.gameOverState;
            gp.ui.commandNum = -1;
            gp.stopMusic();
            gp.playSE(12);
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
    	    damageMonster(monsterIndex, attack);

            int iTileIndex = gp.cChecker.checkEntity(this, gp.iTile);
            damageInteractiveTile(iTileIndex);

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
            // Pickup only items
            if(gp.obj[gp.currentMap][i].type == type_pickupOnly) {
                gp.obj[gp.currentMap][i].use(this);
                gp.obj[gp.currentMap][i] = null;
            }
            // Inventory items
            else {
                String text;
                if (inventory.size() != maxInventorySize) {

                inventory.add(gp.obj[gp.currentMap][i]);
                gp.playSE(1);
                text = "Got a " + gp.obj[gp.currentMap][i].name + "!";
                }
                else {
                text = "You cannot carry any more!";
                }
                gp.ui.addMessage(text);
                gp.obj[gp.currentMap][i] = null;
            }
            
        }
    }

    public void interactNPC(int i) {
    	if(gp.keyH.enterPressed == true){
    		if (i != 999) {      
                attackCanceled = true;  	
            	gp.gameState = gp.dialogueState;          
                gp.npc[gp.currentMap][i].speak();           	
    		}      
    	}  
    }
    
    public void contactMonster(int i) {
    	if(i != 999) {
    		if(invincible == false && gp.monster[gp.currentMap][i].dying == false){
                int damage = gp.monster[gp.currentMap][i].attack - defense;
                if (damage < 0) {
                    damage = 0; // Prevent negative damage
                }
    			gp.playSE(6);
    			life -= damage;
    			invincible = true;
    		}
    		
    	}
    }
    
    public void damageMonster(int i, int attack) {
    	if (i != 999) {
    	    if (gp.monster[gp.currentMap][i].invincible == false) {

    	    	gp.playSE(5);

                int damage = attack - gp.monster[gp.currentMap][i].defense;
                if (damage < 0) {
                    damage = 0; // Prevent negative damage
                }
    	        gp.monster[gp.currentMap][i].life -= damage;
                gp.ui.addMessage(damage + " damage!");
    	        gp.monster[gp.currentMap][i].invincible = true;
    	        gp.monster[gp.currentMap][i].damageReaction();
    	        if (gp.monster[gp.currentMap][i].life <= 0) {
    	            gp.monster[gp.currentMap][i].dying = true;                
                    gp.ui.addMessage("killed the " + gp.monster[gp.currentMap][i].name + "!");
                    gp.ui.addMessage("Exp + " + gp.monster[gp.currentMap][i].exp);
                    exp += gp.monster[gp.currentMap][i].exp;
                    checkLevelup();
    	        }
    	    }
    	}
    }

    public void damageInteractiveTile(int i) {
        if(i != 999 && gp.iTile[gp.currentMap][i].destructible == true 
                && gp.iTile[gp.currentMap][i].isCorrrectItem(this) == true && gp.iTile[gp.currentMap][i].invincible == false) {

            gp.iTile[gp.currentMap][i].playSE();
            gp.iTile[gp.currentMap][i].life--;
            gp.iTile[gp.currentMap][i].invincible = true;

            generatorParticle((gp.iTile[gp.currentMap][i]), gp.iTile[gp.currentMap][i]);

            if(gp.iTile[gp.currentMap][i].life == 0) {
                gp.iTile[gp.currentMap][i] = gp.iTile[gp.currentMap][i].getDestroyedForm();
            }
        }
    }

    public void checkLevelup(){
        if(exp >= nextLevelExp) {
            level++;
            nextLevelExp = nextLevelExp*2;
            maxLife += 2;
            strength++;
            dexterity++;
            attack = getAttack();
            defense = getDefense();
            gp.playSE(8);
            gp.gameState = gp.dialogueState;
            gp.ui.currentDialogue = "You are level " + level + " now!\n";
        }
    }
    
    public void selectItem() {

        int itemIndex = gp.ui.getItemIndexOnSlot(gp.ui.playerSlotCol, gp.ui.playerSlotRow);

        if (itemIndex < inventory.size()) {
            
            Entity selectedItem = inventory.get(itemIndex);
            
            if (selectedItem.type == type_sword || selectedItem.type == type_axe) {
                currentWeapon = selectedItem;
                attack = getAttack();
                getPlayerAttackImage();
            }
            
            if (selectedItem.type == type_shield) {
                currentShield = selectedItem;
                defense = getDefense();
            }
            
            if (selectedItem.type == type_light) {
                if(currentLight == selectedItem) {
                    // Turn off the light
                    currentLight = null;
                } else {
                    // Equip the new light source
                    currentLight = selectedItem;
                }
                lightUpdated = true;
            }

            if (selectedItem.type == type_consumable) {
                selectedItem.use(this);
                inventory.remove(itemIndex);
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
                    tempScreenY = screenY - gp.tileSize; // vung kiáº¿m lÃªn trÃªn
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
                    tempScreenX = screenX - gp.tileSize; // vung kiáº¿m sang trÃ¡i
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
