/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sppbe;

import controllers.AdminJpaController;
import static sppbe.Config.EMF;
import views.Utama;

/**
 *
 * @author idham
 */
public class Init {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                // sementara
                AdminJpaController control = new AdminJpaController(EMF);
                final model.Admin model = control.findAdmin("admin");
                Global.setModelAdmin(model);
                //new Login().setVisible(true);
                new Utama().setVisible(true);
            }
        });
    }
}
