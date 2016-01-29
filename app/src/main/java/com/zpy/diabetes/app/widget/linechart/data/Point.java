package com.zpy.diabetes.app.widget.linechart.data;

public class Point {
	
	public String title = "";
	public String subtitle = "";
	
	public boolean isSelected;
	public int x;
	public Double y;
	
	public int canvasX;
	public int canvasY;
	
	public Point(int x, Double y) {
		this.x = x;
		this.y = y;
	}
	
	@Override
	public String toString() {
		return x + ", " + y;
	}
	
}