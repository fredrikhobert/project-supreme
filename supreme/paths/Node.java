package supreme.paths;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.Serializable;

import javax.swing.*;


public class Node extends JComponent implements Serializable{

	private String name;
	private boolean active = false;
	private Color blue = Color.BLUE;
	private Color red = Color.RED;
	

	public Node(String name){
		this.name = name;
	}


	public void setActiveNode(boolean status){
		active = status;
		repaint();
	}

	
	public String getName(){
		return name;
	}
		
	
	
	public String toString(){
		return name;
	}
	
	
	protected void paintComponent(Graphics g){ 
		super.paintComponent(g);
		if(active){
			g.setColor(red);
			g.fillOval(0, 0, getWidth(), getHeight());
		} else {
			g.setColor(blue);
			g.fillOval(0, 0, getWidth(), getHeight());		
		}
	}


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Node other = (Node) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}
	
}