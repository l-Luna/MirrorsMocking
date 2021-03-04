package tests.mirrors;

import java.util.EnumSet;
import java.util.Set;

public class MirrorComponent{
	
	protected int x = 0, y = 0;
	protected Direction direction = Direction.RIGHT;
	protected Set<Direction> poweredBy = EnumSet.noneOf(Direction.class);
	
	public boolean powered(){
		return !poweredBy.isEmpty();
	}
	
	public Set<Direction> getPoweredBy(){
		return poweredBy;
	}
	
	public void step(MirrorRoom room){
		// TODO: replace inheritance of MirrorComponent with inheritance of a MirrorComponentType
		// just to make other code (e.g. selection) nicer
	}
	
	public int getX(){
		return x;
	}
	
	public int getY(){
		return y;
	}
	
	public Direction getDirection(){
		return direction;
	}
}