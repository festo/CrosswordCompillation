import java.awt.Desktop;
import java.io.*;
import java.sql.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.xhtmlrenderer.pdf.ITextRenderer;
import org.xhtmlrenderer.resource.FSEntityResolver;

import com.lowagie.text.DocumentException;


public class PDF {
	public PDF(Grid g) throws IOException, DocumentException, Exception {
		String outputFileName = generateFilename();
		
		StringBuffer buf = new StringBuffer();
		buf.append("<?xml version='1.0' encoding='UTF-8'?>");
		buf.append("<!DOCTYPE html PUBLIC '-//W3C//DTD XHTML 1.0 Strict//EN' 'http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd'>");
		buf.append("<html xmlns='http://www.w3.org/1999/xhtml'>");
		buf.append("    <head>");
		buf.append("		<style language='text/css'>");
		buf.append("table {  border: 0;}#grid-container #table {  border: 1px solid black;  border-bottom: 0;  border-right: 0;  margin: 0 auto;}#grid-container #table td {  width: 30px !important;  height: 30px !important;  text-align: left;  vertical-align: top;  font-size: 10px;  padding-left: 2px;  padding-top: 2px;  border-right: 1px solid black;  border-bottom: 1px solid black;}#grid-container #table .blank {  background-color: black;}#clues {  padding-top: 50px;}#clues #clues-table {  max-width: 700px;  margin: 0 auto;}#clues #clues-table th {  min-width: 200px;}#clues #clues-table td {  padding-left: 10px;}#clues #clues-table .column {  max-width: 300px;}");
		buf.append("		</style>");
		buf.append("	</head>");
		buf.append("	<body>");
		buf.append("		<div id='grid-container'>");
		buf.append("			<table id='table' cellpadding='0' cellspacing='0'>");
		buf.append(g.toHTML());
		buf.append("</table></div>");
		buf.append("<div id='clues'><table id='clues-table'><tr><th>Fuggõleges</th><th>Vízszintes</th></tr><tr><td>");
		buf.append(g.getVertivalHTML());
		buf.append("</td><td>");
		buf.append(g.getHorisontalHTML());
		buf.append("</td></tr></table>");
		buf.append("</div></body></html>");
		
		System.setProperty("java.net.useSystemProxies", "true");
				     		
        DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        builder.setEntityResolver(FSEntityResolver.instance());
        Document doc = builder.parse(new ByteArrayInputStream(buf.toString().getBytes("UTF-8")));

        ITextRenderer renderer = new ITextRenderer();
        renderer.setDocument(doc, null);
        
        OutputStream os = new FileOutputStream(outputFileName);
        renderer.layout();
        renderer.createPDF(os);
        os.close();
        
        if (Desktop.isDesktopSupported()) {
          try {
              File myFile = new File(outputFileName);
              Desktop.getDesktop().open(myFile);
          } catch (IOException ex) {
              // no application registered for PDFs
          }
      }
        
    }
	
	private String generateFilename() {
		Date date = new Date(System.currentTimeMillis());
		DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
		String dateFormatted = formatter.format(date);
		return "saved/"+dateFormatted+".pdf";
	}
}
