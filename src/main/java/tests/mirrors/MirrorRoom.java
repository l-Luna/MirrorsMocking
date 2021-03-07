package tests.mirrors;

import java.util.*;

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
		int maxLaserLength = 0;
		for(Laser laser : lasers){
			int len = 24;
			int startX = laser.originX;
			int startY = laser.originY;
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
			laser.length = 0;
			laser.blockHitLength = len;
			maxLaserLength = Math.max(maxLaserLength, laser.blockHitLength);
		}
		// then handle laser intra-collision
		// until all lasers are blocked by other lasers or have reached their max length from blocks:
		//  - extend each laser, and add a LaserTrace
		//  - if there's already a LaserTrace there, we have an intersection:
		//    - add eachother to blockedBy, and remove that laser's further traces
		//    - if that laser blocked other lasers, they should be unblocked
		record Coord(int x, int y){}
		
		Map<Coord, List<Laser>> traces = new HashMap<>();
		boolean anyUnblockedLaser = true;
		while(anyUnblockedLaser){
			anyUnblockedLaser = false;
			for(Laser laser : lasers){
				// try to extend the laser if not blocked
				if(laser.blockedBy.isEmpty() && laser.length < laser.blockHitLength){
					anyUnblockedLaser = true;
					// check if there's already a trace there
					Coord myPos = new Coord(laser.originX + laser.length * laser.direction.xOff, laser.originY + laser.length * laser.direction.yOff);
					boolean blocking = traces.containsKey(myPos) && !traces.get(myPos).isEmpty();
					// if there *is* already one, I've been blocked
					if(blocking){
						// block the other lasers and myself
						Map<Coord, List<Laser>> toRemove = new HashMap<>();
						for(Laser trace : traces.get(myPos)){
							if(trace != laser){
								laser.blockedBy.add(trace);
								trace.blockedBy.add(laser);
								// cut the others to length
								int oldLen = trace.length;
								trace.length = trace.direction.horizontal ? (myPos.x() - trace.originX) / trace.direction.xOff : (myPos.y() - trace.originY) / trace.direction.yOff;
								// remove their future traces
								for(int i = 0; i < oldLen - trace.length; i++){
									Coord at = new Coord(trace.originX + (i + trace.length) * trace.direction.xOff, trace.originY + (i + trace.length) * trace.direction.yOff);
									toRemove.computeIfAbsent(at, coord -> new ArrayList<>()).add(trace);
								}
								// and unblock any lasers they're blocking that aren't also on this block
								for(Laser unblocking : trace.blockedBy)
									// prevents three-way collisions from crashing
									if(unblocking.length * unblocking.direction.xOff + unblocking.originX != myPos.x()
											&& unblocking.length * unblocking.direction.yOff + unblocking.originY != myPos.y()){
										unblocking.blockedBy.remove(trace);
										trace.blockedBy.remove(unblocking);
									}
							}
						}
						toRemove.forEach((coord, lasers1) -> lasers1.forEach(laser1 -> traces.get(coord).remove(laser1)));
					}else if(laser.length < laser.blockHitLength)
						laser.length++;
					// and add my own anyways
					traces.computeIfAbsent(myPos, __ -> new ArrayList<>()).add(laser);
				}
			}
		}
		
		// then update blocks to tell them "hey you're powered"
		for(MirrorComponent component : components)
			component.poweredBy.clear();
		for(Laser laser : lasers){
			int endX = laser.originX + laser.direction.xOff * (laser.length);
			int endY = laser.originY + laser.direction.yOff * (laser.length);
			for(MirrorComponent component : components){
				if(component.x == endX && component.y == endY && laser.blockedBy.isEmpty())
					component.poweredBy.add(laser.direction.opposite());
			}
		}
	}
	
	public void addLaser(MirrorComponent component, Direction dir){
		lasers.add(new Laser(component.x + dir.xOff, component.y + dir.yOff, -1, dir));
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