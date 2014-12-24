/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sppbe;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JRootPane;
import static sppbe.Config.BUTTON_NO;
import static sppbe.Config.BUTTON_OK;
import static sppbe.Config.REMOVE_RECORD_MESSAGE;
import static sppbe.Config.REMOVE_RECORD_TITLE;

/**
 *
 * @author idham
 */
public class konfirmasiHapus {

    public konfirmasiHapus(final JRootPane rootPane, final AksiHapus aksiHapus) {
        Icon iconQuestion = new ImageIcon(getClass().getResource("/icons/tanya-64x64.png"));

        Icon iconOK = new ImageIcon(getClass().getResource("/icons/check.png"));
        Icon iconNO = new ImageIcon(getClass().getResource("/icons/x.png"));
        JButton okButton = new JButton(BUTTON_OK, iconOK);
        JButton noButton = new JButton(BUTTON_NO, iconNO);
        okButton.addActionListener(new ButtonHapusListener(aksiHapus));
        noButton.addActionListener(new ButtonHapusListener(aksiHapus));

        Object stringArray[] = {okButton, noButton};
        int showOptionDialog = JOptionPane.showOptionDialog(rootPane, REMOVE_RECORD_MESSAGE, REMOVE_RECORD_TITLE, JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, iconQuestion, stringArray, stringArray[0]);

        // Jika jendela dialog ditutup panggil ini
        aksiHapus.run(showOptionDialog);
    }

    class ButtonHapusListener implements ActionListener {

        AksiHapus aksiHapus;

        ButtonHapusListener(final AksiHapus aksiHapus) {
            this.aksiHapus = aksiHapus;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            Global.closeDialog();
            if (e.getActionCommand().equals(BUTTON_OK)) {
                aksiHapus.run(JOptionPane.OK_OPTION);
            } else {
                aksiHapus.run(JOptionPane.NO_OPTION);
            }
        }

    }
}
