package myles.servers;



/**
 *
 * @author User
 */
public interface ServerConnection {
    public boolean is_active();
    public int get_server_id();
    public boolean connect() throws Exception;


}
