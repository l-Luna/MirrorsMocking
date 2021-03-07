package tests.launcher;

import arc.Core;
import arc.fx.FxProcessor;
import arc.fx.filters.BloomFilter;
import arc.graphics.Color;
import arc.graphics.Pixmap;
import arc.graphics.g2d.*;
import arc.math.Mathf;
import arc.scene.ui.layout.Scl;
import arc.util.Disposable;
import arc.util.Time;
import tests.Palette;

import java.util.Random;

import static arc.Core.assets;
import static arc.Core.graphics;

public class LoadingScreen implements Disposable{
	
	private FxProcessor fx = new FxProcessor(Pixmap.Format.rgba8888, 2, 2, false, true);
	private Random random = new Random();
	
	private int progress = 0;
	private int stages = 5;
	
	public LoadingScreen(){
		fx.addEffect(new BloomFilter());
	}
	
	public void draw(){
		// fancy fx!
		if(fx.getWidth() != graphics.getWidth() || fx.getHeight() != graphics.getHeight())
			fx.resize(graphics.getWidth(), graphics.getHeight());
		fx.begin();
		Core.graphics.clear(Color.black);
		
		float w = Core.graphics.getWidth(), h = Core.graphics.getHeight(), s = Scl.scl();
		float stroke = 5f * s;
		Lines.stroke(stroke);
		Draw.proj().setOrtho(0, 0, w, h);
		Draw.color(Palette.accent, Color.black, .6f);
		
		float dist = Math.min(w, h) / 4;
		// draw squares with trail
		float l = 15;
		for(int i = 0; i < l; i++){
			Draw.color(Palette.accent, Color.black, .7f + .3f * ((l - i) / l));
			Fill.poly(Mathf.sin((Time.globalTime() + i) / 12f) * dist + w / 2, Mathf.cos((Time.globalTime() + i) / 12f) * dist + h / 2, 4, 45, Time.globalTime());
		}
		for(int i = 0; i < l; i++){
			Draw.color(Palette.accent, Color.black, .7f + .3f * ((l - i) / l));
			Fill.poly(Mathf.sin((Time.globalTime() + i) / 12f + Mathf.pi) * dist + w / 2, Mathf.cos((Time.globalTime() + i) / 12f + Mathf.pi) * dist + h / 2, 4, 45, Time.globalTime());
		}
		
		random.setSeed(413);
		for(int i = 0; i < 40; i++){
			// pick a velocity "randomly"
			int velocity = random.nextInt(4) + 2;
			// pick a start point "randomly"
			int start = random.nextInt((int)w);
			// pick a start time "randomly"
			float elapsed = Time.globalTime() - random.nextInt(6000) + 6000;
			// and pick a rotation "randomly"
			int rotation = random.nextInt(360);
			// draw the polygon
			Draw.color(Palette.accent.r, Palette.accent.g, Palette.accent.b, .5f);
			if(elapsed > 0)
				Fill.poly(start, h - (((velocity * elapsed) / 3f) % h), 3, 6f, rotation);
		}
		
		if(assets.isLoaded("munro")){
			Font font = assets.get("munro");
			font.getData().markupEnabled = true;
			GlyphLayout layout = GlyphLayout.obtain();
			String text = Math.round(progress / (float)stages * 100) + "%";
			layout.setText(font, text);
			font.draw(text, (w - layout.width) / 2, (h - layout.height) / 2);
		}
		
		Draw.flush();
		fx.end();
		fx.applyEffects();
		fx.render();
	}
	
	public void incrementProgress(){
		progress++;
	}
	
	public void dispose(){
		// no more fancy fx :(
		fx.dispose();
	}
}