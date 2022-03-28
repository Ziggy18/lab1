import java.io.FileReader;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Scanner;

public class ObjectUtils {

    // считывание объекта из .obj файла
    public static Object ObjectFromFile(String filename) throws Exception {

        Object object=new Object();
        String string;
        Scanner lineScanner;

        FileReader fileReader= new FileReader(filename);
        Scanner fileScanner = new Scanner(fileReader);

        while (fileScanner.hasNextLine()) {

            string=fileScanner.nextLine();

            // пропускаем пустые строки
            if(string.length()<3) {
                continue;
            }

            // если это строка вершины
            if(string.charAt(0)=='v'){
                lineScanner = new Scanner(string.substring(1)).useLocale(Locale.ENGLISH);
                // считываем координаты
                object.addCoordinate(new Coordinate(lineScanner.nextDouble(),lineScanner.nextDouble(),lineScanner.nextDouble()));
            }

            // если это строка полигона
            else if(string.charAt(0)=='f'){
                Polygon currPolygon=new Polygon();
                int v=0, vt=0, vn=0, k=0, currInt=0;
                boolean isNeg=false;
                // проходим по всей строке
                for(int i=2; i<string.length(); i++){
                    char currChar=string.charAt(i);
                    // если закончили считывать текущее значение
                    if(currChar==' '||currChar=='/'){
                        // если мы считали число
                        if(currInt!=0){
                            if(isNeg) currInt*=-1;
                            // понимаем, за какой компонент вершины отвечает данное число
                            if(k==0) v=currInt;
                            else if(k==1) vt=currInt;
                            else vn=currInt;
                            currInt=0;
                        }
                        isNeg=false;
                        // если закончили считывать вершины - сохраняем её
                        if(currChar==' '){
                            k=0;
                            if(v!=0)
                                currPolygon.addPeak(new Peak(v, vt, vn));
                            v=0; vt=0; vn=0;
                        }
                        else k++;
                    }
                    // если текущий символ '-' - запоминаем это
                    else if(currChar=='-') isNeg = true;
                        // если считываем цифру, записываем ее в текущее число
                    else if(currChar>='0'&&currChar<='9')
                        currInt=currInt*10+currChar-'0';
                }
                // после считывания строки сохраняем последнюю вершину при необходимости
                if(currInt!=0){
                    if(isNeg) currInt*=-1;
                    // понимаем, за какой компонент вершины отвечает данное число
                    if(k==0) v=currInt;
                    else if(k==1) vt=currInt;
                    else vn=currInt;
                    currInt=0;
                }
                if(v!=0)
                    currPolygon.addPeak(new Peak(v, vt, vn));
                // добавляем полигон в список полигонов
                object.addPolygon(currPolygon);
            }
        }
        fileReader.close();
        return object;
    }

    // отрисовка изображения со всеми вершинами
    public static Picture ObjectToPeaks(Object object, int w, int h, double scale, int xShift, int yShift){
        Picture picture=new Picture(w,h);
        ArrayList<Coordinate> coordinates=object.getCoordinates();
        // проходим по всем вершинам и рисуем соответствующий пиксель
        for(int i=0; i<coordinates.size();i++)
            PictureUtils.Pixel(picture, (int)Math.round(coordinates.get(i).getX()*scale+xShift), (int)Math.round(coordinates.get(i).getY()*scale+yShift), new Color(255,255,255));
        return picture;
    }

    // отрисовка изображения со всеми рёбрами
    public static Picture ObjectToRibs(Object object, int w, int h, double scale, int xShift, int yShift){
        Picture picture=new Picture(w,h);
        ArrayList<Coordinate> coordinates=object.getCoordinates();
        ArrayList<Polygon> polygons= object.getPolygons();
        // проходим по всем полигонам
        for(int i=0; i<polygons.size();i++){
            ArrayList<Peak> peaks = polygons.get(i).getPeaks();
            // соединяем последовательно все соседние вершины
            for(int j=0; j<peaks.size()-1; j++){
                int v0=peaks.get(j).getP()>0?peaks.get(j).getP()-1:coordinates.size()+peaks.get(j).getP();
                int v1=peaks.get(j+1).getP()>0?peaks.get(j+1).getP()-1:coordinates.size()+peaks.get(j+1).getP();
                PictureUtils.Line4(picture,
                        (int)Math.round(coordinates.get(v0).getX()*scale+xShift),
                        (int)Math.round(coordinates.get(v0).getY()*scale+yShift),
                        (int)Math.round(coordinates.get(v1).getX()*scale+xShift),
                        (int)Math.round(coordinates.get(v1).getY()*scale+yShift),
                        new Color(255,255,255)
                );
            }
            // соединяем последовательно все вершины с первой
            for(int j=2; j<peaks.size(); j++){
                int v0=peaks.get(0).getP()>0?peaks.get(0).getP()-1:coordinates.size()+peaks.get(0).getP();
                int v1=peaks.get(j).getP()>0?peaks.get(j).getP()-1:coordinates.size()+peaks.get(j).getP();
                PictureUtils.Line4(picture,
                        (int)Math.round(coordinates.get(v0).getX()*scale+xShift),
                        (int)Math.round(coordinates.get(v0).getY()*scale+yShift),
                        (int)Math.round(coordinates.get(v1).getX()*scale+xShift),
                        (int)Math.round(coordinates.get(v1).getY()*scale+yShift),
                        new Color(255,255,255)
                );
            }
        }
        return picture;
    }
}