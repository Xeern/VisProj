package timeline.story;

import javax.swing.Timer;

import timeline.story.View;

public class MenuController {
	
	private View view = null;
	Timer scale = null;
	
	private static MenuController menuController = null;
	MenuController(){
	}
	
	public static MenuController getMenuController(){
		if (menuController == null){
			menuController = new MenuController();
		}
		return menuController;  
	}
	
	public static MenuController getInstance(){
		return getMenuController();    
	}
	
	public void toggleOverview(boolean b) {
		if(b) {
			view.overview = true;
			view.detailView = false;
			view.repaint();
		} else {
			view.overview = false;
			view.detailView = true;
			view.repaint();
		}		
	}
	public View getView() {
		return view;
	}
	
	public void setView(View view) {
		this.view = view;
	}
}