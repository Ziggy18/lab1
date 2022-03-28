import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import static java.awt.image.BufferedImage.TYPE_INT_RGB;

// методы для работы с изображением
public class PictureUtils {

    // сохранение изображения в файл
    public static void Save(Picture picture, String filename) throws IOException {
        BufferedImage png = new BufferedImage(picture.getW(), picture.getH(), TYPE_INT_RGB);
        for(int i = 0; i < picture.getW(); i++) {
            for(int j = 0; j < picture.getH(); j++) {
                Color color = picture.getColorArray()[i][j];
                png.setRGB(i, j, new java.awt.Color(color.getR(), color.getG(), color.getB()).getRGB());
            }
        }
        ImageIO.write(png, "png", new File(filename));
    }

    // отрисовка градиента
    public static void Gradient(Picture picture) {
        for(int i = 0; i < picture.getW(); i++) {
            for(int j = 0; j < picture.getH(); j++) {
                int k = (i + j) % 256;
                picture.getColorArray()[i][j] = new Color(k,k,k);
            }
        }
    }

    // отрисовка одного пикселя
    public static void Pixel(Picture picture, int x, int y, Color color) {
        int h= picture.getH();
        picture.getColorArray()[x][h-y-1] = color;
    }

    // первый способ отрисовки прямой
    public static void Line1(Picture picture, int x0, int y0, int x1, int y1, Color color, double dt) {
        for(double t = 0.0; t < 1.0; t+= dt) {
            Pixel(picture, (int)Math.round(x0 * (1-t) + x1 * t), (int)Math.round(y0 * (1-t) + y1 * t), color);
        }
    }

    // второй способ отрисовки прямой
    public static void Line2(Picture picture, int x0, int y0, int x1, int y1, Color color) {
        for(int x = x0; x < x1; x++) {
            double t = (x - x0) / (double)(x1 - x0);
            Pixel(picture, x, (int)Math.round(y0 * (1-t) + y1 * t), color);
        }
    }

    // третий способ отрисовки прямой
    public static void Line3(Picture picture, int x0, int y0, int x1, int y1, Color color) {
        boolean steep = false;
        if (Math.abs(x0-x1)<Math.abs(y0-y1)) {
            int k=x0; x0=y0; y0=k;
                k=x1; x1=y1; y1=k;
                steep = true;
        }
        if (x0>x1) {
            int k=x0; x0=x1; x1=k;
                k=y0; y0=y1; y1=k;
        }
        for(int x = x0; x < x1; x++) {
            double t = (x - x0) / (double)(x1 - x0);
            int y=(int)Math.round(y0 * (1-t) + y1 * t);
            if (steep)
                Pixel(picture, y, x, color);
            else
                Pixel(picture, x, y, color);
        }
    }

    // итоговый способ отрисовки прямой
    public static void Line4(Picture picture, int x0, int y0, int x1, int y1, Color color) {
        boolean steep = false;
        if (Math.abs(x0-x1)<Math.abs(y0-y1)) {
            int k=x0; x0=y0; y0=k;
                k=x1; x1=y1; y1=k;
                steep = true;
        }
        if (x0>x1) {
            int k=x0; x0=x1; x1=k;
                k=y0; y0=y1; y1=k;
        }
        int dx = x1-x0;
        int dy = y1-y0;
        double derror = Math.abs(dy/(double)(dx));
        double error = 0;
        int y = y0;

        for(int x = x0; x < x1; x++) {
            if (steep)
                Pixel(picture, y, x, color);
            else
                Pixel(picture, x, y, color);
            error += derror;
            if (error>0.5) {
                y += (y1>y0?1:-1);
                error -= 1;
            }
        }
    }

    // отрисовка звезды 1
    public static void Star1(Picture picture, Color color) {
        for(int i = 0; i < 13; i++) {
            double alpha = 2 * Math.PI * i / 13;
            Line1(picture, 100, 100, (int)Math.round(100 + 95 * Math.cos(alpha)), (int)Math.round(100 + 95 * Math.sin(alpha)), color, 0.1);
        }
    }

    // отрисовка звезды 2
    public static void Star2(Picture picture, Color color) {
        for(int i = 0; i < 13; i++) {
            double alpha = 2 * Math.PI * i / 13;
            Line2(picture, 100, 100, (int)Math.round(100 + 95 * Math.cos(alpha)), (int)Math.round(100 + 95 * Math.sin(alpha)), color);
        }
    }

    // отрисовка звезды 3
    public static void Star3(Picture picture, Color color) {
        for(int i = 0; i < 13; i++) {
            double alpha = 2 * Math.PI * i / 13;
            Line3(picture, 100, 100, (int)Math.round(100 + 95 * Math.cos(alpha)), (int)Math.round(100 + 95 * Math.sin(alpha)), color);
        }
    }

    // отрисовка звезды 4
    public static void Star4(Picture picture, Color color) {
        for(int i = 0; i < 13; i++) {
            double alpha = 2 * Math.PI * i / 13;
            Line4(picture, 100, 100, (int)Math.round(100 + 95 * Math.cos(alpha)), (int)Math.round(100 + 95 * Math.sin(alpha)), color);
        }
    }
}
