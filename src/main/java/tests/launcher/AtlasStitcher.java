package tests.launcher;

import arc.Core;
import arc.assets.Loadable;
import arc.files.Fi;
import arc.graphics.Pixmap;
import arc.graphics.Texture.TextureFilter;
import arc.graphics.g2d.PixmapPacker;
import arc.graphics.g2d.PixmapRegion;
import arc.graphics.g2d.TextureAtlas;
import arc.struct.Seq;
import arc.util.Log;
import arc.util.Time;
import arc.util.io.Streams;

import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Combines all registered images into a texture atlas.
 */
public class AtlasStitcher implements Loadable{
	
	private static Seq<Fi> allImages = new Seq<>();
	
	public static void addImageForLoading(String imageName){
		allImages.add(Core.files.internal("core/assets/sprites/" + imageName + ".png"));
	}
	
	public static void addImagesForLoading(String... imageNames){
		for(String name : imageNames)
			addImageForLoading(name);
	}
	
	public static void addDomainImagesForLoading(String domain, String... imageNames){
		for(String name : imageNames)
			addImageForLoading(domain + "/" + name);
	}
	
	public void loadSync(){
		// Split into multiple atlases later
		PixmapPacker packer = new PixmapPacker(2048, 2048, 0, false);
		AtomicInteger total = new AtomicInteger();
		Time.mark();
		
		allImages.each(file -> {
			// Log.info("# Sprite: @ (@, @).", file.name(), file.isDirectory() ? "dir" : "file", file.exists() ? "real" : "imaginary");
			if(!file.extEquals("png"))
				return;
			
			try(InputStream stream = file.read()){
				byte[] bytes = Streams.copyBytes(stream, Math.max((int)file.length(), 512));
				Pixmap pixmap = new Pixmap(bytes, 0, bytes.length);
				packer.pack(file.nameWithoutExtension(), new PixmapRegion(pixmap));
				pixmap.dispose();
				total.incrementAndGet();
			}catch(IOException e){
				Core.app.post(() -> {
					Log.err("! Error packing image @.", file.name());
					Log.err(e);
					// if(!headless) ui.showException(e);
				});
			}
		});
		
		TextureFilter filter = Core.settings.getBool("linear") ? TextureFilter.linear : TextureFilter.nearest;
		TextureAtlas newAtlas = TextureAtlas.blankAtlas();
		packer.updateTextureAtlas(newAtlas, filter, filter, false, false);
		Core.atlas = newAtlas;
		
		Log.info("~ Stitching @ sprites took @ms.", total.get(), Time.elapsed());
	}
}