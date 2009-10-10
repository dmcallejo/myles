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
        VagosConnection v1 = new VagosConnection("ju4nk4r", "231d175a6f588a685bc28d34133fa5ca", "1");
        try {
            /*ServerHandler master_servers = new ServerHandler(Config.get_login_info());
            master_servers.Init();
            System.out.println(master_servers);*/
            System.out.println("Hemos conectado? "+v1.is_connected()+" "+v1.user());
            v1.connect();
            System.out.println("Hemos conectado? "+v1.is_connected()+" "+v1.user());
            v1.search("prototype");

        } catch (Exception ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        } finally{
            try {
                v1.disConnect();
            } catch (Exception e){
                System.out.println("cohone, ni desconcta esto, que dice que "+e);
            }
        }
    }
}

