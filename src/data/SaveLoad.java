package data;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

// import entity.Entity;
import main.GamePanel;

public class SaveLoad {

    GamePanel gp;

    public SaveLoad(GamePanel gp) {
        this.gp = gp;
    }

    public void save() {

        try {
            try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(new File("save.dat")))) {
                DataStorage ds = new DataStorage();

                // PLAYER STATS
                ds.level = gp.player.level;
                ds.maxLife = gp.player.maxLife;
                ds.life = gp.player.life;
                ds.maxMana = gp.player.maxMana;
                ds.mana = gp.player.mana;
                ds.strength = gp.player.strength;
                ds.dexterity = gp.player.dexterity;
                ds.exp = gp.player.exp;
                ds.nextLevelExp = gp.player.nextLevelExp;
                ds.coin = gp.player.coin;
  
                // PLAYER INVENTORY
                for (int i = 0; i < gp.player.inventory.size(); i++) {
                    ds.itemNames.add(gp.player.inventory.get(i).name);
                    ds.itemAmounts.add(gp.player.inventory.get(i).amount);
                } 

                // PLAYER EQUIPMENT
                ds.currentWeaponSlot = gp.player.getCurrentWeaponSlot();
                ds.currentShieldSlot = gp.player.getCurrentShieldSlot();

                // --- THÊM MỚI: LƯU VỊ TRÍ PLAYER ---
                ds.playerWorldX = gp.player.worldX;
                ds.playerWorldY = gp.player.worldY;
                ds.currentMap = gp.currentMap; // Lưu map hiện tại của người chơi

                ds.mapMonsterNames = new String[gp.maxMap][gp.monster[1].length];
                ds.mapMonsterWorldX = new int[gp.maxMap][gp.monster[1].length];
                ds.mapMonsterWorldY = new int[gp.maxMap][gp.monster[1].length];
                ds.mapMonsterLife = new int[gp.maxMap][gp.monster[1].length];
                ds.mapMonsterAlive = new boolean[gp.maxMap][gp.monster[1].length];

                for(int mapNum = 0; mapNum < gp.maxMap; mapNum++) {
                    for(int i = 0; i < gp.monster[mapNum].length; i++) {
                        // Nếu quái vật slot đó không tồn tại (đã chết và biến mất) hoặc đã chết (alive=false)
                        if(gp.monster[mapNum][i] == null || gp.monster[mapNum][i].alive == false) {
                            ds.mapMonsterNames[mapNum][i] = "NA"; // Đánh dấu là không có/đã chết
                        } 
                        else {
                            // Lưu thông tin quái đang sống
                            ds.mapMonsterNames[mapNum][i] = gp.monster[mapNum][i].name;
                            ds.mapMonsterWorldX[mapNum][i] = gp.monster[mapNum][i].worldX;
                            ds.mapMonsterWorldY[mapNum][i] = gp.monster[mapNum][i].worldY;
                            ds.mapMonsterLife[mapNum][i] = gp.monster[mapNum][i].life;
                            ds.mapMonsterAlive[mapNum][i] = true;
                        }
                    }
                }

                // OBJECTS ON MAP
                ds.mapObjectNames = new String[gp.maxMap][gp.obj[1].length];
                ds.mapObjectWorldX = new int[gp.maxMap][gp.obj[1].length];
                ds.mapObjectWorldY = new int[gp.maxMap][gp.obj[1].length];
                ds.mapObjectLootNames = new String[gp.maxMap][gp.obj[1].length];
                ds.mapObjectOpened = new boolean[gp.maxMap][gp.obj[1].length];

                for (int mapNum = 0; mapNum < gp.maxMap; mapNum++) {
                    
                    for (int i = 0; i < gp.obj[1].length; i++) {
                        
                        if (gp.obj[mapNum][i] == null) {
                            ds.mapObjectNames[mapNum][i] = "NA";
                        }
                        else {
                            ds.mapObjectNames[mapNum][i] = gp.obj[mapNum][i].name;
                            ds.mapObjectWorldX[mapNum][i] = gp.obj[mapNum][i].worldX;
                            ds.mapObjectWorldY[mapNum][i] = gp.obj[mapNum][i].worldY;
                            
                            if (gp.obj[mapNum][i].loot != null) {
                                ds.mapObjectLootNames[mapNum][i] = gp.obj[mapNum][i].loot.name;
                            }
                            
                            ds.mapObjectOpened[mapNum][i] = gp.obj[mapNum][i].opened;
                        }
                    }
                }

                // SAVE PROGRESS
                ds.skeletonLordDefeated = Progress.skeletonLordDefeated;

                // Write the DataStorage object
                oos.writeObject(ds);
            }
        }
        catch(Exception e){
            System.out.println("Save Exception!");
        }
    }
    public void load() {

        try {
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(new File("save.dat")))) {
                // Read the DataStorage object
                DataStorage ds = (DataStorage)ois.readObject();

                gp.player.level = ds.level;
                gp.player.maxLife = ds.maxLife;
                gp.player.life = ds.life;
                gp.player.maxMana = ds.maxMana;
                gp.player.mana = ds.mana;
                gp.player.strength = ds.strength;
                gp.player.dexterity = ds.dexterity;
                gp.player.exp = ds.exp;
                gp.player.nextLevelExp = ds.nextLevelExp;
                gp.player.coin = ds.coin;

                // PLAYER INVENTORY
                gp.player.inventory.clear();
                for (int i = 0; i < ds.itemNames.size(); i++) {
                    gp.player.inventory.add(gp.eGenerator.getObject(ds.itemNames.get(i)));
                    gp.player.inventory.get(i).amount = ds.itemAmounts.get(i);
                }   

                // PLAYER EQUIPMENT
                // Load Vũ khí
                if(ds.currentWeaponSlot != 999 && ds.currentWeaponSlot < gp.player.inventory.size()) {
                    gp.player.currentWeapon = gp.player.inventory.get(ds.currentWeaponSlot);
                } else {
                    gp.player.currentWeapon = null; // Nếu là 999 hoặc lỗi index thì không cầm gì
                }

                // Load Khiên
                if(ds.currentShieldSlot != 999 && ds.currentShieldSlot < gp.player.inventory.size()) {
                    gp.player.currentShield = gp.player.inventory.get(ds.currentShieldSlot);
                } else {
                    gp.player.currentShield = null;
                }

                gp.player.getAttack();
                gp.player.getDefense();
                gp.player.getAttackImage();

                gp.currentMap = ds.currentMap;
                gp.player.worldX = ds.playerWorldX;
                gp.player.worldY = ds.playerWorldY;

                // LOAD PROGRESS
                Progress.skeletonLordDefeated = ds.skeletonLordDefeated;

                // LOAD MONSTERS
                for(int mapNum = 0; mapNum < gp.maxMap; mapNum++) {
                    for(int i = 0; i < gp.monster[mapNum].length; i++) {
                        
                        // Nếu trong file save ghi là "NA" -> Quái đã chết -> Xóa khỏi game
                        if(ds.mapMonsterNames[mapNum][i].equals("NA")) {
                            gp.monster[mapNum][i] = null;
                        } 
                        else {
                            // Luôn luôn tạo mới quái vật từ tên trong file save để đảm bảo đúng loại (Red/Blue/Green)
                            
                            gp.monster[mapNum][i] = gp.eGenerator.getMonster(ds.mapMonsterNames[mapNum][i]);
                            
                            // Cập nhật thông số cho quái (vị trí, máu)
                            if (gp.monster[mapNum][i] != null) {
                                gp.monster[mapNum][i].worldX = ds.mapMonsterWorldX[mapNum][i];
                                gp.monster[mapNum][i].worldY = ds.mapMonsterWorldY[mapNum][i];
                                gp.monster[mapNum][i].life = ds.mapMonsterLife[mapNum][i];
                                gp.monster[mapNum][i].alive = true;
                                gp.monster[mapNum][i].dying = false;
                                gp.monster[mapNum][i].invincible = false;
                            }
                        }
                    }
                }

                // OBJECTS ON MAP
                for (int mapNum = 0; mapNum < gp.maxMap; mapNum++) {

                    for (int i = 0; i < gp.obj[1].length; i++) {

                        if (ds.mapObjectNames[mapNum][i].equals("NA")) {
                            gp.obj[mapNum][i] = null;
                        } else {
                            gp.obj[mapNum][i] = gp.eGenerator.getObject(ds.mapObjectNames[mapNum][i]);
                            gp.obj[mapNum][i].worldX = ds.mapObjectWorldX[mapNum][i];
                            gp.obj[mapNum][i].worldY = ds.mapObjectWorldY[mapNum][i];
                            if (ds.mapObjectLootNames[mapNum][i] != null) {
                                gp.obj[mapNum][i].setLoot(gp.eGenerator.getObject(ds.mapObjectLootNames[mapNum][i]));
                            }

                            gp.obj[mapNum][i].opened = ds.mapObjectOpened[mapNum][i];
                            if (gp.obj[mapNum][i].opened == true) {
                                gp.obj[mapNum][i].down1 = gp.obj[mapNum][i].image2;
                            }
                        }
                    }
                }
            }
        } 
        catch (Exception e) {
            System.out.println("Load Exception!");
            e.printStackTrace();
        }
    }
}
