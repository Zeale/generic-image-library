package org.alixia.gil;

import java.util.Arrays;

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

	public final void resizeLength(int length) {
		expandLength(length - length());
	}

	public final void resizeHeight(int height) {
		expandHeight(height - height());
	}
}
