package object;

import entity.Entity;
import main.GamePanel;

public class OBJ_Lantern extends Entity{

    public static final String objName = "Lantern";

    public OBJ_Lantern(GamePanel gp) {
        super(gp);
        
        type = type_light;
        name = objName;
        down1 = setup("/objects/lantern", gp.tileSize, gp.tileSize);
        description = "[Lantern]\nIlluminates your \nsurroundings.";
        price = 100;
        lightRadius = 250; // Set the light radius for the lantern
    }
}
