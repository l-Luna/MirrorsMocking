package tests.ui;

import arc.Core;
import arc.scene.Group;
import arc.scene.ui.Button;
import arc.scene.ui.TextButton;
import arc.scene.ui.layout.Cell;
import arc.scene.ui.layout.Table;
import tests.mirrors.Direction;
import tests.mirrors.MirrorComponent;
import tests.mirrors.MirrorRoom;
import tests.mirrors.components.Block;
import tests.mirrors.components.Emitter;
import tests.mirrors.components.Redirector;
import tests.mirrors.components.Splitter;

import java.util.Set;

import static tests.Styles.strifeTextButton;

public class MirrorsMenuFragment implements Menu, ClickListener{
	
	MirrorRoom room = new MirrorRoom();
	MirrorsUi ui = new MirrorsUi(room, this);
	
	Table bar;
	
	public ComponentType selected = null;
	public Direction placementDirection = Direction.RIGHT;
	
	public void build(Group parent){
		// display the game
		parent.fill(bg -> {
			bg.fill((x, y, width, height) -> ui.render((int)width, (int)height));
			bg.setFillParent(true);
		});
		
		// show a toolbar for mirror types and step
		parent.fill(container -> {
			container.bottom();
			container.table(table -> {
				bar = table;
				
				Cell<TextButton> stepButton = table.button("Step", strifeTextButton, room::step);
				stepButton.width(120);
				Cell<TextButton> emitterButton = table.button("Emitter", strifeTextButton, () -> selected = ComponentType.EMITTER);
				emitterButton.width(120);
				Cell<TextButton> splitterButton = table.button("Splitter", strifeTextButton, () -> selected = ComponentType.SPLITTER);
				splitterButton.width(120);
				Cell<TextButton> redirectorButton = table.button("Redirector", strifeTextButton, () -> selected = ComponentType.REDIRECTOR);
				redirectorButton.width(120);
				Cell<TextButton> blockButton = table.button("Block", strifeTextButton, () -> selected = ComponentType.BLOCK);
				blockButton.width(120);
				Cell<TextButton> eraseButton = table.button("Erase", strifeTextButton, () -> selected = null);
				eraseButton.width(120);
				// TODO: proper resetting
				Cell<TextButton> restartButton = table.button("Restart", strifeTextButton, () -> {
					room.getLasers().clear();
					for(MirrorComponent component : room.getComponents())
						component.getPoweredBy().clear();
				});
				restartButton.width(120);
				Cell<TextButton> resetButton = table.button("Reset", strifeTextButton, () -> {
					room.getComponents().clear();
					room.getLasers().clear();
				});
				resetButton.width(120);
			});
		});
	}
	
	public void dispose(){
	
	}
	
	public MirrorRoom getRoom(){
		return room;
	}
	
	public void onClick(int mouseX, int mouseY){
		if(!bar.hasMouse()){
			// find the square we're on
			int pixels = 40;
			int squareX = Math.round((mouseX - Core.scene.getWidth() / 2f) / pixels);
			int squareY = -Math.round((mouseY - Core.scene.getHeight() / 2f) / pixels);
			// remove anything that might be there
			MirrorComponent toRemove = null;
			boolean changed = selected != null;
			for(MirrorComponent component : room.getComponents())
				if(component.getX() == squareX && component.getY() == squareY){
					toRemove = component;
					changed = true;
				}
			room.getComponents().remove(toRemove);
			if(selected != null)
				room.addComponent(selected.newOfType(), squareX, squareY, placementDirection);
			// and reset steps
			// TODO: proper resetting
			room.getLasers().clear();
			for(MirrorComponent component : room.getComponents())
				component.getPoweredBy().clear();
		}
	}
	
	public void onScroll(int x, int y, float axis){
		placementDirection = axis > 0 ? placementDirection.counterclockwise() : placementDirection.clockwise();
	}
	
	public enum ComponentType{
		EMITTER, SPLITTER, REDIRECTOR, BLOCK;
		
		MirrorComponent newOfType(){
			return switch(this){
				case EMITTER -> new Emitter();
				case BLOCK -> new Block();
				case SPLITTER -> new Splitter();
				case REDIRECTOR -> new Redirector();
			};
		}
	}
}