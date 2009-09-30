package myles.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Iterator;
import java.util.LinkedList;
import java.net.*;
import java.util.logging.Level;
import java.util.logging.Logger;
/**
 *
 * @author User
 */
public class HttpUtils {
    public static HttpURLConnection setHeaders(String host, HttpURLConnection conn){
        conn.addRequestProperty("Accept", "text/html");
        conn.addRequestProperty("Accept-Language", "es-es,es;q=0.8,en-us;q=0.5,en;q=0.3");
        conn.addRequestProperty("Accept-Encoding", "deflate");
        conn.addRequestProperty("Accept-Charset", "ISO-8859-1,utf-8;q=0.7,*;q=0.7");
        conn.addRequestProperty("Content-type", "application/x-www-form-urlencoded");
        conn.addRequestProperty("User-Agent", "Mozilla/5.0 (Windows; U; Windows NT 5.1; es-ES; rv:1.9.1.2) Gecko/20090729 Firefox/3.5.2");
        conn.addRequestProperty("Connection", "keep-alive");
        conn.addRequestProperty("Host", host);
        return conn;
    }
    public static String getPage(String desiredURL, String cookies, String host){
        try {
            URL vagos_login_url = new URL(desiredURL);
            try {
                HttpURLConnection NetObj = (HttpURLConnection) vagos_login_url.openConnection();
                NetObj.setDoOutput(true);
                NetObj = HttpUtils.setHeaders(host, NetObj);
                NetObj.setRequestMethod("POST");
                if(cookies != null){
                        NetObj.addRequestProperty("Cookie", cookies);
                }
                BufferedReader page_o = new BufferedReader(new InputStreamReader(NetObj.getInputStream()));
                String page_html = "";
                String aux;
                while(true){
                    aux = page_o.readLine();
                    if(aux!=null){
                        page_html += page_o.readLine()+"\n";
                    }else{
                        break;
                    }
            }
                return page_html;

            } catch (IOException ex) {
                Logger.getLogger(HttpUtils.class.getName()).log(Level.SEVERE, null, ex);
                return "";
            }


        } catch (MalformedURLException ex) {
            Logger.getLogger(HttpUtils.class.getName()).log(Level.SEVERE, null, ex);
            return "";
        }
    }

}
