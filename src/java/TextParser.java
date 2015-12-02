/*
 * This class is used to convert pdf, word, and txt files
 * to lucene documents
 */

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.StringReader;
import java.net.URL;
import java.util.*;

import org.apache.tika.detect.DefaultDetector;
import org.apache.tika.detect.Detector;
import org.apache.tika.exception.TikaException;
import org.apache.tika.io.TikaInputStream;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.pdf.PDFParser;
import org.apache.tika.parser.Parser;
import org.apache.tika.sax.BodyContentHandler;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;

public class TextParser {
	public static Set<String> metadataFields = new HashSet<String>();
	
	@SuppressWarnings("deprecation")
	public TextParser() {
		metadataFields.add(Metadata.RESOURCE_NAME_KEY);
		metadataFields.add(Metadata.COMMENTS);
		metadataFields.add(Metadata.AUTHOR);
		metadataFields.add(Metadata.KEYWORDS);
		metadataFields.add(Metadata.TITLE);
		metadataFields.add(Metadata.LAST_MODIFIED.getName());
		metadataFields.add(Metadata.LAST_SAVED.getName());
	}
	
	public String parsePDF(File file) throws IOException, SAXException, TikaException {
		Metadata md = new Metadata();
		md.add(Metadata.CONTENT_ENCODING, "UTF-8");
		ContentHandler handler = new BodyContentHandler(-1);
		PDFParser p = new PDFParser();
		//TikaInputStream in = TikaInputStream.get(file,md);
		InputStream in = new FileInputStream(file);
		md.add(Metadata.RESOURCE_NAME_KEY, file.getName());
		p.parse(in, handler, md, null);
		//System.out.println(handler.toString());
		in.close();
		//filter the useless metadata
		for(String name:md.names())
			if(!metadataFields.contains(name))
				md.remove(name);
		return handler.toString();
	}
	
	public String parseWord(File file) throws IOException, SAXException, TikaException {
		ParseContext context = new ParseContext();
        Detector detector = new DefaultDetector();
        Parser parser = new AutoDetectParser(detector);
        context.set(Parser.class, parser);
        OutputStream outputstream = new ByteArrayOutputStream();
		Metadata md = new Metadata();
		md.add(Metadata.CONTENT_ENCODING, "UTF-8");
		//ContentHandler handler = new BodyContentHandler(-1);
		URL url;
        //File file = new File(filename);
        if (file.isFile()) {
            url = file.toURI().toURL();
        } else {
            url = new URL(file.getName());
        }
        InputStream input = TikaInputStream.get(url, md);
        ContentHandler handler = new BodyContentHandler(outputstream);
        parser.parse(input, handler, md, context); 
        input.close();
        return outputstream.toString();
	}
}