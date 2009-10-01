package myles.servers;
import java.io.IOException;
import java.net.MalformedURLException;
import java.security.NoSuchAlgorithmException;
import java.util.LinkedList;
import myles.search.Result;



/**
 *
 * @author User
 */
public interface ServerConnection {
    public boolean is_active();
    public boolean is_connected();
    public int get_server_id();
    public boolean connect() throws Exception;
    public ServerInfo get_server_info();
    public String user();
    public LinkedList<Result> search(String query)  throws MalformedURLException, IOException, NoSuchAlgorithmException;


}
