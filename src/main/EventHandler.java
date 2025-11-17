package main;

import entity.Entity;
public class EventHandler {

    GamePanel gp;
    EventRect[][] eventRect;
    int previousEventX, previousEventY;
    boolean canTouchEvent = true;
//    Entity eventMaster; // thêm để quản lý người kích hoạt (player)

    public EventHandler(GamePanel gp) {
        this.gp = gp;
        eventRect = new EventRect[gp.maxWorldCol][gp.maxWorldRow];

        int col = 0;
        int row = 0;
        while (col < gp.maxWorldCol && row < gp.maxWorldRow) {
            eventRect[col][row] = new EventRect();
            eventRect[col][row].x = 23;
            eventRect[col][row].y = 23;
            eventRect[col][row].width = 2;
            eventRect[col][row].height = 2;
            eventRect[col][row].eventRectDefaultX = eventRect[col][row].x;
            eventRect[col][row].eventRectDefaultY = eventRect[col][row].y;
            col++;
            if (col == gp.maxWorldCol) {
                col = 0;
                row++;
            }
        }
    }

    public void checkEvent() {
        // khoảng cách giữa lần kích hoạt trước và hiện tại
        int xDistance = Math.abs(gp.player.worldX - previousEventX);
        int yDistance = Math.abs(gp.player.worldY - previousEventY);
        int distance = Math.max(xDistance, yDistance);

        if (distance > gp.tileSize) {
            canTouchEvent = true;
        }

        if (canTouchEvent == true) {
            // DAMAGE PIT
            if (hit(27, 16, "right") == true) damagePit(27, 16, gp.dialogueState);
            // HEALING POOL
            if (hit(23, 12, "up") == true) healingPool(23, 12, gp.dialogueState);
            // TELEPORT
            if (hit(20, 20, "any") == true) teleport(10, 39, gp.dialogueState);
        }
    }

    public boolean hit(int col, int row, String reqDirection) {
        boolean hit = false;

        // chỉnh vị trí thật trong thế giới
        gp.player.solidArea.x = gp.player.worldX + gp.player.solidArea.x;
        gp.player.solidArea.y = gp.player.worldY + gp.player.solidArea.y;

        eventRect[col][row].x = col * gp.tileSize + eventRect[col][row].x;
        eventRect[col][row].y = row * gp.tileSize + eventRect[col][row].y;

        if (gp.player.solidArea.intersects(eventRect[col][row]) && eventRect[col][row].eventDone == false) {
            if (gp.player.direction.equals(reqDirection) || reqDirection.equals("any")) {
                hit = true;
                previousEventX = gp.player.worldX;
                previousEventY = gp.player.worldY;
            }
        }

        // Reset lại vị trí gốc
        gp.player.solidArea.x = gp.player.solidAreaDefaultX;
        gp.player.solidArea.y = gp.player.solidAreaDefaultY;
        eventRect[col][row].x = eventRect[col][row].eventRectDefaultX;
        eventRect[col][row].y = eventRect[col][row].eventRectDefaultY;

        return hit;
    }

    // --- SỰ KIỆN ---

    public void damagePit(int col, int row, int gameState) {
        gp.gameState = gameState;
        gp.playSE(6);
        gp.ui.currentDialogue = "You fell into a pit!";
        gp.player.life -= 1;
        canTouchEvent = false;
    }

    public void healingPool(int col, int row, int gameState) {
        if (gp.keyH.enterPressed == true) {
            gp.gameState = gameState;
            gp.player.attackCanceled = true;
            gp.playSE(2);
            gp.ui.currentDialogue = "You drink the water.\nYour life and mana has been recovered!";
            gp.player.life = gp.player.maxLife;
            gp.player.mana = gp.player.maxMana;
            gp.aSetter.setMonster();
        }
    }

    public void teleport(int col, int row, int gameState) {
        gp.gameState = gameState;
        gp.ui.currentDialogue = "Teleport!";
        gp.player.worldX = gp.tileSize * 12;
        gp.player.worldY = gp.tileSize * 13;
        canTouchEvent = false;
    }
}
