package object;

import entity.Entity;
import main.GamePanel;

public class OBJ_Door extends Entity {

    GamePanel gp;

    public OBJ_Door(GamePanel gp) {
        super(gp);
        this.gp=gp;

        type = type_obstacle;
        name = "Door";
        down1 = setup("/objects/door", gp.tileSize, gp.tileSize);
        collision = true; // Cửa có va chạm để chặn người chơi khi chưa mở
        solidArea.x = 0;
        solidArea.y = 16;
        solidArea.width = 48;
        solidArea.height = 32;
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;
    }

    public void interact(Entity entity) {
        gp.gameState = gp.dialogueState;
        gp.ui.currentDialogue = "You need a key to open this.";
    }
}
