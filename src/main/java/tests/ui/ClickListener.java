package tests.ui;

public interface ClickListener{
	
	void onClick(int mouseX, int mouseY);
	
	void onScroll(int x, int y, float axis);
}