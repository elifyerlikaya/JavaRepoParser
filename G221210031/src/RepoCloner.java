/**
*
* @Elif Yerlikaya elif.yerlikaya1@ogr.sakarya.edu.tr
* @20.03.2024
* <p>
* Depoyu klonlama işlemlerini gerçekleştirir.
* </p>
*/



import org.eclipse.jgit.api.Git; // JGit kütüphanesindeki git işlemleri için kullanılır.
import org.eclipse.jgit.api.errors.GitAPIException;// Git işlemleri sırasında oluşabilecek istisnaları yakalıyor.
import java.io.File;
import java.io.IOException;//Giriş çıkış işlemleri sırasında oluşabilecek istisnaları yakalar.
import java.nio.file.Files;//Dosya işlemleri.
import java.nio.file.Path;//Dosya yolları.
import java.util.Comparator;//Karşılaştırma işleri.
import java.util.stream.Stream;

public class RepoCloner {//git deposunu klonlamak için kullanılan sınıf.

    public static void cloneGitRepo(String repo, String targetPath) throws GitAPIException {//parametre olarak url ve klonlanacak dosyanın yolu.
    	File dir = new File(targetPath);//yeni bir hedef dizini temsil eden file oluşur.
        if (dir.exists()) {//burada içindeki dosyaları vb. temizlemek için.
            try (Stream<Path> paths = Files.walk(dir.toPath())) {
                paths.sorted(Comparator.reverseOrder()).map(Path::toFile)
                										.forEach(File::delete);
            } catch (IOException e) {
                throw new RuntimeException("Klonlama dizini temizlenemedi.", e);
            }
        }
    	
    	try {
            Git.cloneRepository().setURI(repo).setDirectory(new File(targetPath)).call();//klonlama işlemi.
        } catch (GitAPIException e) {
            System.err.println("Depo klonlanırken hata: " + e.getMessage());
        }
    }
}
