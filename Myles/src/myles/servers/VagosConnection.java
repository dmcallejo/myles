package myles.servers;

import myles.utils.MD5;
import myles.utils.HttpUtils;
import java.util.*;
import java.io.*;
import java.net.*;


public class VagosConnection implements ServerConnection {
    private int server_id = 1;
    private String vagos_user;
    private String vagos_password;
    private LinkedList<String> vagos_cookies;
    private String bbsessionhash;   //Almacena el hash de la sesion para poder cerrarla.

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

    public boolean Connect() throws Exception{

        // Obtenemos la Session Id de vBulletin
        String PHPSESSID = VagosConnection.GetPhpSessID();

        // Construimos objeto HTTPUrl...
        URL vagos_login_url = new URL("http://vagos.wamba.com/login.php?do=login");

        HttpURLConnection vagos_conn = (HttpURLConnection)vagos_login_url.openConnection();

        vagos_conn.setDoOutput(true);

        // Headers
        vagos_conn = HttpUtils.setHeaders("vagos.wamba.com", vagos_conn);
        vagos_conn.setRequestProperty("Cookie", "PHPSESSID="+PHPSESSID);
        
        // Seteamos métodos
        vagos_conn.setRequestMethod("POST");

        // Y datos Post a enviar
        String vagos_post = URLEncoder.encode("vb_login_username", "UTF-8") + "=" + URLEncoder.encode(vagos_user, "UTF-8");
        vagos_post += "&" + URLEncoder.encode("cookieuser", "UTF-8") + "=" + URLEncoder.encode("1", "UTF-8");
        vagos_post += "&" + URLEncoder.encode("vb_login_password", "UTF-8") + "=" + URLEncoder.encode("", "UTF-8");
        vagos_post += "&" + URLEncoder.encode("do", "UTF-8") + "=" + URLEncoder.encode("login", "UTF-8");
        String vagos_md5pwd = MD5.MD5(vagos_password);
        vagos_post += "&" + URLEncoder.encode("vb_login_md5password", "UTF-8") + "=" + URLEncoder.encode(vagos_md5pwd, "UTF-8");
        vagos_post += "&" + URLEncoder.encode("vb_login_md5password_utf", "UTF-8") + "=" + URLEncoder.encode(vagos_md5pwd, "UTF-8");

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

        LinkedList<String> cookies = new LinkedList<String>();
        // Cerramos los readers
        vagos_conn_i.close();
        vagos_conn_o.close();

        // Y añadimos los headers Set-Cookie en el LinkedList
        if(login_success){
            String headerName=null;
            for (int i=1; (headerName = vagos_conn.getHeaderFieldKey(i))!=null; i++) {
                if (headerName.equals("Set-Cookie")) {
                cookies.add(vagos_conn.getHeaderField(i));
                }
            }
        }

        // Asignamos el Atributo
        this.vagos_cookies = cookies;

        // Debug
        System.out.println("Debugging:");
        Iterator<String> it = cookies.iterator();
        String str=null;            //Temporary String for next while.
        while(it.hasNext()){
            str=it.next();
            System.out.println(str);
            if (str.startsWith("bbsessionhash=")){
                this.bbsessionhash=str.substring(14,46);
                System.out.println("Session Hash = "+this.bbsessionhash);
            }
        }

        return true;
    }
    /**
     * Permite cerrar la sesión.
     * @return éxito.
     */
    public boolean disConnect() throws java.io.IOException, java.net.MalformedURLException{
        // Construimos objeto HTTPUrl con la dirección del logout + bbsessionhash
        URL vagos_logout_url = new URL("http://vagos.wamba.com/login.php?do=logout&logouthash="+this.bbsessionhash);
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
            System.out.println(vagos_logout_url);
            return false;
        }
        System.out.println("Sesión cerrada");
        return true;
    }

    /**
     * Función que se conecta y consigue el Session ID de vBulletin
     * @return Session id de vBulletin.
     * @throws java.net.MalformedURLException
     * @throws java.io.IOException
     */
    public static String GetPhpSessID() throws java.net.MalformedURLException, java.io.IOException {

        // Construimos el objeto HTTPUrl...
        URL vagos_login_url = new URL("http://vagos.wamba.com/index.php");
        HttpURLConnection vagos_conn = (HttpURLConnection)vagos_login_url.openConnection();
        vagos_conn.setDoOutput(true);
        // Headers
        vagos_conn.addRequestProperty("Host", "vagos.wamba.com");
        vagos_conn.addRequestProperty("Accept", "text/html");
        vagos_conn.addRequestProperty("Accept-Language", "es-es,es;q=0.8,en-us;q=0.5,en;q=0.3");
        vagos_conn.addRequestProperty("Accept-Encoding", "deflate");
        vagos_conn.addRequestProperty("Accept-Charset", "ISO-8859-1,utf-8;q=0.7,*;q=0.7");
        vagos_conn.addRequestProperty("Content-type", "application/x-www-form-urlencoded");
        vagos_conn.addRequestProperty("User-Agent", "Mozilla/5.0 (Windows; U; Windows NT 5.1; es-ES; rv:1.9.1.2) Gecko/20090729 Firefox/3.5.2");
        vagos_conn.addRequestProperty("Connection", "close");

        // Seteamos método
        vagos_conn.setRequestMethod("GET");
        System.out.println("\tGET");
        Map<String,List<String>> vagos_headers = vagos_conn.getHeaderFields();
        Collection<List<String>> vagos_l_headers = vagos_headers.values();
        Set<String> vagos_keys = vagos_headers.keySet();
        String[] keys = new String[vagos_keys.size()];
        int i = 0;
        String[] values = new String[keys.length];
        for (Iterator it=vagos_keys.iterator(); it.hasNext ();){
            Object o = it.next();
            if(o != null){ keys[i] = o.toString(); }
            i++;

        }
        i = 0;
        for (Iterator it=vagos_l_headers.iterator(); it.hasNext( )==true; ) {
            Object anObject = it.next();
            values[i] = anObject.toString();
            i++;
        }
        System.out.println("\tCookies");
        String[] cookies_rdy = null;
        for(i = 0; i < keys.length; i++){
            System.out.println("\tfor "+i+" = "+keys[i]);
            if(keys[i] != null){
                if(keys[i].startsWith("Set-Cookie")){
                    String cookies_raw = values[i];
                    cookies_raw = cookies_raw.replace("[", "");
                    cookies_raw = cookies_raw.replace("]", "");
                    cookies_raw = cookies_raw.split("PHPSESSID=")[1];
                    return cookies_raw.split(";")[0];
                }
            }
        }

        return null;
        // Tenemos las claves en keys[] y los valores en value[]
    }
    public static void EchoPageText(){
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
