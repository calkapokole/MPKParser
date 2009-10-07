
import _root_.java.io.{StringReader, StringWriter}
import _root_.java.net.{URL, URI}
import _root_.scala.io.Source
import _root_.scala.xml.{XML, Node, Elem}
import _root_.org.w3c.tidy.Tidy;

object WebCrawler {
  def getXMLfromURL(url:URL): Node = {
    val source = Source.fromURL(url)
    XML.loadString(convertHTMLtoXML(source))
  }
  
  def convertHTMLtoXML(source:Source): String = {
    val in = new StringReader(source.mkString)
    val out = new StringWriter
    val tidy = new Tidy()
    //Tell Tidy to convert HTML to XML
    tidy.setXmlOut(true)
    tidy.setMakeClean(true)
    tidy.setMakeBare(true)
    tidy.setDropFontTags(true)
    tidy.setIndentContent(false)
    //tidy.setSpaces(0)
    //tidy.setTabsize(0)
    tidy.setDropEmptyParas(true)
    // convert
    tidy.parse(in, out)
    out.toString
  }
}

object XHTMLParserDemo {
  def main(args: Array[String]) = {
    val url = new URI("http://ztm.kielce.pl/pl/rozklady/0028/0028t050.htm")
    val doc = WebCrawler.getXMLfromURL(url.toURL)
    // Get interesting rows (and drop the first one)
    val rows2 = (doc \ "body" \ "table" \ "tr" \ "td" \ "table" drop 1) \ "tr"
    println(rows2)
    println(":::::::::::::::::::::\n")
    for (row <- rows2) {
      val col = (row \ "td")
      col.size match {
        case 1 => {
          // znjadz wszyskie elementy <b> (tak sie sklada ze sa to pola jakich szukamy
          val b = (col \\ "b")
          println("Naglowek: " + col.size + " | " + b.size)
          b.foreach((n:Node) => println(n.text))
        }
        case 20 => { 
          for (el <- col) {
            println("0: " + el)
            el match {
              case <td>
<b>{g}</b>
</td> => println("2:" + g)
              case <td>{c @ _*}</td> => println("1:" + c)
            }
          }
          /*
          catalog match {
            case <catalog>{therms @ _*}</catalog> =>
              for (therm @ <cctherm>{_*}</cctherm> <- therms)
                println("processing: "+
                        (therm \ "description").text)
          }*/

          println
        }
      }
    }
    //println(rows.size)
    println(rows2.size)
  }
  
  def factorial(n: Int): Int = {
    def factorialAcc(acc: Int, n: Int): Int = {
      if (n <= 1) acc
      else factorialAcc(n * acc, n - 1)
    }
    factorialAcc(1, n)
  }
}