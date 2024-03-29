/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package views.reports;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Locale;
import javax.swing.table.DefaultTableModel;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.view.JRSaveContributor;
import net.sf.jasperreports.view.JRViewer;
import net.sf.jasperreports.view.save.JRDocxSaveContributor;
import net.sf.jasperreports.view.save.JRPdfSaveContributor;
import sppbe.Config;

/**
 *
 * @author Gerardo
 */
public class Laporan extends javax.swing.JInternalFrame {

    /**
     * Creates new form perizinan
     */
    public Laporan() {
        initComponents();
        Object row_pajak[] = {"Kode Pajak", "No NPWP", "Jenis Pajak", "Sumber Pajak", "Pokok Pajak", "Tanggal Jatuh Tempo"};
        DefaultTableModel tableModelPajak = new DefaultTableModel(null, row_pajak) {

            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }

        };
        jTable1.setModel(tableModelPajak);

        Object row_perizinan[] = {"Kode Perizinan", "No Perizinan", "Jenis Perizinan", "Kegunaan Perizinan", "Sumber Perizinan", "Tanggal Jatuh Tempo"};
        DefaultTableModel tableModelPerizinan = new DefaultTableModel(null, row_perizinan) {

            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }

        };
        jTable2.setModel(tableModelPerizinan);

        Object row_pengujian[] = {"Kode Pengujian", "No Pengujian", "Jenis Pengujian", "Metode Pengujian", "Sumber Pengujian", "Tanggal Jatuh Tempo"};
        DefaultTableModel tableModelPengujian = new DefaultTableModel(null, row_pengujian) {

            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }

        };
        jTable3.setModel(tableModelPengujian);

        Object row_sertifikasi[] = {"Kode Sertifikasi", "No Sertifikasi", "Jenis Sertifikasi", "Sumber Sertifikasi", "Tanggal Jatuh Tempo"};
        DefaultTableModel tableModelSertifikasi = new DefaultTableModel(null, row_sertifikasi) {

            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }

        };
        jTable4.setModel(tableModelSertifikasi);

        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");

        try (Connection conn = DriverManager.getConnection(Config.DB_CONNECTION, Config.DB_USER, Config.DB_PASSWORD)) {

            Statement statement = conn.createStatement();

            String sql_pajak = "SELECT * FROM Pajak WHERE Tgl_Jatuh_Tempo_Pjk <= LAST_DAY(DATE_ADD(NOW(), INTERVAL 1 MONTH))";
            ResultSet result_pajak = statement.executeQuery(sql_pajak);

            while (result_pajak.next()) {
                String[] data = {
                    result_pajak.getString(1),
                    result_pajak.getString(3),
                    result_pajak.getString(4),
                    result_pajak.getString(5),
                    result_pajak.getString(6),
                    sdf.format(result_pajak.getDate(7))
                };
                tableModelPajak.addRow(data);
            }

            String sql_perizinan = "SELECT * FROM Perizinan WHERE Tgl_Jatuh_Tempo_Przn <= LAST_DAY(DATE_ADD(NOW(), INTERVAL 1 MONTH))";
            ResultSet result_perizinan = statement.executeQuery(sql_perizinan);

            while (result_perizinan.next()) {
                String[] data = {
                    result_perizinan.getString(1),
                    result_perizinan.getString(3),
                    result_perizinan.getString(4),
                    result_perizinan.getString(5),
                    result_perizinan.getString(6),
                    sdf.format(result_perizinan.getDate(7))
                };
                tableModelPerizinan.addRow(data);
            }

            String sql_pengujian = "SELECT * FROM Pengujian WHERE Tgl_Jatuh_Tempo_Pgjn <= LAST_DAY(DATE_ADD(NOW(), INTERVAL 1 MONTH))";
            ResultSet result_pengujian = statement.executeQuery(sql_pengujian);

            while (result_pengujian.next()) {
                String[] data = {
                    result_pengujian.getString(1),
                    result_pengujian.getString(3),
                    result_pengujian.getString(4),
                    result_pengujian.getString(5),
                    result_pengujian.getString(7),
                    sdf.format(result_pengujian.getDate(6))
                };
                tableModelPengujian.addRow(data);
            }

            String sql_sertifikasi = "SELECT * FROM Sertifikasi WHERE Tgl_Jatuh_Tempo_Srks <= LAST_DAY(DATE_ADD(NOW(), INTERVAL 1 MONTH))";
            ResultSet result_sertifikasi = statement.executeQuery(sql_sertifikasi);

            while (result_sertifikasi.next()) {
                String[] data = {
                    result_sertifikasi.getString(1),
                    result_sertifikasi.getString(3),
                    result_sertifikasi.getString(4),
                    result_sertifikasi.getString(5),
                    sdf.format(result_sertifikasi.getDate(6))
                };
                tableModelSertifikasi.addRow(data);
            }

        } catch (SQLException ex) {
        }
    }

    private void cetak(JasperPrint jasperPrint, String judul) {
        JRViewer jv = new JRViewer(jasperPrint);
        jv.setSaveContributors(new JRSaveContributor[]{
            new JRPdfSaveContributor(Locale.getDefault(), null),
            new JRDocxSaveContributor(Locale.getDefault(), null)
        });

        CetakLaporan.cetak(jv, judul);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jSeparator4 = new javax.swing.JSeparator();
        jSeparator3 = new javax.swing.JSeparator();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTable2 = new javax.swing.JTable();
        jScrollPane3 = new javax.swing.JScrollPane();
        jTable3 = new javax.swing.JTable();
        jScrollPane4 = new javax.swing.JScrollPane();
        jTable4 = new javax.swing.JTable();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();

        setBackground(new java.awt.Color(255, 255, 255));
        setClosable(true);
        setTitle("Form Laporan");
        setMinimumSize(new java.awt.Dimension(558, 33));

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane1.setViewportView(jTable1);

        jLabel11.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        jLabel11.setText("S P P B E");

        jLabel12.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel12.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel12.setText("Data Laporan");

        jLabel7.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel7.setText("PT. PUTRA SINDO");

        jLabel8.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel8.setText("Data Laporan Peringatan Pajak :");

        jLabel9.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel9.setText("Data Laporan Peringatan Perizinan :");

        jLabel13.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel13.setText("Data Laporan Peringatan Pengujian :");

        jLabel14.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel14.setText("Data Laporan Peringatan Sertifikasi :");

        jTable2.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane2.setViewportView(jTable2);

        jTable3.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane3.setViewportView(jTable3);

        jTable4.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane4.setViewportView(jTable4);

        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/printer.png"))); // NOI18N
        jButton1.setText("Print Laporan");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/printer.png"))); // NOI18N
        jButton2.setText("Print Laporan");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jButton3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/printer.png"))); // NOI18N
        jButton3.setText("Print Laporan");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        jButton4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/printer.png"))); // NOI18N
        jButton4.setText("Print Laporan");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jSeparator3, javax.swing.GroupLayout.Alignment.TRAILING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(44, 44, 44)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jSeparator4)
                    .addComponent(jLabel11)
                    .addComponent(jLabel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 807, Short.MAX_VALUE)
                .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 122, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(103, Short.MAX_VALUE))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel9)
                            .addComponent(jLabel13)
                            .addComponent(jLabel14)
                            .addComponent(jLabel8)))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jButton3)
                            .addComponent(jButton1)
                            .addComponent(jButton2)
                            .addComponent(jButton4))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addComponent(jScrollPane1)
            .addComponent(jScrollPane2, javax.swing.GroupLayout.Alignment.TRAILING)
            .addComponent(jScrollPane3)
            .addComponent(jScrollPane4, javax.swing.GroupLayout.Alignment.TRAILING)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel11)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jSeparator4, javax.swing.GroupLayout.PREFERRED_SIZE, 5, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jLabel12))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator3, javax.swing.GroupLayout.PREFERRED_SIZE, 5, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel8)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 84, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(3, 3, 3)
                .addComponent(jLabel9)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(1, 1, 1)
                .addComponent(jLabel13)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel14)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        try {
            try (Connection conn = DriverManager.getConnection(Config.DB_CONNECTION, Config.DB_USER, Config.DB_PASSWORD)) {
                HashMap param = new HashMap();
                param.clear();
                JasperPrint jasperPrint = JasperFillManager.fillReport("src/views/reports/laporan_pajak.jasper", param, conn);
                cetak(jasperPrint, "Cetak Laporan Pajak");
            } catch (SQLException ex) {
            }
        } catch (Exception e) {
        }
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        try {
            try (Connection conn = DriverManager.getConnection(Config.DB_CONNECTION, Config.DB_USER, Config.DB_PASSWORD)) {
                HashMap param = new HashMap();
                param.clear();
                JasperPrint jasperPrint = JasperFillManager.fillReport("src/views/reports/laporan_perizinan.jasper", param, conn);
                cetak(jasperPrint, "Cetak Laporan Perizinan");
            } catch (SQLException ex) {
            }
        } catch (Exception e) {
        }
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        try {
            try (Connection conn = DriverManager.getConnection(Config.DB_CONNECTION, Config.DB_USER, Config.DB_PASSWORD)) {
                HashMap param = new HashMap();
                param.clear();
                JasperPrint jasperPrint = JasperFillManager.fillReport("src/views/reports/laporan_pengujian.jasper", param, conn);
                cetak(jasperPrint, "Cetak Laporan Pengujian");
            } catch (SQLException ex) {
            }
        } catch (Exception e) {
        }
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        try {
            try (Connection conn = DriverManager.getConnection(Config.DB_CONNECTION, Config.DB_USER, Config.DB_PASSWORD)) {
                HashMap param = new HashMap();
                param.clear();
                JasperPrint jasperPrint = JasperFillManager.fillReport("src/views/reports/laporan_sertifikasi.jasper", param, conn);
                cetak(jasperPrint, "Cetak Laporan Sertifikasi");
            } catch (SQLException ex) {
            }
        } catch (Exception e) {
        }
    }//GEN-LAST:event_jButton4ActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JSeparator jSeparator3;
    private javax.swing.JSeparator jSeparator4;
    private javax.swing.JTable jTable1;
    private javax.swing.JTable jTable2;
    private javax.swing.JTable jTable3;
    private javax.swing.JTable jTable4;
    // End of variables declaration//GEN-END:variables
}
