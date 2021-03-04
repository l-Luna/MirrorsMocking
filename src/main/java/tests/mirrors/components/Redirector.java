package tests.mirrors.components;

import tests.mirrors.MirrorComponent;
import tests.mirrors.MirrorRoom;

public class Redirector extends MirrorComponent{
	
	public void step(MirrorRoom room){
		super.step(room);
		// emit if powered
		if(powered())
			room.addLaser(this, direction);
	}
}
