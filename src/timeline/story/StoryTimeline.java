package timeline.story;

import timeline.story.Data;

import java.io.IOException;

import javax.swing.SwingUtilities;

//import infovis.example.Example;
import timeline.window.Window;

public class StoryTimeline {
	private View view = null;
	private MouseController mouse = null;
	private MenuController menu = null;
	
	public View getView() {
		if (view == null) generateDiagram();
		return view;
	}
	public void generateDiagram(){
	   view = new View();
	   mouse = new MouseController();
	   menu = MenuController.getMenuController();
	   view.addMouseListener(mouse);
	   view.addMouseMotionListener(mouse);
	   mouse.setView(view);
	   menu.setView(view);
	}
	
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				try {
					Data.importValues();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				Window application = new Window();
				application.setView(new StoryTimeline().getView());
				application.getJFrame().setVisible(true);
			}
		});
	}
}