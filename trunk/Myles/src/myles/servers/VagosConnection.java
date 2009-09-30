package myles.servers;

import java.security.NoSuchAlgorithmException;
import myles.utils.MD5;
import myles.utils.HttpUtils;
import java.util.*;
import java.io.*;
import java.net.*;
import myles.exceptions.*;

public class VagosConnection implements ServerConnection {

    private int server_id = 1;
    private String vagos_user;
    private String vagos_password;
    private String vagos_cookies;
    private boolean is_active;
    private boolean is_connected;

    /**
     * ------------------------------------------------------------
     *  M y l e s   S o u r c e   C o d e
     *  S e r v e r :   V a g o s . e s
     * ------------------------------------------------------------
     *
     * MÃ©todo constructor de la clase, que asigna datos de usuario
     * @param vagos_user Usuario en vagos.es
     * @param vagos_password Password en vagos.es
     */
    public VagosConnection(String vagos_user, String vagos_password, String act) {
        this.vagos_user = vagos_user;
        this.vagos_password = vagos_password;
        is_active = Integer.parseInt(act)==1;
    }
    public boolean is_active(){
        return is_active;
    }

    public boolean connect() throws MalformedURLException, IOException, NoSuchAlgorithmException {

        // Construimos objeto HTTPUrlConnection con la pÃ¡gina de Login
        URL vagos_login_url = new URL("http://vagos.wamba.com/login.php?do=login");
        HttpURLConnection vagos_conn = (HttpURLConnection) vagos_login_url.openConnection();

        vagos_conn.setDoOutput(true);

        // Headers
        vagos_conn = HttpUtils.setHeaders("www.vagos.es", vagos_conn);

        // Seteamos mÃ©todos
        vagos_conn.setRequestMethod("POST");

        // Y datos Post a enviar
        String vagos_post = "vb_login_username=" + vagos_user + "&cookieuser=1&do=login&vb_login_md5password=";
        vagos_post += MD5.MD5(vagos_password) + "&vb_login_md5password_utf=" + MD5.MD5(vagos_password);
        vagos_post = URLEncoder.encode(vagos_post, "UTF-8");
        vagos_post = vagos_post.replace("%3D", "=");
        vagos_post = vagos_post.replace("%26", "&");

        // Enviamos los datos Post
        OutputStreamWriter vagos_conn_i = new OutputStreamWriter(vagos_conn.getOutputStream());
        vagos_conn_i.write(vagos_post);
        vagos_conn_i.flush();

        // Y recibimos el HTML de respuesta
        BufferedReader vagos_conn_o = new BufferedReader(new InputStreamReader(vagos_conn.getInputStream()));
        String line;

        // Veamos si ha habido Ã©xito al loggearse. Hay Ã©xito si el html contiene "redirige",
        // en el mensaje de "Pulsa aquÃ­ si no se te redirige automÃ¡ticamente."
        boolean login_success = false;

        while ((line = vagos_conn_o.readLine()) != null) {
            login_success = login_success || line.contains("Gracias por iniciar");
        }
        if (!login_success) {
            return false;
        }

        String cookies = "";
        // Cerramos los readers
        vagos_conn_i.close();
        vagos_conn_o.close();

        // Y aÃ±adimos los headers Set-Cookie en el LinkedList
        if (login_success) {
            String headerName = null;
            for (int i = 1; (headerName = vagos_conn.getHeaderFieldKey(i)) != null; i++) {
                if (headerName.equals("Set-Cookie")) {
                    cookies += vagos_conn.getHeaderField(i).split(";")[0] + "; ";
                }
            }
        }

        // Asignamos el Atributo
        this.vagos_cookies = cookies;
        System.out.println(cookies);
        return true;
    }

