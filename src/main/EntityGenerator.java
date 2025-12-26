package main;

import entity.Entity;
import object.*;
import monster.MON_Bat;
import monster.MON_GreenSlime;
import monster.MON_Orc;
import monster.MON_SkeletonLord;

public class EntityGenerator {

    GamePanel gp;

    public EntityGenerator(GamePanel gp)
    {
        this.gp = gp;
    }

    public Entity getObject(String itemName)
    {
        Entity obj = null;

        switch (itemName)
        {
            case OBJ_Axe.objName: obj = new OBJ_Axe(gp);break;
            case OBJ_Boots.objName: obj = new OBJ_Boots(gp);break;
            case OBJ_Chest.objName: obj = new OBJ_Chest(gp);break;
            case OBJ_Coin_Bronze.objName: obj = new OBJ_Coin_Bronze(gp);break;
            case OBJ_Door.objName: obj = new OBJ_Door(gp);break;
            case OBJ_Door_Iron.objName: obj = new OBJ_Door_Iron(gp);break;
            case OBJ_Fireball.objName: obj = new OBJ_Fireball(gp);break;
            case OBJ_Heart.objName: obj = new OBJ_Heart(gp);break;
            case OBJ_Key.objName: obj = new OBJ_Key(gp);break;
            case OBJ_Lantern.objName: obj = new OBJ_Lantern(gp);break;
            case OBJ_ManaCrystal.objName: obj = new OBJ_ManaCrystal(gp);break;
            case OBJ_Pickaxe.objName: obj = new OBJ_Pickaxe(gp);break;
            case OBJ_Potion_Red.objName: obj = new OBJ_Potion_Red(gp);break;
            case OBJ_Rock.objName: obj = new OBJ_Rock(gp);break;
            case OBJ_Shield_Blue.objName: obj = new OBJ_Shield_Blue(gp);break;
            case OBJ_Shield_Wood.objName: obj = new OBJ_Shield_Wood(gp);break;
            case OBJ_Sword_Normal.objName: obj = new OBJ_Sword_Normal(gp);break;
            case OBJ_Tent.objName: obj = new OBJ_Tent(gp);break;
            case OBJ_BlueHeart.objName: obj = new OBJ_BlueHeart(gp);break;
        }
        return obj;
    }

    public Entity getMonster(String itemName) {
        Entity obj = null;
        if (itemName == null) return null;

        switch (itemName) {
            
            // --- XỬ LÝ 3 LOẠI SLIME ---
            case "Green Slime": 
                MON_GreenSlime green = new MON_GreenSlime(gp);
                green.setSlimeType(1); // Ép về màu xanh
                obj = green;
                break;
                
            case "Blue Slime": 
                MON_GreenSlime blue = new MON_GreenSlime(gp);
                blue.setSlimeType(2); // Ép về màu dương
                obj = blue;
                break;
                
            case "Red Slime": 
                MON_GreenSlime red = new MON_GreenSlime(gp);
                red.setSlimeType(3); // Ép về màu đỏ
                obj = red;
                break;
            // ---------------------------

            case "Orc": obj = new MON_Orc(gp); break;
            case "Bat": obj = new MON_Bat(gp); break;
            case "Skeleton Lord": obj = new MON_SkeletonLord(gp); break;
        }
        return obj;
    }
}