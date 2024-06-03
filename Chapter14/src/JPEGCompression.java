import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.FileImageOutputStream;

/*
 * Here's the demonstration how to use the javax.imageio package to perform JPEG compression in Java:
 * 
 * In this code, we use the ImageWriter and ImageWriteParam classes from the javax.imageio 
 * package to perform the JPEG compression. The compressImage method takes as input an original 
 * BufferedImage, the desired compression quality (as a float value between 0 and 1), and the 
 * output file path. The method creates a new FileImageOutputStream object and sets the 
 * ImageWriter to write to it. Then, it calls the write method of the ImageWriter to write the 
 * compressed image to the output file.
 * 
 * */

public class JPEGCompression {
	
    public static void main(String[] args) {
    	
        BufferedImage originalImage = null;
        try {
            originalImage = ImageIO.read(new File("input.jpg"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        // convert the image to a different image format with lower quality
        float quality = 0.5f; // compression level (0.0f = worst, 1.0f = best)
        try {
            JPEGCompression.compressImage(originalImage, quality, "output.jpg");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void compressImage(BufferedImage originalImage, float quality, String outputPath) throws IOException {
        ImageWriter writer = ImageIO.getImageWritersByFormatName("jpeg").next();
        ImageWriteParam param = writer.getDefaultWriteParam();
        param.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
        param.setCompressionQuality(quality);
        try (FileImageOutputStream fios = new FileImageOutputStream(new File(outputPath))) {
            writer.setOutput(fios);
            writer.write(null, new IIOImage(originalImage, null, null), param);
        }
        writer.dispose();
    }
}
