package tests.mirrors;

import java.util.ArrayList;
import java.util.List;

public class MirrorRoom{
	
	List<MirrorComponent> components = new ArrayList<>();
	List<Laser> lasers = new ArrayList<>();
	
	public void step(){
		// clear lasers
		lasers.clear();
		// update all components
		for(MirrorComponent component : components)
			component.step(this);
		// handle propagating all lasers
		// first extend all lasers until they hit a block or are 24 long
		for(Laser laser : lasers){
			int len = 24;
			int startX = laser.originX + laser.direction.xOff;
			int startY = laser.originY + laser.direction.yOff;
			for(MirrorComponent component : components)
				if(laser.direction.horizontal && component.y == startY){
					if(laser.direction == Direction.LEFT && component.x <= startX)
						len = Math.min(len, startX - component.x);
					else if(laser.direction == Direction.RIGHT && component.x >= startX)
						len = Math.min(len, component.x - startX);
				}else if(!laser.direction.horizontal && component.x == startX){
					if(laser.direction == Direction.UP && component.y <= startY)
						len = Math.min(len, startY - component.y);
					else if(laser.direction == Direction.DOWN && component.y >= startY)
						len = Math.min(len, component.y - startY);
				}
			laser.length = len;
		}
		// then handle laser intra-collision
		// -----|
		//      |
		// -----|
		//      |
		// then update blocks to tell them "hey you're powered"
		for(MirrorComponent component : components)
			component.poweredBy.clear();
		for(Laser laser : lasers){
			int endX = laser.originX + laser.direction.xOff * (laser.length + 1);
			int endY = laser.originY + laser.direction.yOff * (laser.length + 1);
			for(MirrorComponent component : components){
				if(component.x == endX && component.y == endY)
					component.poweredBy.add(laser.direction.opposite());
			}
		}
	}
	
	public void addLaser(MirrorComponent component, Direction dir){
		lasers.add(new Laser(component.x, component.y, -1, dir));
	}
	
	// mostly for testing
	
	public void addComponent(MirrorComponent component, int x, int y){
		components.add(component);
		component.x = x;
		component.y = y;
	}
	
	public void addComponent(MirrorComponent component, int x, int y, Direction dir){
		addComponent(component, x, y);
		component.direction = dir;
	}
	
	public List<Laser> getLasers(){
		return lasers;
	}
	
	public List<MirrorComponent> getComponents(){
		return components;
	}
}