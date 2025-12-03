package tile_interactive;

import entity.Entity;
import main.GamePanel;
import object.OBJ_Coin_Bronze;
import object.OBJ_Heart;
import object.OBJ_ManaCrystal;

import java.awt.Color;
import java.util.Random;

public class IT_DestructibleWall extends InteractiveTile{

    GamePanel gp;

    public IT_DestructibleWall(GamePanel gp, int col, int row) {
        super(gp, col, row);
        this.gp =gp;

        this.worldX = gp.tileSize * col;
        this.worldY = gp.tileSize * row;

        down1 = setup("/tiles_interactive/destructiblewall", gp.tileSize, gp.tileSize);
        destructible = true;
        life = 3;
    }
    public boolean isCorrrectItem(Entity entity) {
        boolean isCorrrectItem = false;

        if(entity.currentWeapon.type == type_pickaxe) {
            isCorrrectItem = true;
        }

        return isCorrrectItem;
    }
    public void playSE() {
        gp.playSE(19);
    }

    public InteractiveTile getDestroyedForm() {
        InteractiveTile tile = null;
        return tile;
    }
    public Color getParticleColor() {
        Color color = new Color(65, 65, 65);
        return color;
    }
    public int getParticleSize() {
        int size = 6;   //  6 pixels
        return size;
    }
    public int getParticleSpeed() {
        int speed = 1;
        return speed;
    }
    public int getParticleMaxLife() {
        int maxLife = 20;
        return maxLife;
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
