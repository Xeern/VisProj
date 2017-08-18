package timeline.story;

import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RectangularShape;

import javax.swing.JPanel;

import timeline.window.Window;

public class View extends JPanel {
	
	public void paint(Graphics g) {
		
		Graphics2D g2D = (Graphics2D)g;
		g2D.scale(1, 1);
        
        g2D.drawLine(400, 0, 400, 600);
        
        Rectangle2D rect = new Rectangle2D.Double(50,50,200,200);
        Rectangle2D rect2 = new Rectangle2D.Double(10,10,100,100);
        
        Color color1 = Color.BLUE;
        
        g2D.setColor(color1);
        g2D.fill(rect);
        Color color2 = Color.GREEN;
        g2D.setColor(color2);
        g2D.draw(rect);
        
        g2D.scale(1, 1);
        Color color3 = Color.BLACK;
        g2D.fill(rect2);
	}
}