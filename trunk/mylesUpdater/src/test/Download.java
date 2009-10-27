package test;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.Date;

/**
 * Ejemplo de descarga de un fichero de imagen desde la web.
 *
 * @author chuidiang
 *
 */
public class Download extends Thread {

    String Url;
    String fpath;
    UpdaterFrame parent;

    /**
     * Descarga un fichero jpeg y lo guarda en e:/foto.jpg
     *
     * @param args
     */
    public Download(String url, String local, UpdaterFrame parent) {
        this.Url = url;
        this.fpath = local;
        this.parent = parent;
    }

    @Override
    public void run() {
        String[] versions = GetVersion.GetAppVersion();
        File f = new File("tmp.tmp"); // Creamos un objeto file
        String path = f.getAbsolutePath(); // Llamamos al método que devuelve la ruta absoluta
        path = path.split("tmp.tmp")[0];


        // Aquí actualizamos
        parent.log("\nComenzando la actualización.");
        parent.log("----------------------------");
        parent.log("Actualizando a la versión " + versions[0] + " lanzada el " + versions[1]);
        download();
        parent.log("La nueva versión del programa ha sido descargada correctamente.\n");
        parent.jButton1.setEnabled(false);
        parent.jButton1.setText("Espere, por favor...");
        File viejo = new File(path + "myles.pdf");
        parent.log("Haciendo copia de seguridad de la versión anterior. Utilice el menú Restaurar");
        parent.log("si la actualización actual falla (antes de darle a actualizar).\n");
        File backup = new File(path + "backup.pdf");
        if (viejo.renameTo(backup)) {
            parent.log("Copia de seguridad realizada con éxito.");
            backup = new File(path + fpath);
            viejo = new File(path + "myles.pdf");
            parent.log("Actualización completada con éxito. Pulse lanzar para volver a Myles. Gracias.");
            backup.renameTo(viejo);
            parent.actualizando = false;
            parent.jButton1.setEnabled(true);
            parent.jLabel3.setText("Actualizado");
            parent.jButton1.setText("Re-Actualizar");
        }
        parent.actualizando = false;
        parent.jButton1.setEnabled(true);
        parent.jButton1.setText("Re-Actualizar");

    }

    public void download() {
        try {
            // Url con la foto
            URL url = new URL(Url);

            // establecemos conexion
            URLConnection urlCon = url.openConnection();

            // Sacamos por pantalla el tipo de fichero

            // Se obtiene el inputStream de la foto web y se abre el fichero
            // local.
            InputStream is = urlCon.getInputStream();
            FileOutputStream fos = new FileOutputStream(fpath);

            // Lectura de la foto de la web y escritura en fichero local
            byte[] array = new byte[1000]; // buffer temporal de lectura.
            int leido = is.read(array);
            int progreso = 0;
            double velocidad = 0;
            int tLeido = leido;
            NumberFormat formatter = new DecimalFormat("#0.00");
            parent.log("Tamaño del nuevo JAR: " + formatter.format((double) urlCon.getContentLength() / 1048576) + " MB.");
            /*
             * El siguiente algoritmo calcula la velocidad de descarga a partir de dos Dates.
             */
            Date ref1 = new Date();
            int tRef1 = tLeido;
            String velocidadEnBonito = "(calculando)";
            while (leido > 0) {
                Date ref2 = new Date();
                if (Math.abs(ref1.getTime() - ref2.getTime()) >= 1000) {    //Se estima el segundo de tiempo pasado.
                    velocidad = (Math.abs((double) tRef1 - (double) tLeido)/(Math.abs(ref1.getTime() - ref2.getTime())/1000) / 1024); //Se calcula
                    if (velocidad > 1024) {             //En caso de que se llegue a la inimagniable cifra del mega
                        velocidad = velocidad / 1024;   // se trata el valor para que se muestre mas bonito.
                        velocidadEnBonito = formatter.format(velocidad) + "MB/s";
                    } else {
                        velocidadEnBonito = formatter.format(velocidad) + "KB/s";
                    }
                    tRef1 = tLeido;
                    ref1 = new Date();      //Nuevo date para la proxima medición de velocidad.
                }
                tLeido += leido;
                progreso = (int) (((double) tLeido / (double) urlCon.getContentLength()) * 100);
                parent.setProgress1(progreso, velocidadEnBonito);
                fos.write(array, 0, leido);
                leido = is.read(array);
            }

            // cierre de conexion y fichero.
            is.close();
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

