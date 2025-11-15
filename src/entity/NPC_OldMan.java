package entity;

import java.util.Random;

import main.GamePanel;

public class NPC_OldMan extends Entity{

    public NPC_OldMan(GamePanel gp) {
        super(gp);

        direction = "down";
        speed = 1;
        
        getImage();
        setDialogue();
    }
    public void getImage() {
        up1 = setup("/npc/oldman_up_1", gp.tileSize, gp.tileSize);
        up2 = setup("/npc/oldman_up_2", gp.tileSize, gp.tileSize);
        down1 = setup("/npc/oldman_down_1", gp.tileSize, gp.tileSize);
        down2 = setup("/npc/oldman_down_2", gp.tileSize, gp.tileSize);
        left1 = setup("/npc/oldman_left_1", gp.tileSize, gp.tileSize);
        left2 = setup("/npc/oldman_left_2", gp.tileSize, gp.tileSize);
        right1 = setup("/npc/oldman_right_1", gp.tileSize, gp.tileSize);
        right2 = setup("/npc/oldman_right_2", gp.tileSize, gp.tileSize);
    }
    
    public void setDialogue() {
        dialogues[0] = "Hello there!\nWelcome to our village.";
        dialogues[1] = "Be careful of monsters\nin the forest!";
        dialogues[2] = "Come back later,\nI may have more news.";
    }

    
    public void setAction() {
    	
    	actionLockCounter++;
    	if(actionLockCounter == 120) {
    		Random random = new Random();
            int i = random.nextInt(100) + 1; // pick up a number from 1 to 100

            if (i % 4 == 0) {
                direction = "up";
            }
            if (i % 4 == 1) {
                direction = "down";
            }
            if (i % 4 == 2) {
                direction = "left";
            }
            if (i % 4 == 3) {
                direction = "right";
            }
            actionLockCounter = 0;	
    	} 
    }
    public void speak() {
        // Đưa câu thoại hiện tại vào UI
        super.speak();
    }
    

}