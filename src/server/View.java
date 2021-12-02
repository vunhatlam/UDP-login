/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

/**
 *
 * @author kami
 */
public class View {

    public View() {
        showMessage("UDP server is running and ready to serv...");
        Control control = new Control();
    }
    
    public void showMessage(String s){
        System.out.println(s);
    }
}
