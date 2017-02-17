/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.dev.eth0.wiki2aem;

import java.io.IOException;
import javax.xml.stream.XMLStreamException;

public class Main {

  public static void main(String[] args) throws IOException, XMLStreamException {
    if (args.length != 2) {
      printHelp();
    }
    else {
      Wiki2Aem wiki2aem = new Wiki2Aem(args[0], args[1]);
      wiki2aem.convert();
    }

  }

  private static void printHelp() {
    System.out.println("ou ou...");
  }
}
