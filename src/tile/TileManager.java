package tile;

import java.awt.Graphics2D;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import main.GamePanel;
import main.UtilityTool;

public class TileManager {
	GamePanel gp;
	public Tile[] tile;
	public int mapTileNum[][][];
	boolean drawPath = true;
	ArrayList<String> fileNames = new ArrayList<>();
	ArrayList<String> collisionStatus = new ArrayList<>();

	
	
	public TileManager(GamePanel gp) {
		this.gp = gp;

		// READ TILE DATA FILE
		InputStream is = getClass().getResourceAsStream("/maps/tiledata.txt");
		BufferedReader br = new BufferedReader(new InputStreamReader(is));

		// GETTING FILE NAMES AND COLLISION INFO FROM TILE DATA
		String line;

		try {
			while((line = br.readLine()) != null) {
				fileNames.add(line);
				collisionStatus.add(br.readLine());
			}
			br.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
		//initialize the tile array based on the tilenames size
		tile = new Tile[fileNames.size()];
		getTileImage();

		// Get the maxWorldCol & Row
		is = getClass().getResourceAsStream("/maps/worldV3.txt");
		br = new BufferedReader(new InputStreamReader(is));


		try{
			String line2 = br.readLine();
			String maxTile[] = line2.split(" ");

			gp.maxWorldCol = maxTile.length;
			gp.maxWorldRow = maxTile.length;
			mapTileNum = new int [gp.maxMap][gp.maxWorldCol][gp.maxWorldRow];
			br.close();

		}catch(IOException e){
			System.out.println("Exception");
		}
		
		
		loadMap("/maps/worldmap.txt",0); 
		loadMap("/maps/indoor01.txt",1);
		loadMap("/maps/dungeon01.txt",2);
		loadMap("/maps/dungeon02.txt",3);

	}
	public void getTileImage() {

		for(int i = 0; i < fileNames.size(); i++)
		{
			String fileName;
			boolean collision;
			// Get a file name
			fileName = fileNames.get(i);
			// get a collision status
			if(collisionStatus.get(i).equals("true")) {
				collision = true;
			}else{
				collision = false;
			}

			setup(i, fileName, collision);

		}

	    
	}
	public void loadMap(String filePath, int map) {
		try {
			InputStream is = getClass().getResourceAsStream(filePath);
			BufferedReader br = new BufferedReader(new InputStreamReader(is));
			
			int col = 0;
			int row = 0;
			while(col < gp.maxWorldCol && row < gp.maxWorldRow)
			{
				String line = br.readLine();
				while(col < gp.maxWorldCol) {
					String numbers[] = line.split(" ");
					int num = Integer.parseInt(numbers[col]);
					
					mapTileNum[map][col][row] = num;
					col++;
				}
				if(col == gp.maxWorldCol) {
					col = 0;
					row++;
				}
			}
			br.close();
		}catch(Exception e) {
			
		}
	}
	
	public void setup(int index, String imagePath, boolean collision) {
	    
	    UtilityTool uTool = new UtilityTool(); // Khởi tạo UtilityTool để sử dụng phương thức thay đổi kích thước ảnh
	    try {
	        tile[index] = new Tile(); // Tạo một đối tượng Tile mới tại vị trí (index) trong mảng 'tile'.
	        
	        // Đọc file ảnh từ thư mục tài nguyên (ví dụ: /tiles/...).
	        tile[index].image = ImageIO.read(getClass().getResourceAsStream("/tiles/" + imagePath));
	        
	        // Thay đổi kích thước ảnh đã đọc thành kích thước ô gạch chuẩn của game (gp.tileSize).
	        tile[index].image = uTool.scaleImage(tile[index].image, gp.tileSize, gp.tileSize);
	        
	        tile[index].collision = collision; // Thiết lập thuộc tính va chạm (true/false) cho ô gạch này.
	        
	    } catch (IOException e) {
	        e.printStackTrace(); // Bắt lỗi nếu không thể đọc file ảnh và in thông báo lỗi.
	    }
	}
	
	
	
	public void draw (Graphics2D g2) {
		
		int worldCol = 0;
		int worldRow = 0;

		while(worldCol < gp.maxWorldCol && worldRow < gp.maxWorldRow) {
			
			int tileNum = mapTileNum[gp.currentMap][worldCol][worldRow];
			
			int worldX = worldCol * gp.tileSize;
			int worldY = worldRow * gp.tileSize;
			
			int srceenX = worldX - gp.player.worldX + gp.player.screenX;
			int screenY = worldY - gp.player.worldY + gp.player.screenY;
			
			if(worldX + gp.tileSize > gp.player.worldX - gp.player.screenX && 
				worldX - gp.tileSize < gp.player.worldX + gp.player.screenX && 	
				worldY + gp.tileSize > gp.player.worldY - gp.player.screenY && 
				worldY - gp.tileSize < gp.player.worldY + gp.player.screenY)
			{
				g2.drawImage(tile[tileNum].image, srceenX, screenY, null);	
			}
			
			worldCol++;
			if(worldCol == gp.maxWorldCol) {
				worldCol = 0;
				worldRow++;

			}
		}
		
		// if (drawPath == true) {
		// 	g2.setColor(new java.awt.Color(255, 0, 0, 70));

		// 	for (int i = 0; i < gp.pFinder.pathList.size(); i++) {

		// 		int worldX = gp.pFinder.pathList.get(i).col * gp.tileSize;
		// 		int worldY = gp.pFinder.pathList.get(i).row * gp.tileSize;
		// 		int screenX = worldX - gp.player.worldX + gp.player.screenX;
		// 		int screenY = worldY - gp.player.worldY + gp.player.screenY;

		// 		g2.fillRect(screenX, screenY, gp.tileSize, gp.tileSize);
		// 	}
		// }
	}
}
