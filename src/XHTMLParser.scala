
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
    tidy.setSpaces(0)
    tidy.setTabsize(0)
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
          // Regular Expresions Spaghetti
          val minute = """^(\d+)[\s]*""".r
          val doubleMinute = """^(\d+)[\s]+(\d+)[\s]+""".r
          val tripleMinute = """^(\d+)[\s]+(\d+)[\s]+(\d+)[\s]+""".r
          val xChar = """^x""".r
          val hour = """^[\s]+(\d+)[\s]+""".r
          val rowTitle = """^[\s]+([a-zA-Z\.]+)[\s]+""".r
          for (el <- col) {
            //println("0: " + el)
            /*println("1: " + el.text)
            for (c:Char <- (el.text)) {
              printf("|%H|", c.toInt)
            }
            println*/
            el.text match {
              case minute(d)                => printf(" %d |", d.toInt)
              case doubleMinute(d1, d2)     => printf(" %d,%d |", d1.toInt, d2.toInt)
              case tripleMinute(d1, d2, d3) => printf("%d,%d,%d |", d1.toInt, d2.toInt, d3.toInt)
              case hour(d)                  => printf(" %d |", d.toInt)
              case rowTitle(s)              => printf("   %s |", s)
              case xChar                    => printf(" x |")
            }
          }
          println
        }
      }
    }
    //println(rows.size)
    println(rows2.size)
  }
  
  // przykladowa funkcja z tail-recursion
  def factorial(n: Int): Int = {
    def factorialAcc(acc: Int, n: Int): Int = {
      if (n <= 1) acc
      else factorialAcc(n * acc, n - 1)
    }
    factorialAcc(1, n)
  }
}