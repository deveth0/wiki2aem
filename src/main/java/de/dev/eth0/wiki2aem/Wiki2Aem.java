/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.dev.eth0.wiki2aem;

import com.github.jknack.handlebars.Context;
import com.github.jknack.handlebars.Handlebars;
import com.github.jknack.handlebars.Template;
import com.github.jknack.handlebars.context.MapValueResolver;
import io.wcm.sling.commons.util.Escape;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;

public class Wiki2Aem {

  private final String sourceFile;
  private final String targetFolder;
  private final Template pageTemplate;
  private final Template rootFolderTemplate;
  private final Map<String, Set<String>> pages = new HashMap<>();

  public Wiki2Aem(String sourceFile, String targetFolder) throws IOException {
    this.sourceFile = sourceFile;
    this.targetFolder = targetFolder;

    // ensure the target Folder exists
    File f = new File(targetFolder);
    f.mkdirs();

    Handlebars handlebars = new Handlebars();
    pageTemplate = handlebars.compile("wcmio-content-page");
    rootFolderTemplate = handlebars.compile("rootfolder");
  }

  public void convert() throws FileNotFoundException, XMLStreamException {
    InputStream in = new FileInputStream(sourceFile);
    XMLInputFactory factory = XMLInputFactory.newInstance();
    XMLStreamReader reader = factory.createXMLStreamReader(in);
    parse(reader);
    writeIndex();
  }

  private void parse(XMLStreamReader reader) throws XMLStreamException {
    Map<String, String> content = new HashMap<>();
    while (reader.hasNext()) {
      switch (reader.getEventType()) {
        case XMLStreamConstants.START_ELEMENT:
          switch (reader.getLocalName()) {
            case "page":
              content = new HashMap<>();
              break;
            case "timestamp":
              content.put("date", reader.getElementText());
              break;
            case "title":
              content.put("title", reader.getElementText());
              break;
            case "text":
              content.put("text", "<p>" + StringEscapeUtils.escapeXml(reader.getElementText()) + "</p>");
              break;
          }
          break;
        case XMLStreamConstants.END_ELEMENT:
          switch (reader.getLocalName()) {
            case "page":
              writePage(content);
              break;
          }
        default:
          break;
      }
      reader.next();
    }
  }

  private void writePage(Map<String, String> content) {
    try {
      Context context = Context
              .newBuilder(content)
              .resolver(MapValueResolver.INSTANCE)
              .build();
      String pageTitle = Escape.validName(content.get("title"));
      String key = StringUtils.substring(pageTitle, 0, 2);
      File folder = new File(targetFolder + "/" + key + "/" + pageTitle);
      folder.mkdirs();
      try (PrintWriter out = new PrintWriter(
              new OutputStreamWriter(new FileOutputStream(folder.getPath() + "/.content.xml"), StandardCharsets.UTF_8),
              true)) {
        out.print(pageTemplate.apply(context));
        if (!pages.containsKey(key)) {
          pages.put(key, new HashSet<>());
        }
        pages.get(key).add(pageTitle);
      }
    }
    catch (IOException ioe) {
      ioe.printStackTrace();
    }
  }

  /**
   * writes the index for the wiki pages. the pages are grouped in subfolders named by their first two characters (a, aa, ab,...)
   */
  private void writeIndex() {
    writeIndex(targetFolder, pages.keySet());
    pages.forEach((key, value) -> {
      writeIndex(targetFolder + "/" + key, value);
    });
  }

  private void writeIndex(String fileName, Set<String> subpages) {
    try {
      Map<String, Set<String>> m = new HashMap<>();
      m.put("pages", subpages);
      Context context = Context
              .newBuilder(m)
              .resolver(MapValueResolver.INSTANCE)
              .build();
      File folder = new File(fileName);
      folder.mkdirs();
      try (PrintWriter out = new PrintWriter(
              new OutputStreamWriter(new FileOutputStream(folder.getPath() + "/.content.xml"), StandardCharsets.UTF_8),
               true)) {
        out.print(rootFolderTemplate.apply(context));
      }
    }
    catch (IOException ioe) {
      ioe.printStackTrace();
    }
  }

}
