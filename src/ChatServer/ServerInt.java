/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ChatServer;
import ChatClient.ClientInt;
import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 *
 * @author yogapermanatanaya
 */
public interface ServerInt extends Remote{
    public boolean RegisterToServer(ClientInt client, String name)throws RemoteException;
    public void MsgToServer(String msg, String FromUser, String toName)throws RemoteException;
    public void LogoutToServer(ClientInt client, String name)throws RemoteException;
    
}
