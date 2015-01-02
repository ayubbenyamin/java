/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package views;

import views.reports.Laporan;
import java.awt.AWTException;
import java.awt.Container;
import java.awt.Image;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.beans.PropertyVetoException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JOptionPane;
import javax.swing.plaf.basic.BasicInternalFrameUI;
import sppbe.AksiKeluarSistem;
import sppbe.Config;
import static sppbe.Config.INTERVAL_CHECK_SMS;
import static sppbe.Config.TRAY_MENU_ABOUT;
import static sppbe.Config.TRAY_MENU_EXIT;
import static sppbe.Config.TRAY_MENU_SHOW;
import static sppbe.Config.TRAY_TOOLTIP;
import sppbe.Global;

/**
 *
 * @author Gerardo
 */
public class Utama extends javax.swing.JFrame {

    TrayIcon trayIcon = null;
    Timer mTimer = null;
    Timer mCheckTimer = null;
    int interval = (int) (1.5 * 1000); // 1.5 seconds
    static boolean isON = true;

    class UserSMS {

        private String no_hp;
        private String nama_user;

        UserSMS(String nama_user, String no_hp) {
            this.no_hp = no_hp;
            this.nama_user = nama_user;
        }

        private String getNoHP() {
            return no_hp;
        }

        private String getNamaUser() {
            return nama_user;
        }
    }

