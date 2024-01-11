import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;

public class getData {
	private static String imgPath;
	private static BufferedImage img;
	private static String type;
	private static int loadIndex;
	
	public String getImgPath() {
		return this.imgPath;
	}
	public void setImgPath(String getPath) {
		this.imgPath = getPath;
	}
	public BufferedImage getImg() {
		return this.img;
	}
	public void setImg(BufferedImage i) {
		this.img = i;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public int getLoadIndex() {
		return loadIndex;
	}
	public void setLoadIndex(int loadIndex) {
		this.loadIndex = loadIndex;
	}
}
