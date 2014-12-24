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
import static sppbe.Config.SYSTEM_EXIT_MESSAGE;
import static sppbe.Config.SYSTEM_EXIT_TITLE;

/**
 *
 * @author idham
 */
public class KonfirmasiKeluarSistem {

    public KonfirmasiKeluarSistem(final JRootPane rootPane, final AksiKeluarSistem aksiKeluarSistem) {
        Icon iconQuestion = new ImageIcon(getClass().getResource("/icons/tanya-64x64.png"));

        Icon iconOK = new ImageIcon(getClass().getResource("/icons/check.png"));
        Icon iconNO = new ImageIcon(getClass().getResource("/icons/x.png"));
        JButton okButton = new JButton(BUTTON_OK, iconOK);
        JButton noButton = new JButton(BUTTON_NO, iconNO);
        okButton.addActionListener(new ButtonKeluarListener(aksiKeluarSistem));
        noButton.addActionListener(new ButtonKeluarListener(aksiKeluarSistem));

        Object stringArray[] = {okButton, noButton};

        JOptionPane.showOptionDialog(rootPane, SYSTEM_EXIT_MESSAGE, SYSTEM_EXIT_TITLE, JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, iconQuestion, stringArray, stringArray[0]);
    }

    class ButtonKeluarListener implements ActionListener {

        AksiKeluarSistem mListener;

        public ButtonKeluarListener(final AksiKeluarSistem aksiKeluarSistem) {
            mListener = aksiKeluarSistem;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            Global.closeDialog();
            if (e.getActionCommand().equals(BUTTON_OK)) {
                mListener.run(JOptionPane.OK_OPTION);
                System.exit(0);
            } else {
                mListener.run(JOptionPane.NO_OPTION);
            }
        }
    }
}
