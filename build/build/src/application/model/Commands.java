package application.model;

public interface Commands {

	public final String CONNECT_TO_HOST1 = "host1";
	public final String CONNECT_TO_HOST2 = "host2";
	public final String CONNECT_TO_HOST3 = "host3";
	public final String CONNECT_TO_HOST4 = "host4";
	public final String CONNECT_TO_HOST5 = "host5";
	public final String CONNECT_TO_HOST6 = "host6";
	public final String CONNECT_TO_HOST7 = "host7";
	public final String CONNECT_TO_HOST8 = "host8";
	
	public final String LIST_DIRECTORY = "ls -la";
	
	public final String RUN_SCRIPT = "./logcap.sh -c -z";
	public final String DEBUG_MODE = "./logcap.sh -d all";
	
	public final String EXIT = "exit";
	
	public final String SEND_TO_ROOT = "getfrom X /root/spidrlogs/blablabla";
}
