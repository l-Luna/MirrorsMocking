package tests.mirrors;

import java.util.EnumSet;

public enum Direction{
	
	UP(0, -1, false),
	LEFT(-1, 0, true),
	DOWN(0, 1, false),
	RIGHT(1, 0, true);
	
	public int xOff, yOff;
	public boolean horizontal;
	
	Direction(int xOff, int yOff, boolean horizontal){
		this.xOff = xOff;
		this.yOff = yOff;
		this.horizontal = horizontal;
	}
	
	public static EnumSet<Direction> ALL = EnumSet.allOf(Direction.class);
	
	public Direction opposite(){
		return switch(this){
			case UP -> DOWN;
			case LEFT -> RIGHT;
			case DOWN -> UP;
			case RIGHT -> LEFT;
		};
	}
	
	public Direction clockwise(){
		return switch(this){
			case UP -> RIGHT;
			case LEFT -> UP;
			case DOWN -> LEFT;
			case RIGHT -> DOWN;
		};
	}
	
	public Direction counterclockwise(){
		return switch(this){
			case UP -> LEFT;
			case LEFT -> DOWN;
			case DOWN -> RIGHT;
			case RIGHT -> UP;
		};
	}
	
	public int toDegrees(){
		return switch(this){
			case UP -> 270;
			case LEFT -> 180;
			case DOWN -> 90;
			case RIGHT -> 0;
		};
	}
	
	public double toRadians(){
		return Math.toRadians(toDegrees());
	}
}