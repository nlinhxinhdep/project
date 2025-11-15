package monster;

import java.util.Random;

import entity.Entity;
import main.GamePanel;

public class MON_GreenSlime extends Entity{
	GamePanel gp;

	public MON_GreenSlime(GamePanel gp) {
		super(gp);
		// TODO Auto-generated constructor stub
		this.gp = gp;
		type = 2;
		name = "Green Slime";
		speed = 2;
		maxLife = 6;
		life = maxLife;
		
		solidArea.x = 3;
		solidArea.y = 18;
		solidArea.width = 42;
		solidArea.height = 30;
		
		solidAreaDefaultX = solidArea.x; 
		solidAreaDefaultY = solidArea.y; 
		
		getImage();
	}
	public void getImage() {
	    up1 = setup("/monster/greenslime_down_1", gp.tileSize, gp.tileSize);
	    up2 = setup("/monster/greenslime_down_2", gp.tileSize, gp.tileSize);
	    down1 = setup("/monster/greenslime_down_1", gp.tileSize, gp.tileSize);
	    down2 = setup("/monster/greenslime_down_2", gp.tileSize, gp.tileSize);
	    left1 = setup("/monster/greenslime_down_1", gp.tileSize, gp.tileSize);
	    left2 = setup("/monster/greenslime_down_2", gp.tileSize, gp.tileSize);
	    right1 = setup("/monster/greenslime_down_1", gp.tileSize, gp.tileSize);
	    right2 = setup("/monster/greenslime_down_2", gp.tileSize, gp.tileSize);
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
	public void damageReaction(){
		actionLockCounter = 0;
		direction = gp.player.direction;
    	
    }

}
