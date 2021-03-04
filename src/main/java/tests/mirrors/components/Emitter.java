package tests.mirrors.components;

import tests.mirrors.MirrorComponent;
import tests.mirrors.MirrorRoom;

public class Emitter extends MirrorComponent{
	
	public void step(MirrorRoom room){
		// always emit
		room.addLaser(this, direction);
	}
}