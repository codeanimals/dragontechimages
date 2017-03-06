package tech.dragon.webimage;

/**
 * Created by djwj on 3/5/2017.
 */
public class ImgItem
{
     private String src;
     private int w;
     private int h;

     public ImgItem(String src, int w, int h)
     {
          this.src = src;
          this.w = w;
          this.h = h;
     }

     public ImgItem()
     {

     }

     public String getSrc()
     {
          return src;
     }

     public void setSrc(String src)
     {
          this.src = src;
     }

     public int getW()
     {
          return w;
     }

     public void setW(int w)
     {
          this.w = w;
     }

     public int getH()
     {
          return h;
     }

     public void setH(int h)
     {
          this.h = h;
     }
}
