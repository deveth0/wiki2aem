Wiki2AEM
=======================================

This project provides a simple way to create AEM Pages from a Wikipedia XML Dump.

It uses handlebars to provide an easy extensibility of the created pages. By default [wcm.io Content Pages](http://wcm.io/samples/) are created and the wikipedia article's text is used as richtext.


## Usage

You can run the project with the following arguments:

* input filename
* output folder

## Output

After a successful run you'll find the created pages in separate folders in the given output folder. Furthermore a .content.xml for the root page is created. You can now copy this content into you content-page and install in AEM.


## Warning

This tool can create very many pages, your AEM instance should be capable to handle this amount of content.

