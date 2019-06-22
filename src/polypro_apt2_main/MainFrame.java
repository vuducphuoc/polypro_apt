/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package polypro_apt2_main;

// <editor-fold defaultstate="collapsed" desc="IMPORT ">
import HeThong.ChangePassFrame;
import HeThong.ChaoJDialog;
import HeThong.DangNhapFrame;
import Helper.JdbcHelper;
import QuanLy.QLCD;
import QuanLy.QLHV;
import QuanLy.QLHVKH;
import QuanLy.QLKH;
import QuanLy.QLNV;
import ThongKe_TongHop.BangDiemKhoaHoc;
import ThongKe_TongHop.DoanhThuChuyenDe;
import ThongKe_TongHop.ThongKeNguoiDangKyHoc;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
// </editor-fold>

/**
 *
 * @author MSI
 */
public class MainFrame extends JFrame {

    JList listFunc;
    JPanel pnDetailFunc;
//    public static BangDiemKhoaHoc pnTHBĐ;
//    public static DoanhThuChuyenDe pnTKDT;
//    public static QLCD pnQLCĐ;
//    public static QLKH pnQLKH;
//    public static QLNV pnQLNV;
//    public static QLHV pnQLHV;
//    public static QLHVKH pnQLHVKH;
//    public static ThongKeNguoiDangKyHoc pnTKHV;

    CardLayout cardLayout;
    JLabel lblTime, lblTitleDetailFunc;
    JMenuBar mnuBar;
    JMenu mnuSystem;
    public static JMenuItem mnuSys_Exit, mnuSys_Signout, mnuSys_Changepass;
    public JMenuItem mnuSys_showUsername;
    JFrame frChangePass, frLogin;
    JFrame frThis = this;

    final int ROLE_TRUONG_PHONG = 1;

    public MainFrame() {
        super("HỆ THỐNG QUẢN LÝ KHÓA HỌC - NHÓM APT");
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
        } catch (UnsupportedLookAndFeelException ex) {
            Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
        addControls();
        addEvents();
        showWindow();
    }

