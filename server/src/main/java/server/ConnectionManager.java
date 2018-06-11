package server;

public interface ConnectionManager extends Runnable {
    void addClient(ClientConnection client);
    boolean removeClient(ClientConnection client);
    void exit();
}
