package org.alixia.gil.effects;

import org.alixia.gil.Color;
import org.alixia.gil.Image;
import org.alixia.gil.Image.PixelData;

public interface ImageEffect {
	default void apply(Image image) {
		for (int i = 0; i < image.length(); i++)
			for (int j = 0; j < image.height(); j++) {
				Color result = affect(image.new PixelData(i, j));
				if (result != null)
					image.setPixel(i, j, result);
			}

	}

	Color affect(PixelData data);
}
