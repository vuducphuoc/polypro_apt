package HeThong;

import Helper.JdbcHelper;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;

public class ChangePassFrame extends JFrame{
    JdbcHelper jdbcHelper   = new JdbcHelper();
    
    public static JLabel lblShowUser, lblShowName;
    JPasswordField txtOldPass,txtNewPass,txtRepass;
    JButton btnSave, btnCancel;
    JFrame frame    = this;

    public ChangePassFrame() {
        super("Thay đổi mật khẩu");
        addControls();
        addEvents();
        showWindows();
    }
    
    // <editor-fold defaultstate="collapsed" desc="SHOW WINDOW ">
    private void showWindows() {
        this.setSize(470, 500);
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        this.setLocationRelativeTo(null);
        this.setResizable(false);
        this.setVisible(true);
        
        ImageIcon icon = new ImageIcon("src/img/logo_apt.png");
        this.setIconImage(icon.getImage());
    }
    // </editor-fold>
    

    // <editor-fold defaultstate="collapsed" desc="ADD CONTROLS ">
    private void addControls() {
        Container con = getContentPane();
        con.setLayout(new FlowLayout());
        
        JPanel pnTitle      = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 30));
        JLabel lblTitle     = new JLabel("ĐỔI MẬT KHẨU");
        JLabel lblUser      = new JLabel("Tên đăng nhập");
        lblShowUser         = new JLabel();
        JLabel lblName      = new JLabel("Họ và tên");
        lblShowName         = new JLabel();
        JLabel lblOldPass   = new JLabel("Mật khẩu cũ");
        txtOldPass          = new JPasswordField(20);
        JLabel lblNewPass   = new JLabel("Mật khẩu mới");
        txtNewPass          = new JPasswordField(20);
        JLabel lblRepass    = new JLabel("Nhập lại mật khẩu");
        txtRepass           = new JPasswordField(20);
        JPanel pnButton     = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 30));
        JLabel lblEmpty     = new JLabel();
        btnSave             = new JButton("Lưu");
        btnCancel           = new JButton("Hủy");
        
        pnTitle.setPreferredSize(new Dimension(450, 70));
        
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 26));
        
        lblUser.setFont(new Font("Segoe UI", Font.ROMAN_BASELINE, 18));
        lblUser.setPreferredSize(new Dimension(150, 30));
        lblName.setFont(new Font("Segoe UI", Font.ROMAN_BASELINE, 18));
        lblName.setPreferredSize(new Dimension(150, 30));
        
        lblShowUser.setPreferredSize(new Dimension(225, 25));
        lblShowUser.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblShowName.setPreferredSize(new Dimension(225, 25));
        lblShowName.setFont(new Font("Segoe UI", Font.BOLD, 18));
        
        lblOldPass.setFont(lblUser.getFont());
        lblOldPass.setPreferredSize(new Dimension(150, 50));
        
        lblNewPass.setFont(lblUser.getFont());
        lblNewPass.setPreferredSize(lblOldPass.getPreferredSize());
        
        lblRepass.setFont(lblUser.getFont());
        lblRepass.setPreferredSize(lblOldPass.getPreferredSize());
        
        txtOldPass.setPreferredSize(new Dimension(0, 35));
        txtNewPass.setPreferredSize(txtOldPass.getPreferredSize());
        txtRepass.setPreferredSize(txtOldPass.getPreferredSize());
        
        pnButton.setPreferredSize(new Dimension(450, 70));
        lblEmpty.setPreferredSize(new Dimension(30, 0));
        
        btnSave.setPreferredSize(new Dimension(115, 30));
        btnSave.setFont(new Font("Segoe UI", Font.ROMAN_BASELINE, 16));
        
        btnCancel.setPreferredSize(new Dimension(115, 30));
        btnCancel.setFont(new Font("Segoe UI", Font.ROMAN_BASELINE, 16));
        
        pnTitle.add(lblTitle);
        pnButton.add(btnSave);
        pnButton.add(lblEmpty);
        pnButton.add(btnCancel);
        
        con.add(pnTitle);
        con.add(lblUser);
        con.add(lblShowUser);
        con.add(lblName);
        con.add(lblShowName);
        con.add(lblOldPass);
        con.add(txtOldPass);
        con.add(lblNewPass);
        con.add(txtNewPass);
        con.add(lblRepass);
        con.add(txtRepass);
        con.add(pnButton);
    }
    // </editor-fold>
    

    private void addEvents() {
        btnCancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.setVisible(false);
            }
        });
        
        btnSave.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (changePassword()) {
                    JOptionPane.showMessageDialog(rootPane, "Đổi mật khẩu thành công!","Thông báo",JOptionPane.INFORMATION_MESSAGE);
                    frame.setVisible(false);
                }
            }
        });
    }

    //xử lý đổi mật khẩu
    private boolean changePassword(){
        String id       = DangNhapFrame.nvLogin.getID();
        String oldPass  = txtOldPass.getText();
        String newPass  = txtNewPass.getText();
        String rePass   = txtRepass.getText();
        
        if (checkInfo()) {
            String sql = "UPDATE dbo.NhanVien SET matKhau=? WHERE id=?";
            
            try {
                int i = jdbcHelper.executeUpdate(sql, newPass,id);
                
                if (i > 0) {
                    return true;
                }
            } catch (SQLException ex) {
                Logger.getLogger(ChangePassFrame.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        return false;
    }
    
    //check thông tin đổi mật khẩu
    private boolean checkInfo(){
        if (txtOldPass.getText().length() == 0) {
            JOptionPane.showMessageDialog(rootPane, "Vui lòng nhập mật khẩu hiện tại!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            txtOldPass.requestFocus();
            return false;
        }
        
        if (!DangNhapFrame.nvLogin.getMatKhau().equalsIgnoreCase(txtOldPass.getText())) {
            JOptionPane.showMessageDialog(rootPane, "Mật khẩu hiện tại không đúng!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            txtOldPass.requestFocus();
            return false;
        }
        
        if (txtNewPass.getText().length() == 0) {
            JOptionPane.showMessageDialog(rootPane, "Vui lòng nhập mật khẩu mới!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            txtNewPass.requestFocus();
            return false;
        }
        
        if (txtNewPass.getText().length() == 0) {
            JOptionPane.showMessageDialog(rootPane, "Vui lòng nhập mật khẩu mới!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            txtNewPass.requestFocus();
            return false;
        }
        
        if (txtNewPass.getText().length() < 6) {
            JOptionPane.showMessageDialog(rootPane, "Mật khẩu mới phải có độ dài ít nhất 6 ký tự và không chứa ký tự khoảng trắng!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            txtNewPass.requestFocus();
            return false;
        }
        
        if (txtRepass.getText().length() == 0) {
            JOptionPane.showMessageDialog(rootPane, "Vui lòng nhập lại mật khẩu mới!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            txtRepass.requestFocus();
            return false;
        }
        
        if (!txtNewPass.getText().equalsIgnoreCase(txtRepass.getText())) {
            JOptionPane.showMessageDialog(rootPane, "Mật khẩu mới và nhập lại mật khẩu không trùng nhau!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        
        if (txtNewPass.getText().equals(txtOldPass.getText())) {
            JOptionPane.showMessageDialog(rootPane, "Mật khẩu hiện tại và mật khẩu mới không được giống nhau!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            txtNewPass.requestFocus();
            return false;
        }
        
        return true;
    }
}
