package tests.core;

import arc.ApplicationListener;
import arc.graphics.Camera;
import arc.graphics.Color;
import arc.graphics.Gl;
import arc.math.Mathf;
import arc.scene.ui.layout.Scl;

import static arc.Core.graphics;

public class Renderer implements ApplicationListener{
	
	Camera camera;
	private float targetscale = Scl.scl(4);
	private float camerascale = targetscale;
	
	public Renderer(){
		camera = new Camera();
	}
	
	public void update(){
		Color.white.set(1f, 1f, 1f, 1f);
		Gl.clear(Gl.stencilBufferBit);
		
		float dest = Mathf.round(targetscale, 0.5f);
		camerascale = Mathf.lerpDelta(camerascale, dest, 0.1f);
		if(Mathf.within(camerascale, dest, 0.001f)) camerascale = dest;
		
		camera.width = graphics.getWidth() / camerascale;
		camera.height = graphics.getHeight() / camerascale;
	}
	
	public void draw(){
	
	}
}