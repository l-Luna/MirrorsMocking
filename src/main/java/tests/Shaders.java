package tests;

import arc.Core;
import arc.graphics.gl.Shader;

public class Shaders{
	
	public static Shader battleGrid;
	
	public static void load(){
		battleGrid = new Shader(Core.files.internal("core/assets/shaders/default.vert"), Core.files.internal("core/assets/shaders/default.frag")){
			public void apply(){
				super.apply();
				
			}
		};
	}
}