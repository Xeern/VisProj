package timeline.story;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.LayoutManager;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Ellipse2D;
import java.awt.geom.GeneralPath;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.regex.Pattern;

import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.Timer;

import timeline.story.View;
import timeline.window.Window;

public class MouseController implements MouseListener,MouseMotionListener {
	
	private View view = null;
	JPanel openPanel = null;
	boolean open = false;
	Timer down = null;
	Timer up = null;

	Rectangle2D oldBounds = null;
	
	MouseListener labellistener = new MouseAdapter() {
        @Override
        public void mouseClicked(MouseEvent e) {
            JLabel label = (JLabel)e.getComponent();
//            String labelchar = (String)((JLabel) e.getSource()).getText();
            String labelchar = label.getText();
//            System.out.println("text: " + labelchar);
            view.drawOverview(labelchar);
        }
    };
	
	public void mouseClicked(MouseEvent e) {
		int x = e.getX();
		int y = e.getY();
		
		Shape triDown = view.createDownTriangle();
		Shape triUp = view.createUpTriangle();
		Shape cross = view.createCross();
		
		if(cross.getBounds2D().contains(x, y)) {
			view.getMainview();
		}
		
		if(view.detailView == true) {
			if(!view.contains(0, (int)view.storyPoints.get(view.storyPoints.size()-1).getY())) {
				if(triUp.contains(x,y)) {
					up = new Timer(10, moveUp);
					up.start();
					if(down != null) {	
						down.stop();
					}
				}
			}
			if(!view.contains(0, (int)view.storyPoints.get(0).getY())) {
				if(triDown.contains(x,y)) {
					down = new Timer(2, moveDown);
					down.start();
					if(up != null) {
						up.stop();
					}
				}
			}
			
		}
		Point2D currentPoint = null;
		for(int i = view.storyEllipses.size()-1; i >= 0; --i) {
			if(view.overview == true) {
				Ellipse2D tmp = view.storyEllipses.get(i);
				if(tmp.contains(x, y)) {
					currentPoint = view.storyPoints.get(i);
					int currentSegment = view.segmentAtPoint.get(currentPoint);
					ArrayList<String> currentEvent = view.sorted_events.get(currentSegment);
//					System.out.println(currentEvent);
				}
			}
			if(view.detailView == true) {
				double tmpX = view.storyEllipses.get(i).getMinX()-3;
				double tmpY = (view.storyEllipses.get(i).getMinY()-view.translateY)*2;
//				System.out.println("tmpY: " + tmpY);
				Ellipse2D tmp = new Ellipse2D.Double(tmpX, tmpY, 10, 10);
				if(tmp.contains(x,y)) {
					currentPoint = view.storyPoints.get(i);
					int currentSegment = view.segmentAtPoint.get(currentPoint);
					ArrayList<String> currentEvent = view.sorted_events.get(currentSegment);
//					System.out.println(currentEvent);					
				}
			}
		}
		if(openPanel != null) {
			if(!openPanel.getBounds().contains(x, y)) {
				openPanel.setBounds((Rectangle) oldBounds);
			}
		}
//		System.out.println(currentPoint);
		if(currentPoint != null) {
//			System.out.println("paint panel");
			int currentSegment = view.segmentAtPoint.get(currentPoint);
			ArrayList<String> currentEvent = view.sorted_events.get(currentSegment);
			String content = currentEvent.get(2);
			String characters = currentEvent.get(3);
			int contentRows = (int)(Math.ceil(content.length()/45.0) + Math.ceil(characters.length()/45.0));
		
			JPanel viewPanel = view.panelAtPoint.get(currentPoint);
			oldBounds = (Rectangle2D) viewPanel.getBounds();
			
			viewPanel.setBounds((int)currentPoint.getX() + 8, (int)currentPoint.getY() - 21, 300, 25 + contentRows*25);

			if((currentPoint.getY() - 21 + (25 + contentRows*25)) >= view.lastHeight) {
				int ueberschuss = (int) (currentPoint.getY() - 21 + (25 + contentRows*25) - view.lastHeight) + 10;
				int temp = (int) (currentPoint.getY() - 21 + (25 + contentRows*25));
						
				System.out.println("currentY: " + currentPoint.getY());
				System.out.println("temp: " + temp);
				System.out.println("lastheight: " + view.lastHeight);
				System.out.println("ueberschuss: " + ueberschuss + "\n");
			
				viewPanel.setBounds((int)currentPoint.getX() + 8, (int)currentPoint.getY() - ueberschuss, 300, 25 + contentRows*25);				
			}

//			viewPanel.setLayout((LayoutManager) new FlowLayout(FlowLayout.LEFT));
			
			JTextArea textArea = new JTextArea(content);
//			textArea.setLayout((LayoutManager) new FlowLayout(FlowLayout.RIGHT));
			textArea.setBounds((int)currentPoint.getX() + 8, (int)currentPoint.getY() -10, 290, 25 + contentRows*55);
			
			textArea.setFont(new Font("Serif", Font.PLAIN, 14));
			textArea.setOpaque(true);
			textArea.setBackground(null);
			textArea.setLineWrap(true);
			textArea.setWrapStyleWord(true);
			viewPanel.add(textArea);
		
//			System.out.println(characters);
			String splittoken = Pattern.quote("+");
			String[] splitter = characters.split(splittoken);
			for(String j : splitter) {
				JLabel jchar = new JLabel(j); 
			    jchar.addMouseListener(labellistener);
				viewPanel.add(jchar);
			}
			openPanel = viewPanel;
			openPanel.setBackground(Color.WHITE);
			view.setTrue();
		}
	}
	
