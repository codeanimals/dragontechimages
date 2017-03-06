package tech.dragon.webimage;

import org.json.HTTP;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import javax.json.stream.JsonParser;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringReader;
import java.util.Iterator;
import java.util.Map;
import javax.json.Json;
import javax.json.JsonReader;
import javax.json.JsonStructure;

/**
 * Created by djwj on 3/4/2017.
 *
 * @author djwj
 *         <p>
 *         You need to make sure to add a global library to your local Tomcat
 *         or provide for the import of Servlet dependencies in some other way.
 *         You should NOT include these in your jar, since they will be
 *         available in your classpath via your Servlet Container (Tomcat etc.)
 *         once you deploy.
 */
public class ImageItemObjectUpdater extends HttpServlet
{
     public void doGet(HttpServletRequest request, HttpServletResponse response)
               throws ServletException, IOException
     {
          PrintWriter out = response.getWriter();
          out.println("POST only please.");
     }

     public void doPost(HttpServletRequest request, HttpServletResponse response)
               throws ServletException, IOException
     {

          PrintWriter out = response.getWriter();
          BufferedReader reader = request.getReader();
          StringBuilder sb = new StringBuilder();

          String line;
          while ((line = reader.readLine()) != null)
          {
               sb.append(line);
          }


          ImageDirectoryScanner ids = new ImageDirectoryScanner();
          String updatedImgArray = ids.updateItemsArray(sb.toString(), "/pics");
          //      out.println(imgArrayString);

          response.setContentType("text/plain");
          out.print(updatedImgArray);
     }

}
