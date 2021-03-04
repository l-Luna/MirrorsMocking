package tests.ui;

import arc.Core;
import arc.graphics.g2d.Draw;
import arc.graphics.g2d.Lines;
import tests.Palette;
import tests.mirrors.Direction;
import tests.mirrors.Laser;
import tests.mirrors.MirrorComponent;
import tests.mirrors.MirrorRoom;
import tests.mirrors.components.Block;
import tests.mirrors.components.Emitter;
import tests.mirrors.components.Redirector;
import tests.mirrors.components.Splitter;

public class MirrorsUi{
	
	MirrorRoom room;
	MirrorsMenuFragment menu;
	
	private static final int pixels = 40;
	
	public MirrorsUi(MirrorRoom room, MirrorsMenuFragment menu){
		this.room = room;
		this.menu = menu;
	}
	
	public void render(int w, int h){
		// treat 1 space as 20 pixels
		Draw.color(Palette.grid);
		Lines.stroke(1);
		for(int x = -11; x < 12; x++)
			for(int y = -11; y < 12; y++)
				Lines.square(x * pixels + w / 2f, y * -pixels + h / 2f, pixels / 2f);
		
		Draw.color(Palette.accent);
		Lines.stroke(5);
		for(Laser laser : room.getLasers())
			Lines.line((laser.originX + laser.direction.xOff * .5f) * pixels + w / 2f, (laser.originY + laser.direction.yOff * .5f) * -pixels + h / 2f, (laser.originX + laser.direction.xOff * (laser.length + .5f)) * pixels + w / 2f, (laser.originY + laser.direction.yOff * (laser.length + .5f)) * -pixels + h / 2f);
		for(MirrorComponent component : room.getComponents()){
			Draw.color(Palette.accent);
			Lines.stroke(5);
			float compX = component.getX() * pixels + w / 2f;
			float compY = component.getY() * -pixels + h / 2f;
			Lines.square(compX, compY, pixels / 2f);
			
			Draw.color(Palette.orange);
			Lines.stroke(4);
			for(Direction direction : component.getPoweredBy()){
				switch(direction){
					case UP -> Lines.line(compX - pixels / 2f + 2, compY + pixels / 2f - 2, compX + pixels / 2f - 2, compY + pixels / 2f - 2);
					case LEFT -> Lines.line(compX - pixels / 2f + 2, compY + pixels / 2f - 2, compX - pixels / 2f + 2, compY - pixels / 2f + 2);
					case DOWN -> Lines.line(compX - pixels / 2f + 2, compY - pixels / 2f + 2, compX + pixels / 2f - 2, compY - pixels / 2f + 2);
					case RIGHT -> Lines.line(compX + pixels / 2f - 2, compY + pixels / 2f - 2, compX + pixels / 2f - 2, compY - pixels / 2f + 2);
				}
			}
			
			if(component instanceof Block){
				drawBlock(compX, compY);
			}else if(component instanceof Emitter){
				drawEmitter(compX, compY, component.getDirection());
			}else if(component instanceof Splitter){
				drawSplitter(compX, compY);
			}else if(component instanceof Redirector){
				drawRedirector(compX, compY, component.getDirection());
			}
		}
		
		Draw.color(Palette.orange);
		Lines.stroke(3);
		int squareX = Math.round((Core.input.mouseX() - Core.scene.getWidth() / 2f) / pixels);
		int squareY = Math.round((Core.input.mouseY() - Core.scene.getHeight() / 2f) / pixels);
		Lines.square(squareX * pixels + w / 2f, squareY * pixels + h / 2f, pixels / 2f);
		
		float compX = squareX * pixels + w / 2f;
		float compY = squareY * pixels + h / 2f;
		
		if(menu.selected != null)
			switch(menu.selected){
				case EMITTER -> drawEmitter(compX, compY, menu.placementDirection);
				case SPLITTER -> drawSplitter(compX, compY);
				case REDIRECTOR -> drawRedirector(compX, compY, menu.placementDirection);
				case BLOCK -> drawBlock(compX, compY);
			}
		
		Draw.reset();
	}
	
	// TODO: replace with proper textures lol, this is just mockups
	
	private void drawBlock(float compX, float compY){
		Lines.line(compX - pixels / 5f, compY - pixels / 5f, compX + pixels / 5f, compY + pixels / 5f);
		Lines.line(compX + pixels / 5f, compY - pixels / 5f, compX - pixels / 5f, compY + pixels / 5f);
	}
	
	private void drawEmitter(float compX, float compY, Direction direction){
		switch(direction){
			case UP -> {
				Lines.line(compX + pixels / 5f, compY - pixels / 5f + 5, compX, compY + 5);
				Lines.line(compX - pixels / 5f, compY - pixels / 5f + 5, compX, compY + 5);
			}
			case LEFT -> {
				Lines.line(compX + pixels / 5f - 5, compY + pixels / 5f, compX - 5, compY);
				Lines.line(compX + pixels / 5f - 5, compY - pixels / 5f, compX - 5, compY);
			}
			case DOWN -> {
				Lines.line(compX + pixels / 5f, compY + pixels / 5f - 5, compX, compY - 5);
				Lines.line(compX - pixels / 5f, compY + pixels / 5f - 5, compX, compY - 5);
			}
			case RIGHT -> {
				Lines.line(compX - pixels / 5f + 5, compY + pixels / 5f, compX + 5, compY);
				Lines.line(compX - pixels / 5f + 5, compY - pixels / 5f, compX + 5, compY);
			}
		}
	}
	
	private void drawSplitter(float compX, float compY){
		Lines.line(compX, compY + pixels / 4f, compX, compY - pixels / 4f);
		Lines.line(compX - pixels / 4f, compY, compX + pixels / 4f, compY);
	}
	
	private void drawRedirector(float compX, float compY, Direction direction){
		switch(direction){
			case UP -> {
				Lines.line(compX + pixels / 5f, compY - pixels / 5f + 9, compX, compY + 9);
				Lines.line(compX - pixels / 5f, compY - pixels / 5f + 9, compX, compY + 9);
				Lines.line(compX + pixels / 5f, compY - pixels / 5f - 1, compX, compY - 1);
				Lines.line(compX - pixels / 5f, compY - pixels / 5f - 1, compX, compY - 1);
			}
			case LEFT -> {
				Lines.line(compX + pixels / 5f - 9, compY + pixels / 5f, compX - 9, compY);
				Lines.line(compX + pixels / 5f - 9, compY - pixels / 5f, compX - 9, compY);
				Lines.line(compX + pixels / 5f + 1, compY + pixels / 5f, compX + 1, compY);
				Lines.line(compX + pixels / 5f + 1, compY - pixels / 5f, compX + 1, compY);
			}
			case DOWN -> {
				Lines.line(compX + pixels / 5f, compY + pixels / 5f - 9, compX, compY - 9);
				Lines.line(compX - pixels / 5f, compY + pixels / 5f - 9, compX, compY - 9);
				Lines.line(compX + pixels / 5f, compY + pixels / 5f + 1, compX, compY + 1);
				Lines.line(compX - pixels / 5f, compY + pixels / 5f + 1, compX, compY + 1);
			}
			case RIGHT -> {
				Lines.line(compX - pixels / 5f + 9, compY + pixels / 5f, compX + 9, compY);
				Lines.line(compX - pixels / 5f + 9, compY - pixels / 5f, compX + 9, compY);
				Lines.line(compX - pixels / 5f - 1, compY + pixels / 5f, compX - 1, compY);
				Lines.line(compX - pixels / 5f - 1, compY - pixels / 5f, compX - 1, compY);
			}
		}
	}
}