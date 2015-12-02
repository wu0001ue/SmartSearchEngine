import java.io.File;
import java.io.FileFilter;

public class TextFileFilter implements FileFilter {

   @Override
   public boolean accept(File pathname) {
	  String path = pathname.getName().toLowerCase();
      return path.endsWith(".txt") || path.endsWith(".pdf") || path.endsWith(".doc") || path.endsWith(".docx");
   }
}