import java.io.IOException;
import java.io.StringReader;
<<<<<<< HEAD
import java.util.ArrayList;
import java.util.HashMap;
=======
>>>>>>> 723cfeb200eff43d308af2dee9cd3a39d3c97d88

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
	
<<<<<<< HEAD
   //String indexDir = "./src/java/index";
   String indexDir = "C:\\Users\\lorraine\\Downloads\\SmartSearchEngine-master\\SmartSearchEngine-master\\src\\java\\index";
   int k = indexDir.hashCode();
   //String dataDir = "./src/java/data";
   String dataDir = "C:\\Users\\lorraine\\Downloads\\SmartSearchEngine-master\\SmartSearchEngine-master\\src\\java\\data";
=======
   String indexDir = "./index";
   int k = indexDir.hashCode();
   String dataDir = "./data";
>>>>>>> 723cfeb200eff43d308af2dee9cd3a39d3c97d88
   Indexer indexer;
   Searcher searcher;
   Analyzer analyzer;

   public static void main(String[] args) throws SAXException, TikaException {
      LuceneTester tester;
      try {
         tester = new LuceneTester();
         tester.createIndex();
<<<<<<< HEAD
         tester.search("search engine");
=======
         tester.search("SmartFusion2");
>>>>>>> 723cfeb200eff43d308af2dee9cd3a39d3c97d88
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

<<<<<<< HEAD
   public ArrayList<String> search(String searchQuery) throws IOException, ParseException{
	  QueryParser parser = new QueryParser(Version.LUCENE_43,LuceneConstants.CONTENTS,this.analyzer);
	  Query q = parser.parse(searchQuery);
	  SimpleHTMLFormatter formatter = new SimpleHTMLFormatter("<mark>","</mark>");
	  QueryScorer scorer = new QueryScorer(q,LuceneConstants.CONTENTS);
	  Highlighter highlighter = new Highlighter(formatter,scorer);
	  highlighter.setTextFragmenter(new SimpleSpanFragmenter(scorer));
          ArrayList<String> results = new ArrayList<String>();
          
=======
   private void search(String searchQuery) throws IOException, ParseException{
	  QueryParser parser = new QueryParser(Version.LUCENE_43,LuceneConstants.CONTENTS,this.analyzer);
	  Query q = parser.parse(searchQuery);
	  SimpleHTMLFormatter formatter =
				new SimpleHTMLFormatter("<mark>","</mark>");
	  QueryScorer scorer = new QueryScorer(q,LuceneConstants.CONTENTS);
	  Highlighter highlighter = new Highlighter(formatter,scorer);
	  highlighter.setTextFragmenter(new SimpleSpanFragmenter(scorer));
>>>>>>> 723cfeb200eff43d308af2dee9cd3a39d3c97d88
      searcher = new Searcher(indexDir);
      long startTime = System.currentTimeMillis();
      TopDocs hits = searcher.search(searchQuery);
      long endTime = System.currentTimeMillis();
   
      System.out.println(hits.totalHits +
         " documents found. Time :" + (endTime - startTime));
<<<<<<< HEAD
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
            //System.out.println(result);
            results.add(fileName + "," + result);
      }
      searcher.close();
      return results;
   }
   
   public ArrayList<String> testSearch(String searchQuery) throws IOException, ParseException, SAXException, TikaException {
       createIndex();
       ArrayList<String> results = search(searchQuery);
       return results;
   }
      
=======
      //TokenStream tokens = null;
      String result = null;
      
      for(ScoreDoc scoreDoc : hits.scoreDocs) {
         Document doc = searcher.getDocument(scoreDoc);
         String text = doc.get(LuceneConstants.CONTENTS);
         //System.out.println(text.substring(0, 100));
         //tokens = analyzer.tokenStream("content",new StringReader(LuceneConstants.CONTENTS));
			try {
				result = highlighter.getBestFragment(analyzer, LuceneConstants.CONTENTS, text);
			} catch (InvalidTokenOffsetsException e) {
				e.printStackTrace();
			}
            System.out.println("File: "
            + doc.get(LuceneConstants.FILE_PATH));
            System.out.println(result);
      }
      searcher.close();
   }
>>>>>>> 723cfeb200eff43d308af2dee9cd3a39d3c97d88
}