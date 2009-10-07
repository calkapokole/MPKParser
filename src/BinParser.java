import java.awt.Image;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.zip.ZipInputStream;


// TODO Rozkminic i opisac pola z danymi z plikow binarnych, zmienic nazwy zmiennyh tak,
// aby mowily jasno co zawiera dane pole 
public class BinParser {
  
  public static class Data {
    public short[] sX;
    public short[] sY;
    public short[] sX1;
    public short[] sY1;
    public short[] sXn;
    public short[] sYn;
    public short[] sW;
    public short ile_Idx;
    public short ile_L;
    public String[] sLinie;
    public short[] linIdx;
    public short[] linStart;
    public short[] linEnd;
    public int ile_IMG;
    public short[] imgX1;
    public short[] imgX2;
    public short[] imgY1;
    public short[] imgY2;
    public String[] imgN;
    public Image[] IMG;
    public short ile_gr;
    public short[] sgX;
    public short[] sgY;
    public String[] sGrafika;
    
    String sTransform(String paramString)
    {
      String str = "";
      for (int i = 0; i < paramString.length(); ++i)
      {
        char[] arrayOfChar = new char[1];
        paramString.getChars(i, i + 1, arrayOfChar, 0);
        if (arrayOfChar[0] > 128)
          if (arrayOfChar[0] == 185)
            arrayOfChar[0] = 261;
          else if (arrayOfChar[0] == 234)
            arrayOfChar[0] = 281;
          else if (arrayOfChar[0] == 156)
            arrayOfChar[0] = 347;
          else if (arrayOfChar[0] == 179)
            arrayOfChar[0] = 322;
          else if (arrayOfChar[0] == 243)
            arrayOfChar[0] = 243;
          else if (arrayOfChar[0] == 241)
            arrayOfChar[0] = 324;
          else if (arrayOfChar[0] == 191)
            arrayOfChar[0] = 380;
          else if (arrayOfChar[0] == 198)
            arrayOfChar[0] = 262;
          else if (arrayOfChar[0] == 163)
            arrayOfChar[0] = 321;
          else if (arrayOfChar[0] == 230)
            arrayOfChar[0] = 263;
          else if (arrayOfChar[0] == 211)
            arrayOfChar[0] = 211;
          else if (arrayOfChar[0] == 202)
            arrayOfChar[0] = 280;
          else if (arrayOfChar[0] == 175)
            arrayOfChar[0] = 379;
          else if (arrayOfChar[0] == 163)
            arrayOfChar[0] = 321;
          else if (arrayOfChar[0] == 209)
            arrayOfChar[0] = 323;
          else if (arrayOfChar[0] == 165)
            arrayOfChar[0] = 260;
          else if (arrayOfChar[0] == 143)
            arrayOfChar[0] = 377;
          else if (arrayOfChar[0] == 140)
            arrayOfChar[0] = 346;
          else if (arrayOfChar[0] == 159)
            arrayOfChar[0] = 378;
        str = str + new String(arrayOfChar);
      }
      return str;
    }
  }
  
  public static class Entry1 {

    public short ile_ODC;
    public int _subX;
    public int _subY;
    public int _div;
    public short minX;
    public short maxX;
    public short minY;
    public short maxY;
    public short[] ODC;
    public short[] dXi;
    public short[] dYi;
    public short[] dDi;
    public short ileLinii;
    public short[] IDXG;
    public short[] rlg;
    public short[] glg;
    public short[] blg;
  }
  
  public static class Entry2 {
    public short ile_S;
    public String[] sNazwy;
    public short divider;
  }
  
  public Data data = new Data();
  
  public Entry1 entry1 = new Entry1();
  public Entry2 entry2 = new Entry2();

  private static final String dataFilePath = "file:/home/kojot/Desktop/Internet/Mapa/dane.jar";
  
