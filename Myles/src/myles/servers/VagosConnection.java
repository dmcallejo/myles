package myles.servers;

import java.security.NoSuchAlgorithmException;
import java.util.logging.Level;
import java.util.logging.Logger;
import myles.utils.HttpUtils;
import java.util.*;
import java.io.*;
import java.net.*;
import myles.exceptions.*;
import myles.search.*;
import org.jdom.Element;

public class VagosConnection implements ServerConnection {

    private int server_id = 1;
    private String vagos_user;
    private String vagos_password;
    private String vagos_cookies;
    private boolean is_active;
    private boolean is_connected;
    private ServerInfo server_info;

    /**
     * ------------------------------------------------------------
     *  M y l e s   S o u r c e   C o d e
     *  S e r v e r :   V a g o s . e s
     * ------------------------------------------------------------
     *
     * MÃ©todo constructor de la clase (DEBUG), que asigna datos de usuario
     * @param vagos_user Usuario en vagos.es
     * @param vagos_password Password en vagos.es
     */
    public VagosConnection(String vagos_user, String vagos_password, String act) {
        this.vagos_user = vagos_user;
        this.vagos_password = vagos_password;
        is_active = Integer.parseInt(act) == 1;
    }

    /**
     * Metodo constructor de la clase (OFICIAL), que contiene un atributo objeto
     * serverInfo con toda la info del servidor.
     */
    public VagosConnection(Element data) {
        this.vagos_user = data.getChildText("user");
        this.vagos_password = data.getChildText("encpass");
        if (Integer.parseInt(data.getChildText("active")) == 1) {
            this.enable();
        } else {
            this.disable();
        }
        data.removeChild("active");
        data.removeChild("user");
        data.removeChild("encpass");
        this.server_info = new ServerInfo(data);
    }

    @Override
    public boolean is_connected() {
        return is_connected;
    }

    @Override
    public boolean is_active() {
        return is_active;
    }

    public void enable() {
        is_active = true;
    }

    public void disable() {
        is_active = false;
    }

    public void toggle() {
        is_active = !is_active;
    }

