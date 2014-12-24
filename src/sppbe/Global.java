/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sppbe;

import java.awt.Component;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import static sppbe.Config.SYSTEM_EXIT_BUTTON_NO;
import static sppbe.Config.SYSTEM_EXIT_BUTTON_OK;
import static sppbe.Config.SYSTEM_EXIT_MESSAGE;
import static sppbe.Config.SYSTEM_EXIT_TITLE;

/**
 *
 * @author idham
 */
public class Global {

    private AksiKeluarSistem aksiListener;

    public Global() {
    }

    public void konfirmasiKeluar(final Component parentComponent) {
        Icon iconQuestion = new ImageIcon(getClass().getResource("/icons/tanya-64x64.png"));

        Icon iconOK = new ImageIcon(getClass().getResource("/icons/check.png"));
        Icon iconNO = new ImageIcon(getClass().getResource("/icons/x.png"));
        JButton okButton = new JButton(SYSTEM_EXIT_BUTTON_OK, iconOK);
        JButton noButton = new JButton(SYSTEM_EXIT_BUTTON_NO, iconNO);
        okButton.addActionListener(new ButtonKeluarListener());
        noButton.addActionListener(new ButtonKeluarListener());

        Object stringArray[] = {okButton, noButton};

        int showOptionDialog = JOptionPane.showOptionDialog(parentComponent, SYSTEM_EXIT_MESSAGE, SYSTEM_EXIT_TITLE, JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, iconQuestion, stringArray, stringArray[0]);

        if (aksiListener != null && JOptionPane.CLOSED_OPTION == showOptionDialog) {
            aksiListener.abaikanKeluar();
        }
    }

    public void aksiKeluarSistemListener(AksiKeluarSistem listener) {
        aksiListener = listener;
    }

    void keluarSistem(int status) {
        if (aksiListener != null) {
            aksiListener.keluarSistem(status);
        }
        System.exit(status);
    }

    class ButtonKeluarListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getActionCommand().equals(SYSTEM_EXIT_BUTTON_OK)) {
                keluarSistem(0);
            } else {
                if (aksiListener != null) {
                    aksiListener.batalKeluar();
                }
            }

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
    }

    public static interface AksiKeluarSistem {

        void keluarSistem(int status);

        void batalKeluar();

        void abaikanKeluar();
    }

    public static String doHasing(final String passwordToHash) {
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
}
