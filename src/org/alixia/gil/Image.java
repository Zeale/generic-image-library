package org.alixia.gil;

import java.util.Arrays;
import java.util.function.BiFunction;
import java.util.function.Function;

public class Image {

	private Color[][] image;

	private Color defaultColor = Color.TRANSPARENT;

	public Image(int length, int width) {
		image = new Color[length][width];
	}

	public final Color getPixel(int x, int y) {
		return image[x][y];
	}

	public final void setPixel(int x, int y, Color color) {
		if (color == null)
			throw new IllegalArgumentException("Can't set the color of a pixel to null.");
	}

	public final void resize(int length, int height) {
		// TODO Improve
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
		else
			for (int i = 0; i < image.length; i++)
				image[i] = Arrays.copyOf(image[i], height() - amount);// Throws NegativeArraySizeException on first
																		// iteration if there's an issue. This fails
																		// safely.
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
		else {
			for (int i = 0; i < image.length; i++) {
				for (int j = (image[i] = Arrays.copyOf(image[i], height() + amount)).length
						- amount; j < image[i].length; image[i][j++] = defaultColor)
					;
			}
		}
	}

	public final class PixelData {
		public final int x, y;

		public PixelData(int x, int y) {
			this.x = x;
			this.y = y;
		}

		public Color color() {
			return getPixel(x, y);
		}

		public Color up(int distance) {
			int pos = y - distance;
			return pos < 0 || pos >= image.length ? null : image[x][pos];
		}

		public Color down(int distance) {
			int pos = y + distance;
			return pos < 0 || pos >= image.length ? null : image[x][pos];
		}

		public Color left(int distance) {
			int pos = x - distance;
			return pos < 0 || pos >= height() ? null : image[pos][y];
		}

		public Color right(int distance) {
			int pos = x + distance;
			return pos < 0 || pos >= height() ? null : image[pos][y];
		}

		public Color up() {
			return up(1);
		}

		public Color down() {
			return down(1);
		}

		public Color left() {
			return left(1);
		}

		public Color right() {
			return right(1);
		}

	}

	public final void apply(Function<PixelData, Color> effect) {
		for (int i = 0; i < image.length; i++)
			for (int j = 0; j < height(); effect.apply(new PixelData(i, j++)))
				;
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
