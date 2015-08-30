package supreme.paths;


import javax.swing.*;
import java.awt.*;
import java.io.Serializable;

public class Background extends JPanel implements Serializable{

	private ImageIcon bg;
	private int width, height;
	private final int MENY_HEIGHT = 80;
	
	
	public Background(String path){
		if(path.equals(""))
			return;		
		bg = new ImageIcon(path);
		height = bg.getIconWidth();
		width = bg.getIconHeight();		
        setMaximumSize(new Dimension(width, height));
        setMinimumSize(new Dimension(width, height));
        setPreferredSize(new Dimension(width, height));
	}
	
	
	public int getWidth(){
		return width;
	}
	
	
	public int getHeight(){
		return height + MENY_HEIGHT;
	}
		
	
	protected void paintComponent(Graphics g){
		super.paintComponent(g);
		g.drawImage(bg.getImage(), 0, 0, width, height, this);
	}
}

