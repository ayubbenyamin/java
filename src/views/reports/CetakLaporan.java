/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package views.reports;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Toolkit;
import net.sf.jasperreports.view.JRViewer;

/**
 *
 * @author idham
 */
public class CetakLaporan extends javax.swing.JDialog {

    /**
     * Creates new form CetakLaporan
     */
    public CetakLaporan() {
    }

    public CetakLaporan(JRViewer viewer, String title, boolean modal) {
        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle(title);
        setLayout(new GridLayout(1, 1));

        add(viewer);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        screenSize.setSize(screenSize.getWidth() - 250, screenSize.getHeight() - 250);
        setPreferredSize(screenSize);

        setModal(true);
        pack();
        setLocationRelativeTo(null);
    }

    public static void cetak(final JRViewer viewer, final String title) {
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
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(CetakLaporan.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                CetakLaporan cetakLaporan = new CetakLaporan(viewer, title, true);
                cetakLaporan.setVisible(true);
            }
        });
    }

}
