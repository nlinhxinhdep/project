package main;

import entity.NPC_OldMan;
import entity.NPC_BigRock;
import entity.NPC_Merchant;
import monster.MON_Bat;
import monster.MON_GreenSlime;
import monster.MON_Orc;
import monster.MON_SkeletonLord;
import object.*;
import tile_interactive.IT_DestructibleWall;
import tile_interactive.IT_DryTree;
import tile_interactive.IT_MetalPlate;
import data.Progress;

public class AssetSetter {

    GamePanel gp;
    public AssetSetter(GamePanel gp) {
        this.gp = gp;
    }

    public void setObject() {
        int mapNum = 0;
        int i = 0;

        // gp.obj[mapNum][i] = new OBJ_Coin_Bronze(gp);
        // gp.obj[mapNum][i].worldX = gp.tileSize*25;
        // gp.obj[mapNum][i].worldY = gp.tileSize*23;  
        // i++;
        // gp.obj[mapNum][i] = new OBJ_Axe(gp);
        // gp.obj[mapNum][i].worldX = gp.tileSize*33;
        // gp.obj[mapNum][i].worldY = gp.tileSize*7;  
        // i++; 
        // gp.obj[mapNum][i] = new OBJ_Lantern(gp);
        // gp.obj[mapNum][i].worldX = gp.tileSize*18;
        // gp.obj[mapNum][i].worldY = gp.tileSize*20;  
        // i++;
        gp.obj[mapNum][i] = new OBJ_Potion_Red(gp);
        gp.obj[mapNum][i].worldX = gp.tileSize*15;
        gp.obj[mapNum][i].worldY = gp.tileSize*38;  
        i++;
        gp.obj[mapNum][i] = new OBJ_Potion_Red(gp);
        gp.obj[mapNum][i].worldX = gp.tileSize*17;
        gp.obj[mapNum][i].worldY = gp.tileSize*38;  
        i++;
        gp.obj[mapNum][i] = new OBJ_Chest(gp);
        gp.obj[mapNum][i].setLoot(new OBJ_Sword_Normal(gp));
        gp.obj[mapNum][i].worldX = gp.tileSize*20;
        gp.obj[mapNum][i].worldY = gp.tileSize*10;  
        i++;
        gp.obj[mapNum][i] = new OBJ_Chest(gp);
        gp.obj[mapNum][i].setLoot(new OBJ_Shield_Wood(gp));
        gp.obj[mapNum][i].worldX = gp.tileSize*21;
        gp.obj[mapNum][i].worldY = gp.tileSize*10;  
        i++;
        // gp.obj[mapNum][i] = new OBJ_Chest(gp);
        // gp.obj[mapNum][i].setLoot(new OBJ_Potion_Red(gp));
        // gp.obj[mapNum][i].worldX = gp.tileSize*16;
        // gp.obj[mapNum][i].worldY = gp.tileSize*20;  
        // i++;

        mapNum = 2;
        i = 0;
        gp.obj[mapNum][i] = new OBJ_Door(gp);
        gp.obj[mapNum][i].worldX = gp.tileSize * 7;
        gp.obj[mapNum][i].worldY = gp.tileSize * 40;
        i++;
        gp.obj[mapNum][i] = new OBJ_Chest(gp);
        gp.obj[mapNum][i].setLoot(new OBJ_Pickaxe(gp));
        gp.obj[mapNum][i].worldX = gp.tileSize * 7;
        gp.obj[mapNum][i].worldY = gp.tileSize * 36;
        i++;
        gp.obj[mapNum][i] = new OBJ_Chest(gp);
        gp.obj[mapNum][i].setLoot(new OBJ_Potion_Red(gp));
        gp.obj[mapNum][i].worldX = gp.tileSize * 19;
        gp.obj[mapNum][i].worldY = gp.tileSize * 39;
        i++;
        gp.obj[mapNum][i] = new OBJ_Chest(gp);
        gp.obj[mapNum][i].setLoot(new OBJ_Potion_Red(gp));
        gp.obj[mapNum][i].worldX = gp.tileSize * 13;
        gp.obj[mapNum][i].worldY = gp.tileSize * 29;
        i++;
        gp.obj[mapNum][i] = new OBJ_Chest(gp);
        gp.obj[mapNum][i].setLoot(new OBJ_Potion_Red(gp));
        gp.obj[mapNum][i].worldX = gp.tileSize * 29;
        gp.obj[mapNum][i].worldY = gp.tileSize * 40;
        i++;
        gp.obj[mapNum][i] = new OBJ_Chest(gp);
        gp.obj[mapNum][i].setLoot(new OBJ_Potion_Red(gp));
        gp.obj[mapNum][i].worldX = gp.tileSize * 13;
        gp.obj[mapNum][i].worldY = gp.tileSize * 14;
        i++;
        gp.obj[mapNum][i] = new OBJ_Door_Iron(gp);
        gp.obj[mapNum][i].worldX = gp.tileSize * 15;
        gp.obj[mapNum][i].worldY = gp.tileSize * 34;
        i++;

        mapNum = 3;
        i = 0;
        gp.obj[mapNum][i] = new OBJ_Door_Iron(gp);
        gp.obj[mapNum][i].worldX = gp.tileSize * 25;
        gp.obj[mapNum][i].worldY = gp.tileSize * 15;
        i++;

        gp.obj[mapNum][i] = new OBJ_BlueHeart(gp);
        gp.obj[mapNum][i].worldX = gp.tileSize * 25;
        gp.obj[mapNum][i].worldY = gp.tileSize * 8;
        i++;
    }
    
