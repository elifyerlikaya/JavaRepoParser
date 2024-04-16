/**
*
* @author Elif Yerlikaya elif.yerlikaya1@ogr.sakarya.edu.tr
* @since 25.03.2024
* <p>
* .java uzantılı dosyaları bulur ve istenilen özellikleri gerçekleştirir.
* </p>
*/

import java.io.*;
import java.nio.file.*;
import java.util.regex.*;

public class JavaParser {

    private static Pattern METHOD_PATTERN = Pattern.compile("\\b(public|protected|private|static|final)\\b.+\\)");

    public static void parseDirectory(String directoryPath) { //java dosyalarını bulur, her dosya için parsejavafile metodunu çağırır.
        try {
            Files.walk(Paths.get(directoryPath))
                .filter(path -> path.toString().endsWith(".java"))//.java olan dosyaları seçer filtre gibi
                .forEach(JavaParser::parseJavaFile);//her java dosyası için parsejavafile ı çağırır.
        } catch (IOException e) {
            System.out.println("Dosyalar okunurken bir hata oluştu: " + e.getMessage());
        }
    }

    private static void parseJavaFile(Path filePath) {
        int javadocCount = 0;
        int commentCount = 0;
        int codeLineCount = 0;
        int lineCount = 0;
        int functionCount = 0;
        boolean inJavadoc = false;

        try (BufferedReader reader = Files.newBufferedReader(filePath)) {//dosya okuma işlemleri
            String line;
            while ((line = reader.readLine()) != null) {//dosyanın her satırını bitene kadar oku.
                lineCount++;//satır sayısı için sayaç
                if (line.trim().startsWith("/**")) {
                    inJavadoc = true;
                }
                if (inJavadoc) {
                    javadocCount++;
                    if (line.trim().endsWith("*/")) {
                        inJavadoc = false;
                    }
                    continue;
                }
                if (line.trim().startsWith("//")) {//tek satırlı yorum satırları
                    commentCount++;
                } else if (!line.trim().isEmpty()) {//satırda kod varsa kod satır sayısı arttırılır. içinde metod tanımları varsa onları da sayıyor.
                    codeLineCount++;
                    Matcher methodMatcher = METHOD_PATTERN.matcher(line);
                    if (methodMatcher.find()) {
                        functionCount++;
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Dosya okunurken bir hata oluştu: " + filePath);
            e.printStackTrace();
        }

        double yg = ((double) javadocCount + commentCount) * 0.8 / Math.max(1, functionCount); 
        double yh = (double) codeLineCount / Math.max(1, functionCount) * 0.3;
        double commentDeviationPerc = ((100.0 * yg) / yh) - 100;//yorum sapma yüzdesi

        printAnalysis(filePath.getFileName().toString(), javadocCount, commentCount, codeLineCount, lineCount, functionCount, commentDeviationPerc);
    }

    private static void printAnalysis(String fileName, int javadocCount, int commentCount, int codeLineCount, int lineCount, int functionCount, double commentDeviationPerc) {
        System.out.println("--------------------------------------------------");
        System.out.println("Dosya: " + fileName);
        System.out.println("Javadoc Satır Sayısı: " + javadocCount);
        System.out.println("Yorum Satır Sayısı: " + commentCount);
        System.out.println("Kod Satır Sayısı: " + codeLineCount);
        System.out.println("LOC: " + lineCount);
        System.out.println("Fonksiyon Sayısı: " + functionCount);
        System.out.printf("Yorum Sapma Yüzdesi: %.2f%%\n", commentDeviationPerc);
    }
}
