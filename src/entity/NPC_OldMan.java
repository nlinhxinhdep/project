package entity;
import java.util.Random;
import java.awt.Rectangle;
import main.GamePanel;
public class NPC_OldMan extends Entity{
    public NPC_OldMan(GamePanel gp) {
        super(gp);
        direction = "down";
        speed = 2;
        
        getImage();
        setDialogue();
        solidArea = new Rectangle();
        solidArea.x = 8;      // Cách lề trái 8px
        solidArea.y = 16;     // Cách lề trên 16px (thường chừa đầu cho thoáng)
        solidArea.width = 32; // Chiều rộng nhỏ hơn tile (48 - 8 - 8 = 32)
        solidArea.height = 32;// Chiều cao nhỏ hơn tile
        
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;
        collision = false;
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
        dialogues[0][0] = "Hello there!\nWelcome to our village.";
        dialogues[0][1] = "Be careful of monsters\nin the forest!";
        dialogues[0][2] = "Come back later,\nI may have more news.";
        dialogues[0][3] = "Well, good luck on you";

        dialogues[1][0] = "If you become tired, rest at the water.";
        dialogues[1][1] = "However, the monsters reappear if you rest.\nI don't know why but that's how it works.";
        dialogues[1][2] = "In any case, don't push yourself too hard.";

        dialogues[2][0] = "I wonder how to open that door...";
            }
    
    public void setAction() {
    	
        if (onPath == true) {

            // int goalCol=12;
            // int goalRow=9;
            int goalCol = (gp.player.worldX + gp.player.solidArea.x) / gp.tileSize;
            int goalRow = (gp.player.worldY + gp.player.solidArea.y) / gp.tileSize;

            searchPath(goalCol, goalRow);
        }
        else{
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
    }
    public void speak() {
        // Đưa câu thoại hiện tại vào UI
        facePlayer();
        startDialogue(this, dialogueSet);
        onPath = true;
    }
    

}