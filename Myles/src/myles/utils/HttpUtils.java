package myles.utils;

import java.net.HttpURLConnection;
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
        conn.addRequestProperty("Connection", "close");
        conn.addRequestProperty("Host", host);
        return conn;
    }
}
