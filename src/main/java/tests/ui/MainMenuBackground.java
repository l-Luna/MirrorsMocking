package tests.ui;

import arc.Core;
import arc.fx.FxProcessor;
import arc.graphics.Color;
import arc.graphics.Pixmap;
import arc.graphics.g2d.Draw;
import arc.graphics.g2d.Fill;
import arc.math.Mathf;
import arc.util.Disposable;
import arc.util.Time;
import tests.Palette;

import java.util.Random;

import static arc.Core.graphics;

public class MainMenuBackground{
	
	private static FxProcessor fx = new FxProcessor(Pixmap.Format.rgba8888, 2, 2, false, true);
	private static Random random = new Random();
	
	public void render(int w, int h){
		if(fx.getWidth() != graphics.getWidth() || fx.getHeight() != graphics.getHeight())
			fx.resize(graphics.getWidth(), graphics.getHeight());
		fx.begin();
		Core.graphics.clear(Color.black);
		
		// random polygons
		random.setSeed(413);
		if(w > 0 && h > 0)
			for(int i = 0; i < 60; i++){
				// pick a velocity "randomly"
				int velocity = random.nextInt(4) + 2;
				// pick a start point "randomly"
				int start = random.nextInt(w);
				// pick a start time "randomly"
				float elapsed = Time.globalTime - random.nextInt(2000);
				// pick a rotation "randomly"
				int rotation = random.nextInt(360) - 180;
				// and pick number of sides "randomly"
				int sides = random.nextInt(3) + 3;
				// draw the polygon six times, with different time
				for(int j = 0; j < 7; j++){
					float alteredElapsed = elapsed - (j * 2);
					Draw.color(Palette.accent.r, Palette.accent.g, Palette.accent.b, 1f - (j * .1f));
					if(alteredElapsed > 0)
						Fill.poly(start + Mathf.sin(alteredElapsed / 8) * j * Mathf.pow(-1, j), velocity * alteredElapsed / 2f % h, sides, 6f, rotation + (alteredElapsed * Mathf.sign(rotation)));
				}
			}
		
		Draw.flush();
		fx.end();
		fx.applyEffects();
		fx.render();
	}
	
	public void dispose(){
		fx.dispose();
	}
}