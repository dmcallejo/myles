package myles.servers;

import java.security.NoSuchAlgorithmException;
import myles.utils.MD5;
import myles.utils.HttpUtils;
import java.util.*;
import java.io.*;
import java.net.*;
import myles.exceptions.InvalidSessionException;


public class VagosConnection implements ServerConnection {
    private int server_id = 1;
    private String vagos_user;
    private String vagos_password;
    private String vagos_cookies;
    private String bbsh;

    /**
     * ------------------------------------------------------------
     *  M y l e s   S o u r c e   C o d e
     *  S e r v e r :   V a g o s . e s
     * ------------------------------------------------------------
     *
     * Método constructor de la clase, que asigna datos de usuario
     * @param vagos_user Usuario en vagos.es
     * @param vagos_password Password en vagos.es
     */
    public VagosConnection(String vagos_user, String vagos_password){
        this.vagos_user = vagos_user;
        this.vagos_password = vagos_password;
    }

    public boolean connect() throws MalformedURLException, IOException, NoSuchAlgorithmException{

        // Construimos objeto HTTPUrlConnection con la página de Login
        URL vagos_login_url = new URL("http://vagos.wamba.com/login.php?do=login");
        HttpURLConnection vagos_conn = (HttpURLConnection)vagos_login_url.openConnection();

        vagos_conn.setDoOutput(true);

        // Headers
        vagos_conn = HttpUtils.setHeaders("www.vagos.es", vagos_conn);
        
        // Seteamos métodos
        vagos_conn.setRequestMethod("POST");

        // Y datos Post a enviar
        String vagos_post = "vb_login_username="+vagos_user+"&cookieuser=1&do=login&vb_login_md5password=";
        vagos_post += MD5.MD5(vagos_password)+"&vb_login_md5password_utf="+MD5.MD5(vagos_password);
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

        // Veamos si ha habido éxito al loggearse. Hay éxito si el html contiene "redirige",
        // en el mensaje de "Pulsa aquí si no se te redirige automáticamente."
        boolean login_success = false;

        while ((line = vagos_conn_o.readLine()) != null) {
            login_success = login_success || line.contains("redirige");
        }
        if(!login_success) return false;

        String cookies = "";
        // Cerramos los readers
        vagos_conn_i.close();
        vagos_conn_o.close();

        // Y añadimos los headers Set-Cookie en el LinkedList
        if(login_success){
            String headerName=null;
            for (int i=1; (headerName = vagos_conn.getHeaderFieldKey(i))!=null; i++) {
                if (headerName.equals("Set-Cookie")) {
                    cookies += vagos_conn.getHeaderField(i).split(";")[0]+"; ";
                }
            }
        }

        // Asignamos el Atributo
        this.vagos_cookies = cookies;
        System.out.println(cookies);
        return true;
    }
    /**
     * Permite cerrar la sesión.
     * @return éxito.
     */
    public boolean disConnect() throws java.io.IOException, java.net.MalformedURLException{
        //Obtenemos el hash de desconexión del html de la página.
        String html = HttpUtils.getPage("http://www.vagos.es", this.vagos_cookies, "www.vagos.es");

       System.out.println(html);
       
        /*
        System.out.println(bbsh);
        // Construimos objeto HTTPUrl con la dirección del logout + bbsessionhash
        URL vagos_logout_url = new URL("http://vagos.wamba.com/login.php?do=logout&logouthash="+bbsh);
        HttpURLConnection vagos_conn = (HttpURLConnection)vagos_logout_url.openConnection();
        BufferedReader vagos_disconn_o = new BufferedReader(new InputStreamReader(vagos_conn.getInputStream()));
        String line;

        // Si el HTML no contiene la palabra cerrado, la sesión no se habrá cerrado con éxito.
        boolean logout_success = false;

        while ((line = vagos_disconn_o.readLine()) != null) {
            logout_success = logout_success || line.contains("cerrado");
        }
        if(!logout_success){
            //Debug solo en caso de error
            return false;
        }
          */

        return true;
    }

    public static void echoPageText(){
    }

    /**
     * Implementación de la búsqueda.
     * @param search_query texto a buscar.
     * @return una búsqueda Search.
     */
     // A poner luego
    /**
     * 
     * @return
     */
    public int get_server_id(){
        return this.server_id;
    }
}