    @Override
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
        vagos_post += vagos_password + "&vb_login_md5password_utf=" + vagos_password;
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
        this.is_connected = true;
        return true;
    }

    /**
     * Permite cerrar la sesiÃ³n.
     * @return Ã©xito.
     */
    public boolean disConnect() throws java.io.IOException, java.net.MalformedURLException, NotConnectedException {
        if (vagos_cookies == null) {
            throw new NotConnectedException();
        }
        // Construimos objeto HTTPUrl con la direcciÃ³n del logout + logouthash
        URL vagos_logout_url = new URL("http://vagos.wamba.com/login.php?do=logout&logouthash=" + getHash());
        HttpURLConnection vagos_conn = (HttpURLConnection) vagos_logout_url.openConnection();
        BufferedReader vagos_disconn_o = new BufferedReader(new InputStreamReader(vagos_conn.getInputStream()));
        String line;

        // Si el HTML no contiene la expresiÃ³n "cookies han sido borradas", la sesiÃ³n no se habrÃ¡ cerrado con Ã©xito.
        boolean logout_success = false;

        while ((line = vagos_disconn_o.readLine()) != null) {
            logout_success = logout_success || line.contains("cookies han sido borradas");
        }
        if (!logout_success) {
            //Debug solo en caso de error
            return false;
        }
        vagos_cookies = null;
        this.is_connected = false;
        return true;
    }

    /**
     * ImplementaciÃ³n de la bÃºsqueda.
     * @param search_query texto a buscar.
     * @return una bÃºsqueda Search.
     */
    @Override
    public SearchResult search(String search_query,int[] servers) throws MalformedURLException, IOException, NoSuchAlgorithmException {
        // Construimos objeto HTTPUrlConnection con la pÃ¡gina de Login
        URL vagos_search_url = new URL("http://www.vagos.es/search.php?do=process");
        HttpURLConnection vagos_search = (HttpURLConnection) vagos_search_url.openConnection();

        vagos_search.setFollowRedirects(false);

        search_query = search_query.replace(' ', '+');
        vagos_search.setDoOutput(true);
        vagos_search.addRequestProperty("Cookie", vagos_cookies);
        // Headers
        vagos_search = HttpUtils.setHeaders("www.vagos.es", vagos_search);

        // Seteamos mÃ©todos
        vagos_search.setRequestMethod("POST");

        // Y datos Post a enviar
        String vagos_searchPost = "securitytoken=" + getHash() + "&do=process&searchthreadid=&query=" + search_query + "&titleonly=1&searchuser&starteronly=0&exactname=1&replyless=0&replylimit=0&searchdate=0&beforeafter=after&sortby=lastpost&order=descending&showposts=0&tag=&forumchoice%5B%5D=60&childforums=1&dosearch=Buscar+Ahora&saveprefs=1";
        vagos_searchPost = URLEncoder.encode(vagos_searchPost, "UTF-8");
        vagos_searchPost = vagos_searchPost.replace("%3D", "=");
        vagos_searchPost = vagos_searchPost.replace("%26", "&");

        // Enviamos los datos Post
        OutputStreamWriter vagos_search_i = new OutputStreamWriter(vagos_search.getOutputStream());
        vagos_search_i.write(vagos_searchPost);
        vagos_search_i.flush();

        // Y recibimos el HTML de respuesta
        vagos_search.getInputStream();
        URL new_url = vagos_search.getURL();
        vagos_search = (HttpURLConnection) new_url.openConnection();
        vagos_search = HttpUtils.setHeaders("www.vagos.es", vagos_search);
        vagos_search.addRequestProperty("Cookie", vagos_cookies);
        vagos_search.setRequestMethod("GET");

        BufferedReader search_reader = new BufferedReader(new InputStreamReader(vagos_search.getInputStream()));
        String line, search_html = "";
        line = search_reader.readLine();
        while (line != null) {
            if (line.contains("<td class=\"tfoot\" ")) {
                break;
            }
            search_html += line;
            line = search_reader.readLine();
        }

        // Empecemos a buscar resultados
        String[] slices = search_html.split("id=\"thread_title_");
        LinkedList<Result> results = new LinkedList<Result>();
        System.out.println("Buscamos Resultados...");
        for (int i = 1; i < slices.length; i++) {
            String title = slices[i].split("\">")[1];
            String id = slices[i].split("\">")[0];
            title= title.split("</a>")[0];
            id = id.split("\">")[0];
            slices[i] = slices[i].split("\" style")[0];
            LinkedList<Result> r = getResult(id,title,servers);
            System.out.println(r);
            results.addAll(r);
        }
        return new SearchResult(search_query,servers,results);


    }

    public LinkedList<Result> getResult(String identifier,String title,int[] servers) {
        try {
            URL new_url = new URL("http://www.vagos.es/showthread.php?t=" + identifier);
            System.out.println("\n\n\n\n\n######## BUSCANDO EN http://www.vagos.es/showthread.php?t=" + identifier + "###########\n\n");
            System.out.println("Título: "+title+"\n");
            HttpURLConnection post_connection = (HttpURLConnection) new_url.openConnection();
            post_connection = HttpUtils.setHeaders("www.vagos.es", post_connection);
            post_connection.setRequestMethod("GET");
            String cookies = vagos_cookies.split("bbsessionhash=")[1].split(";")[0];
            post_connection.setRequestProperty("Cookie", "bbsessionhash=" + cookies + ";");
            BufferedReader page_o = new BufferedReader(new InputStreamReader(post_connection.getInputStream()));
            String page_html = "";
            String[] firstpost;
            String aux;
            aux = page_o.readLine();
            while (true) {
                if (aux != null) {
                    if (aux.contains("showpost.php?p")) {
                        page_o.close();
                        post_connection.disconnect();
                        post_connection = null;
                        firstpost = aux.split("showpost.php?p");
                        firstpost = firstpost[1].split("&amp");
                        aux = "http://www.vagos.es/showpost.php" + firstpost[0] + "&amp;postcount=1";
                        System.out.println("####### Hemos conseguido la direccion del primer post. A por ello, tigre.");
                        break;
                    }
                    aux = page_o.readLine();
                } else {
                    page_o = null;
                    post_connection.disconnect();
                    post_connection = null;
                    break;
                }
            }

            page_html = HttpUtils.getPage(aux, cookies, "www.vagos.es"); 
            return SearchResult.parseLinks(page_html,title,servers,"http://www.vagos.es/showthread.php?t="+identifier);

        } catch (MalformedURLException ex) {
            System.out.println(ex+" cohone");
            Logger.getLogger(VagosConnection.class.getName()).log(Level.SEVERE, null, ex);
        } catch (java.io.IOException ex) {
            System.out.println(ex);
        } catch (Exception ex){
            System.out.println(ex);
        } finally {
            System.out.println("\n getResult FINALLY");;
            return null;
        }

    }

    public String getHash() {
        //Obtenemos el hash de desconexiÃ³n del html de la pÃ¡gina.
        String html = HttpUtils.getPage("http://www.vagos.es", this.vagos_cookies, "www.vagos.es");
        String[] tHash = html.split("var SECURITYTOKEN = \"", 2);
        tHash = tHash[1].split("\";");
        System.out.println(tHash[0]);
        return tHash[0];
    }

    /**
     *
     * @return
     */
    @Override
    public int get_server_id() {
        return this.server_id;
    }

    @Override
    public ServerInfo get_server_info() {
        return this.server_info;
    }

    @Override
    public String user() {
        return this.vagos_user;
    }
}
