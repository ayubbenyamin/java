/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sppbe;

import com.toedter.calendar.JDateChooser;
import java.awt.Component;
import java.awt.Window;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRootPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.text.JTextComponent;
import model.Admin;

/**
 *
 * @author idham
 */
public class Global {

    private static Admin modelAdmin;

    public static void setModelAdmin(final Admin admin) {
        modelAdmin = admin;
    }

    public static Admin getModelAdmin() {
        return modelAdmin;
    }

    public static void setEnabledTextField(JPanel jPanel) {
        setEnabledTextField(jPanel, true);
    }

    public static void setEnabledTextField(JPanel jPanel, boolean b) {
        Component[] component = jPanel.getComponents();
        for (Component com : component) {
            if (com instanceof JTextField || com instanceof JDateChooser) {
                com.setEnabled(b);
            }
            if (com instanceof JScrollPane) {
                Component[] component2 = ((JScrollPane) com).getViewport().getComponents();
                for (Component com2 : component2) {
                    if (com2 instanceof JTextField || com2 instanceof JTextArea) {
                        com2.setEnabled(b);
                    }
                }
            }
        }
    }

    public static void setClearTextField(JPanel jPanel) {
        Component[] component = jPanel.getComponents();
        for (Component com : component) {
            if (com instanceof JTextField || com instanceof JTextArea) {
                ((JTextComponent) com).setText("");
            }
            if (com instanceof JDateChooser) {
                ((JDateChooser) com).setDate(null);
            }
            if (com instanceof JScrollPane) {
                Component[] component2 = ((JScrollPane) com).getViewport().getComponents();
                for (Component com2 : component2) {
                    if (com2 instanceof JTextField || com2 instanceof JTextArea) {
                        ((JTextComponent) com2).setText("");
                    }
                }
            }
        }
    }

    public Global() {
    }

    public static void closeDialog() {
        Window[] windows = Window.getWindows();
        for (Window window : windows) {
            if (window instanceof JDialog) {
                JDialog dialog = (JDialog) window;
                if (dialog.getContentPane().getComponentCount() == 1
                        && dialog.getContentPane().getComponent(0) instanceof JOptionPane) {
                    dialog.dispose();
                }
            }
        }
    }

    public static String setPasswordHashing(final String passwordToHash) {
        String generatedPassword = null;
        try {
            // Create MessageDigest instance for MD5
            MessageDigest md = MessageDigest.getInstance("MD5");
            //Add password bytes to digest
            md.update(passwordToHash.getBytes());
            //Get the hash's bytes
            byte[] bytes = md.digest();
            //This bytes[] has bytes in decimal format;
            //Convert it to hexadecimal format
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < bytes.length; i++) {
                sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
            }
            //Get complete hashed password in hex format
            generatedPassword = sb.toString();
        } catch (NoSuchAlgorithmException e) {
        }
        return generatedPassword;
    }

    public static void showProgress() {
        // TODO show progress dialog here
    }

    public static konfirmasiHapus konfirmasiHapus(final JRootPane rootPane, final AksiHapus aksiHapus) {
        return new konfirmasiHapus(rootPane, aksiHapus);
    }

    public static KonfirmasiKeluarSistem konfirmasiKeluar(final JRootPane rootPane) {
        return new KonfirmasiKeluarSistem(rootPane, new AksiKeluarSistem() {

            @Override
            public void run(int status) {
            }
        });
    }

    public static KonfirmasiKeluarSistem konfirmasiKeluar(final JRootPane rootPane, final AksiKeluarSistem aksiKeluarSistem) {
        return new KonfirmasiKeluarSistem(rootPane, aksiKeluarSistem);
    }

}
