package object;

public class OBJ_Shield_Wood extends entity.Entity {
    public OBJ_Shield_Wood(main.GamePanel gp) {
        super(gp);
        type = type_shield;
        name = "Wooden Shield";
        down1 = setup("/objects/shield_wood", gp.tileSize, gp.tileSize);
        defenseValue = 1;
        description = "[" + name + "]\nMade by wood";
        price = 100;
    }


}
