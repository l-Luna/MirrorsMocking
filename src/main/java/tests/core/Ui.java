package tests.core;

import arc.ApplicationListener;
import arc.Core;
import arc.assets.Loadable;
import arc.graphics.Gl;
import arc.input.KeyCode;
import arc.scene.Element;
import arc.scene.Group;
import arc.scene.Scene;
import arc.scene.ui.TextField;
import arc.scene.ui.layout.WidgetGroup;
import arc.util.Disposable;
import tests.Fonts;
import tests.Shaders;
import tests.Styles;
import tests.launcher.DesktopLauncher;
import tests.ui.ClickListener;
import tests.ui.MainMenuFragment;
import tests.ui.Menu;

public class Ui implements ApplicationListener, Loadable, Disposable{
	
	public Menu menuFragment;
	public Group menuGroup = new WidgetGroup();
	
	static{
		Fonts.loadFonts();
		Shaders.load();
	}
	
	public void loadSync(){
		Core.scene = new Scene();
		Core.input.addProcessor(Core.scene);
		
		Fonts.regular.getData().markupEnabled = true;
		Styles.loadStyles();
		
		menuFragment = new MainMenuFragment();
		
		Core.scene.add(menuGroup);
		
		menuGroup.setFillParent(true);
		menuFragment.build(menuGroup);
		
		DesktopLauncher.loadingScreen.incrementProgress();
	}
	
	// Called before loadSync.
	public void init(){
	
	}
	
	public void update(){
		if(Core.scene == null)
			return;
		
		Gl.clear(Gl.stencilBufferBit | Gl.colorBufferBit | Gl.depthBufferBit);
		
		Core.scene.act();
		Core.scene.draw();
		
		if(Core.input.keyTap(KeyCode.mouseLeft) && Core.scene.getKeyboardFocus() instanceof TextField){
			Element e = Core.scene.hit(Core.input.mouseX(), Core.input.mouseY(), true);
			if(!(e instanceof TextField))
				Core.scene.setKeyboardFocus(null);
		}
		
		if(menuFragment instanceof ClickListener listener){
			if(Core.input.keyTap(KeyCode.mouseLeft))
				listener.onClick(Core.input.mouseX(), Core.input.mouseY());
			if(Core.input.axis(KeyCode.scroll) != 0)
				listener.onScroll(Core.input.mouseX(), Core.input.mouseY(), Core.input.axis(KeyCode.scroll));
		}
	}
	
	public void dispose(){
		menuFragment.dispose();
	}
	
	public void resize(int width, int height){
		if(Core.scene != null)
			Core.scene.resize(width, height);
	}
	
	public void switchMenu(Menu menu){
		menuFragment.dispose();
		menuFragment = menu;
		menuGroup.clear();
		menuFragment.build(menuGroup);
	}
}