    // <editor-fold defaultstate="collapsed" desc="SHOW WINDOW ">
    private void showWindow() {
        this.setSize(1500, 800);
        this.setResizable(true);
        this.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        this.setLocationRelativeTo(null);
        //set icon ứng dụng
        ImageIcon icon = new ImageIcon("src/img/logo_apt.png");
        this.setIconImage(icon.getImage());
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="ADD CONTROLS ">
    private void addControls() {
        Container con = getContentPane();
        con.setLayout(new BorderLayout(10, 0));

        //Menubar
        mnuBar = new JMenuBar();
        mnuSystem = new JMenu("Tùy chọn");
        mnuSys_showUsername = new JMenuItem("Nguyễn Văn A");
        mnuSys_Changepass = new JMenuItem("Thay đổi mật khẩu");
        mnuSys_Signout = new JMenuItem("Đăng xuất");
        mnuSys_Exit = new JMenuItem("Thoát");

        mnuSystem.setFont(new Font("Segoe UI", Font.ROMAN_BASELINE, 16));
        mnuSys_Changepass.setFont(new Font("Segoe UI", Font.ROMAN_BASELINE, 14));
        mnuSys_Signout.setFont(new Font("Segoe UI", Font.ROMAN_BASELINE, 14));
        mnuSys_Exit.setFont(new Font("Segoe UI", Font.ROMAN_BASELINE, 14));
        mnuSys_showUsername.setFont(new Font("Segoe UI", Font.BOLD, 16));
        mnuSys_showUsername.setForeground(Color.BLUE);

        mnuSystem.add(mnuSys_showUsername);
        mnuSystem.addSeparator();
        mnuSystem.add(mnuSys_Changepass);
        mnuSystem.add(mnuSys_Signout);
        mnuSystem.addSeparator();
        mnuSystem.add(mnuSys_Exit);
        mnuBar.add(mnuSystem);

        con.add(mnuBar, BorderLayout.NORTH);

        //Panel chức năng
        JPanel pnFunction = new JPanel(new BorderLayout());
        pnFunction.setPreferredSize(new Dimension(300, 0));
        pnFunction.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.BLACK), "CÁC CHỨC NĂNG"));

        listFunc = new JList();
        listFunc.setFont(new Font("Segoe UI", Font.ROMAN_BASELINE, 15));
        listFunc.setSelectedIndex(0);
        listFunc.setBackground(new Color(240, 240, 240));

        pnFunction.add(listFunc, BorderLayout.CENTER);

        //Panel Chi tiết chức năng
        JPanel pnWrapDetailFunc = new JPanel(new BorderLayout());

        JPanel pnTitleDetailFunc = new JPanel();
        pnWrapDetailFunc.add(pnTitleDetailFunc, BorderLayout.NORTH);
        lblTitleDetailFunc = new JLabel("QUẢN LÝ HỌC VIÊN CỦA KHÓA HỌC");

        pnTitleDetailFunc.add(lblTitleDetailFunc);
        lblTitleDetailFunc.setFont(new Font("Segoe UI", Font.BOLD, 18));

        pnDetailFunc = new JPanel();
        pnWrapDetailFunc.add(pnDetailFunc, BorderLayout.CENTER);

        cardLayout = new CardLayout();
        pnDetailFunc.setLayout(cardLayout);

        pnDetailFunc.add(DangNhapFrame.pnQLHVKH, "QuanLyHocVienKhoaHoc");
        pnDetailFunc.add(DangNhapFrame.pnQLHV, "QuanLyHocVien");
        pnDetailFunc.add(DangNhapFrame.pnQLCĐ, "QuanLyChuyenDe");
        pnDetailFunc.add(DangNhapFrame.pnQLKH, "QuanLyKhoaHoc");
        
        pnDetailFunc.add(DangNhapFrame.pnTKHV, "ThongKeNguoiDangKyHoc");
        if (DangNhapFrame.nvLogin.getVaiTro() == ROLE_TRUONG_PHONG) {
            pnDetailFunc.add(DangNhapFrame.pnQLNV, "QuanLyNhanVien");
            pnDetailFunc.add(DangNhapFrame.pnTKDT, "ThongKeDoanhThu");
        }
        pnDetailFunc.add(DangNhapFrame.pnTHBĐ, "TongHopBangDiem");

        //panel footer
        JPanel pnFooter = new JPanel(new BorderLayout());
        pnFooter.setPreferredSize(new Dimension(0, 30));

        JLabel lblInfo = new JLabel("", new ImageIcon("src/img/Info.png"), JLabel.CENTER);
        lblTime = new JLabel("");
        lblTime.setPreferredSize(new Dimension(150, 30));
        lblTime.setFont(new Font("Segoe UI", 0, 14));
        lblInfo.setFont(new Font("Segoe UI", Font.ROMAN_BASELINE, 14));

        pnFooter.add(lblInfo, BorderLayout.WEST);
        pnFooter.add(lblTime, BorderLayout.EAST);

        con.add(pnFunction, BorderLayout.WEST);
        con.add(pnWrapDetailFunc, BorderLayout.CENTER);
        con.add(pnFooter, BorderLayout.SOUTH);
    }
    // </editor-fold>

    private void addEvents() {
        //Hiển thị họ tên nhân viên trong mnuSystem
        mnuSys_showUsername.setText(DangNhapFrame.nvLogin.getHoTen());
        setTime();
        showFunc();

        listFunc.setSelectedIndex(0);

        // <editor-fold defaultstate="collapsed" desc="Sự kiện click chọn chức năng ">
        listFunc.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                showDetailFunc();
            }

            @Override
            public void mousePressed(MouseEvent e) {
            }

            @Override
            public void mouseReleased(MouseEvent e) {
            }

            @Override
            public void mouseEntered(MouseEvent e) {
            }

            @Override
            public void mouseExited(MouseEvent e) {
            }
        });
        // </editor-fold>

        // <editor-fold defaultstate="collapsed" desc="Sự kiện chọn chức năng băng key up down ">
        listFunc.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
            }

            @Override
            public void keyPressed(KeyEvent e) {

            }

            @Override
            public void keyReleased(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_UP || e.getKeyCode() == KeyEvent.VK_DOWN) {
                    showDetailFunc();
                }
            }
        });
        // </editor-fold>

        // <editor-fold defaultstate="collapsed" desc="Sự kiện mnuSys_Changepass ">
        mnuSys_Changepass.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frChangePass = new ChangePassFrame();
                ChangePassFrame.lblShowUser.setText(DangNhapFrame.nvLogin.getID());
                ChangePassFrame.lblShowName.setText(DangNhapFrame.nvLogin.getHoTen());
            }
        });
        // </editor-fold>

        // <editor-fold defaultstate="collapsed" desc="Sự kiện mnuSys_Signout ">
        mnuSys_Signout.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                signOut();
            }
        });
        // </editor-fold>

        // <editor-fold defaultstate="collapsed" desc="Sự kiện mnuSys_Exit ">
        mnuSys_Exit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int i = JOptionPane.showConfirmDialog(rootPane, "Bạn có muốn thoát chương trình không?", "Thông báo", JOptionPane.YES_NO_OPTION);

                if (i == JOptionPane.YES_OPTION) {
                    try {
                        JdbcHelper.connection.close();
                    } catch (SQLException ex) {
                        Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    System.exit(0);
                }
            }
        });
        // </editor-fold>

        // <editor-fold defaultstate="collapsed" desc="Sự kiện click vào nút x ">
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                int i = JOptionPane.showConfirmDialog(null, "Bạn có muốn đóng chương trình không", "Thông báo", JOptionPane.YES_NO_OPTION);

                if (i == JOptionPane.YES_OPTION) {
                    try {
                        JdbcHelper.connection.close();
                    } catch (SQLException ex) {
                        Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    System.exit(0);
                }
            }
        });
        // </editor-fold>

    }

    // <editor-fold defaultstate="collapsed" desc="SET TIME ">
    public void setTime() {
        new Thread() {
            @Override
            public void run() {
                while (true) {
                    Date now = new Date();
                    SimpleDateFormat fomater = new SimpleDateFormat();
                    fomater.applyPattern("dd/MM/yyyy HH:mm");
                    String time = fomater.format(now);
                    lblTime.setText(time);

                    try {
                        Thread.sleep(60000);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        }.start();
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="HIỂN THỊ CHỨC NĂNG THEO ROLE ">
    private void showFunc() {
        Object[] func = null;
        if (DangNhapFrame.role == ROLE_TRUONG_PHONG) {
            func = new Object[]{"Quản lý Học viên của khóa học",
                "Quản lý Chuyên đề",
                "Quản lý Khóa học",
                "Quản lý Học viên",
                "Quản lý Nhân viên",
                "Thống kê Người đăng ký học theo tháng",
                "Thống kê Doanh thu theo Chuyên đề",
                "Tổng hợp bảng điểm Khóa học"};
        } else {
            func = new Object[]{"Quản lý Học viên của khóa học",
                "Quản lý Chuyên đề",
                "Quản lý Khóa học",
                "Quản lý Học viên",
                "Thống kê Người đăng ký học theo tháng",
                "Tổng hợp Bảng điểm Khóa học"};
        }

        listFunc.setListData(func);
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="HIỂN THỊ CHI TIẾT CHỨC NĂNG ">
    private void showDetailFunc() {
        Object obj = listFunc.getSelectedValue();

        switch (obj.toString()) {
            case "Quản lý Nhân viên":
                cardLayout.show(pnDetailFunc, "QuanLyNhanVien");
                break;
            case "Quản lý Học viên":
                cardLayout.show(pnDetailFunc, "QuanLyHocVien");
                break;
            case "Quản lý Chuyên đề":
                cardLayout.show(pnDetailFunc, "QuanLyChuyenDe");
                break;
            case "Quản lý Khóa học":
                cardLayout.show(pnDetailFunc, "QuanLyKhoaHoc");
                break;
            case "Quản lý Học viên của khóa học":
                cardLayout.show(pnDetailFunc, "QuanLyHocVienKhoaHoc");
                break;
            case "Thống kê Người đăng ký học theo tháng":
                cardLayout.show(pnDetailFunc, "ThongKeNguoiDangKyHoc");
                break;
            case "Thống kê Doanh thu theo Chuyên đề":
                cardLayout.show(pnDetailFunc, "ThongKeDoanhThu");
                break;
            case "Tổng hợp bảng điểm Khóa học":
                cardLayout.show(pnDetailFunc, "TongHopBangDiem");
                break;
            default:
                break;
        }

        lblTitleDetailFunc.setText(obj.toString().toUpperCase());
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="SIGN OUT ">
    private void signOut() {
        int i = JOptionPane.showConfirmDialog(rootPane, "Bạn có muốn đăng xuất không?", "Thông báo", JOptionPane.YES_NO_OPTION);

        if (i == JOptionPane.YES_OPTION) {
            frThis.setVisible(false);
            ChaoJDialog.frLogin.setVisible(true);
            ChaoJDialog.frLogin.progressBar.setValue(0);
        }
    }
    // </editor-fold>
}
