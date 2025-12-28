package data;

import java.io.Serializable;
import java.util.ArrayList;

public class DataStorage implements Serializable {

    // PLAYER STATS
    int level;
    int maxLife;
    int life;
    int maxMana;
    int mana;
    int strength;
    int dexterity;
    int exp;
    int nextLevelExp;
    int coin;

    // PLAYER INVENTORY
    ArrayList<String> itemNames = new ArrayList<>();
    ArrayList<Integer> itemAmounts = new ArrayList<>(); 

    int currentWeaponSlot;
    int currentShieldSlot;

    // 1. PLAYER POSITION (Vị trí người chơi)
    int playerWorldX;
    int playerWorldY;
    int currentMap;

    // 2. MONSTER DATA (Dữ liệu quái vật)
    String mapMonsterNames[][];
    int mapMonsterWorldX[][];
    int mapMonsterWorldY[][];
    int mapMonsterLife[][];
    boolean mapMonsterAlive[][]; // Kiểm tra quái còn sống hay chết

    // OBJECT ON MAP
    String mapObjectNames[][];
    int mapObjectWorldX[][];
    int mapObjectWorldY[][];
    String mapObjectLootNames[][];
    boolean mapObjectOpened[][];

    //SKELETON LORD
    int skeletonLordDefeated;
}