    public void setNPC() {
        int mapNum = 0;
        int i = 0;
    	gp.npc[mapNum][i] = new NPC_OldMan(gp);
    	gp.npc[mapNum][i].worldX = gp.tileSize*19;
    	gp.npc[mapNum][i].worldY = gp.tileSize*20;
        i++;

        mapNum = 1;
        i = 0;
        gp.npc[mapNum][i] = new NPC_Merchant(gp);
    	gp.npc[mapNum][i].worldX = gp.tileSize*12;
    	gp.npc[mapNum][i].worldY = gp.tileSize*7;
        i++;

        mapNum = 2;
        i = 0;

        gp.npc[mapNum][i] = new NPC_BigRock(gp);
        gp.npc[mapNum][i].worldX = gp.tileSize*29;
        gp.npc[mapNum][i].worldY = gp.tileSize*45;
        i++;
        gp.npc[mapNum][i] = new NPC_BigRock(gp);
        gp.npc[mapNum][i].worldX = gp.tileSize*31;
        gp.npc[mapNum][i].worldY = gp.tileSize*40;
        i++;
        gp.npc[mapNum][i] = new NPC_BigRock(gp);
        gp.npc[mapNum][i].worldX = gp.tileSize*10;
        gp.npc[mapNum][i].worldY = gp.tileSize*39;
        i++;
    }
    
