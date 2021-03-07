package tests.mirrors;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class Laser{
	
	// Length of -1 means uncalculated
	// blockHitLength is the length of the laser colliding with blocks (*not* other lasers)
	public int originX, originY, length, blockHitLength;
	public Direction direction;
	public Set<Laser> blockedBy = new HashSet<>();
	
	public Laser(int originX, int originY, int length, Direction direction){
		this.originX = originX;
		this.originY = originY;
		this.length = length;
		this.direction = direction;
	}
	
	public boolean equals(Object o){
		if(this == o)
			return true;
		if(o == null || getClass() != o.getClass())
			return false;
		Laser laser = (Laser)o;
		return originX == laser.originX &&
				originY == laser.originY &&
				length == laser.length &&
				direction == laser.direction;
	}
	
	public int hashCode(){
		return Objects.hash(originX, originY, length, direction);
	}
}