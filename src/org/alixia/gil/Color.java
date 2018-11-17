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

	public Color(javafx.scene.paint.Color color) {
		this(color.getRed() * 255, color.getGreen() * 255, color.getBlue() * 255, color.getOpacity() * 255);
	}

	public Color(java.awt.Color color) {
		this(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha());
	}

	public Color(double red, double green, double blue) {
		this(red, green, blue, 1);
	}

	public Color correct() {
		return new Color(red(), green(), blue(), opacity());
	}

	public int red() {
		return red > 255 ? 255 : red < 0 ? 0 : (int) red;
	}

	public int blue() {
		return blue > 255 ? 255 : blue < 0 ? 0 : (int) blue;
	}

	public int green() {
		return green > 255 ? 255 : green < 0 ? 0 : (int) green;
	}

	public int alpha() {
		return opacity();
	}

	public int opacity() {
		return opacity > 255 ? 255 : opacity < 0 ? 0 : (int) opacity;
	}

	public int ired() {
		return (int) red & 0xff;
	}

	public int igreen() {
		return (int) green & 0xff;
	}

	public int iblue() {
		return (int) blue & 0xff;
	}

	public int ialpha() {
		return iopacity();
	}

	public int iopacity() {
		return (int) opacity & 0xff;
	}

	public javafx.scene.paint.Color toJavaFXColor() {
		return new javafx.scene.paint.Color(red / 255, green / 255, blue / 255, opacity / 255);
	}

	public java.awt.Color toAWTColor() {
		return new java.awt.Color((int) red, (int) green, (int) blue, (int) opacity);
	}

	public int toAWT_RGB() {
		return iopacity() << 24 | ired() << 16 | igreen() << 8 | iblue();
	}

}
