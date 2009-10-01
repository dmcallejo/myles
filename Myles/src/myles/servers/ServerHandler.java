package myles.servers;

import org.jdom.*;
import java.util.LinkedList;
import java.util.Iterator;
import myles.exceptions.WrongServerException;
import myles.servers.ServerInfo;

/**
 *
 * @author User
 */
public class ServerHandler {
    // Atributos
    private LinkedList<ServerConnection> working_servers = new LinkedList<ServerConnection>();

    public ServerHandler(LinkedList<Element> servers){
        Iterator server_it = servers.iterator();
        try{
            while(server_it.hasNext()){ working_servers.add(createConnection((Element)server_it.next())); }
        }
        catch(WrongServerException e){
            System.out.println("Error al crear server en XML");
        }
    }
    public int size(){
        return working_servers.size();
    }
    public ServerConnection createConnection(Element e) throws WrongServerException{

    switch(Integer.parseInt(e.getAttributeValue("type"))){
        case 1:
            return new VagosConnection(e);
        default:
            return null;
    }

}
    public boolean Init(){
        boolean result = true;
        Iterator server_iterator = working_servers.iterator();
        while(server_iterator.hasNext()){
            ServerConnection cur_server = (ServerConnection)server_iterator.next();
            if(cur_server.is_active()){
                try{
                    result = result && cur_server.connect();
                }
                catch(Exception e){
                    result = false;
                }
            }
        }
        return result;
    }
    public boolean Stop(){
        boolean result = true;
        Iterator server_iterator = working_servers.iterator();
        while(server_iterator.hasNext()){
            ServerConnection cur_server = (ServerConnection)server_iterator.next();
            try{
                result = result && cur_server.connect();
            }
            catch(Exception e){
                result = false;
            }
        }
        return result;
    }
    @Override
    public String toString(){
        Iterator it = working_servers.iterator();
        String msg = "";
        while(it.hasNext()){
            ServerConnection aux = (ServerConnection)it.next();
            msg += "[Connection - Server:"+aux.get_server_info().name()+" User:"+aux.user()+" ] Active?  "+aux.is_active()+" Connected? "+aux.is_connected()+"\n";
        }
        return msg;
    }
    }
