package myles;

import java.util.logging.Level;
import java.util.logging.Logger;
import myles.servers.*;
/**
*
* @author Juankar
*/

public class Main {
    public static void main(String[] args){
        try {
            /*ServerHandler master_servers = new ServerHandler(Config.get_login_info());
            master_servers.Init();
            System.out.println(master_servers);*/
            VagosConnection v1 = new VagosConnection("ju4nk4r", "231d175a6f588a685bc28d34133fa5ca", "1");
            System.out.println("Hemos conectado? "+v1.is_connected()+" "+v1.user());
            v1.connect();
            System.out.println("Hemos conectado? "+v1.is_connected()+" "+v1.user());
            v1.search("prototype");

        } catch (Exception ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
