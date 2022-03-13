package json;

public class Node {
	private String ip;
	private int port;
	private boolean isAlive;


	public Node(String ip, int port, boolean isAlive) {
		this.ip = ip;
		this.port = port;
		this.isAlive = isAlive;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public boolean isAlive() {
		return isAlive;
	}

	public void setAlive(boolean alive) {
		isAlive = alive;
	}
}
