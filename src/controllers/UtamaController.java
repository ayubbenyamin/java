/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import sppbe.Config;

/**
 *
 * @author idham
 */
public class UtamaController {

    private final SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");

    public UtamaController() {
    }

    public void kirimPesan() {
        try (Connection conn = DriverManager.getConnection(Config.DB_CONNECTION, Config.DB_USER, Config.DB_PASSWORD)) {
            Statement statement = conn.createStatement();
            pesanPajak(statement);
            pesanPengujian(statement);
            pesanPerizinan(statement);
            pesanSertifikasi(statement);
        } catch (SQLException ex) {
            Logger.getLogger(UtamaController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private List<UserSMS> daftarUser(Statement statement) {
        List<UserSMS> daftarUser = new ArrayList<>();
        try {
            String sql_user = "SELECT * FROM User";
            ResultSet result_user = statement.getConnection().createStatement().executeQuery(sql_user);

            while (result_user.next()) {
                UserSMS userSMS = new UserSMS(result_user.getString("Nama_User"), result_user.getString("No_Hp_User"));
                daftarUser.add(userSMS);
            }

            return daftarUser;
        } catch (SQLException ex) {
            Logger.getLogger(UtamaController.class.getName()).log(Level.SEVERE, null, ex);
        }

        return daftarUser;
    }

    private void kirimPesan(String isiPesan, Statement statement) {

        String sql_kirim_sms = "INSERT INTO outbox (DestinationNumber, UDH, TextDecoded, MultiPart, CreatorID) VALUES (?, ?, ?, ?, 'PPBE')";
        String sql_kirim_sms_multi = "INSERT INTO outbox_multipart (UDH, TextDecoded, ID, SequencePosition) VALUES (?, ?, ?, ?)";
        try {
            for (UserSMS userSMS : daftarUser(statement)) {
                // insert ke table sms
                String[] sms_multi = String.format(isiPesan, userSMS.getNamaUser()).split("(?s)(?<=\\G.{153})");
                int insert_id = 1;
                int pos = 1;

                for (String pesan : sms_multi) {
                    String udh = "050003A7" + String.format("%02d", sms_multi.length) + String.format("%02d", pos);

                    if (pos == 1) {
                        PreparedStatement statement_kirim_sms = statement.getConnection().prepareStatement(sql_kirim_sms, Statement.RETURN_GENERATED_KEYS);
                        statement_kirim_sms.setString(1, userSMS.getNoHP());
                        statement_kirim_sms.setString(2, sms_multi.length == 1 ? null : udh);
                        statement_kirim_sms.setString(3, pesan);
                        statement_kirim_sms.setString(4, sms_multi.length == 1 ? "false" : "true");
                        statement_kirim_sms.executeUpdate();
                        ResultSet generatedKeys = statement_kirim_sms.getGeneratedKeys();
                        if (generatedKeys.next()) {
                            insert_id = generatedKeys.getInt(1);
                        }
                    } else {
                        PreparedStatement statement_kirim_sms_multi = statement.getConnection().prepareStatement(sql_kirim_sms_multi);
                        statement_kirim_sms_multi.setString(1, udh);
                        statement_kirim_sms_multi.setString(2, pesan);
                        statement_kirim_sms_multi.setInt(3, insert_id);
                        statement_kirim_sms_multi.setInt(4, pos);
                        statement_kirim_sms_multi.executeUpdate();
                    }

                    pos++;
                }
                System.out.println("- kepada user: " + userSMS.getNamaUser() + "\n");
            }
        } catch (SQLException ex) {
            Logger.getLogger(UtamaController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void pesanPajak(Statement statement) {
        try {
            String sql_pajak = "SELECT Kode_Pajak, Tgl_Jatuh_Tempo_Pjk FROM Pajak WHERE Tgl_Jatuh_Tempo_Pjk <= LAST_DAY(DATE_ADD(NOW(), INTERVAL 1 MONTH)) AND sms_terkirim='0'";
            ResultSet result_pajak = statement.executeQuery(sql_pajak);

            String sql_update_pajak = "UPDATE Pajak SET sms_terkirim='1' WHERE Kode_Pajak=?";

            while (result_pajak.next()) {
                String sms = "YTH User %s"
                        + ", pajak dengan kode " + result_pajak.getString(1) + " akan memasuki masa jatuh tempo pada tanggal "
                        + sdf.format(result_pajak.getDate(2)) + ", mohon mengambil tindakan perpanjangan.\n\nTerimakasih\\nSistem SPPBE";

                System.out.println("Kirim sms pajak, kode pajak: " + result_pajak.getString(1) + "\n");
                kirimPesan(sms, statement);

                // update tabel pajak
                PreparedStatement statement_update_pajak = statement.getConnection().prepareStatement(sql_update_pajak);
                statement_update_pajak.setString(1, result_pajak.getString(1));

                statement_update_pajak.executeUpdate();
            }
        } catch (SQLException ex) {
            Logger.getLogger(UtamaController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void pesanPengujian(Statement statement) {
        try {
            String sql_pengujian = "SELECT Kode_Pengujian, Tgl_Jatuh_Tempo_Pgjn FROM Pengujian WHERE Tgl_Jatuh_Tempo_Pgjn <= LAST_DAY(DATE_ADD(NOW(), INTERVAL 1 MONTH)) AND sms_terkirim='0'";
            ResultSet result_pengujian = statement.executeQuery(sql_pengujian);

            String sql_update_pengujian = "UPDATE Pengujian SET sms_terkirim='1' WHERE Kode_Pengujian=?";

            while (result_pengujian.next()) {
                String sms = "YTH User %s"
                        + ", pengujian dengan kode " + result_pengujian.getString(1) + " akan memasuki masa jatuh tempo pada tanggal "
                        + sdf.format(result_pengujian.getDate(2)) + ", mohon mengambil tindakan perpanjangan.\n\nTerimakasih\nSistem SPPBE";

                System.out.println("Kirim sms pengujian, kode pengujian: " + result_pengujian.getString(1) + "\n");
                kirimPesan(sms, statement);

                // update tabel pengujian
                PreparedStatement statement_update_pengujian = statement.getConnection().prepareStatement(sql_update_pengujian);
                statement_update_pengujian.setString(1, result_pengujian.getString(1));

                statement_update_pengujian.executeUpdate();
            }
        } catch (SQLException ex) {
            Logger.getLogger(UtamaController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void pesanPerizinan(Statement statement) {
        try {
            String sql_perizinan = "SELECT Kode_Perizinan, Tgl_Jatuh_Tempo_Przn FROM Perizinan WHERE Tgl_Jatuh_Tempo_Przn <= LAST_DAY(DATE_ADD(NOW(), INTERVAL 1 MONTH)) AND sms_terkirim='0'";
            ResultSet result_perizinan = statement.executeQuery(sql_perizinan);

            String sql_update_perizinan = "UPDATE Perizinan SET sms_terkirim='1' WHERE Kode_Perizinan=?";

            while (result_perizinan.next()) {
                String sms = "YTH User %s"
                        + ", perizinan dengan kode " + result_perizinan.getString(1) + " akan memasuki masa jatuh tempo pada tanggal "
                        + sdf.format(result_perizinan.getDate(2)) + ", mohon mengambil tindakan perpanjangan.\n\nTerimakasih\nSistem SPPBE";

                System.out.println("Kirim sms perizinan, kode perizinan: " + result_perizinan.getString(1) + "\n");
                kirimPesan(sms, statement);

                // update tabel perizinan
                PreparedStatement statement_update_perizinan = statement.getConnection().prepareStatement(sql_update_perizinan);
                statement_update_perizinan.setString(1, result_perizinan.getString(1));

                statement_update_perizinan.executeUpdate();
            }
        } catch (SQLException ex) {
            Logger.getLogger(UtamaController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void pesanSertifikasi(Statement statement) {
        try {
            String sql_sertifikasi = "SELECT Kode_Sertifikasi, Tgl_Jatuh_Tempo_Srks FROM Sertifikasi WHERE Tgl_Jatuh_Tempo_Srks <= LAST_DAY(DATE_ADD(NOW(), INTERVAL 1 MONTH)) AND sms_terkirim='0'";
            ResultSet result_sertifikasi = statement.executeQuery(sql_sertifikasi);

            String sql_update_sertifikasi = "UPDATE Sertifikasi SET sms_terkirim='1' WHERE Kode_Sertifikasi=?";

            while (result_sertifikasi.next()) {
                String sms = "YTH User %s"
                        + ", sertifikasi dengan kode " + result_sertifikasi.getString(1) + " akan memasuki masa jatuh tempo pada tanggal "
                        + sdf.format(result_sertifikasi.getDate(2)) + ", mohon mengambil tindakan perpanjangan.\n\nTerimakasih\nSistem SPPBE";

                System.out.println("Kirim sms sertifikasi, kode sertifikasi: " + result_sertifikasi.getString(1) + "\n");
                kirimPesan(sms, statement);

                // update tabel sertifikasi
                PreparedStatement statement_update_sertifikasi = statement.getConnection().prepareStatement(sql_update_sertifikasi);
                statement_update_sertifikasi.setString(1, result_sertifikasi.getString(1));

                statement_update_sertifikasi.executeUpdate();
            }
        } catch (SQLException ex) {
            Logger.getLogger(UtamaController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    class UserSMS {

        private final String no_hp;
        private final String nama_user;

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
}
