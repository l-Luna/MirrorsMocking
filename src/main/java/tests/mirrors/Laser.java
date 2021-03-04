package tests.mirrors;

public class Laser{
	
	// Length of -1 means uncalculated
	public int originX, originY, length;
	public Direction direction;
	
	public Laser(int originX, int originY, int length, Direction direction){
		this.originX = originX;
		this.originY = originY;
		this.length = length;
		this.direction = direction;
	}
}