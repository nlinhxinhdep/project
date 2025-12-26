package monster;

import java.util.Random;

import entity.Entity;
import main.GamePanel;
import object.OBJ_Coin_Bronze;
import object.OBJ_Fireball;
import object.OBJ_Heart;
import object.OBJ_ManaCrystal;
import object.OBJ_Rock;

public class MON_GreenSlime extends Entity{
	GamePanel gp;

	public MON_GreenSlime(GamePanel gp) {
        super(gp);
        this.gp = gp;

        type = type_monster;
        defaultSpeed = 1;
        speed = defaultSpeed;
        
        solidArea.x = 3;
        solidArea.y = 18;
        solidArea.width = 42;
        solidArea.height = 30;
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;

        // Mặc định ban đầu
        setSlimeType(1); 
        
        // Random ngay khi khởi tạo
        int i = new Random().nextInt(3) + 1;
        setSlimeType(i);
    }

	public void setSlimeType(int i) {
        switch(i) {
        case 1: 
            name = "Green Slime";
            maxLife = 4;
            attack = 2; // Slime xanh yếu
            exp = 2;     // Exp nhận được
            projectile = new OBJ_Rock(gp);
            break;
        case 2: 
            name = "Blue Slime";
            maxLife = 8;
            attack = 5; // Slime xanh dương mạnh hơn
            exp = 4;
            projectile = new OBJ_Rock(gp);
            break;
        case 3: 
            name = "Red Slime";
            maxLife = 12;
            attack = 8; // Slime đỏ mạnh nhất
            exp = 10;
            projectile = new OBJ_Fireball(gp); // Ví dụ: Slime đỏ bắn cầu lửa
            break;
        }
        
        life = maxLife; // Hồi đầy máu theo loại mới
        getImage(i);    // Load ảnh tương ứng
    }

	public void getImage(int i) {
        switch(i) {
        case 1: // GREEN
            up1 = down1 = left1 = right1 = setup("/monster/greenslime1", gp.tileSize, gp.tileSize);
            up2 = down2 = left2 = right2 = setup("/monster/greenslime2", gp.tileSize, gp.tileSize);
            break;
        case 2: // BLUE
            up1 = down1 = left1 = right1 = setup("/monster/blueslime1", gp.tileSize, gp.tileSize);
            up2 = down2 = left2 = right2 = setup("/monster/blueslime2", gp.tileSize, gp.tileSize);
            break;
        case 3: // RED
            up1 = down1 = left1 = right1 = setup("/monster/redslime1", gp.tileSize, gp.tileSize);
            up2 = down2 = left2 = right2 = setup("/monster/redslime2", gp.tileSize, gp.tileSize);
            break;
        }
    }
		
	public void setAction() {

		if (onPath == true) {
			// Check if it stop chasing 
			checkStopChasingOrNot(gp.player,15,100);

			// Search the direction to go 
            searchPath(getGoalCol(gp.player), getGoalRow(gp.player));

			// Check if it shoot the projectile 
			checkShootOrNot(200,30);
        }
		else {
			// Check if it start chasing 
			checkStartChasingOrNot(gp.player,5,100);

			// Get a random direction
			getRandomDirection(120);
		}
	}
	public void damageReaction(){
		actionLockCounter = 0;
		// direction = gp.player.direction;
		onPath = true;
    }
	public void checkDrop() {
        // Tỷ lệ rơi đồ tùy theo loại Slime (Optional)
        int i = new Random().nextInt(100) + 1;
        if(i < 50) dropItem(new OBJ_Coin_Bronze(gp));
        if(i >= 50 && i < 75) dropItem(new OBJ_Heart(gp));
        if(i >= 75 && i < 100) dropItem(new OBJ_ManaCrystal(gp));
    }
}

