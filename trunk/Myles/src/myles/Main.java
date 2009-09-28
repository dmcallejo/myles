/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package myles;
import myles.servers.*;
/**
 *
 * @author User
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try{
            System.out.println("Conexión...");
            VagosConnection vagos1 = new VagosConnection("ju4nk4r", "ninguna");
            System.out.println(vagos1.Connect()+"\nDesconexión...");
            System.out.println(vagos1.disConnect());
        }
        catch(java.net.MalformedURLException e){
            System.out.println("MalformedURLException");
        }
        catch(java.io.IOException e){
            System.out.println("Excepción I/O");
        }
        catch(java.lang.Exception e){
            System.out.println(e);
        }

    }

}
