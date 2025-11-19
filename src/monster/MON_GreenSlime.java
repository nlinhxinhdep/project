package monster;

import java.util.Random;

import entity.Entity;
import main.GamePanel;
import object.OBJ_Coin_Bronze;
import object.OBJ_Heart;
import object.OBJ_ManaCrystal;
import object.OBJ_Rock;

public class MON_GreenSlime extends Entity{
	GamePanel gp;

	public MON_GreenSlime(GamePanel gp) {
		super(gp);
		// TODO Auto-generated constructor stub
		this.gp = gp;
		type = type_monster;
		//name = "Slime";
		//speed = 1;
		//maxLife = 4;
		attack = 5;
		defense = 0;
		exp = 2;
		projectile = new OBJ_Rock(gp);
		
		solidArea.x = 2;
		solidArea.y = 10;
		solidArea.width = 44;
		solidArea.height = 38;
		
		solidAreaDefaultX = solidArea.x; 
		solidAreaDefaultY = solidArea.y; 
		
		getImage();
		life = maxLife;
	}
	public void getImage() {
		//Random 3 types of slime
		Random random = new Random();
        int i = random.nextInt(3) + 1;
		switch(i) {
		case 1: //GREEN SLIME
			name = "Green Slime";
			speed = 1;
			maxLife = 4;
			up1 = down1 = left1 = right1 = setup("/monster/greenslime1", gp.tileSize, gp.tileSize);
			up2 = down2 = left2 = right2 = setup("/monster/greenslime2", gp.tileSize, gp.tileSize);
			break;
		case 2: //BLUE SLIME
			name = "Blue Slime";
			speed = 1;
			maxLife = 10;
			up1 = down1 = left1 = right1 = setup("/monster/blueslime1", gp.tileSize, gp.tileSize);
			up2 = down2 = left2 = right2 = setup("/monster/blueslime2", gp.tileSize, gp.tileSize);
			break;
		case 3: //RED SLIME
			name = "Red Slime";
			speed = 2;
			maxLife = 10;
			up1 = down1 = left1 = right1 = setup("/monster/redslime1", gp.tileSize, gp.tileSize);
			up2 = down2 = left2 = right2 = setup("/monster/redslime2", gp.tileSize, gp.tileSize);
			break;
		}
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
		int i = new Random().nextInt(100)+1;
		if (i > 99 && projectile.alive == false && shotAvailableCounter == 30) {
			
			projectile.set(worldX, worldY, direction, true, this);
			gp.projectileList.add(projectile);
			shotAvailableCounter = 0;
		} 
    	
	}
	public void damageReaction(){
		actionLockCounter = 0;
		direction = gp.player.direction;
    }
	public void checkDrop() {
		int i = new Random().nextInt(100)+1;

		if(i < 50) {
			dropItem(new OBJ_Coin_Bronze(gp));
		}
		if(i >= 50 && i < 75) {
			dropItem(new OBJ_Heart(gp));
		}
		if(i >= 75 && i < 100) {
			dropItem(new OBJ_ManaCrystal(gp));
		}
	}
}
