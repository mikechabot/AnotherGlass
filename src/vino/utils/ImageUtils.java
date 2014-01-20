package vino.utils;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.apache.log4j.Logger;

public class ImageUtils {
	
	private static final Logger log = Logger.getLogger(ImageUtils.class);
	
	public static byte[] processUserUploadedAvatar(final byte[] input) {
		return toJpegByteArray(resize(read(input), 220, 220));
	}
	
	private static Image read(byte[] data) {
		if (data == null) return null;
		
		try {
			return ImageIO.read(new ByteArrayInputStream(data));
		}
		catch (IOException e) {
			log.debug("byte array did not read in as valid image", e);
		}
		return null;
	}
	
	private static BufferedImage resize(Image image, int width, int height) {
		if (image == null) return null;
		
    	BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
    	Graphics2D graphics = bufferedImage.createGraphics();
    	graphics.drawImage(image, 0, 0, width, height, null); 
    	graphics.dispose();
    	return bufferedImage;
	}
	
	private static byte[] toJpegByteArray(BufferedImage bufferedImage) {
		if (bufferedImage == null) return null;
		
		ByteArrayOutputStream baos = null;
		try {
			baos = new ByteArrayOutputStream();
			ImageIO.write(bufferedImage, "jpg", baos);
			baos.flush();
			return baos.toByteArray();
		}
		catch (IOException e) {
			log.debug("unable to convert bufferedImage to jpeg byte array", e);
		}
		finally {
			if (baos != null) {
				try {
					baos.close();
					baos = null;
				}
				catch (IOException e) {
					// do nothing
				}
			}
		}
		
		return null;
	}
	
}
