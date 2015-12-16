import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.highlight.Highlighter;
import org.apache.lucene.search.highlight.InvalidTokenOffsetsException;
import org.apache.lucene.search.highlight.QueryScorer;
import org.apache.lucene.search.highlight.SimpleHTMLFormatter;
import org.apache.lucene.search.highlight.SimpleSpanFragmenter;
import org.apache.lucene.util.Version;
import org.apache.tika.exception.TikaException;
import org.xml.sax.SAXException;

public class LuceneTester {
   //String indexDir = "./src/java/index";
   String indexDir = "src\\java\\index"; //This is the path for windows machine
   int k = indexDir.hashCode();
   //String dataDir = "./src/java/data";
   String dataDir = "src\\java\\data"; // This is the path for windows machine
   Indexer indexer;
   Searcher searcher;
   Analyzer analyzer;

   public static void main(String[] args) throws SAXException, TikaException {
      LuceneTester tester;
      try {
         tester = new LuceneTester();
         tester.createIndex();
         tester.search("search engine", null);
      } catch (IOException e) {
         e.printStackTrace();
      } catch (ParseException e) {
         e.printStackTrace();
      }
   }
   
   public LuceneTester() {
	   analyzer = new StandardAnalyzer(Version.LUCENE_43);
   }

   private void createIndex() throws IOException, SAXException, TikaException{
      indexer = new Indexer(indexDir);
      int numIndexed;
      long startTime = System.currentTimeMillis();	
      numIndexed = indexer.createIndex(dataDir, new TextFileFilter());
      long endTime = System.currentTimeMillis();
      indexer.close();
      System.out.println(numIndexed+" File indexed, time taken: "
         +(endTime-startTime)+" ms");		
   }

   public ArrayList<String> search(String searchQuery, String textOnly) throws IOException, ParseException{
	  QueryParser parser = new QueryParser(Version.LUCENE_43,LuceneConstants.CONTENTS,this.analyzer);
	  Query q = parser.parse(searchQuery);
	  SimpleHTMLFormatter formatter;
          if (textOnly != null && textOnly.equalsIgnoreCase("true")) {
              formatter = new SimpleHTMLFormatter("","");
          } else {
              formatter = new SimpleHTMLFormatter("<mark>", "</mark>");
          }
	  QueryScorer scorer = new QueryScorer(q,LuceneConstants.CONTENTS);
	  Highlighter highlighter = new Highlighter(formatter,scorer);
	  highlighter.setTextFragmenter(new SimpleSpanFragmenter(scorer));
          ArrayList<String> results = new ArrayList<String>();
          
      searcher = new Searcher(indexDir);
      long startTime = System.currentTimeMillis();
      TopDocs hits = searcher.search(searchQuery);
      long endTime = System.currentTimeMillis();
   
      System.out.println(hits.totalHits +
         " documents found. Time :" + (endTime - startTime));
      String result = null;

      for(ScoreDoc scoreDoc : hits.scoreDocs) {
         Document doc = searcher.getDocument(scoreDoc);
         String text = doc.get(LuceneConstants.CONTENTS);
         //tokens = analyzer.tokenStream("content",new StringReader(LuceneConstants.CONTENTS));
            try {
                    result = highlighter.getBestFragment(analyzer, LuceneConstants.CONTENTS, text);
            } catch (InvalidTokenOffsetsException e) {
                    e.printStackTrace();
            }
            System.out.println("File: "
            + doc.get(LuceneConstants.FILE_PATH));
            String filePath = doc.get(LuceneConstants.FILE_PATH);
            String fileName = filePath.substring(filePath.lastIndexOf("\\")+1);
            results.add(fileName + "," + result );
      }
      searcher.close();
      results.add(hits.totalHits + " documents found. Time :" + (endTime - startTime) + " ms");
      return results;
   }
   
   // Search method that used by Servelet
   public ArrayList<String> testSearch(String searchQuery, boolean buildIndex, String textOnly) throws IOException, ParseException, SAXException, TikaException {
       if (buildIndex) createIndex();
       ArrayList<String> results = search(searchQuery, textOnly);
       System.out.print(buildIndex);
       return results;
   }
      
}