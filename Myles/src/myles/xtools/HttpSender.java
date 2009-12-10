package myles.xtools;

import myles.xtools.exceptions.*;
import java.io.IOException;
import java.net.*;
import java.io.OutputStreamWriter;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.util.LinkedList;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
/**
 * Myles X-Tools
 * Clase utilizada para manejar peticiones de HTTP personalizadas.
 * @author Juankar
 */
public class HttpSender {
    private HttpURLConnection httpConnection;
    private LinkedList<String> httpCookies;
    private LinkedList<String> httpPostData;
    private Map<String, String> httpRequestProperties;
    private String httpHost;
    /**
     * Constructor de la clase
     */
    public HttpSender(String URL) throws MalformedURLException, IOException{
        httpConnection = (HttpURLConnection)(new URL(URL).openConnection());
        httpCookies = new LinkedList<String>();
        httpPostData = new LinkedList<String>();
    }

    /** !!!!!!!!!! COOKIES !!!!!!!!!!! */


    /**
     * Función que devuelve el valor de la cookie si la cookie ya existe o
     * devuelve "" si no existe.
     * @param name nombre de la cookie
     * @return
     */
    public String containsCookie(String name){
        String auxString;
        boolean result;
        Iterator<String> cookieIterator = httpCookies.iterator();
        while(cookieIterator.hasNext()){
            auxString = cookieIterator.next();
            if(auxString.startsWith(auxString)){
                auxString = auxString.split("=")[1];
                return auxString;
            }
        }
        return "";
    }

    /**
     * Función de borrado de cookie
     * @param name nombre de la cookie
     * @throws NoSuchCookieException cuando no existe la cookie se lanza la exc.
     *
     */
    public void deleteCookie(String name) throws NoSuchCookieException{
        if(!(containsCookie(name).equals(""))) throw(new NoSuchCookieException());
        Iterator<String> cookieIterator = httpCookies.iterator();
        while(cookieIterator.hasNext()){
            if(cookieIterator.next().startsWith(name)) cookieIterator.remove();
        }
    }

    /**
     * Función que añade una cookie si no existe, y borra la actual e inserta
     * el nuevo valor si ya existe
     * @param name nombre de la cookie
     * @param value valor nuevo de la cookie
     * @return devuelve el éxito de la operación.
     */
    public boolean addCookie(String name, String value){
        try{
            if(!(containsCookie(name).equals(""))) deleteCookie(name);
            httpCookies.add(name+"="+value);
        }
        catch(NoSuchCookieException e){
            httpCookies.add(name+"="+value);
        }
        return true;
    }

    /**
     * Función que devuelve una lista enlazada de Strings con las cookies
     * en formato nombre=valor.
     * @return
     */
    public LinkedList<String> getCookies(){
        return httpCookies;
    }

    /**
     * Función que identifica si existe la clave Post indicada.
     * @param name nombre de la clave Post
     * @return "" si no existe. Su valor si sí lo hace
     */
    public String containsPostData(String name){
        String auxString;
        boolean result;
        Iterator<String> cookieIterator = httpPostData.iterator();
        while(cookieIterator.hasNext()){
            auxString = cookieIterator.next();
            if(auxString.startsWith(auxString)){
                auxString = auxString.split("=")[1];
                return auxString;
            }
        }
        return "";
    }

    /**
     * Función de borrado de datos Post
     * @param name nombre de la clave Post
     * @throws NoSuchPostException
     *
     */
    public void deletePostData(String name) throws NoSuchPostDataException{
        if(!(containsCookie(name).equals(""))) throw(new NoSuchPostDataException());
        Iterator<String> cookieIterator = httpPostData.iterator();
        while(cookieIterator.hasNext()){
            if(cookieIterator.next().startsWith(name)) cookieIterator.remove();
        }
    }

    /**
     * Función que añade una cookie si no existe, y borra la actual e inserta
     * el nuevo valor si ya existe
     * @param name nombre de la cookie
     * @param value valor nuevo de la cookie
     * @return devuelve el éxito de la operación.
     */
    public boolean addPostData(String name, String value){
        try{
            if(!(containsCookie(name).equals(""))) deletePostData(name);
            httpPostData.add(name+"="+value);
        }
        catch(NoSuchPostDataException e){
            httpPostData.add(name+"="+value);
        }
        return true;
    }

