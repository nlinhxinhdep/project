package object;

import entity.Entity;

public class OBJ_Axe extends Entity {

    public OBJ_Axe(main.GamePanel gp) {
        super(gp);
        type = type_axe;
        name = "Woodcutter's Axe";
        down1 = setup("/objects/axe", gp.tileSize, gp.tileSize);
        attackValue = 3;
        attackArea.width = 30;
        attackArea.height = 30;
        description = "[" + name + "]\nA bit rusty but still \ncan cut some trees.";
    }
}
