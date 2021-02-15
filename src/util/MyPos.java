package util;

public class MyPos{
	public int x;
	public int y;
	int id;
	
	public MyPos(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	public MyPos(int x, int y, int id) {
		this.x = x;
		this.y = y;
		
		this.id = id;
	}
}