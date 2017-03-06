package tech.dragon.webimage;

import jdk.nashorn.internal.runtime.JSONFunctions;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

/**
 * Created by djwj on 2/25/2017.
 */
public class ImageDirectoryScanner
{
     final String webAbs = System.getenv("WEB_DIR_ABSOLUTE_PATH");

     public String getItemsArray(String imageDir)
     {
          Path absoluteImageDir = Paths.get(webAbs + "/" + imageDir);

          StringBuilder jsImgArrString = new StringBuilder();

          int x = 0;

          try (DirectoryStream<Path> stream = Files.newDirectoryStream(absoluteImageDir))
          {
               for (Path entry : stream)
               {
                    /*
               JS items array for Photoswipe looks like:

               var items =
               [
                   {
                       src: 'pics/notacrook.jpg',
                       w: 800,
                       h: 450
                   },
                   {
                       src: 'pics/dropmike.jpg',
                       w: 400,
                       h: 294
                   }
               ];
                */
                    File image = entry.toFile();


                    try
                    {
                         BufferedImage img = ImageIO.read(image);
                         int w = img.getWidth();
                         int h = img.getHeight();
                         jsImgArrString.append("{ src: '" + imageDir + "/" + image.getName() + "',");
                         jsImgArrString.append("w: " + w + ",");
                         jsImgArrString.append("h: " + h);
                         jsImgArrString.append("},");

                    }
                    catch (IOException e)
                    {
                         e.printStackTrace();
                    }
               }
          } catch (IOException e)
          {
               e.printStackTrace();
          }

          return jsImgArrString.toString();
     }


     public HashMap<String, ImgItem> getImgItems(String imageDir)
     {
          HashMap<String, ImgItem> images = new HashMap<>();

          Path absoluteImageDir = Paths.get(webAbs + "/" + imageDir);

          StringBuilder jsImgArrString = new StringBuilder();

          int x = 0;

          try (DirectoryStream<Path> stream = Files.newDirectoryStream(absoluteImageDir))
          {
               for (Path entry : stream)
               {
                    /*
               JS items array for Photoswipe looks like:

               var items =
               [
                   {
                       src: 'pics/notacrook.jpg',
                       w: 800,
                       h: 450
                   },
                   {
                       src: 'pics/dropmike.jpg',
                       w: 400,
                       h: 294
                   }
               ];
                */
                    File image = entry.toFile();


                    try
                    {
                         BufferedImage img = ImageIO.read(image);
                         int w = img.getWidth();
                         int h = img.getHeight();
                         String src = imageDir + "/" + image.getName();
                         ImgItem imgItem = new ImgItem(src, w, h);
                         images.put(src, imgItem);


                    }
                    catch (IOException e)
                    {
                         e.printStackTrace();
                    }
               }
          } catch (IOException e)
          {
               e.printStackTrace();
          }

          return images;
     }

     /*
          The idea here is that we are going to compare what the client has
          with what is in the image directory.  We will then return a simple JS array
          that contains any new items.
      */
     public String updateItemsArray(String s, String imgDir)
     {

          List<String> itemsIn = Arrays.asList(s.split("},"));

          HashMap<String, ImgItem> clientsImgs = new HashMap<>();

          // really should have done this with JSON but oh well
          for(String item : itemsIn)
          {
               item = item.replace("{","");
               item = item.trim();
               String[] parts = item.split(",");
               ImgItem it = new ImgItem();
               for(String p : parts)
               {
                    String[] kvp = p.split(":");
                    String thisVal = kvp[1];
                    thisVal = thisVal.trim();
                    thisVal = thisVal.replace("\"","");
                    String thisKey = kvp[0];
                    thisKey = thisKey.trim();

                    thisKey = thisKey.replace("&quot;","");
                    thisKey = thisKey.replace("(","");
                    thisKey = thisKey.replace("\"","");
                    thisKey = thisKey.replace(")","");



                    if(thisKey.equalsIgnoreCase("src"))
                    {
                         it.setSrc(thisVal);
                    }
                    else if(thisKey.equalsIgnoreCase("w"))
                    {
                         thisVal = thisVal.replaceAll("[^0-9]", "");
                         it.setW(Integer.parseInt(thisVal));
                    }
                    else if(thisKey.equalsIgnoreCase("h"))
                    {
                         thisVal = thisVal.replaceAll("[^0-9]", "");
                         it.setH(Integer.parseInt(thisVal));
                    }


               }
               if(it.getSrc() != null)
               {
                    clientsImgs.put(it.getSrc(), it);
               }
          }

          StringBuilder newImgs = new StringBuilder("[");

          // now we have a map of what the client already had, and we can check the disk
          // to see if we need to add anything.

          // TODO: we should have some kind of flag for deleted images since we don't want to push the whole array back and have client reload from scratch

          HashMap<String, ImgItem> diskImgs = getImgItems(imgDir);

          for(String diskSrc : diskImgs.keySet())
          {
               if(!clientsImgs.containsKey(diskSrc))
               {
                    ImgItem newImg = diskImgs.get(diskSrc);
                   newImgs.append("{\"src\":\"" + newImg.getSrc() + "\",\"w\":"+newImg.getW()+",\"h\":"+newImg.getH()+"},");
               }
          }


          newImgs.append("]");

          return newImgs.toString();
     }
}
