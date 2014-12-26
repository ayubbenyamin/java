/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sppbe;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 *
 * @author idham
 */
public final class Config {

    public static final String BUTTON_OK = "Ya";
    public static final String BUTTON_NO = "Tidak";
    public static final String SYSTEM_EXIT_TITLE = "Keluar Sistem";
    public static final String SYSTEM_EXIT_MESSAGE = "Apakah Anda yakin ingin keluar dari sistem?\n\nBila Anda keluar dari aplikasi maka SMS peringatan jatuh\ntempo akan sangat mungkin tidak bisa disampaikan tepat waktu.\n\nKlik " + BUTTON_OK + " untuk melanjutkan.";
    public static final String TRAY_MENU_SHOW = "Tampilkan Aplikasi";
    public static final String TRAY_MENU_ABOUT = "Tentang";
    public static final String TRAY_MENU_EXIT = "Keluar Aplikasi";
    public static final String TRAY_TOOLTIP = "Aplikasi SMS Peringatan SPPBE";
    public static final EntityManagerFactory EMF = Persistence.createEntityManagerFactory("SPPBEPU");
    public static final String REMOVE_RECORD_TITLE = "Hapus data";
    public static final String REMOVE_RECORD_MESSAGE = "Anda yakin untuk menghapus data yang dipilih?\nKlik " + BUTTON_OK + " untuk melanjutkan";
    public static final String DB_CONNECTION = "jdbc:mysql://localhost:3306/sppbe";
    public static final String DB_USER = "root";
    public static final String DB_PASSWORD = "";
    // Interval ini dihitung dalam detik, contoh:
    // 15 detik = 15 * 1000
    // 1 menit = 1 * 60 * 1000
    // 1 jam = 1 * 60 * 60 * 1000
    // dst
    public static final long INTERVAL_CHECK_SMS = 15 * 1000;
}