    public void setMonster() {
        int mapNum = 0;
        int i = 0;
    	gp.monster[mapNum][i] = new MON_GreenSlime(gp);
    	gp.monster[mapNum][i].worldX = gp.tileSize*14;
    	gp.monster[mapNum][i].worldY = gp.tileSize*24;
        i++;

        gp.monster[mapNum][i] = new MON_GreenSlime(gp);
    	gp.monster[mapNum][i].worldX = gp.tileSize*15;
    	gp.monster[mapNum][i].worldY = gp.tileSize*22;
        i++;

        gp.monster[mapNum][i] = new MON_GreenSlime(gp);
    	gp.monster[mapNum][i].worldX = gp.tileSize*26;
    	gp.monster[mapNum][i].worldY = gp.tileSize*31;
        i++;

        gp.monster[mapNum][i] = new MON_GreenSlime(gp);
    	gp.monster[mapNum][i].worldX = gp.tileSize*15;
    	gp.monster[mapNum][i].worldY = gp.tileSize*36;
        i++;

        gp.monster[mapNum][i] = new MON_Orc(gp);
        gp.monster[mapNum][i].worldX = gp.tileSize * 39;
        gp.monster[mapNum][i].worldY = gp.tileSize * 30;

        mapNum = 2;
        i = 0;

        gp.monster[mapNum][i] = new MON_Bat(gp);
        gp.monster[mapNum][i].worldX = gp.tileSize*9;
        gp.monster[mapNum][i].worldY = gp.tileSize*43;
        i++;

        gp.monster[mapNum][i] = new MON_Bat(gp);
        gp.monster[mapNum][i].worldX = gp.tileSize*28;
        gp.monster[mapNum][i].worldY = gp.tileSize*37;
        i++;

        gp.monster[mapNum][i] = new MON_Bat(gp);
        gp.monster[mapNum][i].worldX = gp.tileSize*13;
        gp.monster[mapNum][i].worldY = gp.tileSize*36;
        i++;

        mapNum = 3;
        i = 0;

        if(Progress.skeletonLordDefeated == 0){
            gp.monster[mapNum][i] = new MON_SkeletonLord(gp);
            gp.monster[mapNum][i].worldX = gp.tileSize*23;
            gp.monster[mapNum][i].worldY = gp.tileSize*16;
            i++;
        }
    }
    public void setInteractiveTile() {

        int mapNum = 0;
        int i = 0;

        gp.iTile[mapNum][i] = new IT_DryTree(gp, 39, 38);
        i++;
        gp.iTile[mapNum][i] = new IT_DryTree(gp, 39, 37);
        i++;
        gp.iTile[mapNum][i] = new IT_DryTree(gp, 39, 36);
        i++;
        gp.iTile[mapNum][i] = new IT_DryTree(gp, 39, 32);
        i++;
        gp.iTile[mapNum][i] = new IT_DryTree(gp, 36, 35);
        i++;
        gp.iTile[mapNum][i] = new IT_DryTree(gp, 36, 36);
        i++;
        gp.iTile[mapNum][i] = new IT_DryTree(gp, 40, 36);
        i++;
        gp.iTile[mapNum][i] = new IT_DryTree(gp, 40, 37);
        
        mapNum = 2;
        i = 0;

        // gp.iTile[mapNum][i] = new IT_DestructibleWall(gp,18,30);i++;
        // gp.iTile[mapNum][i] = new IT_DestructibleWall(gp,17,31);i++;
        // gp.iTile[mapNum][i] = new IT_DestructibleWall(gp,17,32);i++;
        // gp.iTile[mapNum][i] = new IT_DestructibleWall(gp,17,34);i++;
        // gp.iTile[mapNum][i] = new IT_DestructibleWall(gp,18,34);i++;
        // gp.iTile[mapNum][i] = new IT_DestructibleWall(gp,10,33);i++;
        // gp.iTile[mapNum][i] = new IT_DestructibleWall(gp,10,22);i++;
        // gp.iTile[mapNum][i] = new IT_DestructibleWall(gp,38,24);i++;
        // gp.iTile[mapNum][i] = new IT_DestructibleWall(gp,38,18);i++;
        // gp.iTile[mapNum][i] = new IT_DestructibleWall(gp,38,19);i++;
        // gp.iTile[mapNum][i] = new IT_DestructibleWall(gp,38,21);i++;
        // gp.iTile[mapNum][i] = new IT_DestructibleWall(gp,18,13);i++;
        // gp.iTile[mapNum][i] = new IT_DestructibleWall(gp,18,14);i++;
        // gp.iTile[mapNum][i] = new IT_DestructibleWall(gp,22,28);i++;
        // gp.iTile[mapNum][i] = new IT_DestructibleWall(gp,30,28);i++;
        // gp.iTile[mapNum][i] = new IT_DestructibleWall(gp,32,28);i++;
        //map moi
        gp.iTile[mapNum][i] = new IT_DestructibleWall(gp,12,42);i++;
        gp.iTile[mapNum][i] = new IT_DestructibleWall(gp,12,43);i++;
        gp.iTile[mapNum][i] = new IT_DestructibleWall(gp,20,41);i++;
        gp.iTile[mapNum][i] = new IT_DestructibleWall(gp,24,33);i++;
        gp.iTile[mapNum][i] = new IT_DestructibleWall(gp,25,33);i++;
        gp.iTile[mapNum][i] = new IT_DestructibleWall(gp,20,36);i++;


        gp.iTile[mapNum][i] = new IT_MetalPlate(gp,31,45);i++;
        gp.iTile[mapNum][i] = new IT_MetalPlate(gp,31,43);i++;
        gp.iTile[mapNum][i] = new IT_MetalPlate(gp,9,39);i++;
    }
}