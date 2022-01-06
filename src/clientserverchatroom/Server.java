/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package clientserverchatroom;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author SHROUK
 */

public class Server extends javax.swing.JFrame {

    /**
     * Creates new form Server
     */
    
   static ArrayList c_os;
    static ArrayList<String> clients;
    static Socket clientsocket;
    private static ExecutorService pool = Executors.newFixedThreadPool(30);

    
    
    public class server_listening implements Runnable
    {
        
        @Override
        public void run()
        {
            c_os=new ArrayList();
            clients=new ArrayList();
            
            try
            {
                ServerSocket ss=new ServerSocket(8000);
                while(true)
                {
                    clientsocket=ss.accept();
                    PrintWriter w=new PrintWriter(clientsocket.getOutputStream());
                    c_os.add(w);
                    
                    ClientHandler clienthread = new ClientHandler(clientsocket,w);
                    pool.execute(clienthread);
                    msg_area.setText("\nGOT A CONNECTION.\n");
                
                }
            }
            catch(IOException e)
            {
                msg_area.setText("\nERROR");
            }
        }
    }
    
    
    
    
     public void SendMsg(String msg)
        {
            Iterator it=c_os.iterator();
            msg_area.setText("SENDING: "+msg+"\n");
            
            while(it.hasNext())
            {
                try
                {
                    PrintWriter w=(PrintWriter) it.next();
                    w.println(msg);
                    w.flush();
                }
                catch(Exception e)
                {
                 msg_area.setText("\nAN ERROR OCCURS WHILE TELLING EVERYONE.\n");   
                }
            }
        }
    
    
    
    
    public class ClientHandler implements Runnable
        {
            BufferedReader reader;
            PrintWriter writer;
            
            public ClientHandler(Socket cs,PrintWriter w)
            {
                writer=w;
                try
                {
                    InputStreamReader in=new InputStreamReader(cs.getInputStream());
                    reader= new BufferedReader(in);
                }
                catch(Exception e)
                {
                     msg_area.setText("\nAN ERROR OCCURS WHILE READING FROM INPUTSTREAM.\n");
                }
            }
            
            @Override
            public void run()
            {
                int c=0;
                String msg;
                String[] i;
                
                try
                {
                       while((msg=reader.readLine())!=null)
                       {
                       i=msg.split(":");
                       msg_area.setText("\nRECEIVED:"+msg+"\n");
                       
                        switch (i[2]) {
                               case "connect":
                                  for(String u:clients)
                                  {
                                    if(i[0].equals(u))
                                        c=1;
                                    else
                                        c=0;
                                   }
                                  if(c==0)
                                  {
                                   SendMsg(msg);
                                   clients.add(i[0]);
                                   msg_area.setText("SERVER HAS ADDED "+i[0]+" TO THE CHAT USERS LIST.\n");
                                  }
                                  else
                                  SendMsg("CU");
                                   break;
                               case "disconnect":
                                   SendMsg(msg);
                                   clients.remove(i[0]);
                                   c_os.remove(writer);
                                   msg_area.setText("SERVER HAS REMOVED "+i[0]+" FROM THE CHAT USERS LIST.\n");
                                   break;
                               case "chat":
                                   SendMsg(msg);
                                   break;
                               default:
                                   break;
                        }
                   }
                }
                catch(Exception e)
                {
                     msg_area.setText("A USER LOST THE CONNECTION.\n");
                     c_os.remove(writer);
                }
            }
        }
    
       
    
    
    
    public Server() {
        initComponents();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings(value = "unchecked")

    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        msg_area = new javax.swing.JTextArea();
        connect = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jLabel1.setFont(new java.awt.Font("Times New Roman", 1, 36)); // NOI18N
        jLabel1.setText("Server");

        msg_area.setColumns(20);
        msg_area.setRows(5);
        jScrollPane1.setViewportView(msg_area);

        connect.setText("Connect");
        connect.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                connectActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(26, 26, 26)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(connect, javax.swing.GroupLayout.PREFERRED_SIZE, 158, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 420, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(30, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(27, 27, 27)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(connect, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 38, Short.MAX_VALUE)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 230, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(35, 35, 35))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void connectActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_connectActionPerformed
        // TODO add your handling code here:
              pool.execute(new server_listening());
              msg_area.setText("Server is ready.. :D");
              new Login().setVisible(true);
    }//GEN-LAST:event_connectActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Server.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Server.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Server.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Server.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Server().setVisible(true);
            }
        });
    }
    
    
    
    
   //////////////////////////////////////////////////////////////////////////////////////////////////////////////
    
	
  
    
    
    
    
    
    

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton connect;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextArea msg_area;
    // End of variables declaration//GEN-END:variables
}