    /**
     * Permite cerrar la sesiÃ³n.
     * @return Ã©xito.
     */
    public boolean disConnect() throws java.io.IOException, java.net.MalformedURLException, NotConnectedException {
        if (vagos_cookies==null) {
            throw new NotConnectedException();
        }
        //Obtenemos el hash de desconexiÃ³n del html de la pÃ¡gina.
        /*String html = HttpUtils.getPage("http://www.vagos.es", this.vagos_cookies, "www.vagos.es");
        String[] tHash = html.split("logouthash=",2);
        tHash = tHash[1].split("\" onclick=");
        System.out.println(tHash[0]);
        */
        // Construimos objeto HTTPUrl con la direcciÃ³n del logout + logouthash
        URL vagos_logout_url = new URL("http://vagos.wamba.com/login.php?do=logout&logouthash="+getHash());
        HttpURLConnection vagos_conn = (HttpURLConnection)vagos_logout_url.openConnection();
        BufferedReader vagos_disconn_o = new BufferedReader(new InputStreamReader(vagos_conn.getInputStream()));
        String line;

        // Si el HTML no contiene la expresiÃ³n "cookies han sido borradas", la sesiÃ³n no se habrÃ¡ cerrado con Ã©xito.
        boolean logout_success = false;

        while ((line = vagos_disconn_o.readLine()) != null) {
        logout_success = logout_success || line.contains("cookies han sido borradas");
        }
        if(!logout_success){
        //Debug solo en caso de error
        return false;
        }
        vagos_cookies=null;
        return true;
    }


    /**
     * ImplementaciÃ³n de la bÃºsqueda.
     * @param search_query texto a buscar.
     * @return una bÃºsqueda Search.
     */
    public void search(String search_query) throws MalformedURLException, IOException, NoSuchAlgorithmException {
        // Construimos objeto HTTPUrlConnection con la pÃ¡gina de Login
        URL vagos_search_url = new URL("http://www.vagos.es/search.php?do=process");
        HttpURLConnection vagos_search = (HttpURLConnection) vagos_search_url.openConnection();
        search_query=search_query.replace(' ', '+');
        System.out.println(search_query);
        vagos_search.setDoOutput(true);
        vagos_search.addRequestProperty("Cookie",vagos_cookies);
        vagos_search.addRequestProperty("Accept", "text/html");
        vagos_search.addRequestProperty("Accept-Language", "es-es,es;q=0.8,en-us;q=0.5,en;q=0.3");
        vagos_search.addRequestProperty("Accept-Encoding", "deflate");
        vagos_search.addRequestProperty("Accept-Charset", "ISO-8859-1,utf-8;q=0.7,*;q=0.7");
        vagos_search.addRequestProperty("Content-type", "application/x-www-form-urlencoded");
        vagos_search.addRequestProperty("User-Agent", "Mozilla/5.0 (Windows; U; Windows NT 5.1; es-ES; rv:1.9.1.2) Gecko/20090729 Firefox/3.5.2");
        vagos_search.addRequestProperty("Keep-Alive", "300");
        vagos_search.addRequestProperty("Connection", "keep-alive");
        vagos_search.addRequestProperty("Host", "www.vagos.es");
        // Headers
        vagos_search = HttpUtils.setHeaders("www.vagos.es", vagos_search);

        // Seteamos mÃ©todos
        vagos_search.setRequestMethod("POST");

        // Y datos Post a enviar
        String vagos_searchPost = "s=&securitytoken="+getHash()+"&do=process&searchthreadid=&query="+search_query+"&titleonly=1&searchuser=&starteronly=0&exactname=1&replyless=0&replylimit=0&searchdate=0&beforeafter=after&sortby=lastpost&order=descending&showposts=0&tag=&forumchoice%5B%5D=60&childforums=1&dosearch=Buscar+Ahora&saveprefs=1";
        vagos_searchPost = URLEncoder.encode(vagos_searchPost, "UTF-8");
        vagos_searchPost = vagos_searchPost.replace("%3D", "=");
        vagos_searchPost = vagos_searchPost.replace("%26", "&");

        // Enviamos los datos Post
        OutputStreamWriter vagos_search_i = new OutputStreamWriter(vagos_search.getOutputStream());
        vagos_search_i.write(vagos_searchPost);
        vagos_search_i.flush();

        // Y recibimos el HTML de respuesta
        BufferedReader vagos_search_o = new BufferedReader(new InputStreamReader(vagos_search.getInputStream()));
        String aux = vagos_search_o.readLine();
        while(aux != null){
            aux=vagos_search_o.readLine();
            System.out.println(aux);
        }
    }
    public String getHash(){
        //Obtenemos el hash de desconexiÃ³n del html de la pÃ¡gina.
        String html = HttpUtils.getPage("http://www.vagos.es", this.vagos_cookies, "www.vagos.es");
        String[] tHash = html.split("logouthash=",2);
        tHash = tHash[1].split("\" onclick=");
        return tHash[0];
    }
    /**
     *
     * @return
     */
    public int get_server_id() {
        return this.server_id;
    }
}