    /**
     * Creates new form utama
     */
    public Utama() {
        initComponents();

        mCheckTimer = new Timer();

        mCheckTimer.scheduleAtFixedRate(new TimerTask() {

            @Override
            public void run() {
                System.out.println("Interval cek sms berjalan\n");
                SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");

                try (Connection conn = DriverManager.getConnection(Config.DB_CONNECTION, Config.DB_USER, Config.DB_PASSWORD)) {

                    Statement statement_user = conn.createStatement();
                    Statement statement_pajak = conn.createStatement();
                    Statement statement_pengujian = conn.createStatement();
                    Statement statement_perizinan = conn.createStatement();
                    Statement statement_sertifikasi = conn.createStatement();

                    String sql_kirim_sms = "INSERT INTO outbox (DestinationNumber, TextDecoded) VALUES (?, ?)";

                    String sql_user = "SELECT * FROM User";
                    ResultSet result_user = statement_user.executeQuery(sql_user);

                    List<UserSMS> userSMSList = new ArrayList<UserSMS>();

                    while (result_user.next()) {
                        UserSMS userSMS = new UserSMS(result_user.getString("Nama_User"), result_user.getString("No_Hp_User"));
                        userSMSList.add(userSMS);
                    }

                    String sql_pajak = "SELECT Kode_Pajak, Tgl_Jatuh_Tempo_Pjk FROM Pajak WHERE Tgl_Jatuh_Tempo_Pjk <= LAST_DAY(DATE_ADD(NOW(), INTERVAL 1 MONTH)) AND sms_terkirim='0'";
                    ResultSet result_pajak = statement_pajak.executeQuery(sql_pajak);

                    String sql_update_pajak = "UPDATE Pajak SET sms_terkirim='1' WHERE Kode_Pajak=?";

                    while (result_pajak.next()) {
                        for (UserSMS userSMS : userSMSList) {
                            String sms = "YTH User " + userSMS.getNamaUser()
                                    + ", pajak dengan kode " + result_pajak.getString(1) + " akan memasuki masa jatuh tempo pada tanggal "
                                    + sdf.format(result_pajak.getDate(2)) + ", mohon mengambil tindakan perpanjangan.\n\nTerimakasih\nSistem SPPBE";
                            // insert ke table sms
                            PreparedStatement statement_kirim_sms = conn.prepareStatement(sql_kirim_sms);
                            statement_kirim_sms.setString(1, userSMS.getNoHP());
                            statement_kirim_sms.setString(2, sms);
                            statement_kirim_sms.executeUpdate();

                            System.out.println("User kode pajak " + result_pajak.getString(1) + "\n");
                        }
                        // update tabel pajak
                        PreparedStatement statement_update_pajak = conn.prepareStatement(sql_update_pajak);
                        statement_update_pajak.setString(1, result_pajak.getString(1));

                        statement_update_pajak.executeUpdate();
                    }

                    String sql_pengujian = "SELECT Kode_Pengujian, Tgl_Jatuh_Tempo_Pgjn FROM Pengujian WHERE Tgl_Jatuh_Tempo_Pgjn <= LAST_DAY(DATE_ADD(NOW(), INTERVAL 1 MONTH)) AND sms_terkirim='0'";
                    ResultSet result_pengujian = statement_pengujian.executeQuery(sql_pengujian);

                    String sql_update_pengujian = "UPDATE Pengujian SET sms_terkirim='1' WHERE Kode_Pengujian=?";

                    while (result_pengujian.next()) {
                        for (UserSMS userSMS : userSMSList) {
                            String sms = "YTH User " + userSMS.getNamaUser()
                                    + ", pengujian dengan kode " + result_pengujian.getString(1) + " akan memasuki masa jatuh tempo pada tanggal "
                                    + sdf.format(result_pengujian.getDate(2)) + ", mohon mengambil tindakan perpanjangan.\n\nTerimakasih\nSistem SPPBE";
                            // insert ke table sms
                            PreparedStatement statement_kirim_sms = conn.prepareStatement(sql_kirim_sms);
                            statement_kirim_sms.setString(1, userSMS.getNoHP());
                            statement_kirim_sms.setString(2, sms);
                            statement_kirim_sms.executeUpdate();

                            System.out.println("User pengujian kode" + result_pengujian.getString(1) + "\n");
                        }
                        // update tabel pengujian
                        PreparedStatement statement_update_pengujian = conn.prepareStatement(sql_update_pengujian);
                        statement_update_pengujian.setString(1, result_pengujian.getString(1));

                        statement_update_pengujian.executeUpdate();
                    }

                    String sql_perizinan = "SELECT Kode_Perizinan, Tgl_Jatuh_Tempo_Przn FROM Perizinan WHERE Tgl_Jatuh_Tempo_Przn <= LAST_DAY(DATE_ADD(NOW(), INTERVAL 1 MONTH)) AND sms_terkirim='0'";
                    ResultSet result_perizinan = statement_perizinan.executeQuery(sql_perizinan);

                    String sql_update_perizinan = "UPDATE Perizinan SET sms_terkirim='1' WHERE Kode_Perizinan=?";

                    while (result_perizinan.next()) {
                        for (UserSMS userSMS : userSMSList) {
                            String sms = "YTH User " + userSMS.getNamaUser()
                                    + ", perizinan dengan kode " + result_perizinan.getString(1) + " akan memasuki masa jatuh tempo pada tanggal "
                                    + sdf.format(result_perizinan.getDate(2)) + ", mohon mengambil tindakan perpanjangan.\n\nTerimakasih\nSistem SPPBE";
                            // insert ke table sms
                            PreparedStatement statement_kirim_sms = conn.prepareStatement(sql_kirim_sms);
                            statement_kirim_sms.setString(1, userSMS.getNoHP());
                            statement_kirim_sms.setString(2, sms);
                            statement_kirim_sms.executeUpdate();

                            System.out.println("User perizinan kode " + result_perizinan.getString(1) + "\n");
                        }
                        // update tabel perizinan
                        PreparedStatement statement_update_perizinan = conn.prepareStatement(sql_update_perizinan);
                        statement_update_perizinan.setString(1, result_perizinan.getString(1));

                        statement_update_perizinan.executeUpdate();
                    }

                    String sql_sertifikasi = "SELECT Kode_Sertifikasi, Tgl_Jatuh_Tempo_Srks FROM Sertifikasi WHERE Tgl_Jatuh_Tempo_Srks <= LAST_DAY(DATE_ADD(NOW(), INTERVAL 1 MONTH)) AND sms_terkirim='0'";
                    ResultSet result_sertifikasi = statement_sertifikasi.executeQuery(sql_sertifikasi);

                    String sql_update_sertifikasi = "UPDATE Sertifikasi SET sms_terkirim='1' WHERE Kode_Sertifikasi=?";

                    while (result_sertifikasi.next()) {
                        for (UserSMS userSMS : userSMSList) {
                            String sms = "YTH User " + userSMS.getNamaUser()
                                    + ", sertifikasi dengan kode " + result_sertifikasi.getString(1) + " akan memasuki masa jatuh tempo pada tanggal "
                                    + sdf.format(result_sertifikasi.getDate(2)) + ", mohon mengambil tindakan perpanjangan.\n\nTerimakasih\nSistem SPPBE";
                            // insert ke table sms
                            PreparedStatement statement_kirim_sms = conn.prepareStatement(sql_kirim_sms);
                            statement_kirim_sms.setString(1, userSMS.getNoHP());
                            statement_kirim_sms.setString(2, sms);
                            statement_kirim_sms.executeUpdate();

                            System.out.println("User sertifikasi kode " + result_sertifikasi.getString(1) + "\n");
                        }
                        // update tabel sertifikasi
                        PreparedStatement statement_update_sertifikasi = conn.prepareStatement(sql_update_sertifikasi);
                        statement_update_sertifikasi.setString(1, result_sertifikasi.getString(1));

                        statement_update_sertifikasi.executeUpdate();
                    }

                } catch (SQLException ex) {
                }
            }
        }, 0, INTERVAL_CHECK_SMS);

        this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        this.addWindowListener(new WindowListener() {

            @Override
            public void windowOpened(WindowEvent e) {
            }

            @Override
            public void windowClosing(WindowEvent e) {
                // Tutup semua midi form
                JInternalFrame[] children = jDesktopPane1.getAllFrames();
                for (JInternalFrame f : children) {
                    f.dispose();
                }

                systemTrayIcon();
            }

            @Override
            public void windowClosed(WindowEvent e) {
                if (mCheckTimer != null) {
                    mCheckTimer.cancel();
                }
                System.out.println("keluar");
            }

            @Override
            public void windowIconified(WindowEvent e) {
            }

            @Override
            public void windowDeiconified(WindowEvent e) {
            }

            @Override
            public void windowActivated(WindowEvent e) {
            }

            @Override
            public void windowDeactivated(WindowEvent e) {
            }
        });
    }

