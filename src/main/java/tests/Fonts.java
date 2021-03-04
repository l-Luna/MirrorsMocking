package tests;

import arc.Core;
import arc.freetype.FreeTypeFontGenerator;
import arc.freetype.FreetypeFontLoader;
import arc.graphics.g2d.Font;

public final class Fonts{
	
	public static Font regular;
	
	public static void loadFonts(){
		Core.assets.load("munro", Font.class, new FreetypeFontLoader.FreeTypeFontLoaderParameter("core/assets/fonts/munro.ttf", new FreeTypeFontGenerator.FreeTypeFontParameter())).loaded = f -> Fonts.regular = (Font)f;
	}
}