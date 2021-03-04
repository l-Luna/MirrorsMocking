package tests.mirrors.components;

import tests.mirrors.Direction;
import tests.mirrors.MirrorComponent;
import tests.mirrors.MirrorRoom;

public class Splitter extends MirrorComponent{
	
	public void step(MirrorRoom room){
		super.step(room);
		// emit in all directions that I'm not powered by, if I am powered
		if(powered())
			for(Direction direction : Direction.ALL)
				if(!poweredBy.contains(direction))
					room.addLaser(this, direction);
	}
}