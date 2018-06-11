package server;

public interface MessageDispatcher {
    void addClient(ClientConnection clientConnection);
    void dispatchMessage(ClientConnection clientConnection, String message);
}
