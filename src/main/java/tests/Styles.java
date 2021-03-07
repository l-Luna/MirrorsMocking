package tests;

import arc.Core;
import arc.graphics.Color;
import arc.scene.ui.Button;
import arc.scene.ui.Label;
import arc.scene.ui.TextButton;
import tests.launcher.AtlasStitcher;

public class Styles{

	public static Button.ButtonStyle defaultButton;
	public static TextButton.TextButtonStyle defaultTextButton, strifeTextButton;
	public static Label.LabelStyle defaultLabel;
	
	public static void registerAssets(){
		AtlasStitcher.addImageForLoading("crude_round_patch.9");
	}
	
	public static void loadStyles(){
		defaultButton = new Button.ButtonStyle(){{
			up = Core.atlas.drawable("crude_round_patch");
		}};
		
		defaultTextButton = new TextButton.TextButtonStyle(){{
			up = Core.atlas.drawable("crude_round_patch");
			font = Fonts.regular;
			fontColor = Color.black;
			disabledFontColor = Color.gray;
		}};
		
		strifeTextButton = new TextButton.TextButtonStyle(){{
			up = Core.atlas.drawable("crude_round_patch");
			
			font = Fonts.regular;
			fontColor = Color.black;
			disabledFontColor = Color.black;
		}};
		
		defaultLabel = new Label.LabelStyle(){{
			font = Fonts.regular;
			fontColor = Color.white;
		}};
		
		Core.scene.registerStyles(Styles.class);
	}
}