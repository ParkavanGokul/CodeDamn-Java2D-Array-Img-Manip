import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Random;
import javax.imageio.ImageIO;

public class ImageProcessing {

    public static void main(String[] args) {
        // Load an image into a 2D array
        int[][] image = imgToTwoD("./apple.jpg");

        // Stretch the image horizontally
        int[][] stretchedImage = stretchHorizontally(image);
        twoDToImage(stretchedImage, "./stretched_apple.jpg");

        // Shrink the image vertically
        int[][] shrunkenImage = shrinkVertically(image);
        twoDToImage(shrunkenImage, "./shrunken_apple.jpg");

        // Create a negative version of the image
        int[][] negativeImage = negativeColor(image);
        twoDToImage(negativeImage, "./negative_apple.jpg");

        // Invert the image
        int[][] invertedImage = invertImage(image);
        twoDToImage(invertedImage, "./inverted_apple.jpg");

        // Apply a color filter
        int[][] filteredImage = colorFilter(image, -75, 30, -30);
        twoDToImage(filteredImage, "./filtered_apple.jpg");

        // Paint an image of random colors
        int[][] blankImage = new int[500][500];
        int[][] randomImage = paintRandomImage(blankImage);
        twoDToImage(randomImage, "./random_image.jpg");

        // Draw a rectangle on an image
        int[] rgba = { 255, 255, 0, 255 }; // Yellow color
        int[][] rectangleImage = paintRectangle(randomImage, 200, 200, 100, 100, getColorIntValFromRGBA(rgba));
        twoDToImage(rectangleImage, "./rectangle_image.jpg");

        // Generate abstract geometric art
        int[][] artImage = generateRectangles(blankImage, 1000);
        twoDToImage(artImage, "./geometric_art.jpg");
    }

    private static int[][] imgToTwoD(String imagePath) {
        try {
            BufferedImage bufferedImage = ImageIO.read(new File(imagePath));
            int width = bufferedImage.getWidth();
            int height = bufferedImage.getHeight();
            int[][] result = new int[height][width];

            for (int row = 0; row < height; row++) {
                for (int col = 0; col < width; col++) {
                    result[row][col] = bufferedImage.getRGB(col, row);
                }
            }
            return result;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static void twoDToImage(int[][] image, String fileName) {
        int height = image.length;
        int width = image[0].length;
        BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {
                bufferedImage.setRGB(col, row, image[row][col]);
            }
        }

        try {
            ImageIO.write(bufferedImage, "png", new File(fileName));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static int[] getRGBAFromPixel(int pixel) {
        int[] rgba = new int[4];
        rgba[0] = (pixel >> 16) & 0xFF; // Red
        rgba[1] = (pixel >> 8) & 0xFF;  // Green
        rgba[2] = pixel & 0xFF;         // Blue
        rgba[3] = (pixel >> 24) & 0xFF; // Alpha
        return rgba;
    }

    private static int getColorIntValFromRGBA(int[] rgba) {
        int color = (rgba[3] << 24) | (rgba[0] << 16) | (rgba[1] << 8) | rgba[2];
        return color;
    }

    private static int[][] stretchHorizontally(int[][] image) {
        int height = image.length;
        int width = image[0].length;
        int[][] stretchedImage = new int[height][width * 2];

        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {
                stretchedImage[row][col * 2] = image[row][col];
                stretchedImage[row][col * 2 + 1] = image[row][col];
            }
        }
        return stretchedImage;
    }

    private static int[][] shrinkVertically(int[][] image) {
        int height = image.length;
        int width = image[0].length;
        int[][] shrunkenImage = new int[height / 2][width];

        for (int row = 0; row < height / 2; row++) {
            for (int col = 0; col < width; col++) {
                shrunkenImage[row][col] = image[row * 2][col];
            }
        }
        return shrunkenImage;
    }

    private static int[][] negativeColor(int[][] image) {
        int height = image.length;
        int width = image[0].length;
        int[][] negativeImage = new int[height][width];

        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {
                int[] rgba = getRGBAFromPixel(image[row][col]);
                for (int i = 0; i < 3; i++) {
                    rgba[i] = 255 - rgba[i]; // Invert RGB channels
                }
                negativeImage[row][col] = getColorIntValFromRGBA(rgba);
            }
        }
        return negativeImage;
    }

    private static int[][] invertImage(int[][] image) {
        int height = image.length;
        int width = image[0].length;
        int[][] invertedImage = new int[height][width];

        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {
                invertedImage[row][col] = ~image[row][col]; // Bitwise NOT operation
            }
        }
        return invertedImage;
    }

    private static int[][] colorFilter(int[][] image, int redChangeValue, int greenChangeValue, int blueChangeValue) {
        int height = image.length;
        int width = image[0].length;
        int[][] filteredImage = new int[height][width];

        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {
                int[] rgba = getRGBAFromPixel(image[row][col]);
                rgba[0] = clamp(rgba[0] + redChangeValue);    // Red
                rgba[1] = clamp(rgba[1] + greenChangeValue);  // Green
                rgba[2] = clamp(rgba[2] + blueChangeValue);   // Blue
                filteredImage[row][col] = getColorIntValFromRGBA(rgba);
            }
        }
        return filteredImage;
    }

    private static int clamp(int value) {
        return Math.max(0, Math.min(255, value));
    }

    private static int[][] paintRandomImage(int[][] canvas) {
        int height = canvas.length;
        int width = canvas[0].length;
        int[][] randomImage = new int[height][width];
        Random random = new Random();

        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {
                int[] rgba = { random.nextInt(256), random.nextInt(256), random.nextInt(256), 255 };
                randomImage[row][col] = getColorIntValFromRGBA(rgba);
            }
        }
        return randomImage;
    }

    private static int[][] paintRectangle(int[][] canvas, int width, int height, int rowPosition, int colPosition, int color) {
        int[][] rectangleImage = copyImage(canvas);

        for (int row = rowPosition; row < rowPosition + height; row++) {
            for (int col = colPosition; col < colPosition + width; col++) {
                if (row >= 0 && row < canvas.length && col >= 0 && col < canvas[0].length) {
                    rectangleImage[row][col] = color;
                }
            }
        }
        return rectangleImage;
    }

    private static int[][] generateRectangles(int[][] canvas, int numRectangles) {
        int[][] artImage = copyImage(canvas);
        Random random = new Random();

        for (int i = 0; i < numRectangles; i++) {
            int width = random.nextInt(50) + 10;    // Random width between 10 and 60
            int height = random.nextInt(50) + 10;   // Random height between 10 and 60
            int rowPosition = random.nextInt(canvas.length - height);
            int colPosition = random.nextInt(canvas[0].length - width);
            int[] rgba = { random.nextInt(256), random.nextInt(256), random.nextInt(256), 255 };
            int color = getColorIntValFromRGBA(rgba);

            artImage = paintRectangle(artImage, width, height, rowPosition, colPosition, color);
        }
        return artImage;
    }

    private static int[][] copyImage(int[][] image) {
        int height = image.length;
        int width = image[0].length;
        int[][] copy = new int[height][width];

        final var copy1 = copy;
        for (int row = 0; row < height; row++) {
            System.arraycopy(image[row], 0, copy1[row], 0, width);
        }
        return copy1;
    }
}
