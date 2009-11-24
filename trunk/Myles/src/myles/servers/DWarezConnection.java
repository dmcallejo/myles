/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package myles.servers;

import java.io.OutputStreamWriter;
import java.io.InputStreamReader;
import java.io.IOException;
import java.net.*;
import java.io.BufferedReader;
import java.security.NoSuchAlgorithmException;
import myles.search.SearchResult;
import org.jdom.Element;
import java.net.URL;
import myles.utils.HttpUtils;

/**
 *
 * @author Juankar & Dmcelectrico
 * Este archivo contiene todo el código fuente para establecer una
 * conexión con la página www.DirectorioWarez.com, no generalizable
 * pues no funciona bajo un foro estándar.
 */


public class DWarezConnection implements ServerConnection{
    /* Variables que controlan la actividad y la conexión del objeto. */
    private boolean serverConnected = false;
    private boolean serverActive = false;
    private int serverID = 2;
    private ServerInfo serverInfo;

    public String serverUser;
    public String serverPass;

    /**
     * Método constructor de DWarezConnection
     * @param user , nombre de usuario en la página
     * @param password , contraseña en la página
     * @param activity , está activo o no?
     */
    public DWarezConnection(String user, String password, String activity) {
        this.serverUser = user;
        this.serverPass = password;
        serverActive = Integer.parseInt(activity) == 1;
    }
    public DWarezConnection(Element data) {
        this.serverUser = data.getChildText("user");
        this.serverPass = data.getChildText("encpass");
        if (Integer.parseInt(data.getChildText("active")) == 1) {
            this.enable();
        } else {
            this.disable();
        }
        data.removeChild("active");
        data.removeChild("user");
        data.removeChild("encpass");
        this.serverInfo = new ServerInfo(data);
    }

    /**
     * Métodos para activar o desactivar la conexión actual
     */
    public void enable(){
        this.serverActive = true;
    }
    public void disable(){
        this.serverActive = false;
    }
    @Override
    public ServerInfo get_server_info(){
        return serverInfo;
    }
    /**
     * Método observador de la variable serverActive, que indica
     * el estado de la conexión...(ver declaración serverActive)
     * @return el estado de la conexión.
     */
    @Override
    public boolean is_active(){
        return serverActive;
    }
    /**
     * Metodo observador de la variable serverConnected, que indica
     * el estado de la conexión activa.
     * @return estado de la conexión activa
     */
    @Override
    public boolean is_connected(){
        return serverConnected;
    }

    /**
     * Método que devuelve la ID de este servidor (DWarez) en los servers de
     * Myles.
     * @return la ID del servidor
     */
    @Override
    public int get_server_id(){
        return serverID;
    }
    @Override
    public boolean connect() throws java.net.MalformedURLException, java.io.IOException{
        // Creamos un objeto URL con la dirección de la página
        URL DWIndexUrl = new URL("http://directoriowarez.com/index.php?");

        HttpURLConnection DWConnection = (HttpURLConnection) DWIndexUrl.openConnection();
        
        // Output y headers adecuados
        DWConnection.setDoOutput((true));
        DWConnection = HttpUtils.setHeaders("www.directoriowarez.com", DWConnection);

        // Se enviará todo por Post
        DWConnection.setRequestMethod("POST");

        // Los siguientes datos van en el Post
        String DWPostData = "recordar=0&user="+serverUser+"&referer=http://directoriowarez.com/index.php&pass?=";
        DWPostData += serverPass+"&submit=Entrar";

        // Y son enviados aquí
        OutputStreamWriter DWInput = new OutputStreamWriter(DWConnection.getOutputStream());
        DWInput.write(DWPostData);
        DWInput.flush();

        // Aquí procesamos el HTML recibido
        BufferedReader DWOutput = new BufferedReader(new InputStreamReader(DWConnection.getInputStream()));
        String DWTmpLine;
        Boolean loginSuccess = false;
        while((DWTmpLine = DWOutput.readLine()) != null){
            loginSuccess = loginSuccess || DWTmpLine.contains("href=\"http://www.directoriowarez.com/check.php?logout=1");
        }



        return false;
    }

    @Override
    public String user() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public SearchResult search(String query, int[] servers) throws MalformedURLException, IOException, NoSuchAlgorithmException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}
