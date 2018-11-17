package org.alixia.gil;

import java.awt.image.BufferedImage;
import java.util.Arrays;
import java.util.function.Function;

import org.alixia.gil.effects.ImageEffect;

import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;

public class Image {

	private Color[][] image;

	private Color defaultColor = Color.TRANSPARENT;

	public void setDefaultColor(Color defaultColor) {
		this.defaultColor = defaultColor;
	}

	public Image(int length, int width) {
		image = new Color[length][width];
	}

	public final Color getPixel(int x, int y) {
		return image[x][y];
	}

	public final void setPixel(int x, int y, Color color) {
		if (color == null)
			throw new IllegalArgumentException("Can't set the color of a pixel to null.");
		image[x][y] = color;
	}

	public final void resize(int length, int height) {
//		TODO Improve
//		if (length > length()) {
//			if (height > height()) {
//				
//			}
//		}

		resizeLength(length);
		resizeHeight(height);

	}

	public final void shrinkLength(int amount) {
		if (amount == 0)
			return;
		if (amount < 0)
			expandLength(-amount);
		else
			image = Arrays.copyOf(image, image.length - amount);
	}

	public final void shrinkHeight(int amount) {
		if (amount == 0)
			return;
		if (amount < 0)
			expandHeight(-amount);
		else {
			int height = height();
			for (int i = 0; i < image.length; i++)
				image[i] = Arrays.copyOf(image[i], height - amount);
		}
	}

	public final int height() {
		return image.length > 0 ? image[0].length : -1;
	}

	public final int length() {
		return image.length;
	}

	public final void expandLength(int amount) {
		if (amount == 0)
			return;
		else if (amount < 0)
			shrinkLength(-amount);
		else {
			image = Arrays.copyOf(image, image.length + amount);
			for (int i = image.length - amount; i < image.length; i++)
				for (int j = (image[i] = new Color[height()]).length
						- amount; j < image[i].length; image[i][j++] = defaultColor)
					;
		}
	}

	public final void expandHeight(int amount) {
		if (amount == 0)
			return;
		else if (amount < 0)
			shrinkHeight(amount);
		else
			for (int i = 0; i < image.length; i++)
				for (int j = (image[i] = Arrays.copyOf(image[i], height() + amount)).length
						- amount; j < image[i].length; image[i][j++] = defaultColor)
					;
	}

	public final void apply(ImageEffect effect) {
		effect.apply(this);
	}

	public final class PixelData {
		public final int x, y;

		public PixelData(int x, int y) {
			this.x = x;
			this.y = y;
		}

		public final Image image() {
			return Image.this;
		}

		public final Color color() {
			return getPixel(x, y);
		}

		public final Color up(int distance) {
			int pos = y - distance;
			return inHeightBounds(pos) ? null : image[x][pos];
		}

		public final Color down(int distance) {
			int pos = y + distance;
			return inHeightBounds(pos) ? null : image[x][pos];
		}

		public final Color left(int distance) {
			int pos = x - distance;
			return inLengthBounds(pos) ? null : image[pos][y];
		}

		public final Color right(int distance) {
			int pos = x + distance;
			return inLengthBounds(pos) ? null : image[pos][y];
		}

		public final Color up() {
			return up(1);
		}

		public final Color down() {
			return down(1);
		}

		public final Color left() {
			return left(1);
		}

		public final Color right() {
			return right(1);
		}

		public final boolean inLengthBounds(int x) {
			return x >= 0 && x < length();
		}

		public final boolean inHeightBounds(int y) {
			return y >= 0 && y < height();
		}

		public final Color relative(int xshift, int yshift) {
			return inLengthBounds(x + xshift) && inHeightBounds(y + yshift) ? image[x][y] : null;
		}

	}

	public void setColor(Color color) {
		int height = height();
		for (int i = 0; i < image.length; i++)
			for (int j = 0; j < height; image[i][j++] = color)
				;
	}

	public final void apply(Function<PixelData, Color> effect) {
		int height = height();
		for (int i = 0; i < image.length; i++)
			for (int j = 0; j < height; j++) {
				Color result = effect.apply(new PixelData(i, j));
				if (result != null)
					image[i][j] = result;
			}
	}

	public WritableImage toJavaFXImage() {
		int height = height();
		WritableImage img = new WritableImage(length(), height);
		PixelWriter writer = img.getPixelWriter();
		for (int i = 0; i < image.length; i++)
			for (int j = 0; j < height; j++)
				writer.setColor(i, j, image[i][j].toJavaFXColor());
		return img;
	}

	public BufferedImage toAWTImage() {
		int height = height();
		BufferedImage img = new BufferedImage(image.length, height, BufferedImage.TYPE_INT_ARGB);
		for (int i = 0; i < image.length; i++)
			for (int j = 0; j < height; img.setRGB(i, j, image[i][j++].toAWTColor().getRGB()))
				;
		return img;
	}

	/**
	 * <p>
	 * A new 2-D {@link Color} array is created, and the {@link Color} in this
	 * {@link Image}'s backing array are copied over.
	 * </p>
	 * <p>
	 * Modifying the value returned by this method will not modify this object.
	 * </p>
	 * 
	 * @return A clone of the 2-D array of {@link Color}s that backs this
	 *         {@link Image}.
	 */
	public Color[][] colorMatrix() {
		Color[][] arr = new Color[image.length][height()];
		for (int i = 0; i < image.length; i++)
			for (int j = 0; j < image.length; arr[i][j] = image[i][j++])
				;
		return arr;
	}

	private Image(Color[][] arr) {
		image = arr;
	}

	@Override
	public Object clone() throws CloneNotSupportedException {
		return new Image(colorMatrix());
	}

	public final void resizeLength(int length) {
		expandLength(length - length());
	}

	public final void resizeHeight(int height) {
		expandHeight(height - height());
	}
}
