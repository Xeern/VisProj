package timeline.story;

import timeline.story.View;

public class MenuController {
	
	private View view = null;
	
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
			view.overview = true;
			view.detailView = false;
//			view.updateUI();
			view.repaint();

		} else {
			view.overview = false;
			view.detailView = true;
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