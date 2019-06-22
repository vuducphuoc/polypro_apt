package HeThong;

import Helper.JdbcHelper;
import DAO.NhanVienDAO;
import Model.NhanVien;
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
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JProgressBar;
import javax.swing.JTextField;
import polypro_apt2_main.MainFrame;

public class DangNhapFrame extends JFrame {

    public JProgressBar progressBar;
    JdbcHelper jdbcHelper = new JdbcHelper();
    NhanVienDAO nvDAO = new NhanVienDAO();

    JTextField txtUser;
    JPasswordField txtPass;
    JButton btnLogin, btnExit;

    JFrame frThis = this;

    public static MainFrame mainFrame;

    public static int role;

    public static NhanVien nvLogin;

    public static BangDiemKhoaHoc pnTHBĐ;
    public static DoanhThuChuyenDe pnTKDT;
    public static QLCD pnQLCĐ;
    public static QLKH pnQLKH;
    public static QLNV pnQLNV;
    public static QLHV pnQLHV;
    public static QLHVKH pnQLHVKH;
    public static ThongKeNguoiDangKyHoc pnTKHV;
    
    final int TRUONG_PHONG = 1;

    public DangNhapFrame() {
        super("Đăng nhập");
        addControls();
        addEvents();
        showWindows();
    }

    private void showWindows() {
        this.setSize(450, 350);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null);
        this.setResizable(false);
//        this.setVisible(true);

