/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ChatServer;
import ChatClient.ClientInt;
import java.net.MalformedURLException;
import java.rmi.AlreadyBoundException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author yogapermanatanaya
 */
public class ServerObj extends UnicastRemoteObject implements ServerInt{

    @Override
    public void LogoutToServer(ClientInt client, String name) throws RemoteException {
        clients.remove(client);
        clientsName.remove(name);
        for(ClientInt cInt: clients){
            cInt.UpdateUserList(clientsName);
        }
    }

   
    @Override
    public boolean RegisterToServer(ClientInt client, String name) throws RemoteException {
       boolean resultToReturn=false;
       try{
           System.out.println(clientsName.indexOf(name));
           if(clientsName.indexOf(name)>-1){
//               user already exists with the same name...
           }else{
               clients.add(client);
               clientsName.add(name);
               resultToReturn=true;
               for(int i=0; i<clients.size(); i++){
                   try{
                       clients.get(i).UpdateUserList(clientsName);
                   }catch(Exception e){
                       System.out.println(e);
                       clients.remove(i);
                       clientsName.remove(i);
                       
                   }
               }
           }
       }
       catch(Exception e){
           
       }
       return resultToReturn;
       
    }

    @Override
    public void MsgToServer(String msg, String FromUser, String toName) throws RemoteException {
       if(toName.equalsIgnoreCase("All Users")){
           for(int i=0; i<clients.size(); i++){
               try{
                   if(clientsName.get(i).equals(FromUser)){
                       clients.get(i).MsgArrived(msg, "You");
                   }else{
                       clients.get(i).MsgArrived(msg, FromUser);
                   }
               }
               catch(Exception e){
                   System.out.println(e);
                   clients.remove(i);
                   clientsName.remove(i);
                   for(ClientInt cInt: clients){
                       cInt.UpdateUserList(clientsName);
                   }
               }
           }
       }
       else{
           int count=0;
           for(int i=0; i<clients.size(); i++){
               if(clientsName.get(i).equals(toName)){
                   count++;
                   try{
                       clients.get(i).MsgArrived(msg, FromUser);
                   }catch(Exception e){
                       System.out.println(e);
                       clients.remove(i);
                       clientsName.remove(i);
                       for(ClientInt cInt: clients ){
                           cInt.UpdateUserList(clientsName);
                       }
                       MsgToServer("User "+toName+ " seems unavailable. ","Server",FromUser);
                       count++;
                       
                   }
                   
               }
               else if(clientsName.get(i).equals(FromUser)){
                   count++;
                   try{
                       clients.get(i).MsgArrived(msg, "You");
                   }catch(Exception e){
                       System.out.println(e);
                       clients.remove(i);
                       clientsName.remove(i);
                       
                       for(ClientInt cInt: clients){
                           cInt.UpdateUserList(clientsName);
                       }
                       
                   }
                   
               }
               if(count==2){
                   break;
               }
           }
       }
    }
    
    static Registry reg=null;

    public ServerObj()throws RemoteException {
    }
    
//    start server
    public static void StartServer(){
        main(new String[0]);
        
    }
    
//    stop server
    public static void StopServer(){
        try{
            reg.unbind("DpkServer");
            UnicastRemoteObject.unexportObject(reg, true);
            System.out.println("Server Stopped...");
        }catch(RemoteException ex){
            Logger.getLogger(ServerObj.class.getName()).log(Level.SEVERE,null, ex);
        }catch(NotBoundException ex){
            Logger.getLogger(ServerObj.class.getName()).log(Level.SEVERE,null, ex);
        }
    }
   
    public static void main(String[]args){
        {
            try{
                reg=java.rmi.registry.LocateRegistry.createRegistry(1099);
                Naming.bind("DpkServer", new ServerObj());
                System.out.println("Server is ready...");
            }catch(AlreadyBoundException ex){
                Logger.getLogger(ServerObj.class.getName()).log(Level.SEVERE,null, ex);
                
            }catch(MalformedURLException ex){
                Logger.getLogger(ServerObj.class.getName()).log(Level.SEVERE,null,ex);
            }catch(RemoteException ex){
                Logger.getLogger(ServerObj.class.getName()).log(Level.SEVERE,null,ex);
            }
        }
        
    }
    List<ClientInt>clients=new ArrayList<ClientInt>();
    List<String>clientsName=new ArrayList<String>();
    
    
    
}
