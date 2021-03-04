package tests.ui;

import arc.Core;
import arc.scene.Group;
import arc.util.Disposable;
import arc.util.Log;
import tests.Vars;

public class MainMenuFragment implements Menu, Disposable{
	
	MainMenuBackground background = new MainMenuBackground();
	
	public void build(Group parent){
		parent.fill(bg -> {
			bg.fill((x, y, width, height) -> background.render((int)width, (int)height));
			bg.setFillParent(true);
		});
		
		parent.fill(container -> {
			container.left();
			
			container.add().width(Core.graphics.getWidth() / 10f);
			container.table(table -> {
				table.button("Mirrors", () -> {
					Log.info("Playing mirrors");
					Vars.ui.switchMenu(new MirrorsMenuFragment());
				});
				table.row().button("Exit", () -> {
					Log.info("Exiting!");
					Core.app.exit();
				});
			});
		});
	}
	
	public void dispose(){
		background.dispose();
	}
}