/*
 * #%L
 * dev-eth0.de
 * %%
 * Copyright (C) 2017 dev-eth0.de
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */
package de.dev.eth0.wiki2aem;

import java.io.IOException;
import javax.xml.stream.XMLStreamException;

public class Main {

  public static void main(String[] args) throws IOException, XMLStreamException {
    if (args.length != 2 && args.length != 3) {
      printHelp();
    }
    else {
      Wiki2Aem wiki2aem = new Wiki2Aem(args[0], args[1], args.length == 3 ? Integer.parseInt(args[2]) : Integer.MAX_VALUE);
      wiki2aem.convert();
    }

  }

  private static void printHelp() {
    System.out.println("Wiki2AEM Usage:");
    System.out.println("---------------------");
    System.out.println("Parameter 1: path to wiki-dump");
    System.out.println("Parameter 2: target folder");
    System.out.println("Parameter 3: limit (optional)");
    System.out.println("Example:");
    System.out.println("java -jar YOUR_JAR_FILE wikidump.xml outputfolder");
  }
}
