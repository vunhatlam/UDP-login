/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

/**
 *
 * @author kami
 */
public class Control {
    private int serverPort = 7899;
    private int clientPort = 6199;
    private String serverHost = "localhost";
    private DatagramSocket myClient;
    
    public Control(){
    }
    
    public DatagramSocket openConnection(){
        try{
            myClient = new DatagramSocket(clientPort);
        }catch(Exception e){
            e.printStackTrace();
        }
        return myClient;
    }
    
    public void closeConnection(){
        try{
            myClient.close();
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public void sendData(User user){
        try{
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(user);
            oos.flush();
            
            InetAddress IPAddress = InetAddress.getByName(serverHost);
            byte[] sendData = baos.toByteArray();
            DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, serverPort);
            myClient.send(sendPacket);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public String receiveData(){
        String result = "";
        try{
            byte[] receiveData = new byte[1024];
            DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
            myClient.receive(receivePacket);
            
            ByteArrayInputStream bais = new ByteArrayInputStream(receiveData);
            ObjectInputStream ois = new ObjectInputStream(bais);
            Object o = ois.readObject();
            if(o instanceof String){
                result = (String) o;
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return result;
    }
}
