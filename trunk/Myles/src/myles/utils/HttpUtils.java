package myles.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.awt.Desktop;
/**
 *
 * @author User
 */
public class HttpUtils {

    public static HttpURLConnection setHeaders(String host, HttpURLConnection conn) {
        conn.addRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
        conn.addRequestProperty("Accept-Language", "es-es,es;q=0.8,en-us;q=0.5,en;q=0.3");
        conn.addRequestProperty("Accept-Charset", "ISO-8859-1,utf-8;q=0.7,*;q=0.7");
        conn.addRequestProperty("Content-type", "application/x-www-form-urlencoded");
        conn.addRequestProperty("User-Agent", "Mozilla/5.0 (X11; U; Linux i686; es-ES; rv:1.9.0.14) Gecko/2009090216 Ubuntu/9.04 (jaunty) Firefox/3.0.14");
        conn.addRequestProperty("Connection", "keep-alive");
        conn.addRequestProperty("Keep-Alive", "300");
        conn.addRequestProperty("Host", host);
        return conn;
    }
    /**
     * Retorne al código fuente de un html pedido por la url.
     *
     * @param desiredURL
     * @param cookies   Cookie (si se desea enviar).
     * @param host
     * @return
     */
    public static String getPage(String desiredURL, String cookies, String host) {
        try {
            URL vagos_login_url = new URL(desiredURL);
            try {
                HttpURLConnection NetObj = (HttpURLConnection) vagos_login_url.openConnection();
                NetObj.setDoOutput(true);
                NetObj = HttpUtils.setHeaders(host, NetObj);
                NetObj.setRequestMethod("POST");
                if (cookies != null) {
                    NetObj.addRequestProperty("Cookie", cookies);
                }
                BufferedReader page_o = new BufferedReader(new InputStreamReader(NetObj.getInputStream()));
                String page_html = "";
                String aux;
                while (true) {
                    aux = page_o.readLine();
                    if (aux != null) {
                        page_html += page_o.readLine() + "\n";
                    } else {
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

    /**
     * Permite abrir una url en el navegador predeterminado de la maquina,
     * independientemente del Sistema Operativo.
     *
     * @param url
     * @throws java.io.IOException Lanzada si ocurren problemas de comunicación.
     *                              También si la URL no tiene "http://"
     * @throws java.net.URISyntaxException  URL incorrecta.
     */
    public static void openBrowser(String url) throws java.io.IOException, java.net.URISyntaxException {
        try {
            Desktop.getDesktop().browse(new URI(url));
        } catch (java.io.IOException e) {
            System.out.println("Error de comunicación");
        } catch (java.net.URISyntaxException e) {
            System.out.println("URL erronea");
        }
    }
}
