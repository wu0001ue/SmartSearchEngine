import java.io.BufferedReader;

import java.io.File;
import java.io.FileFilter;
import java.io.FileReader;
import java.io.IOException;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

import org.apache.tika.exception.TikaException;
import org.xml.sax.SAXException;

public class Indexer {

   private IndexWriter writer;
   private IndexWriterConfig config;

   public Indexer(String indexDirectoryPath) throws IOException{
      //this directory will contain the indexes
      Directory indexDirectory = 
         FSDirectory.open(new File(indexDirectoryPath));

      //create the indexer
      config = new IndexWriterConfig(Version.LUCENE_43, new StandardAnalyzer(Version.LUCENE_43));
      /*writer = new IndexWriter(indexDirectory, 
         new StandardAnalyzer(Version.LUCENE_36),true,
         IndexWriter.MaxFieldLength.UNLIMITED);*/
      writer = new IndexWriter(indexDirectory, config);
      
   }

   public void close() throws CorruptIndexException, IOException{
      writer.close();
   }

   @SuppressWarnings("deprecation")
private Document getDocument(File file) throws IOException, SAXException, TikaException{
      Document document = new Document();
      Field contentField = null;
      if (file.getName().toLowerCase().endsWith(".txt")) {
          BufferedReader br = new BufferedReader(new FileReader(file));
          StringBuilder sb = new StringBuilder();
          for (String line; (line = br.readLine()) != null;) {
        	  sb.append(line);
        	  sb.append('\n');
          }
          contentField = new Field(LuceneConstants.CONTENTS, sb.toString(), Field.Store.YES, Field.Index.ANALYZED);
      } else { // Parse pdf
          TextParser tp = new TextParser();
          //tp.parse(file);
          //System.out.println("Current text: " + tp.getText());
          contentField = new Field(LuceneConstants.CONTENTS, tp.parse(file), Field.Store.YES, Field.Index.ANALYZED);
      }
      //index file contents
      //Field contentField = new Field(LuceneConstants.CONTENTS, 
         //new FileReader(file));
     
      //index file name
      Field fileNameField = new Field(LuceneConstants.FILE_NAME,
         file.getName(),
         Field.Store.YES,Field.Index.NOT_ANALYZED);
      //index file path
      Field filePathField = new Field(LuceneConstants.FILE_PATH,
         file.getCanonicalPath(),
         Field.Store.YES,Field.Index.NOT_ANALYZED);

      document.add(contentField);
      document.add(fileNameField);
      document.add(filePathField);

      return document;
   }   

   private void indexFile(File file) throws IOException, SAXException, TikaException{
      System.out.println("Indexing "+file.getCanonicalPath());
      Document document = getDocument(file);
      writer.addDocument(document);
   }

   public int createIndex(String dataDirPath, FileFilter filter) 
      throws IOException, SAXException, TikaException{
      //get all files in the data directory
      File[] files = new File(dataDirPath).listFiles();

      for (File file : files) {
         if(!file.isDirectory()
            && !file.isHidden()
            && file.exists()
            && file.canRead()
            && filter.accept(file)
         ){
            indexFile(file);
         }
      }
      return writer.numDocs();
   }
}