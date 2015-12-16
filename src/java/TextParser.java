/*
 * This class is used to convert pdf and txt files
 * to lucene documents
 */

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.io.StringReader;
import java.util.*;

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
	/*
	static Parser parser;
	static ContentHandler handler = new BodyContentHandler(-1);
	static Metadata md;*/
	//private ContenHandler handler = new ContentHandler(-1);
	//Metadata to be extract
	public static Set<String> metadataFields = new HashSet<String>();
	/*
		Collections.unmodifiableSet(Metadata.RESOURCE_NAME_KEY,
				Metadata.AUTHOR,Metadata.COMMENTS,
				Metadata.KEYWORDS,Metadata.TITLE,
				Metadata.LAST_MODIFIED.getName(),
				Metadata.LAST_SAVED.getName());

	*/
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
	public String parse(File file) throws IOException, SAXException, TikaException {
		Metadata md = new Metadata();
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
	/*
	public String getText()
	{
		return handler.toString();
	}
	
	//return the text stream
	public Reader getReader()
	{
		return new StringReader(handler.toString());
	}*/

}