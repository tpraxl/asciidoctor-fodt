@Grab('stax:stax-api:1.0.1')
@Grab('stax:stax:1.2.0')
@Grab(group='org.apache.commons', module='commons-lang3', version='3.4')
import javax.xml.stream.*
import javax.xml.stream.events.*
import org.apache.commons.lang3.StringUtils

def processStyles(is) {
	println "/This is autogenerated document - DO NOT EDIT"
	
	def depth = 0
	def reader = XMLInputFactory.newInstance()
            .createXMLStreamReader(is)

	while (reader.hasNext()) {
		if (reader.characters) {
			def str = (reader.getText().trim().length() > 0) ? reader.getText() : ""
			print str
		}

		if (reader.startElement) {
			if (depth > 0) println ""			
			def padding = StringUtils.repeat("  ", depth)
			print "${padding}${reader.getPrefix()}:${reader.name()} "
			reader.attr().each {
				print "${it.prefix}:${it.name}=\"${it.value}\" "
				}		
			depth++	
		}

		if (reader.endElement) { 
			depth--
		}	
		reader.next()
	}
}

class StaxCategory {
	static Collection attr(XMLStreamReader self) {
		def collection = []
		for (int i=0; i<self.getAttributeCount(); i++) {			
			collection.push(
				[prefix: self.getAttributePrefix(i), 
				 name: self.getAttributeLocalName(i),
				 value: self.getAttributeValue(i)
				])
		}

		return collection
	}
    static Object get(XMLStreamReader self, String key) {
        return self.getAttributeValue(null, key)
    }
    static String name(XMLStreamReader self) {
        return self.getLocalName().toString()
    }
    static String text(XMLStreamReader self) {
        return self.elementText
    }
}


def styles = new URL("https://raw.githubusercontent.com/dagwieers/asciidoc-odf/master/backends/odt/asciidoc.odt.styles").getText()
def docHeader = '''<?xml version="1.0" encoding="UTF-8"?>

<office:document
 xmlns:office="urn:oasis:names:tc:opendocument:xmlns:office:1.0"
 xmlns:style="urn:oasis:names:tc:opendocument:xmlns:style:1.0"
 xmlns:text="urn:oasis:names:tc:opendocument:xmlns:text:1.0"
 xmlns:table="urn:oasis:names:tc:opendocument:xmlns:table:1.0"
 xmlns:draw="urn:oasis:names:tc:opendocument:xmlns:drawing:1.0"
 xmlns:fo="urn:oasis:names:tc:opendocument:xmlns:xsl-fo-compatible:1.0"
 xmlns:xlink="http://www.w3.org/1999/xlink"
 xmlns:dc="http://purl.org/dc/elements/1.1/"
 xmlns:meta="urn:oasis:names:tc:opendocument:xmlns:meta:1.0"
 xmlns:number="urn:oasis:names:tc:opendocument:xmlns:datastyle:1.0"
 xmlns:svg="urn:oasis:names:tc:opendocument:xmlns:svg-compatible:1.0"
 xmlns:chart="urn:oasis:names:tc:opendocument:xmlns:chart:1.0"
 xmlns:dr3d="urn:oasis:names:tc:opendocument:xmlns:dr3d:1.0"
 xmlns:math="http://www.w3.org/1998/Math/MathML"
 xmlns:form="urn:oasis:names:tc:opendocument:xmlns:form:1.0"
 xmlns:script="urn:oasis:names:tc:opendocument:xmlns:script:1.0"
 xmlns:config="urn:oasis:names:tc:opendocument:xmlns:config:1.0"
 xmlns:ooo="http://openoffice.org/2004/office"
 xmlns:ooow="http://openoffice.org/2004/writer"
 xmlns:oooc="http://openoffice.org/2004/calc"
 xmlns:dom="http://www.w3.org/2001/xml-events"
 xmlns:xforms="http://www.w3.org/2002/xforms"
 xmlns:xsd="http://www.w3.org/2001/XMLSchema"
 xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
 xmlns:rpt="http://openoffice.org/2005/report"
 xmlns:of="urn:oasis:names:tc:opendocument:xmlns:of:1.2"
 xmlns:xhtml="http://www.w3.org/1999/xhtml"
 xmlns:grddl="http://www.w3.org/2003/g/data-view#"
 xmlns:officeooo="http://openoffice.org/2009/office"
 xmlns:tableooo="http://openoffice.org/2009/table"
 xmlns:drawooo="http://openoffice.org/2010/draw"
 xmlns:calcext="urn:org:documentfoundation:names:experimental:calc:xmlns:calcext:1.0"
 xmlns:loext="urn:org:documentfoundation:names:experimental:office:xmlns:loext:1.0"
 xmlns:field="urn:openoffice:names:experimental:ooo-ms-interop:xmlns:field:1.0"
 xmlns:formx="urn:openoffice:names:experimental:ooxml-odf-interop:xmlns:form:1.0"
 xmlns:css3t="http://www.w3.org/TR/css3-text/"
 office:version="1.2">''' 	
def xmlStyles = docHeader + styles + "</office:document>"
def inputStream = new ByteArrayInputStream(xmlStyles.bytes)

use(StaxCategory) { processStyles(inputStream) }