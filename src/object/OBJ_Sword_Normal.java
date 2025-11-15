package object;

import entity.Entity;

public class OBJ_Sword_Normal extends Entity {
    public OBJ_Sword_Normal(main.GamePanel gp) {
        super(gp);
        name = "Normal Sword";
        down1 = setup("/object/sword_normal", gp.tileSize, gp.tileSize);
        attackValue = 2;
    }

}
