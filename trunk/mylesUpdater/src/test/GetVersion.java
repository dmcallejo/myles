package test;


import java.io.*;
import java.net.*;

/**
 *
 * @author diego
 */
public class GetVersion {


    public GetVersion(){}
    public static String[] GetAppVersion(){
        try{
            URL url = new URL("http://www.labolera.net/myles/xml/appdata.xml");
       

        // establecemos conexion
        URLConnection urlCon = url.openConnection();
        // La siguiente linea saca el tipo tipo de fichero
        //System.out.println(urlCon.getContentType());

        // Abrimos el buffered reader donde guardaremos el texto
        BufferedReader xml = new BufferedReader(new InputStreamReader(urlCon.getInputStream()));
        // Se obtiene el inputStream del fichero listo para recibir
        InputStream is = urlCon.getInputStream();
        String txt = "";
        String aux;
        while (true) {
            aux = xml.readLine();
            if (aux != null) {
                txt += aux + "\n";
            } else {
                break;
            }
        }

        // cierre de conexion y retorna string.
        is.close();

        String[] resultado = new String[4];
        String[] versions = txt.split("version=\"");
        resultado[0] = versions[2].split("\"")[0];
        resultado[1] = versions[2].split("\"")[2];
        resultado[2] = versions[3].split("\"")[0];
        resultado[3] = versions[3].split("\"")[2];
        return resultado;
        }
        catch(Exception e){
            System.out.println("Error de URL en getversion, capullo.");
            return null;
        }

    }
}

