public class Main {

    public static void main(String[] args) {
        try {
            // чёрное изображение
            Picture black = new Picture(200, 200, new Color(0,0,0));
            PictureUtils.Save(black, "pictures/black.png");
            // белое изображение
            Picture white = new Picture(200, 200, new Color(255,255,255));
            PictureUtils.Save(white, "pictures/white.png");
            // красное изображение
            Picture red = new Picture(200, 200, new Color(255,0,0));
            PictureUtils.Save(red, "pictures/red.png");
            // градиент
            Picture gradient = new Picture(200, 200, new Color(0,0,200));
            PictureUtils.Gradient(gradient);
            PictureUtils.Save(gradient, "pictures/gradient.png");

            // отрисовка звезды первым способом
            Picture star1 = new Picture(200, 200, new Color(255,255,255));
            PictureUtils.Star1(star1, new Color(255, 0, 0));
            PictureUtils.Save(star1, "pictures/star1.png");
            // отрисовка звезды вторым способом
            Picture star2 = new Picture(200, 200, new Color(255,255,255));
            PictureUtils.Star2(star2, new Color(255, 0, 0));
            PictureUtils.Save(star2, "pictures/star2.png");
            // отрисовка звезды третьим способом
            Picture star3 = new Picture(200, 200, new Color(255,255,255));
            PictureUtils.Star3(star3, new Color(255, 0, 0));
            PictureUtils.Save(star3, "pictures/star3.png");
            // отрисовка звезды итоговым способом
            Picture star4 = new Picture(200, 200, new Color(255,255,255));
            PictureUtils.Star4(star4, new Color(255, 0, 0));
            PictureUtils.Save(star4, "pictures/star4.png");

            // считываем объект из .obj файла
            Object stormTrooper = ObjectUtils.ObjectFromFile("objects/StormTrooper.obj");

            // все вершины
            Picture peaks = ObjectUtils.ObjectToPeaks(stormTrooper,1000,1000,250,500,500);
            PictureUtils.Save(peaks, "pictures/peaks.png");
            // все рёбра
            Picture ribs = ObjectUtils.ObjectToRibs(stormTrooper,1000,1000,250,500,500);
            PictureUtils.Save(ribs, "pictures/ribs.png");

        } catch (Exception e){
            e.printStackTrace();
        }
    }
}
