/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import client.User;
/**
 *
 * @author kami
 */
public class Control {
    //private View v;
    private Connection con;
    private DatagramSocket myServer;
    private int serverPort = 7899;
    private DatagramPacket receivePacket = null;

    public Control() {
        //this.v = v;
        //v.showMessage("UDP server is running and ready to serve...");
        getDBConnection("users", "root", "06011999");
        openServer(serverPort);
        
        while(true){
            listenning();
        }
    }

    private void getDBConnection(String dbName, String username, String password) {
        String dbUrl = "jdbc:mysql://localhost:3306/" + dbName;
        
        try{
            con = DriverManager.getConnection(dbUrl, username, password);
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    private void openServer(int serverPort) {
        try{
            myServer = new DatagramSocket(serverPort);
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    private void listenning() {
        User user = receiveData();
        
        String result = null;
        if(checkUser(user)){
            result = "ok";
        }
        else{
            result = "fail";
        }
        sendData(result);   
    }
    
    private void sendData(String s){
        try{
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(s);
            oos.flush();
            
            InetAddress IPAddress = receivePacket.getAddress();
            int clientPort = receivePacket.getPort();
            byte[] sendData = baos.toByteArray();
            DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, clientPort);
            myServer.send(sendPacket);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    private User receiveData(){
        User user = null;
        try{
            byte[] receiveData = new byte[1024];
            receivePacket = new DatagramPacket(receiveData, receiveData.length);
            myServer.receive(receivePacket);
            
            ByteArrayInputStream bais = new ByteArrayInputStream(receiveData);
            ObjectInputStream ois = new ObjectInputStream(bais);
            user = (User) ois.readObject();
        }catch(IOException | ClassNotFoundException e){
            e.printStackTrace();
        }
        return user;
    }
    
    private boolean checkUser(User user){
        String query = "SELECT * FROM users.users WHERE username = '" + user.getUsername()
                + "' AND password = '" + user.getPassword() + "'";
        try{
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(query);
            if(rs.next()){
                return true;
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return false;
    }
}