	ActionListener moveDown = new AbstractAction() {
		public void actionPerformed(ActionEvent e) {
			int length = view.getHeight()/2;
			if(view.getHeight()/2 > view.translateY) {
				if(view.translateY < length*1/12) {
					view.setTranslateY((int)view.translateY + view.getHeight()/128);
				}
				if(view.translateY >= length*1/12 && view.translateY < length*2/12) {
					view.setTranslateY((int)view.translateY + view.getHeight()/64);
				}
				if(view.translateY >= length*2/12 && view.translateY < length*4/12) {
					view.setTranslateY((int)view.translateY + view.getHeight()/18);
				}
				if(view.translateY >= length*4/12 && view.translateY < length*8/12) {
					view.setTranslateY((int)view.translateY + view.getHeight()/32);
				}
				if(view.translateY >= length*8/12 && view.translateY < length*10/12) {
					view.setTranslateY((int)view.translateY + view.getHeight()/64);
				}
				if(view.translateY >= length*10/12 && view.translateY < length*23/24) {
					view.setTranslateY((int)view.translateY + view.getHeight()/128);
				}
				if(view.translateY >= length*23/24) {
					view.setTranslateY((int)view.translateY + view.getHeight()/256);
				}
				view.repaint();
			}
		}
	};

	ActionListener moveUp = new AbstractAction() {
		public void actionPerformed(ActionEvent e) {
			int length = view.getHeight()/2;
			System.out.println(up.isRunning());
			if(0 < view.translateY) {
				if(view.translateY > length*13/14) {
					view.setTranslateY((int)view.translateY - view.getHeight()/128);
				}
				if(view.translateY <= length*13/14 && view.translateY > length*10/12) {
					view.setTranslateY((int)view.translateY - view.getHeight()/64);
				}
				if(view.translateY <= length*10/12 && view.translateY > length*8/12) {
					view.setTranslateY((int)view.translateY - view.getHeight()/32);
				}
				if(view.translateY <= length*8/12 && view.translateY > length*4/12) {
					view.setTranslateY((int)view.translateY - view.getHeight()/18);
				}
				if(view.translateY <= length*4/12 && view.translateY > length*2/12) {
					view.setTranslateY((int)view.translateY - view.getHeight()/64);
				}
				if(view.translateY <= length*2/12 && view.translateY > length*1/24) {
					view.setTranslateY((int)view.translateY - view.getHeight()/128);
				}
				if(view.translateY <= length*1/24) {
					view.setTranslateY((int)view.translateY - view.getHeight()/256);
				}
				view.repaint();
			}
		}
	};
	
	public void mouseEntered(MouseEvent arg0) {
		
	}

	public void mouseExited(MouseEvent arg0) {
		
	}
	
	public void mousePressed(MouseEvent e) {
		
	}
	
	public void mouseReleased(MouseEvent arg0) {
		
	}
	
	public void mouseDragged(MouseEvent e) {
		
	}
	
	public void mouseMoved(MouseEvent e){
		int x = e.getX();
		int y = e.getY();
		Graphics2D currentGraphic = (Graphics2D)view.getGraphics();
		Shape triDown = view.createDownTriangle();
		Shape triUp = view.createUpTriangle();
		Shape cross = view.createCross();
		if(cross.getBounds2D().contains(x, y) && !view.mainView) {
			 currentGraphic.setColor(Color.RED);
			 currentGraphic.setStroke(new BasicStroke(3));
			 currentGraphic.draw(cross);
		} else {
			if(!view.mainView) {
				currentGraphic.setStroke(new BasicStroke(3));
				currentGraphic.draw(cross);
			}
			
		}
		for(int i = 0; i < view.storyEllipses.size(); ++i) {
			if(view.overview == true) {
				Ellipse2D tmp = view.storyEllipses.get(i);
				if(tmp.contains(x, y)) {
					currentGraphic.setColor(Color.BLUE);
					currentGraphic.draw(tmp);
					currentGraphic.fill(tmp);
				} else {
					currentGraphic.setColor(Color.BLACK);
					currentGraphic.draw(tmp);
					currentGraphic.fill(tmp);
				}
			}
			if(view.detailView == true) {
				if(!view.contains(0, (int)view.storyPoints.get(view.storyPoints.size()-1).getY())) {
					if(triUp.contains(x,y)) {
						currentGraphic.setColor(new Color(100, 100, 100));
						currentGraphic.fill(triUp);
					} else {
						currentGraphic.setColor(new Color(54,54,54));
						currentGraphic.fill(triUp);
					}
				}
				if(!view.contains(0, (int)view.storyPoints.get(0).getY())) {
					if(triDown.contains(x,y)) {
						currentGraphic.setColor(new Color(100, 100, 100));
						currentGraphic.fill(triDown);
					} else {
						currentGraphic.setColor(new Color(54,54,54));
						currentGraphic.fill(triDown);
					}
				}

				double tmpX = view.storyEllipses.get(i).getMinX()-3;
				double tmpY = (view.storyEllipses.get(i).getMinY()-view.translateY)*2;
				Ellipse2D tmp = new Ellipse2D.Double(tmpX, tmpY, 10, 10);
				if(tmp.contains(x,y)) {
					currentGraphic.setColor(Color.BLUE);
					currentGraphic.draw(tmp);
					currentGraphic.fill(tmp);
 				} else {
					currentGraphic.setColor(Color.BLACK);
					currentGraphic.draw(tmp);
					currentGraphic.fill(tmp);
				}
			}
		}
	}
	
	public View getView() {
		return view;
	}
	
	public void setView(View view) {
		this.view = view;
	}
}