    /**
     * Función que devuelve una lista enlazada de Strings con las cookies
     * en formato nombre=valor.
     * @return
     */
    public LinkedList<String> getPost(){
        return httpPostData;
    }

    /**
     * Método que comprueba si existe la propiedad especificada
     * @param key la clave de la propiedad (nombre)
     * @return "" si no existe. Su valor en caso contrario
     */
    public String containsRequestProperty(String key){
        if(httpRequestProperties.containsKey(key)){
            return httpRequestProperties.get(key);
        }
        return "";
    }


    /**
     * Borra la RequestProperty si existe
     * @param key la clave
     */
    public void deleteRequestProperty(String key) throws NoSuchRequestPropertyException{
        if(!containsRequestProperty(key).equals("")){
            httpRequestProperties.remove(key);
        }else{
            throw(new NoSuchRequestPropertyException());
        }
    }

    public boolean addRequestProperty(String key, String value){
        if(containsRequestProperty(key).equals("")){
            httpRequestProperties.put(key, value);
        }else{
            httpRequestProperties.remove(key);
            httpRequestProperties.put(key, value);
        }
        return true;
    }

    /**
     * Añade al objeto HttpURLConnection las propiedades de la petición que
     * envía FireFox normalmente.
     */
    public void setFirefoxProperties(){
        addRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
        addRequestProperty("Accept-Language", "es-es,es;q=0.8,en-us;q=0.5,en;q=0.3");
        addRequestProperty("Accept-Charset", "ISO-8859-1,utf-8;q=0.7,*;q=0.7");
        addRequestProperty("Content-type", "application/x-www-form-urlencoded");
        addRequestProperty("User-Agent", "Mozilla/5.0 (X11; U; Linux i686; es-ES; rv:1.9.0.14) Gecko/2009090216 Ubuntu/9.04 (jaunty) Firefox/3.0.14");
        addRequestProperty("Connection", "keep-alive");
        addRequestProperty("Keep-Alive", "300");
    }

    /**
     * Método setter de httpHost
     * @param host
     */
    public void setHost(String host){
        httpHost = host;
    }
    /**
     * Método getter de httpHost
     * @return
     */
    public String getHost(){
        return httpHost;
    }

    public LinkedList<String> openConnection() throws NoHostException, IOException{
        Iterator<Entry<String, String>> mapIterator;
        Iterator<String> strIterator;
        String strAux;
        Entry<String, String> auxEntry;

        // Inclusión del host
        if(httpHost.equals("")) throw new NoHostException();
        httpConnection.addRequestProperty("Host", httpHost);

        // Inclusión de las requestProperties
        mapIterator = httpRequestProperties.entrySet().iterator();
        while(mapIterator.hasNext()){
            auxEntry = mapIterator.next();
            httpConnection.addRequestProperty(auxEntry.getKey(), auxEntry.getValue());
        }

        // Inclusión de las cookies
        strIterator = httpCookies.iterator();
        strAux = "";
        while(strIterator.hasNext()){
            if(strAux.equals("")){
                strAux = strIterator.next();
            }else{
                strAux += "&"+strIterator.next();
            }
        }
        httpConnection.addRequestProperty("Cookie", strAux);

        // Creación del String post
        strIterator = httpPostData.iterator();
        strAux = "";
        while(strIterator.hasNext()){
            if(strAux.equals("")){
                strAux = strIterator.next();
            }else{
                strAux += strIterator.next();
            }
        }

        // Abramos la conexión
        httpConnection.setDoOutput(true);
        OutputStreamWriter httpConnectionOutput = new OutputStreamWriter(httpConnection.getOutputStream());
        httpConnectionOutput.write(strAux);
        httpConnectionOutput.flush();

        // Recojamos resultados
        LinkedList<String> httpResponse = new LinkedList<String>();
        BufferedReader brResponse = new BufferedReader(new InputStreamReader(httpConnection.getInputStream()));

        strAux = brResponse.readLine();
        while(strAux != null){
            httpResponse.add(strAux);
        }
        return httpResponse;
    }
}
