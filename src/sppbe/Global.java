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
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
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

    public static List<Component> validatorComponent = new ArrayList<>();
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

    public static boolean validatorComponentEmpty() {
        boolean isNotValid = false;
        for (Component com : validatorComponent) {
            if (com instanceof JTextField || com instanceof JTextArea) {
                isNotValid = "".equals(((JTextComponent) com).getText().trim());
            }
            if (com instanceof JDateChooser) {
                isNotValid = "null".equals(String.valueOf(((JDateChooser) com).getDate()));
            }
            if (com instanceof JPasswordField) {
                isNotValid = "".equals(String.valueOf(((JPasswordField) com).getPassword()).trim());
            }
        }
        return isNotValid;
    }

    // Contoh penggunaan untuk mendapatkan kode sertifikasi:
    // String kode = Global.getCode("SR");
    public static String getCode(String prefix) {
        try (Connection conn = DriverManager.getConnection(Config.DB_CONNECTION, Config.DB_USER, Config.DB_PASSWORD)) {

            String tableName = "pajak";
            String pk = "Kode_Pajak";
            switch (prefix.toLowerCase()) {
                case "sr":
                    tableName = "sertifikasi";
                    pk = "Kode_Sertifikasi";
                    break;
                case "pg":
                    tableName = "pengujian";
                    pk = "Kode_Pengujian";
                    break;
                case "pr":
                    tableName = "peringatan";
                    pk = "Kode_Perizinan";
                    break;
            }

            String sql = "SELECT " + pk + " FROM " + tableName + " ORDER BY " + pk + " DESC LIMIT 1";

            ResultSet result = conn.createStatement().executeQuery(sql);

            int kode_berikut = 1;

            while (result.next()) {
                kode_berikut = Integer.valueOf(result.getString(1).substring(2)) + 1;
            }
            return prefix.toUpperCase() + String.format("%04d", kode_berikut);
        } catch (SQLException ex) {
        }

        return "error";
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
