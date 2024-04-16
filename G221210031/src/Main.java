/**
*
* @author Elif Yerlikaya elif.yerlikaya1@ogr.sakarya.edu.tr
* @since 01.04.2024
* <p>
* Kullanıcıdan git depo urlsi istenerek gerekli fonksiyonlar çağrılır.
* </p>
*/


import java.io.*;
import java.nio.file.*;
import java.util.regex.*;
import java.util.Scanner;
import org.eclipse.jgit.api.errors.GitAPIException;

public class Main {

    public static void main(String[] args) {
    	String cloneDirectoryPath = "ClonnedRepo";
    	
    	File directory = new File(cloneDirectoryPath);
        if (!directory.exists()) {
            if (!directory.mkdir()) {
            	System.out.println(cloneDirectoryPath + " klasörü oluşturulamadı.");
                return;
            }
        }
        
        Scanner sc = new Scanner(System.in);
        System.out.println("GitHub Repository URL:");
        String repoUrl = sc.nextLine();
        
        try {
            RepoCloner.cloneGitRepo(repoUrl, cloneDirectoryPath);
            System.out.println("Repo klonlaniyor...");
            JavaParser.parseDirectory(cloneDirectoryPath);
        } catch (GitAPIException e) {
            System.err.println("Depo klonlanırken hata oluştu: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Bir hata oluştu: " + e.getMessage());
            e.printStackTrace();
        }
        sc.close();
    }
}


