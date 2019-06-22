/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package HeThong;

import QuanLy.QLCD;
import QuanLy.QLHV;
import QuanLy.QLHVKH;
import QuanLy.QLKH;
import QuanLy.QLNV;
import ThongKe_TongHop.BangDiemKhoaHoc;
import ThongKe_TongHop.DoanhThuChuyenDe;
import ThongKe_TongHop.ThongKeNguoiDangKyHoc;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JProgressBar;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

/**
 *
 * @author MSI
 */
public class ChaoJDialog extends JFrame{
    static JProgressBar progressBar;
    static ChaoJDialog chaoJDialogUI;
    public static DangNhapFrame frLogin;
    
    public static void main(String[] args) {
        chaoJDialogUI = new ChaoJDialog();
        
        startProgressbar();
    }
    
    public ChaoJDialog(){
        super();
        
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(ChaoJDialog.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            Logger.getLogger(ChaoJDialog.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            Logger.getLogger(ChaoJDialog.class.getName()).log(Level.SEVERE, null, ex);
        } catch (UnsupportedLookAndFeelException ex) {
            Logger.getLogger(ChaoJDialog.class.getName()).log(Level.SEVERE, null, ex);
        }

        addControls();
        addEvents();
        showWindow();
    }

    private void showWindow() {
        this.setUndecorated(true);
        this.setSize(635, 450);
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        this.setResizable(false);
        this.setLocationRelativeTo(null);
        this.setVisible(true);
        
        ImageIcon icon = new ImageIcon("src/img/logo_apt.png");
        this.setIconImage(icon.getImage());
    }

    private void addControls() {
        Container con = getContentPane();
        con.setLayout(new BorderLayout());
        
        JLabel lblLogo  = new JLabel(new ImageIcon("src/img/background_hello.png"));
        
        progressBar    = new JProgressBar(0, 100);
        
        con.add(lblLogo,BorderLayout.CENTER);
        con.add(progressBar,BorderLayout.SOUTH);
    }

    private void addEvents() {
    }
    
    public static void startProgressbar() {
        int i = 0;

        while (true) {
            i++;

            if (i > 100) {
                chaoJDialogUI.setVisible(false);
                frLogin = new DangNhapFrame();
                frLogin.setVisible(true);
                return;
            }

            progressBar.setValue(i);
            try {
                Thread.sleep(15);
            } catch (InterruptedException ex) {
                Logger.getLogger(ChaoJDialog.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
