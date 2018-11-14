package org.alixia.gil;

public class Color {
	
	public static final Color TRANSPARENT = new Color(0, 0, 0, 0);

	public final double red, green, blue, opacity;

	public Color(double red, double green, double blue, double opacity) {
		this.red = red;
		this.green = green;
		this.blue = blue;
		this.opacity = opacity;
	}

	public Color(double red, double green, double blue) {
		this(red, green, blue, 1);
	}

}