    private void systemTrayIcon() {
        if (SystemTray.isSupported()) {
            // get the SystemTray instance
            final SystemTray tray = SystemTray.getSystemTray();
            // load an image
            Image image = (new ImageIcon(getClass().getResource("/icons/message.png"))).getImage();

            // create a action listener to listen for default action executed on the tray icon
            ActionListener menuListener = new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    switch (e.getActionCommand()) {
                        case TRAY_MENU_SHOW:
                            if (mTimer != null) {
                                mTimer.cancel();
                            }
                            tray.remove(trayIcon);
                            setVisible(true);
                            break;
                        case TRAY_MENU_ABOUT:
                            break;
                        default:
                            Global.konfirmasiKeluar(rootPane, new AksiKeluarSistem() {

                                @Override
                                public void run(int status) {
                                    if (status == JOptionPane.OK_OPTION) {
                                        if (mTimer != null) {
                                            mTimer.cancel();
                                        }
                                        if (trayIcon != null) {
                                            tray.remove(trayIcon);
                                        }
                                    }
                                }
                            });
                            break;
                    }

                }
            };

            ActionListener trayListener = new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    trayIcon.displayMessage("Action Event",
                            "An Action Event Has Been Peformed!",
                            TrayIcon.MessageType.INFO);
                }
            };
            // create a popup menu
            PopupMenu popup = new PopupMenu();

            // create menu item
            MenuItem utama = new MenuItem(TRAY_MENU_SHOW);
            MenuItem tentang = new MenuItem(TRAY_MENU_ABOUT);
            MenuItem tutup = new MenuItem(TRAY_MENU_EXIT);

            utama.addActionListener(menuListener);
            tutup.addActionListener(menuListener);

            popup.add(utama);
            popup.addSeparator();
            popup.add(tentang);
            popup.add(tutup);

            // construct a TrayIcon
            trayIcon = new TrayIcon(image, TRAY_TOOLTIP, popup);
            //adjust to default size as per system recommendation
            trayIcon.setImageAutoSize(true);
            // set the TrayIcon properties
            trayIcon.addActionListener(trayListener);

            try {
                tray.add(trayIcon);
                setVisible(false);
                if (mTimer != null) {
                    mTimer.cancel();
                }
                mTimer = new Timer();
                // schedule task
                mTimer.scheduleAtFixedRate(new ChangeTrayIcon(trayIcon), 0, interval);
            } catch (AWTException ex) {
                System.err.println(ex);
            }
        } else {
            Global.konfirmasiKeluar(rootPane);
        }
    }

    private boolean formHasCreated(Object object) {
        JInternalFrame[] children = jDesktopPane1.getAllFrames();
        for (JInternalFrame form : children) {
            if (form.getClass().getCanonicalName().equals(object.getClass().getCanonicalName())) {
                try {
                    form.setSelected(true);
                } catch (PropertyVetoException ex) {
                }
                return true;
            }
        }
        return false;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jDesktopPane1 = new javax.swing.JDesktopPane();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jSeparator2 = new javax.swing.JSeparator();
        jSeparator3 = new javax.swing.JSeparator();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        jMenu11 = new javax.swing.JMenu();
        jMenu12 = new javax.swing.JMenu();
        jMenu4 = new javax.swing.JMenu();
        jMenu5 = new javax.swing.JMenu();
        jMenu6 = new javax.swing.JMenu();
        jMenu7 = new javax.swing.JMenu();
        jMenuItem1 = new javax.swing.JMenuItem();
        jMenuItem2 = new javax.swing.JMenuItem();
        jMenu9 = new javax.swing.JMenu();
        jMenu10 = new javax.swing.JMenu();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setMinimumSize(new java.awt.Dimension(1000, 0));

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        jLabel1.setText("S P P B E");

        jLabel2.setText("PT. PUTRA SINDO");

        jLabel3.setFont(new java.awt.Font("Cambria", 1, 24)); // NOI18N
        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel3.setText(":: Selamat Datang ::");

        jLabel4.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel4.setText("MENU UTAMA");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jSeparator3)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(60, 60, 60)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jSeparator2)
                    .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 890, Short.MAX_VALUE)
                .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 153, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(23, 23, 23)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 5, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(jLabel4))
                .addGap(23, 23, 23)
                .addComponent(jSeparator3, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 182, Short.MAX_VALUE)
                .addComponent(jLabel3)
                .addContainerGap(213, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jDesktopPane1Layout = new javax.swing.GroupLayout(jDesktopPane1);
        jDesktopPane1.setLayout(jDesktopPane1Layout);
        jDesktopPane1Layout.setHorizontalGroup(
            jDesktopPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jDesktopPane1Layout.setVerticalGroup(
            jDesktopPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jDesktopPane1.setLayer(jPanel1, javax.swing.JLayeredPane.DEFAULT_LAYER);

        jMenu1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/user.png"))); // NOI18N
        jMenu1.setText("User     ");
        jMenu1.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jMenu1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jMenu1MouseClicked(evt);
            }
        });
        jMenuBar1.add(jMenu1);

        jMenu11.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/pajak.png"))); // NOI18N
        jMenu11.setText("Pajak     ");
        jMenu11.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jMenu11.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jMenu11MouseClicked(evt);
            }
        });
        jMenuBar1.add(jMenu11);

        jMenu12.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/perizinan.png"))); // NOI18N
        jMenu12.setText("Perizinan     ");
        jMenu12.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jMenu12.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jMenu12MouseClicked(evt);
            }
        });
        jMenuBar1.add(jMenu12);

        jMenu4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/pengujian.png"))); // NOI18N
        jMenu4.setText("Pengujian     ");
        jMenu4.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jMenu4.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jMenu4MouseClicked(evt);
            }
        });
        jMenuBar1.add(jMenu4);

        jMenu5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/sertifikasi16.png"))); // NOI18N
        jMenu5.setText("Sertifikasi     ");
        jMenu5.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jMenu5.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jMenu5MouseClicked(evt);
            }
        });
        jMenuBar1.add(jMenu5);

        jMenu6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/perpanjangan.png"))); // NOI18N
        jMenu6.setText("Perpanjangan     ");
        jMenu6.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jMenu6.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jMenu6MouseClicked(evt);
            }
        });
        jMenuBar1.add(jMenu6);

        jMenu7.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/peringatan.png"))); // NOI18N
        jMenu7.setText("Peringatan     ");
        jMenu7.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N

        jMenuItem1.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jMenuItem1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/sms.png"))); // NOI18N
        jMenuItem1.setText("SMS Peringatan");
        jMenuItem1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jMenuItem1MousePressed(evt);
            }
        });
        jMenu7.add(jMenuItem1);

        jMenuItem2.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jMenuItem2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/laporan.png"))); // NOI18N
        jMenuItem2.setText("Laporan");
        jMenuItem2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jMenuItem2MousePressed(evt);
            }
        });
        jMenu7.add(jMenuItem2);

        jMenuBar1.add(jMenu7);

        jMenu9.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/admin.png"))); // NOI18N
        jMenu9.setText("Admin     ");
        jMenu9.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jMenu9.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jMenu9MouseClicked(evt);
            }
        });
        jMenuBar1.add(jMenu9);

        jMenu10.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/logout.png"))); // NOI18N
        jMenu10.setText("Logout");
        jMenu10.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jMenu10.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jMenu10MouseClicked(evt);
            }
        });
        jMenuBar1.add(jMenu10);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jDesktopPane1)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jDesktopPane1, javax.swing.GroupLayout.Alignment.TRAILING)
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void jMenu9MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jMenu9MouseClicked

        Admin admin1 = new Admin();
        if (!formHasCreated(admin1)) {
            jDesktopPane1.add(admin1);
            admin1.show();
        }

        BasicInternalFrameUI x = (BasicInternalFrameUI) admin1.getUI();
        Container north = (Container) x.getNorthPane();
        north.remove(0);
        north.validate();
        north.repaint();

        try {
            admin1.setMaximum(true);
        } catch (PropertyVetoException e) {
            //maximize otomatis
        }
    }//GEN-LAST:event_jMenu9MouseClicked

    private void jMenu1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jMenu1MouseClicked

        User user1 = new User();
        if (!formHasCreated(user1)) {
            jDesktopPane1.add(user1);
            user1.show();
        }

        BasicInternalFrameUI x = (BasicInternalFrameUI) user1.getUI();
        Container north = (Container) x.getNorthPane();
        north.remove(0);
        north.validate();
        north.repaint();

        try {
            user1.setMaximum(true);
        } catch (PropertyVetoException e) {
            //maximize otomatis
        }
    }//GEN-LAST:event_jMenu1MouseClicked

    private void jMenu11MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jMenu11MouseClicked

        Pajak pajak1 = new Pajak();
        if (!formHasCreated(pajak1)) {
            jDesktopPane1.add(pajak1);
            pajak1.show();
        }

        BasicInternalFrameUI x = (BasicInternalFrameUI) pajak1.getUI();
        Container north = (Container) x.getNorthPane();
        north.remove(0);
        north.validate();
        north.repaint();

        try {
            pajak1.setMaximum(true);
        } catch (PropertyVetoException e) {
            //maximize otomatis
        }
    }//GEN-LAST:event_jMenu11MouseClicked

    private void jMenu12MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jMenu12MouseClicked

        Perizinan perizinan1 = new Perizinan();
        if (!formHasCreated(perizinan1)) {
            jDesktopPane1.add(perizinan1);
            perizinan1.show();
        }

        BasicInternalFrameUI x = (BasicInternalFrameUI) perizinan1.getUI();
        Container north = (Container) x.getNorthPane();
        north.remove(0);
        north.validate();
        north.repaint();

        try {
            perizinan1.setMaximum(true);
        } catch (PropertyVetoException e) {
            //maximize otomatis
        }
    }//GEN-LAST:event_jMenu12MouseClicked

    private void jMenu4MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jMenu4MouseClicked

        Pengujian pengujian1 = new Pengujian();
        if (!formHasCreated(pengujian1)) {
            jDesktopPane1.add(pengujian1);
            pengujian1.show();
        }

        BasicInternalFrameUI x = (BasicInternalFrameUI) pengujian1.getUI();
        Container north = (Container) x.getNorthPane();
        north.remove(0);
        north.validate();
        north.repaint();

        try {
            pengujian1.setMaximum(true);
        } catch (PropertyVetoException e) {
            //maximize otomatis
        }
    }//GEN-LAST:event_jMenu4MouseClicked

    private void jMenu5MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jMenu5MouseClicked

        Sertifikasi sertifikasi1 = new Sertifikasi();
        if (!formHasCreated(sertifikasi1)) {
            jDesktopPane1.add(sertifikasi1);
            sertifikasi1.show();
        }

        BasicInternalFrameUI x = (BasicInternalFrameUI) sertifikasi1.getUI();
        Container north = (Container) x.getNorthPane();
        north.remove(0);
        north.validate();
        north.repaint();

        try {
            sertifikasi1.setMaximum(true);
        } catch (PropertyVetoException e) {
            //maximize otomatis
        }
    }//GEN-LAST:event_jMenu5MouseClicked

    private void jMenu6MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jMenu6MouseClicked

        Perpanjangan perpanjangan1 = new Perpanjangan();
        if (!formHasCreated(perpanjangan1)) {
            jDesktopPane1.add(perpanjangan1);
            perpanjangan1.show();
        }

        BasicInternalFrameUI x = (BasicInternalFrameUI) perpanjangan1.getUI();
        Container north = (Container) x.getNorthPane();
        north.remove(0);
        north.validate();
        north.repaint();

        try {
            perpanjangan1.setMaximum(true);
        } catch (PropertyVetoException e) {
            //maximize otomatis
        }
    }//GEN-LAST:event_jMenu6MouseClicked

    private void jMenu10MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jMenu10MouseClicked
        Global.konfirmasiKeluar(rootPane);
    }//GEN-LAST:event_jMenu10MouseClicked

    private void jMenuItem2MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jMenuItem2MousePressed
        Laporan laporan = new Laporan();
        if (!formHasCreated(laporan)) {
            jDesktopPane1.add(laporan);
            laporan.show();
        }

        BasicInternalFrameUI x = (BasicInternalFrameUI) laporan.getUI();
        Container north = (Container) x.getNorthPane();
        north.remove(0);
        north.validate();
        north.repaint();

        try {
            laporan.setMaximum(true);
        } catch (PropertyVetoException e) {
            //maximize otomatis
        }
    }//GEN-LAST:event_jMenuItem2MousePressed

    private void jMenuItem1MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jMenuItem1MousePressed
        Smsperingatan smsperingatan1 = new Smsperingatan();
        if (!formHasCreated(smsperingatan1)) {
            jDesktopPane1.add(smsperingatan1);
            smsperingatan1.show();
        }

        BasicInternalFrameUI x = (BasicInternalFrameUI) smsperingatan1.getUI();
        Container north = (Container) x.getNorthPane();
        north.remove(0);
        north.validate();
        north.repaint();

        try {
            smsperingatan1.setMaximum(true);
        } catch (PropertyVetoException e) {
            //maximize otomatis
        }
    }//GEN-LAST:event_jMenuItem1MousePressed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JDesktopPane jDesktopPane1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu10;
    private javax.swing.JMenu jMenu11;
    private javax.swing.JMenu jMenu12;
    private javax.swing.JMenu jMenu4;
    private javax.swing.JMenu jMenu5;
    private javax.swing.JMenu jMenu6;
    private javax.swing.JMenu jMenu7;
    private javax.swing.JMenu jMenu9;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem jMenuItem2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JSeparator jSeparator3;
    // End of variables declaration//GEN-END:variables

    private static class ChangeTrayIcon extends TimerTask {

        TrayIcon timerTrayIcon;

        public ChangeTrayIcon(TrayIcon trayIcon) {
            timerTrayIcon = trayIcon;
        }

        @Override
        public void run() {
            if (timerTrayIcon != null) {
                Image image = (new ImageIcon(getClass().getResource("/icons/message.png"))).getImage();
                Image image_blue = (new ImageIcon(getClass().getResource("/icons/message_blue.png"))).getImage();
                if (isON) {
                    timerTrayIcon.setImage(image);
                    isON = false;
                } else {
                    timerTrayIcon.setImage(image_blue);
                    isON = true;
                }
            }
        }
    }
}