        ImageIcon icon = new ImageIcon("src/img/logo_apt.png");
        this.setIconImage(icon.getImage());
    }

    private void addControls() {
        Container con = getContentPane();
        con.setLayout(new BorderLayout());

        JPanel pnMain = new JPanel(new FlowLayout());

        JPanel pnTitle = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 30));
        JLabel lblTitle = new JLabel("ĐĂNG NHẬP");
        JLabel lblUser = new JLabel("Tài khoản");
        JLabel lblPass = new JLabel("Mật khẩu");
        txtUser = new JTextField(20);
        txtPass = new JPasswordField(20);
        JPanel pnButton = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 30));
        JLabel lblEmpty = new JLabel();
        btnLogin = new JButton("Đăng nhập");
        btnExit = new JButton("Đóng");

        pnTitle.setPreferredSize(new Dimension(450, 70));
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 26));
        lblTitle.setForeground(Color.decode("#32488d"));
        lblUser.setFont(new Font("Segoe UI", Font.ROMAN_BASELINE, 18));
        lblUser.setPreferredSize(new Dimension(110, 50));
        lblUser.setForeground(Color.decode("#32488d"));
        lblPass.setFont(new Font("Segoe UI", Font.ROMAN_BASELINE, 18));
        lblPass.setPreferredSize(lblUser.getPreferredSize());
        lblPass.setForeground(Color.decode("#32488d"));
        txtUser.setPreferredSize(new Dimension(0, 35));
        txtPass.setPreferredSize(new Dimension(0, 35));
        txtUser.setHorizontalAlignment(JTextField.CENTER);
        txtPass.setHorizontalAlignment(JTextField.CENTER);
        pnButton.setPreferredSize(new Dimension(450, 70));
        lblEmpty.setPreferredSize(new Dimension(30, 0));
        btnLogin.setPreferredSize(new Dimension(115, 30));
        btnLogin.setFont(new Font("Segoe UI", Font.ROMAN_BASELINE, 16));
        btnExit.setPreferredSize(new Dimension(115, 30));
        btnExit.setFont(new Font("Segoe UI", Font.ROMAN_BASELINE, 16));

        pnTitle.add(lblTitle);
        pnMain.add(pnTitle);
        pnMain.add(lblUser);
        pnMain.add(txtUser);
        pnMain.add(lblPass);
        pnMain.add(txtPass);
        pnButton.add(btnLogin);
        pnButton.add(lblEmpty);
        pnButton.add(btnExit);
        pnMain.add(pnButton);

        progressBar = new JProgressBar(0, 100);

        con.add(pnMain, BorderLayout.CENTER);
        con.add(progressBar, BorderLayout.SOUTH);
    }

    private void addEvents() {
        // <editor-fold defaultstate="collapsed" desc="Sự kiện nút btnLogin ">
        btnLogin.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                login();
            }
        });
        // </editor-fold>

        // <editor-fold defaultstate="collapsed" desc="Sự kiện nút btnExit ">
        btnExit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        // </editor-fold>

        // <editor-fold defaultstate="collapsed" desc="Sự kiện phím enter và down cho txtUser ">
        txtUser.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
            }

            @Override
            public void keyPressed(KeyEvent e) {
            }

            @Override
            public void keyReleased(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    login();
                }

                if (e.getKeyCode() == KeyEvent.VK_DOWN) {
                    txtPass.requestFocus();
                }
            }
        });
        // </editor-fold>

        // <editor-fold defaultstate="collapsed" desc="Sự kiện phím enter và up cho txtPass ">
        txtPass.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
            }

            @Override
            public void keyPressed(KeyEvent e) {
            }

            @Override
            public void keyReleased(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    login();
                }

                if (e.getKeyCode() == KeyEvent.VK_UP) {
                    txtUser.requestFocus();
                }
            }
        });
        // </editor-fold>

    }

    // <editor-fold defaultstate="collapsed" desc="CHECK INFO ">
    private boolean checkInfo() {
        if (txtUser.getText().length() == 0) {
            JOptionPane.showMessageDialog(null, "Vui lòng nhập tên đăng nhập!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            txtUser.requestFocus();
            return false;
        }

        if (txtPass.getText().length() == 0) {
            JOptionPane.showMessageDialog(null, "Vui lòng nhập mật khẩu!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            txtPass.requestFocus();
            return false;
        }

        return true;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="XỬ LÝ LOGIN ">
    private boolean xuLyDangNhap(String username, String password) {
        nvLogin = new NhanVien();
        String sql = "SELECT * FROM dbo.NhanVien WHERE id=?";

        try {
            ResultSet rs = jdbcHelper.executeQuery(sql, username);

            if (rs.next()) {
                nvLogin = nvDAO.readResultSet(rs);

                if (nvLogin.getMatKhau().equalsIgnoreCase(password)) {
                    role = nvLogin.getVaiTro();

                    return true;
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(DangNhapFrame.class.getName()).log(Level.SEVERE, null, ex);
        }

        return false;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="LOGIN ">
    private void login() {
        if (checkInfo()) {
            boolean flagLogin = xuLyDangNhap(txtUser.getText(), txtPass.getText());
            if (flagLogin) {
                runProgressbar();

                pnQLNV = new QLNV();
                pnQLHV = new QLHV();
                pnQLCĐ = new QLCD();
                pnQLKH = new QLKH();
                pnQLHVKH = new QLHVKH();
                pnTKHV = new ThongKeNguoiDangKyHoc();
                pnTKDT = new DoanhThuChuyenDe();
                pnTHBĐ = new BangDiemKhoaHoc();
            } else {
                JOptionPane.showMessageDialog(rootPane, "Tên đăng nhập hoặc mật khẩu không đúng. Vui lòng kiểm tra lại!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
                txtUser.requestFocus();
            }
        }
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="progressbar ">
    private void runProgressbar() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                int valueProgress = 0;

                while (valueProgress <= 100) {
                    if (DangNhapFrame.pnQLNV.doneLoad == true && valueProgress <= 13) {
                        while (valueProgress <= 13) {
                            progressBar.setValue(valueProgress);
                            valueProgress++;
                            try {
                                Thread.sleep(10);
                            } catch (InterruptedException ex) {
                                Logger.getLogger(DangNhapFrame.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        }
                    }

                    if (DangNhapFrame.pnQLHV.doneLoad == true && valueProgress <= 26) {
                        while (valueProgress <= 26) {
                            progressBar.setValue(valueProgress);
                            valueProgress++;
                            try {
                                Thread.sleep(10);
                            } catch (InterruptedException ex) {
                                Logger.getLogger(DangNhapFrame.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        }
                    }

                    if (DangNhapFrame.pnQLCĐ.doneLoad == true && valueProgress <= 39) {
                        while (valueProgress <= 39) {
                            progressBar.setValue(valueProgress);
                            valueProgress++;
                            try {
                                Thread.sleep(10);
                            } catch (InterruptedException ex) {
                                Logger.getLogger(DangNhapFrame.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        }
                    }

                    if (DangNhapFrame.pnQLKH.doneLoad == true && valueProgress <= 52) {
                        while (valueProgress <= 52) {
                            progressBar.setValue(valueProgress);
                            valueProgress++;
                            try {
                                Thread.sleep(10);
                            } catch (InterruptedException ex) {
                                Logger.getLogger(DangNhapFrame.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        }
                    }

                    if (DangNhapFrame.pnQLHVKH.doneLoad == true && valueProgress <= 65) {
                        while (valueProgress <= 65) {
                            progressBar.setValue(valueProgress);
                            valueProgress++;
                            try {
                                Thread.sleep(10);
                            } catch (InterruptedException ex) {
                                Logger.getLogger(DangNhapFrame.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        }
                    }

                    if (DangNhapFrame.pnTKHV.doneLoad == true && valueProgress <= 78) {
                        while (valueProgress <= 78) {
                            progressBar.setValue(valueProgress);
                            valueProgress++;
                            try {
                                Thread.sleep(10);
                            } catch (InterruptedException ex) {
                                Logger.getLogger(DangNhapFrame.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        }
                    }

                    if (DangNhapFrame.pnTKDT.doneLoad == true && valueProgress <= 91) {
                        while (valueProgress <= 91) {
                            progressBar.setValue(valueProgress);
                            valueProgress++;
                            try {
                                Thread.sleep(10);
                            } catch (InterruptedException ex) {
                                Logger.getLogger(DangNhapFrame.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        }
                    }

                    if (DangNhapFrame.pnTKDT.doneLoad == true && valueProgress <= 100) {
                        while (valueProgress <= 100) {
                            progressBar.setValue(valueProgress);
                            valueProgress++;
                            try {
                                Thread.sleep(10);
                            } catch (InterruptedException ex) {
                                Logger.getLogger(DangNhapFrame.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        }
                    }
                    try {
                        Thread.sleep(30);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(DangNhapFrame.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }

                mainFrame = new MainFrame();
                frThis.setVisible(false);
                mainFrame.setVisible(true);
            }
        }).start();
    }
    // </editor-fold>
}