  public void load() throws IOException {
    
    URL dataUrl;
    ZipInputStream dataZipInputStream;
    
    try {
      dataUrl = new URL(dataFilePath);
      dataZipInputStream = new ZipInputStream(dataUrl.openStream());
    } catch (Exception e) {
      e.printStackTrace();
      throw new RuntimeException(e);
    }
    
    {
      dataZipInputStream.getNextEntry();
      DataInputStream dataStream1 = new DataInputStream(dataZipInputStream);
      entry1.ile_ODC = dataStream1.readShort();
      if (entry1.ile_ODC == 0)
        log(" Brak danych ");
      
      if (dataStream1.readShort() != 3)
        log(" Konflikt wersji ");
      int k = dataStream1.readShort();
      int i2 = dataStream1.readShort();
      entry1._subX = i2;
      i2 = dataStream1.readShort();
      entry1._subY = i2;
      i2 = dataStream1.readShort();
      entry1._div = i2;
      entry1.minX = dataStream1.readShort();
      entry1.maxX = dataStream1.readShort();
      entry1.minY = dataStream1.readShort();
      entry1.maxY = dataStream1.readShort();
      entry1.ODC = new short[entry1.ile_ODC];
      entry1.dXi = new short[entry1.ile_ODC];
      entry1.dYi = new short[entry1.ile_ODC];
      entry1.dDi = new short[entry1.ile_ODC];
      int i4 = 0; // napweno przed petla?
      for (; i4 < k; ++i4)
      {
        entry1.dXi[i4] = (entry1.dYi[i4] = 0);
        entry1.ODC[i4] = dataStream1.readShort();
        if (i4 != entry1.ile_ODC / 2)
          continue;
      }
      entry1.ileLinii = dataStream1.readShort();
      if (entry1.ileLinii > 0)
      {
        entry1.IDXG = new short[entry1.ileLinii];
        entry1.rlg = new short[entry1.ileLinii];
        entry1.glg = new short[entry1.ileLinii];
        entry1.blg = new short[entry1.ileLinii];
      }
      
      for (int i5 = 0; i5 < entry1.ileLinii; ++i5)
      {
        short i6 = dataStream1.readShort();
        entry1.IDXG[i5] = (short)i4;
        entry1.rlg[i5] = dataStream1.readShort();
        entry1.glg[i5] = dataStream1.readShort();
        entry1.blg[i5] = dataStream1.readShort();
        for (int i7 = 0; i7 < i6; ++i7)
        {
          entry1.dXi[i4] = (entry1.dYi[i4] = 0);
          entry1.ODC[(i4++)] = dataStream1.readShort();
        }
      }
    }
    
    {
      dataZipInputStream.getNextEntry();
      BufferedReader dataReader = new BufferedReader(new InputStreamReader(dataZipInputStream));
      entry2.ile_S = (short)Integer.parseInt(dataReader.readLine());
      entry2.sNazwy = new String[entry2.ile_S];
      for (int i = 0; i < entry2.ile_S; ++i)
        entry2.sNazwy[i] = data.sTransform(dataReader.readLine());
      String str1 = data.sTransform(dataReader.readLine());
      str1 = dataReader.readLine();
      if ((str1 != null) && (str1.length() > 0))
        entry2.divider = (short)Integer.parseInt(str1);
    }
    // TODO podzielic dalej data na poszczegolne obiekty
    {
      dataZipInputStream.getNextEntry();
      BufferedReader dataReader = new BufferedReader(new InputStreamReader(dataZipInputStream));
      data.sX = new short[entry2.ile_S];
      data.sY = new short[entry2.ile_S];
      data.sX1 = new short[entry2.ile_S];
      data.sY1 = new short[entry2.ile_S];
      data.sXn = new short[entry2.ile_S];
      data.sYn = new short[entry2.ile_S];
      data.sW = new short[entry2.ile_S];
      for (int j = 0; j < entry2.ile_S; ++j)
      {
        String str2 = dataReader.readLine();
        int i2 = 0;
        int i4 = 6;
        data.sX[j] = (short)Integer.parseInt(str2.substring(i2, i2 + i4));
        i2 += i4 + 1;
        data.sY[j] = (short)Integer.parseInt(str2.substring(i2, i2 + i4));
        i2 += i4 + 1;
        data.sX1[j] = (short)Integer.parseInt(str2.substring(i2, i2 + i4));
        i2 += i4 + 1;
        data.sY1[j] = (short)Integer.parseInt(str2.substring(i2, i2 + i4));
        i2 += i4 + 1;
        data.sXn[j] = (short)Integer.parseInt(str2.substring(i2, i2 + i4));
        i2 += i4 + 1;
        data.sYn[j] = (short)Integer.parseInt(str2.substring(i2, i2 + i4));
        i2 += i4 + 1;
        data.sW[j] = (short)Integer.parseInt(str2.substring(i2, i2 + i4));
      }
    }
    
    {
      dataZipInputStream.getNextEntry();
      DataInputStream dataStream1 = new DataInputStream(dataZipInputStream);
      data.ile_Idx = dataStream1.readShort();
      data.ile_L = dataStream1.readShort();
      data.sLinie = new String[data.ile_L];
      data.linIdx = new short[data.ile_Idx];
      data.linStart = new short[data.ile_L];
      data.linEnd = new short[data.ile_L];
      
      int j = 0;
      for (int l = 0; l < this.data.ile_L; l = (short)(l + 1))
      {
        int i2 = dataStream1.readShort();
        this.data.sLinie[l] = "";
        for (int i = 0; i < i2; ++i)
        {
          int i6 = dataStream1.readByte();
          if (i6 < 0)
            i6 += 256;
          int tmp1371_1369 = l;
          String[] tmp1371_1366 = this.data.sLinie;
          tmp1371_1366[tmp1371_1369] = tmp1371_1366[tmp1371_1369] + (char)i6;
        }
        this.data.sLinie[l] = this.data.sTransform(this.data.sLinie[l]);
        i2 = dataStream1.readShort();
        this.data.linStart[l] = (short)j;
        this.data.linEnd[l] = (short)(j + i2 - 1);
        for (int i = 0; i < i2; i = (short)(i + 1))
          this.data.linIdx[(j + i)] = dataStream1.readShort();
        j = (short)(j + i2);
      }
    }
    
    {
      dataZipInputStream.getNextEntry();
      {
        BufferedReader dataReader = new BufferedReader(new InputStreamReader(dataZipInputStream));
        data.ile_gr = (short)Integer.parseInt(dataReader.readLine());
        data.sgX = new short[data.ile_gr];
        data.sgY = new short[data.ile_gr];
        data.sGrafika = new String[data.ile_gr];
        for (int j = 0; j < data.ile_gr; ++j)
        {
          String localObject3 = data.sTransform(dataReader.readLine());
          data.sgX[j] = (short)Integer.parseInt(localObject3.substring(0, 6));
          data.sgY[j] = (short)Integer.parseInt(localObject3.substring(7, 13));
          data.sGrafika[j] = localObject3.substring(21);
        }
      }
    }
    
    {
      dataZipInputStream.getNextEntry();
      BufferedReader dataReader = new BufferedReader(new InputStreamReader(dataZipInputStream));
      data.ile_IMG = (short)Integer.parseInt(dataReader.readLine());
      String localObject2 = dataReader.readLine();
      if (localObject2 != null)
      {
        // WTF?!
        int i1 = Integer.parseInt(localObject2);
        localObject2 = dataReader.readLine();
        if (localObject2 != null)
          i1 = Integer.parseInt(localObject2);
      }
    
      // co to wogule jest? :P
      data.imgX1 = new short[data.ile_IMG];
      data.imgX2 = new short[data.ile_IMG];
      data.imgY1 = new short[data.ile_IMG];
      data.imgY2 = new short[data.ile_IMG];
      data.imgN = new String[data.ile_IMG];
      data.IMG = new Image[data.ile_IMG];
      for (int i3 = 0; i3 < data.ile_IMG; ++i3)
      {
        String str3 = dataReader.readLine();
        int i5 = 0;
        int i6 = 6;
        data.imgX1[i3] = (short)Integer.parseInt(str3.substring(i5, i5 + i6).trim());
        i5 += i6 + 1;
        data.imgY1[i3] = (short)Integer.parseInt(str3.substring(i5, i5 + i6).trim());
        i5 += i6 + 1;
        data.imgX2[i3] = (short)Integer.parseInt(str3.substring(i5, i5 + i6).trim());
        i5 += i6 + 1;
        data.imgY2[i3] = (short)Integer.parseInt(str3.substring(i5, i5 + i6).trim());
        i5 += i6 + 1;
        data.imgN[i3] = data.sTransform(str3.substring(i5));
        
        //URL localURL3 = new URL("lol:" + str3.substring(i5));
        String str4 = str3.substring(i5 - 1, i5);
        if (!(str4.equals("T")))
          throw new IllegalStateException();
          //data.IMG[i3] = data.getImage(localURL3);
        else
          data.IMG[i3] = null;
      }
    }
  }

  private static void log(String string) {
    // TODO Auto-generated method stub
    System.out.println(string);
  }

  /**
   * @param args
   * @throws IOException 
   */
  public static void main(String[] args) throws IOException {
    BinParser parser = new BinParser();
    parser.load();
    
    log("::::::::::");
    
    
  }

}
