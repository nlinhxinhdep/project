package object;

public class OBJ_Shield_Wood extends entity.Entity {
    public OBJ_Shield_Wood(main.GamePanel gp) {
        super(gp);
        name = "Wooden Shield";
        down1 = setup("/object/shield_wood", gp.tileSize, gp.tileSize);
        defenseValue = 1;
    }


}
