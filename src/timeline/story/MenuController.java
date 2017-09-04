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
		if(b == true) {
//			if(scale != null) {
//				scale.stop();
//			}
			view.overview = true;
			view.detailView = false;
			view.repaint();
		} else {
//			scale = new Timer(550, view.scaleIn);
			view.overview = false;
			view.detailView = true;
//			scale.start();
//			if(view.factor >= 1) {
//				scale.stop();
//			}
//			view.updateUI();
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