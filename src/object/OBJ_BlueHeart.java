package object;

import entity.Entity;
import main.GamePanel;

public class OBJ_BlueHeart extends Entity {
    
    GamePanel gp;
    public final static String objName = "Blue Heart";
    public OBJ_BlueHeart(GamePanel gp) {
        super(gp);
        this.gp = gp;
        
        type = type_pickupOnly;
        name = objName;
        down1 = setup("/objects/blueheart", gp.tileSize, gp.tileSize);

        setDialogues();
    }
    public void setDialogues() {
        dialogues[0][0] = "You obtained a \nBlue Heart!";
        dialogues[0][1] = "Your life has \nincreased by 1 point!";
    }
    public boolean use(Entity entity) {
        gp.gameState = gp.cutsceneState;
        gp.csManager.sceneNum = gp.csManager.ending;
        return true;
    }
}
