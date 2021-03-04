package tests.launcher;

import arc.ApplicationCore;
import arc.ApplicationListener;
import arc.assets.AssetManager;
import arc.assets.Loadable;
import arc.assets.loaders.MusicLoader;
import arc.assets.loaders.SoundLoader;
import arc.assets.loaders.resolvers.InternalFileHandleResolver;
import arc.audio.Music;
import arc.audio.Sound;
import arc.backend.sdl.SdlApplication;
import arc.backend.sdl.SdlConfig;
import arc.freetype.FreeTypeFontGenerator;
import arc.freetype.FreeTypeFontGeneratorLoader;
import arc.freetype.FreetypeFontLoader;
import arc.graphics.Gl;
import arc.graphics.g2d.Font;
import arc.graphics.g2d.SortedSpriteBatch;
import arc.graphics.g2d.TextureAtlas;
import arc.math.Mathf;
import arc.util.Log;
import arc.util.Time;
import tests.Styles;
import tests.Vars;
import tests.core.Renderer;
import tests.core.Ui;

import static arc.Core.*;

public class DesktopLauncher extends ApplicationCore{
	
	public static final String version = "0.0";
	public static final int numericVersion = 0;
	
	private boolean loadComplete = false;
	public static LoadingScreen loadingScreen;
	private static final int loadingFps = 30;
	private long beginTime;
	
	public DesktopLauncher(String[] args){
		// use args...
	}
	
	public static void main(String[] args){
		try{
			//Vars.loadLogger();
			new SdlApplication(new DesktopLauncher(args), new SdlConfig(){{
				title = "Tests";
				maximized = true;
				stencil = 8;
				width = 900;
				height = 700;
				//setWindowIcon(Files.FileType.internal, "icons/icon_64.png");
			}});
		}catch(Throwable e){
			//handleCrash(e);
			e.printStackTrace();
		}
	}
	
	public void setup(){
		Log.info("~ Starting...");
		Log.info("~ Version @ (@).", version, numericVersion);
		// info shamelessly copied from Mindustry
		Log.info("~ GL version: @", graphics.getGLVersion());
		Log.info("~ Max texture size: @", Gl.getInt(Gl.maxTextureSize));
		Log.info("~ Using @ context.", gl30 != null ? "OpenGL 3" : "OpenGL 2");
		Log.info("~ Java version: @", System.getProperty("java.version"));
		Log.info("~ Screen size: @ x @.", graphics.getWidth(), graphics.getHeight());
		
		Time.setDeltaProvider(() -> {
			float result = graphics.getDeltaTime() * 60f;
			return (Float.isNaN(result) || Float.isInfinite(result)) ? 1f : Mathf.clamp(result, 0.0001f, 60f / 10f);
		});
		
		beginTime = Time.millis();
		loadingScreen = new LoadingScreen();
		
		batch = new SortedSpriteBatch();
		assets = new AssetManager();
		assets.setLoader(Sound.class, new SoundLoader(new InternalFileHandleResolver()));
		assets.setLoader(Music.class, new MusicLoader(new InternalFileHandleResolver()));
		assets.setLoader(Font.class, new FreetypeFontLoader(new InternalFileHandleResolver()));
		assets.setLoader(FreeTypeFontGenerator.class, new FreeTypeFontGeneratorLoader(new InternalFileHandleResolver()));
		
		atlas = TextureAtlas.blankAtlas();
		
		// Register textures to stitch first
		Styles.registerAssets();
		
		assets.load(new AtlasStitcher());
		
		add(Vars.renderer = new Renderer());
		add(Vars.ui = new Ui());
	}
	
	public void add(ApplicationListener module){
		super.add(module);
		if(module instanceof Loadable loadable)
			assets.load(loadable);
	}
	
	public void update(){
		if(!loadComplete){
			if(loadingScreen != null)
				loadingScreen.draw();
			if(assets.update(1000 / loadingFps)){
				loadingScreen.dispose();
				loadingScreen = null;
				Log.info("~ Loading took @ms.", Time.timeSinceMillis(beginTime));
				loadComplete = true;
				
				super.resize(graphics.getWidth(), graphics.getHeight());
				app.post(() -> app.post(() -> app.post(() -> app.post(() -> super.resize(graphics.getWidth(), graphics.getHeight())))));
			}
		}else{
			super.update();
		}
		
		/*int targetfps = Core.settings.getInt("fpscap", 120);

        if(targetfps > 0 && targetfps <= 240){
            long target = (1000 * 1000000) / targetfps; //target in nanos
            long elapsed = Time.timeSinceNanos(lastTime);
            if(elapsed < target){
                Threads.sleep((target - elapsed) / 1000000, (int)((target - elapsed) % 1000000));
            }
        }
        
        lastTime = Time.nanos();*/
	}
}