/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package QuanLy;

// <editor-fold defaultstate="collapsed" desc=" IMPORT ">
import DAO.NhanVienDAO;
import HeThong.ChaoJDialog;
import HeThong.DangNhapFrame;
import Helper.JdbcHelper;
import Helper.MailHelper;
import Model.NhanVien;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowEvent;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.mail.MessagingException;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JPopupMenu;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.TitledBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableRowSorter;
import polypro_apt2_main.MainFrame;
// </editor-fold>

/**
 *
 * @author phuoc.bleach
 */
public class QLNV extends JPanel {

    /*- Các chỉ số của flagSave:
        + 0: mặc định
        + 1: thêm mới
        + 2: Sửa*/
    int flagSave = 0;
    JdbcHelper jdbcHelper = new JdbcHelper();
    NhanVienDAO nhanVienDAO = new NhanVienDAO();
    NhanVien nvSelected = null;
    List<NhanVien> listNhanVien = null;
    int indexNvSelectedInTable = -1;

    final int ROLE_TRUONG_PHONG = 1;
    final int FLAG_DEFAULT = 0;
    final int FLAG_INSERT = 1;
    final int FLAG_UPDATE = 2;

    public static boolean doneLoad = false;

    List<NhanVien> listSearchNV;
    int indexInListSearchNV = 0;

    final int FOUND = 1;
    final int NOT_FOUND = 0;
    int flagFind = NOT_FOUND;

    final int SEARCH = 1;
    final int NO_SEARCH = 0;
    int flagSearch = NO_SEARCH;

    MailHelper mailHelper = new MailHelper();

    //<editor-fold defaultstate="collapsed" desc="Component">
    JLabel lblSearchTitle, lblInformationTitle, lblMatKhau, lblXacNhanMatKhau, lblHoTen, lblVaiTro, lblID, lblEmail;
    JTextField txtSearchNV, txtHoTen, txtID, txtEmail;
    JPasswordField pwfMatKhau, pwfXacNhanMatKhau;
    JRadioButton rdoTP, rdoNV;
    ButtonGroup btgVaiTro;

    JButton btnContinueSearch, btnSearch, btnNew, btnUpdate, btnSave, btnDelete, btnCancel, btnResetPass;

    DefaultTableModel model;
    JTable tblNhanVien;
    JScrollPane scTable;
    JTableHeader tblHeader;
    //</editor-fold>

    public QLNV() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            this.init();
            this.addControls();
            this.disableAll();
            this.addEvents();
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex) {
            Logger.getLogger(QLNV.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void init() {
        //<editor-fold defaultstate="collapsed" desc="init">
        lblSearchTitle = new JLabel("Tìm kiếm nhân viên");
        lblInformationTitle = new JLabel("Thông tin nhân viên");
        lblMatKhau = new JLabel("Mật khẩu");
        lblXacNhanMatKhau = new JLabel("Xác nhận mật khẩu");
        lblHoTen = new JLabel("Họ tên");
        lblVaiTro = new JLabel("Vai trò");
        lblID = new JLabel("Mã nhân viên");
        lblEmail = new JLabel("Email");

        txtHoTen = new JTextField();
        txtSearchNV = new JTextField();
        txtID = new JTextField();
        txtEmail = new JTextField();
        txtID.setEditable(false);

        pwfMatKhau = new JPasswordField();
        pwfXacNhanMatKhau = new JPasswordField();

        rdoNV = new JRadioButton("Nhân viên");
        rdoTP = new JRadioButton("Trưởng phòng");
        rdoNV.setSelected(true);
        btgVaiTro = new ButtonGroup();
        btgVaiTro.add(rdoNV);
        btgVaiTro.add(rdoTP);

        btnContinueSearch = new JButton("Tìm tiếp");
        btnSearch = new JButton("Tìm kiếm");
        btnNew = new JButton("Thêm mới");
        btnSave = new JButton("Lưu");
        btnUpdate = new JButton("Sửa");
        btnDelete = new JButton("Xóa");
        btnCancel = new JButton("Bỏ qua");
        btnResetPass = new JButton("Khôi phục mật khẩu");

        model = new DefaultTableModel();
        model.setColumnIdentifiers(new Object[]{
            "STT", "MÃ NHÂN VIÊN", "HỌ VÀ TÊN", "EMAIL", "VAI TRÒ"
        });

        tblNhanVien = new JTable(model) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }

            @Override
            public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {
                JLabel c = (JLabel) super.prepareRenderer(renderer, row, column);
                if (column == 2) {
                    c.setHorizontalAlignment(JLabel.LEFT);
                } else {
                    c.setHorizontalAlignment(JLabel.CENTER);

                }
                return c;
            }
        };
        scTable = new JScrollPane(tblNhanVien, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        tblHeader = tblNhanVien.getTableHeader();
        ((DefaultTableCellRenderer) tblHeader.getDefaultRenderer()).setHorizontalAlignment(JLabel.CENTER);

        tblNhanVien.getColumnModel().getColumn(0).setPreferredWidth(50);
        tblNhanVien.getColumnModel().getColumn(1).setPreferredWidth(110);
        tblNhanVien.getColumnModel().getColumn(2).setPreferredWidth(200);
        tblNhanVien.getColumnModel().getColumn(3).setPreferredWidth(300);
        tblNhanVien.getColumnModel().getColumn(4).setPreferredWidth(140);

        tblNhanVien.setRowHeight(30);
        tblNhanVien.setSelectionBackground(Color.decode("#3a4d8f"));
        //căn giữa nội dung table
        DefaultTableCellRenderer renderer = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                final Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                if (!isSelected) {
                    c.setBackground(row % 2 == 0 ? Color.decode("#e7e7e7") : Color.WHITE);
                    c.setFont(new Font("Segoe UI", 0, 14));
                }
                return c;
            }
        };
        for (int i = 0; i < tblNhanVien.getColumnCount(); i++) {
            tblNhanVien.setDefaultRenderer(tblNhanVien.getColumnClass(i), renderer);
        }

        tblNhanVien.setRowSorter(new TableRowSorter(model));
        tblNhanVien.setAutoCreateRowSorter(true);

        //Font
        lblInformationTitle.setFont(new Font("Segoe UI", 1, 18));
        lblSearchTitle.setFont(new Font("Segoe UI", 1, 18));

        lblHoTen.setFont(new Font("Segoe UI", 0, 14));
        lblMatKhau.setFont(new Font("Segoe UI", 0, 14));
        lblVaiTro.setFont(new Font("Segoe UI", 0, 14));
        lblXacNhanMatKhau.setFont(new Font("Segoe UI", 0, 14));
        lblID.setFont(new Font("Segoe UI", 0, 14));

        btnNew.setFont(new Font("Segoe UI", 0, 13));
        btnSave.setFont(new Font("Segoe UI", 0, 13));
        btnUpdate.setFont(new Font("Segoe UI", 0, 13));
        btnDelete.setFont(new Font("Segoe UI", 0, 13));
        btnSearch.setFont(new Font("Segoe UI", 0, 13));
        btnContinueSearch.setFont(new Font("Segoe UI", 0, 13));
        btnCancel.setFont(new Font("Segoe UI", 0, 13));

        tblNhanVien.setFont(new Font("Segoe UI", 0, 12));

        btnUpdate.setPreferredSize(btnNew.getPreferredSize());
        btnSave.setPreferredSize(btnNew.getPreferredSize());
        btnDelete.setPreferredSize(btnNew.getPreferredSize());
        btnSearch.setPreferredSize(new Dimension(0, 0));
        btnContinueSearch.setPreferredSize(btnNew.getPreferredSize());
        btnCancel.setPreferredSize(btnNew.getPreferredSize());

        //Cursor
        btnContinueSearch.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnSearch.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnNew.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnSave.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnUpdate.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnDelete.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnCancel.setCursor(new Cursor(Cursor.HAND_CURSOR));

        //set phím tắt
        btnSave.setMnemonic(KeyEvent.VK_L);
        btnNew.setMnemonic(KeyEvent.VK_T);
        btnUpdate.setMnemonic(KeyEvent.VK_S);
        btnDelete.setMnemonic(KeyEvent.VK_X);
        btnCancel.setMnemonic(KeyEvent.VK_B);
        btnSearch.setMnemonic(KeyEvent.VK_F);

        // set tooltip
        btnSave.setToolTipText("Phím tắt Alt + S");
        btnNew.setToolTipText("Phím tắt Alt + N");
        btnUpdate.setToolTipText("Phím tắt Alt + U");
        btnDelete.setToolTipText("Phím tắt Alt + D");
        btnCancel.setToolTipText("Phím tắt Alt + Z");

        txtSearchNV.setToolTipText("Tìm kiếm nhân viên theo mã hoặc theo tên");
        pwfMatKhau.setToolTipText("Mật khẩu ít nhất 6 ký tự");

        // Size
        rdoTP.setMargin(new Insets(0, 0, 0, 20));
        //</editor-fold>
    }

    private void addControls() {
        //<editor-fold defaultstate="collapsed" desc="LEFT">
        JPanel pnSearch = new JPanel();
        pnSearch.setBorder(new TitledBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY), "Tìm kiếm", 1, 2, new Font("Segor UI", 1, 18)));

        txtSearchNV.setPreferredSize(new Dimension(300, 25));
        pnSearch.add(txtSearchNV);
        pnSearch.add(btnContinueSearch);
        pnSearch.add(btnSearch);

        JPanel pnLeft = new JPanel(new BorderLayout(0, 5));
        pnLeft.add(scTable, BorderLayout.CENTER);
        pnLeft.add(pnSearch, BorderLayout.NORTH);
        //</editor-fold>

        //<editor-fold defaultstate="collapsed" desc="RIGHT">
        JPanel pnInformation = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        gbc.insets = new Insets(5, 10, 5, 10);
        gbc.fill = GridBagConstraints.BOTH;
        gbc.ipady = 5;

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.ipadx = 115;

        gbc.gridy = 1;
        pnInformation.add(lblInformationTitle, gbc);

        gbc.gridy = 2;
        pnInformation.add(lblID, gbc);

        gbc.gridy = 3;
        pnInformation.add(txtID, gbc);

        gbc.gridy = 4;
        pnInformation.add(lblHoTen, gbc);

        gbc.gridy = 5;
        pnInformation.add(txtHoTen, gbc);

        gbc.gridy = 6;
        pnInformation.add(lblMatKhau, gbc);

        gbc.gridy = 7;
        pnInformation.add(pwfMatKhau, gbc);

        gbc.gridy = 8;
        pnInformation.add(lblXacNhanMatKhau, gbc);

        gbc.gridy = 9;
        pnInformation.add(pwfXacNhanMatKhau, gbc);

        gbc.gridy = 10;
        pnInformation.add(lblEmail, gbc);

        gbc.gridy = 11;
        pnInformation.add(txtEmail, gbc);

        gbc.gridy = 12;
        pnInformation.add(lblVaiTro, gbc);

        JPanel pnGioiTinh = new JPanel(new FlowLayout(FlowLayout.LEADING, 0, 0));
        pnGioiTinh.add(rdoTP);
        pnGioiTinh.add(rdoNV);

        gbc.gridy = 13;
        pnInformation.add(pnGioiTinh, gbc);

        JPanel pnButton1 = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        pnButton1.add(btnNew);
        pnButton1.add(btnUpdate);
        pnButton1.add(btnSave);
        if (DangNhapFrame.nvLogin.getVaiTro() == ROLE_TRUONG_PHONG) {
            pnButton1.add(btnDelete);
        }
        pnButton1.add(btnCancel);

        gbc.ipadx = 10;
        gbc.gridy = 14;
        pnInformation.add(pnButton1, gbc);

        JPanel pnButton2 = new JPanel();
        pnButton2.add(btnResetPass);

        gbc.gridy = 15;
        pnInformation.add(pnButton2, gbc);

        JPanel pnItems = new JPanel();

        JPanel pnRight = new JPanel(new BorderLayout());
        pnRight.add(pnInformation, BorderLayout.NORTH);
        pnRight.add(pnItems, BorderLayout.CENTER);
        //</editor-fold>

        //<editor-fold defaultstate="collapsed" desc="CONTAINER">
        this.setLayout(new BorderLayout(0, 5));
        this.add(pnLeft, BorderLayout.CENTER);
        this.add(pnRight, BorderLayout.EAST);
        //</editor-fold>
    }

    private void disableAll() {
    }

    private void addEvents() {
        lockFormInfo();
        buttonStatus0();

        try {
            //Load dữ liệu lên table
            loadDataToTable();
        } catch (SQLException ex) {
            Logger.getLogger(QLNV.class.getName()).log(Level.SEVERE, null, ex);
        }

        if (listNhanVien.size() > 0) {
            nvSelected = listNhanVien.get(0);
            loadDataToForm();

            tblNhanVien.setRowSelectionInterval(0, 0);

            indexNvSelectedInTable = 0;
        }

        doneLoad = true;

        // <editor-fold defaultstate="collapsed" desc="key listener txt mật khẩu">
        pwfMatKhau.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
            }

            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_DOWN) {
                    pwfXacNhanMatKhau.requestFocus();
                }

                if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                    cancelSave();
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
            }
        });
        // </editor-fold>

        // <editor-fold defaultstate="collapsed" desc="key listener txt nhập lại mật khẩu">
        pwfXacNhanMatKhau.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
            }

            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_DOWN) {
                    txtHoTen.requestFocus();
                }

                if (e.getKeyCode() == KeyEvent.VK_UP) {
                    pwfMatKhau.requestFocus();
                }

                if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                    cancelSave();
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
            }
        });
        // </editor-fold>

        // <editor-fold defaultstate="collapsed" desc="key listener txt họ tên">
        txtHoTen.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
            }

            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_DOWN) {
                    if (rdoTP.isSelected()) {
                        rdoTP.requestFocus();
                    } else {
                        rdoNV.requestFocus();
                    }
                }

                if (e.getKeyCode() == KeyEvent.VK_UP) {
                    pwfXacNhanMatKhau.requestFocus();
                }

                if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                    cancelSave();
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
            }
        });
        // </editor-fold>

        // <editor-fold defaultstate="collapsed" desc="key listener radTP">
        rdoTP.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
            }

            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_UP) {
                    txtHoTen.requestFocus();
                }

                if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                    cancelSave();
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
            }
        });
        // </editor-fold>

        // <editor-fold defaultstate="collapsed" desc="key listener radNV">
        rdoNV.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
            }

            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_UP) {
                    txtHoTen.requestFocus();
                }

                if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                    cancelSave();
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
            }
        });
        // </editor-fold>

        //<editor-fold defaultstate="collapsed" desc="SỰ KIỆN NÚT btnNew">
        btnNew.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                flagSave = FLAG_INSERT;
                unlockFormInfo();
                buttonStatusSave();
                resetForm();
                rdoNV.setSelected(true);
                txtHoTen.requestFocus();

                tblNhanVien.clearSelection();
            }
        });
        //</editor-fold>

        //<editor-fold defaultstate="collapsed" desc="sự kiện nút btnUpdate">
        btnUpdate.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                unlockFormInfo();
                buttonStatusSave();
                txtHoTen.requestFocus();
                flagSave = FLAG_UPDATE;
            }
        });
        //</editor-fold>

        //<editor-fold defaultstate="collapsed" desc="sự kiện nút btnCancel">
        btnCancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cancelSave();
            }
        });
        //</editor-fold>

        // <editor-fold defaultstate="collapsed" desc="sự kiện nút btnSave ">
        btnSave.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                switch (flagSave) {
                    // <editor-fold defaultstate="collapsed" desc="case1: insert">
                    case 1:
                        try {
                            insertNV();
                        } catch (SQLException ex) {
                            Logger.getLogger(QLNV.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        break;
                    // </editor-fold>

                    // <editor-fold defaultstate="collapsed" desc="case 2: update">
                    case 2:
                        try {
                            updateNV();
                        } catch (SQLException ex) {
                            Logger.getLogger(QLNV.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        break;
                    // </editor-fold>

                    default:
                        System.out.println("đang ở flag 0");
                        break;
                }
            }
        });
        // </editor-fold>

        // <editor-fold defaultstate="collapsed" desc="Sự kiện click vào hàng trong bảng">
        tblNhanVien.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                // <editor-fold defaultstate="collapsed" desc="Click chuột phải ">
                if (e.getButton() == MouseEvent.BUTTON3) {
                    int r = tblNhanVien.rowAtPoint(e.getPoint());
                    if (r >= 0 && r < tblNhanVien.getRowCount()) {
                        tblNhanVien.setRowSelectionInterval(r, r);
                    } else {
                        tblNhanVien.clearSelection();
                    }
                }
                // </editor-fold>

                if (flagSave != 0) {
                    String mess = "";
                    switch (flagSave) {
                        case FLAG_INSERT:
                            mess = "Bạn có muốn bỏ qua thao tác thêm mới nhân viên không?";
                            break;
                        case FLAG_UPDATE:
                            mess = "Bạn có muốn bỏ qua thao tác cập nhật thông tin nhân viên không?";
                            break;
                    }

                    int choose = JOptionPane.showConfirmDialog(null, mess, "Hỏi", JOptionPane.YES_NO_OPTION);

                    if (choose == JOptionPane.YES_OPTION) {
                        indexNvSelectedInTable = tblNhanVien.getSelectedRow();
                        int indexNvSelectedInTbl = (int) tblNhanVien.getValueAt(indexNvSelectedInTable, 0) - 1;
                        nvSelected = listNhanVien.get(indexNvSelectedInTbl);

                        loadDataToForm();
                        lockFormInfo();
                        buttonStatus0();

                        flagSave = 0;

                        if (e.getButton() == MouseEvent.BUTTON3) {
                            JPopupMenu popup = new PopupTblNV();
                            popup.show(e.getComponent(), e.getX(), e.getY());
                        }
                    }
                } else {
                    indexNvSelectedInTable = tblNhanVien.getSelectedRow();
                    int indexNvSelectedInTbl = (int) tblNhanVien.getValueAt(indexNvSelectedInTable, 0) - 1;
                    nvSelected = listNhanVien.get(indexNvSelectedInTbl);

                    loadDataToForm();
                    lockFormInfo();
                    buttonStatus0();

                    flagSave = 0;

                    if (e.getButton() == MouseEvent.BUTTON3) {
                        JPopupMenu popup = new PopupTblNV();
                        popup.show(e.getComponent(), e.getX(), e.getY());
                    }
                }
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

        // <editor-fold defaultstate="collapsed" desc="Sự kiện nút search">
        btnSearch.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                txtSearchNV.requestFocus();
            }
        });
        // </editor-fold>

        // <editor-fold defaultstate="collapsed" desc="Sự kiện nút btnSearchContinue">
        btnContinueSearch.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                continueSearch();
            }
        });
        // </editor-fold>

        // <editor-fold defaultstate="collapsed" desc="Sự kiện nút btnDelete ">
        btnDelete.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    deleteNV();
                } catch (SQLException ex) {
                    Logger.getLogger(QLNV.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
// </editor-fold>

        // <editor-fold defaultstate="collapsed" desc="Sự kiện up down table">
        tblNhanVien.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
            }

            @Override
            public void keyPressed(KeyEvent e) {
            }

            @Override
            public void keyReleased(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_UP) {
                    int stt = (int) tblNhanVien.getValueAt(tblNhanVien.getSelectedRow(), 0) - 1;
                    nvSelected = listNhanVien.get(stt);

                    loadDataToForm();

                    indexNvSelectedInTable = tblNhanVien.getSelectedRow();
                }

                if (e.getKeyCode() == KeyEvent.VK_DOWN) {
                    int stt = (int) tblNhanVien.getValueAt(tblNhanVien.getSelectedRow(), 0) - 1;
                    nvSelected = listNhanVien.get(stt);

                    loadDataToForm();

                    indexNvSelectedInTable = tblNhanVien.getSelectedRow();
                }
            }
        });
        // </editor-fold>

        // <editor-fold defaultstate="collapsed" desc="Sự kiện search với txtSearch ">
        txtSearchNV.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                if (txtSearchNV.getText().length() > 0) {
                    indexInListSearchNV = 0;

                    flagSearch = SEARCH;

                    try {
                        listSearchNV = nhanVienDAO.get(txtSearchNV.getText().trim());

                        searchNV();
                    } catch (SQLException ex) {
                        Logger.getLogger(QLNV.class.getName()).log(Level.SEVERE, null, ex);
                    }

                    if (flagFind == NOT_FOUND) {
                        btnUpdate.setEnabled(false);
                    } else {
                        btnUpdate.setEnabled(true);
                    }
                }
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                if (txtSearchNV.getText().length() > 0) {
                    indexInListSearchNV = 0;

                    try {
                        listSearchNV = nhanVienDAO.get(txtSearchNV.getText().trim());

                        searchNV();
                    } catch (SQLException ex) {
                        Logger.getLogger(QLNV.class.getName()).log(Level.SEVERE, null, ex);
                    }

                    if (flagFind == NOT_FOUND) {
                        btnUpdate.setEnabled(false);
                    } else {
                        btnUpdate.setEnabled(true);
                    }
                } else {
                    flagSearch = NO_SEARCH;

                    Rectangle rect = tblNhanVien.getCellRect(0, 0, true);
                    tblNhanVien.scrollRectToVisible(rect);
                }
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
            }
        });
        // </editor-fold>

        // <editor-fold defaultstate="collapsed" desc="sự kiện phím với txtSearch ">
        txtSearchNV.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
            }

            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                    nvSelected = listNhanVien.get(0);

                    tblNhanVien.setRowSelectionInterval(0, 0);

                    loadDataToForm();
                    btnDelete.setEnabled(true);
                    btnUpdate.setEnabled(true);
                    btnCancel.setEnabled(true);
                    txtSearchNV.setText("");

                    flagSearch = NO_SEARCH;

                    Rectangle rect = tblNhanVien.getCellRect(0, 0, true);
                    tblNhanVien.scrollRectToVisible(rect);
                }

                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    continueSearch();
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
            }
        });
        // </editor-fold>

        // <editor-fold defaultstate="collapsed" desc="Sự kiện reset pass ">
        btnResetPass.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                resetPassword();
            }
        });
        // </editor-fold>

        // <editor-fold defaultstate="collapsed" desc="Gợi ý email ">
        txtEmail.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
            }

            @Override
            public void keyPressed(KeyEvent e) {
            }

            @Override
            public void keyReleased(KeyEvent e) {
                Set<String> s = new TreeSet<>();
                s.add("gmail.com");
                s.add("fpt.edu.vn");
                s.add("facebook.com");

                String text = txtEmail.getText();
                int lengthText = text.length();
                int lengthCheck = text.indexOf("@");
                int n = lengthText - lengthCheck - 1;

                for (String data : s) {
                    String str = "";

                    if (lengthCheck >= 0) {
                        str = text.substring(0, lengthCheck + 1);

                        for (int j = 0; j < n; j++) {
                            if (n < data.length()) {
                                str += data.charAt(j);
                            }
                        }
                    }

                    if (str.equals(text) && str.length() > 0 && e.getKeyCode() != 8) {
                        txtEmail.setText(text + data.substring(n));
                        txtEmail.setSelectionStart(lengthText);
                        txtEmail.setSelectionEnd(data.length() + lengthText);
                    }
                }
            }
        });
        // </editor-fold>

        refresh30s();
    }

    // <editor-fold defaultstate="collapsed" desc="TẠO MÃ NHÂN VIÊN">
    private String createID() throws SQLException {
        String lastID = nhanVienDAO.getLastIdNhanVien();

        if (lastID != null) {
            StringBuilder ID = new StringBuilder();
            ID.append("NV");

            int pathNumber = Integer.parseInt(lastID.substring(2)) + 1;

            for (int i = 0; i < 5 - String.valueOf(pathNumber).length(); i++) {
                ID.append("0");
            }

            ID.append(pathNumber);

            return ID.toString();
        } else {
            return "NV00001";
        }
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="KHÓA FORM NHẬP THÔNG TIN VÀ NÚT LƯU XÓA SỬA BỎ QUA">
    private void lockFormInfo() {
        txtSearchNV.setEditable(true);
        btnContinueSearch.setEnabled(true);
        btnSearch.setEnabled(true);
        txtHoTen.setEditable(false);
        pwfMatKhau.setEditable(false);
        rdoTP.setEnabled(false);
        rdoNV.setEnabled(false);
        pwfXacNhanMatKhau.setEditable(false);
        txtEmail.setEditable(false);
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="MỞ KHÓA FORM NHẬP THÔNG TIN ">
    private void unlockFormInfo() {
        txtSearchNV.setEditable(false);
        btnContinueSearch.setEnabled(false);
        btnSearch.setEnabled(false);
        txtHoTen.setEditable(true);
        rdoNV.setEnabled(true);
        rdoTP.setEnabled(true);

        if (flagSave == FLAG_INSERT) {
            txtEmail.setEditable(true);
            pwfMatKhau.setEditable(true);
            pwfXacNhanMatKhau.setEditable(true);
        }
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="BUTTON TRẠNG THÁI MẶC ĐỊNH">
    private void buttonStatus0() {
        btnSave.setVisible(false);
        btnCancel.setVisible(false);
        btnDelete.setVisible(true);
        btnNew.setVisible(true);
        btnUpdate.setVisible(true);
        btnResetPass.setVisible(true);

        btnSave.setEnabled(false);
        btnCancel.setEnabled(false);
        btnDelete.setEnabled(true);
        btnNew.setEnabled(true);
        btnUpdate.setEnabled(true);

    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="BUTTON TRẠNG THÁI SAVE">
    private void buttonStatusSave() {
        btnSave.setVisible(true);
        btnCancel.setVisible(true);
        btnDelete.setVisible(false);
        btnNew.setVisible(false);
        btnUpdate.setVisible(false);
        btnResetPass.setVisible(false);

        btnSave.setEnabled(true);
        btnCancel.setEnabled(true);
        btnDelete.setEnabled(false);
        btnNew.setEnabled(false);
        btnUpdate.setEnabled(false);
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="RESET FORM ">
    private void resetForm() {
        txtID.setText("");
        txtHoTen.setText("");
        pwfMatKhau.setText("");
        pwfXacNhanMatKhau.setText("");
        txtEmail.setText("");
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="LOAD DỮ LIỆU LÊN TABLE ">
    private void loadDataToTable() throws SQLException {
        listNhanVien = nhanVienDAO.getAll();

        model.setRowCount(0);
        int stt = 1;
        for (NhanVien nhanVien : listNhanVien) {
            String vaiTro = "Nhân viên";
            if (nhanVien.getVaiTro() == 1) {
                vaiTro = "Trưởng phòng";
            }

            model.addRow(new Object[]{stt, nhanVien.getID(), nhanVien.getHoTen(), nhanVien.getEmail(), vaiTro});
            stt++;
        }

        tblNhanVien.setRowSelectionInterval(0, 0);
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="LOAD DỮ LIỆU LÊN FORM ">
    private void loadDataToForm() {
        txtID.setText(nvSelected.getID());
        pwfMatKhau.setText(nvSelected.getMatKhau());
        pwfXacNhanMatKhau.setText(nvSelected.getMatKhau());
        txtHoTen.setText(nvSelected.getHoTen());
        txtEmail.setText(nvSelected.getEmail());

        if (nvSelected.getVaiTro() == 0) {
            rdoNV.setSelected(true);
        } else {
            rdoTP.setSelected(true);
        }
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="TÌM KIẾM NHÂN VIÊN ">
    private void searchNV() throws SQLException {
        if (listSearchNV.size() > 0) {
            NhanVien nvInListSearch = listSearchNV.get(indexInListSearchNV);

            for (NhanVien nv : listNhanVien) {
                if (nv.getID().equals(nvInListSearch.getID())) {
                    nvSelected = nv;

                    loadDataToForm();

                    for (int i = 0; i < tblNhanVien.getRowCount(); i++) {
                        if (tblNhanVien.getValueAt(i, 1).equals(nvSelected.getID())) {
                            tblNhanVien.setRowSelectionInterval(i, i);
                            flagFind = FOUND;

                            Rectangle rect = tblNhanVien.getCellRect(i, 0, true);
                            tblNhanVien.scrollRectToVisible(rect);
                            return;
                        }
                    }
                }
            }
        } else {
            flagFind = NOT_FOUND;
            resetForm();
            tblNhanVien.clearSelection();
        }
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="CHECK THÔNG TIN NHÂN VIÊN ">
    private boolean checkInfo() throws SQLException {
        if (txtHoTen.getText().length() == 0) {
            JOptionPane.showMessageDialog(null, "Vui lòng nhập họ tên đầy đủ!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            txtHoTen.requestFocus();
            return false;
        }

        if (!txtHoTen.getText().matches("^[A-Za-zÀÁÂÃÈÉÊÌÍÒÓÔÕÙÚÝàáâãèéêìíòóôõùúýĂăĐđĨĩŨũƠơƯưẠ-ỹ\\s\']{5,100}$")) {
            JOptionPane.showMessageDialog(null, "Vui lòng nhập họ tên đầy đủ, không chứa số và các ký tự đặc biệt \n, . / ; < > ? : \" { } - = _ + ` ~ ! @ $ % ^ & * ( ) \\ |", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            txtHoTen.requestFocus();
            return false;
        }

        if (pwfMatKhau.getPassword().length == 0) {
            JOptionPane.showMessageDialog(null, "Vui lòng nhật mật khẩu!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            pwfMatKhau.requestFocus();
            return false;
        }

        if (pwfMatKhau.getPassword().length < 6 || pwfMatKhau.getText().matches("\\s")) {
            JOptionPane.showMessageDialog(null, "Mật khẩu phải có độ dài ít nhất 6 ký tự và không chứa ký tự khoảng trắng!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            pwfMatKhau.requestFocus();
            return false;
        }

        if (!pwfXacNhanMatKhau.getText().equalsIgnoreCase(pwfMatKhau.getText())) {
            JOptionPane.showMessageDialog(null, "Xác nhận mật khẩu phải trùng với mật khẩu!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            pwfXacNhanMatKhau.requestFocus();
            return false;
        }

        if (txtEmail.getText().length() == 0) {
            JOptionPane.showMessageDialog(null, "Vui lòng nhập email!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            txtEmail.requestFocus();
            return false;
        }

        if (!txtEmail.getText().matches("^([\\w\\.\\-])+\\@(([a-zA-Z0-9\\-])+\\.)+([a-zA-Z0-9]{1})+$")) {
            JOptionPane.showMessageDialog(null, "Email không hợp lệ!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            txtEmail.requestFocus();
            return false;
        }

        if (flagSave == FLAG_INSERT) {
            if (nhanVienDAO.checkEmailExistsInDb(txtEmail.getText().trim())) {
                JOptionPane.showMessageDialog(null, "Email đã được sử dụng để đăng ký nhân viên!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
                txtEmail.requestFocus();
                return false;
            }

            try {
                if (mailHelper.checkEmail(txtEmail.getText()).equals("false")) {
                    JOptionPane.showMessageDialog(null, "Email không tồn tại. Vui lòng kiểm tra lại!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
                    txtEmail.requestFocus();
                    return false;
                }
            } catch (Exception ex) {
                Logger.getLogger(QLNV.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return true;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="THÊM NHÂN VIÊN MỚI ">
    private void insertNV() throws SQLException {
        if (checkInfo()) {
            try {
                NhanVien truongPhong = nhanVienDAO.checkTpExists();

                if (rdoTP.isSelected() == true && truongPhong != null) {
                    int choose = JOptionPane.showConfirmDialog(null, "Ông " + truongPhong.getHoTen() + " đang là trưởng phòng.\nBạn có muốn tiếp tục đổi trưởng phòng mới không?", "Hỏi", JOptionPane.YES_OPTION);

                    if (choose == JOptionPane.YES_OPTION) {
                        insert();

                        loadDataToTable();
                        lockFormInfo();
                        buttonStatus0();
                        flagSave = 0;

                        JOptionPane.showMessageDialog(null, "Tài khoản của bạn đã bị thay đổi quyền thành Nhân viên. Vui lòng đăng nhập lại để sử dụng", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                        ChaoJDialog.frLogin.setVisible(true);
                        DangNhapFrame.mainFrame.setVisible(false);
                    }
                } else {
                    insert();

                    loadDataToTable();
                    lockFormInfo();
                    buttonStatus0();
                    flagSave = 0;
                }

                nvSelected = listNhanVien.get(listNhanVien.size() - 1);

                for (int j = 0; j < tblNhanVien.getRowCount(); j++) {
                    if (tblNhanVien.getValueAt(j, 1).equals(nvSelected.getID())) {
                        tblNhanVien.setRowSelectionInterval(j, j);
                        break;
                    }
                }

                indexNvSelectedInTable = tblNhanVien.getSelectedRow();

                loadDataToForm();

                //send mail thông báo đăng ký thành công
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        String to = nvSelected.getEmail();
                        String sub = "Thông báo đăng ký tài khoản nhân viên tại Lập trình City";
                        String content = "Chào " + nvSelected.getHoTen() + "!"
                                + "\nBạn đã được đăng ký tài khoản để đăng nhập vào Phần mềm quản lý khóa học của Lập trình City."
                                + "\n- Tên đăng nhập: " + nvSelected.getID()
                                + "\n- Mật khẩu: " + nvSelected.getMatKhau()
                                + "\n  Bạn vui lòng đăng nhập vào phần mềm và đổi mật khẩu để đảm bảo tính bảo mật.";

                        try {
                            mailHelper.sendMail(to, sub, content);
                        } catch (MessagingException ex) {
                            Logger.getLogger(QLNV.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                }).start();
            } catch (SQLException ex) {
                Logger.getLogger(QLNV.class.getName()).log(Level.SEVERE, null, ex);
            } catch (SecurityException ex) {
                Logger.getLogger(QLNV.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IllegalArgumentException ex) {
                Logger.getLogger(QLNV.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    private void insert() throws SQLException {
        String id = createID();
        int vaiTro = 0;
        if (rdoTP.isSelected()) {
            vaiTro = 1;
        }

        NhanVien nv = new NhanVien(id, pwfMatKhau.getText(), txtHoTen.getText(), txtEmail.getText(), vaiTro);

        nhanVienDAO.insert(nv);
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="XÓA NHÂN VIÊN ">
    private void deleteNV() throws SQLException {
        if (!nvSelected.getID().equalsIgnoreCase(DangNhapFrame.nvLogin.getID())) {
            int choose = JOptionPane.showConfirmDialog(null, "Bạn có muốn xóa nhân viên này không?", "Hỏi", JOptionPane.YES_NO_OPTION);

            if (choose == JOptionPane.YES_OPTION) {

                String id = nvSelected.getID();

                int i = nhanVienDAO.delete(id);

                if (i > 0) {
                    loadDataToTable();
                    lockFormInfo();
                    loadDataToFormAfterDelete();
                    btnDelete.setEnabled(true);
                    btnUpdate.setEnabled(true);
                    btnCancel.setEnabled(true);
                } else {
                    JOptionPane.showMessageDialog(null, "Xóa không thành công. Vui lòng kiểm tra lại!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
                }

            }
        } else {
            JOptionPane.showMessageDialog(null, "Không được xóa tài khoản đang đăng nhập vào phần mềm!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
        }
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="CẬP NHẬT NHÂN VIÊN ">
    private void updateNV() throws SQLException {
        if (checkInfo()) {
            try {
                NhanVien truongPhong = nhanVienDAO.checkTpExists();

                if (nvSelected.getVaiTro() == 1) {
                    if (rdoNV.isSelected() == true) {
                        int choose = JOptionPane.showConfirmDialog(null, "Ông " + truongPhong.getHoTen() + " đang là trưởng phòng.\nBạn có muốn chuyển Ông " + truongPhong.getHoTen() + " thành nhân viên không?", "Hỏi", JOptionPane.YES_OPTION);

                        if (choose == JOptionPane.NO_OPTION || choose == JOptionPane.CLOSED_OPTION) {
                            return;
                        }

                        update();

                        loadDataToTable();
                        lockFormInfo();
                        buttonStatus0();
                        flagSave = 0;

                        JOptionPane.showMessageDialog(null, "Tài khoản của bạn đã bị thay đổi quyền thành Nhân viên. Vui lòng đăng nhập lại để sử dụng", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                        ChaoJDialog.frLogin.setVisible(true);
                        ChaoJDialog.frLogin.progressBar.setValue(0);
                        DangNhapFrame.mainFrame.setVisible(false);
                    }
                } else {
                    if (truongPhong != null && rdoTP.isSelected()) {
                        int choose = JOptionPane.showConfirmDialog(null, "Ông " + truongPhong.getHoTen() + " đang là trưởng phòng.\nBạn có muốn tiếp tục đổi trưởng phòng mới không?", "Hỏi", JOptionPane.YES_OPTION);

                        if (choose == JOptionPane.NO_OPTION || choose == JOptionPane.CLOSED_OPTION) {
                            return;
                        }
                    }
                    update();

                    loadDataToTable();
                    lockFormInfo();
                    buttonStatus0();
                    flagSave = 0;

                    JOptionPane.showMessageDialog(null, "Tài khoản của bạn đã bị thay đổi quyền thành Nhân viên. Vui lòng đăng nhập lại để sử dụng", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                    ChaoJDialog.frLogin.setVisible(true);
                    ChaoJDialog.frLogin.progressBar.setValue(0);
                    DangNhapFrame.mainFrame.setVisible(false);
                }

                for (int j = 0; j < tblNhanVien.getRowCount(); j++) {
                    if (tblNhanVien.getValueAt(j, 1).equals(nvSelected.getID())) {
                        tblNhanVien.setRowSelectionInterval(j, j);
                        break;
                    }
                }

                indexNvSelectedInTable = tblNhanVien.getSelectedRow();
            } catch (SQLException ex) {
                Logger.getLogger(QLHV.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }

    private void update() throws SQLException {
        int vaiTro = 0;
        if (rdoTP.isSelected()) {
            vaiTro = 1;
        }

        nvSelected.setHoTen(txtHoTen.getText());
        nvSelected.setMatKhau(pwfMatKhau.getText());
        nvSelected.setVaiTro(vaiTro);

        nhanVienDAO.update(nvSelected);
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="KIỂM TRA HV SAU HV ĐƯỢC CHỌN CÓ TỒN TẠI KHÔNG ">
    private boolean checkAfterHvselectedExists() {
        try {
            if (tblNhanVien.getValueAt(indexNvSelectedInTable, 0) != null) {
                return true;
            }
        } catch (Exception e) {
            return false;
        }
        return false;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="LOAD THÔNG HV LÊN FORM SAU DELETE ">
    private void loadDataToFormAfterDelete() {
        if (checkAfterHvselectedExists()) {
            int stt = (int) tblNhanVien.getValueAt(indexNvSelectedInTable, 0) - 1;
            nvSelected = listNhanVien.get(stt);
            loadDataToForm();

            tblNhanVien.setRowSelectionInterval(indexNvSelectedInTable, indexNvSelectedInTable);
        } else {
            int stt = (int) tblNhanVien.getValueAt(indexNvSelectedInTable - 1, 0) - 1;
            nvSelected = listNhanVien.get(stt);
            loadDataToForm();

            tblNhanVien.setRowSelectionInterval(indexNvSelectedInTable - 1, indexNvSelectedInTable - 1);
        }
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="CREATE POPUPMENU ">
    class PopupTblNV extends JPopupMenu {

        public PopupTblNV() {
            JMenuItem mnuUpdate = new JMenuItem("Sửa");
            JMenuItem mnuDelete = new JMenuItem("Xóa");
            this.add(mnuUpdate);
            if (DangNhapFrame.nvLogin.getVaiTro() == 1) {
                this.add(mnuDelete);
            }

            mnuDelete.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    try {
                        deleteNV();
                    } catch (SQLException ex) {
                        Logger.getLogger(QLNV.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            });

            mnuUpdate.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    unlockFormInfo();
                    buttonStatusSave();
                    txtHoTen.requestFocus();
                    flagSave = 2;
                }
            });
        }
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="CANCEL SAVE">
    private void cancelSave() {
        String mess = "";
        switch (flagSave) {
            case FLAG_INSERT:
                mess = "Bạn có muốn bỏ qua thao tác thêm mới nhân viên không?";
                break;
            case FLAG_UPDATE:
                mess = "Bạn có muốn bỏ qua thao tác cập nhật thông tin nhân viên không?";
                break;
        }

        int choose = JOptionPane.showConfirmDialog(null, mess, "Hỏi", JOptionPane.YES_NO_OPTION);

        if (choose == JOptionPane.NO_OPTION || choose == JOptionPane.CLOSED_OPTION) {
            return;
        }

        lockFormInfo();
        buttonStatus0();
        if (flagSave == FLAG_INSERT) {
            resetForm();
            tblNhanVien.setRowSelectionInterval(indexNvSelectedInTable, indexNvSelectedInTable);
            nvSelected = listNhanVien.get((int) tblNhanVien.getValueAt(indexNvSelectedInTable, 0) - 1);
            loadDataToForm();
        }
        flagSave = FLAG_DEFAULT;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="CREATE RANDOM PASSWORD">
    private String createRandomPass() {
        String[] arrWord = new String[]{"z", "c", "x", "v", "b", "n", "m", "a", "s", "d", "f", "g", "h", "j", "k", "l", "q", "w", "e", "r", "t", "y", "u", "i", "o", "p", "Z", "X", "C", "V", "B", "N", "M", "A", "S", "D", "F", "G", "H", "J", "K", "L", "Q", "W", "E", "R", "T", "Y", "U", "I", "O", "P"};

        StringBuilder strPass = new StringBuilder();
        for (int i = 0; i < 6; i++) {
            int numberRandom = (int) Math.ceil(Math.random() * 51);
            strPass.append(arrWord[numberRandom]);
        }

        return strPass.toString();
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="RESET PASSWORD">
    private void resetPassword() {
        int choose = JOptionPane.showConfirmDialog(null, "Bạn có muốn khôi phục mật khẩu cho tài khoản " + nvSelected.getID() + " không?", "Hỏi", JOptionPane.YES_NO_OPTION);

        if (choose == JOptionPane.NO_OPTION || choose == JOptionPane.CLOSED_OPTION) {
            return;
        }

        String newPass = createRandomPass();

        String emailTo = nvSelected.getEmail();
        String sub = "Khôi phục mật khẩu cho tài khoản Phần mềm quản lý khóa học";
        String content = "Chào " + nvSelected.getHoTen() + "!"
                + "\nTài khoản của bạn vừa được yêu cầu khôi phục mật khẩu"
                + "\nMật khẩu mặc định chúng tôi tạo cho bạn là: " + newPass
                + "\nBây giờ bạn đã có thể đăng nhập vào phần mềm bằng mật khẩu này";

        try {
            mailHelper.sendMail(emailTo, sub, content);

            int rs = nhanVienDAO.resetPass(newPass, nvSelected.getID());

            if (rs > 0) {
                JOptionPane.showMessageDialog(null, "Khôi phục mật khẩu thành công!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (MessagingException ex) {
//            Logger.getLogger(QLNV.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(null, "Khôi phục mật khẩu không thành công. Vui lòng kiểm tra lại kết nối Internet!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
        } catch (SQLException ex) {
            Logger.getLogger(QLNV.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="TÌM TIẾP ">
    private void continueSearch() {
        if (flagSearch == SEARCH) {
            if (indexInListSearchNV < listSearchNV.size() - 1) {
                indexInListSearchNV++;
            } else {
                indexInListSearchNV = 0;
            }

            try {
                searchNV();
            } catch (SQLException ex) {
                Logger.getLogger(QLNV.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="REFRESH ">
    private void refresh() {
        try {
            //Load dữ liệu lên table
            loadDataToTable();
        } catch (SQLException ex) {
            Logger.getLogger(QLNV.class.getName()).log(Level.SEVERE, null, ex);
        }

        if (listNhanVien.size() > 0) {
            nvSelected = listNhanVien.get(0);
            loadDataToForm();

            tblNhanVien.setRowSelectionInterval(0, 0);

            indexNvSelectedInTable = 0;
        }
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="REFRESH 30S 1 LẦN ">
    private void refresh30s() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                boolean flagLogout = false;
                while (doneLoad) {
                    if (flagSave == FLAG_DEFAULT) {
                        refresh();
                    }

                    try {
                        NhanVien nv = nhanVienDAO.getByID(DangNhapFrame.nvLogin.getID());

                        if (nv.getVaiTro() != DangNhapFrame.nvLogin.getVaiTro() && flagLogout == false) {
                            JOptionPane.showMessageDialog(null, "Tài khoản của bạn đã bị thay đổi quyền. Vui lòng đăng nhập lại để sử dụng", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                            ChaoJDialog.frLogin.setVisible(true);
                            ChaoJDialog.frLogin.progressBar.setValue(0);
                            DangNhapFrame.mainFrame.setVisible(false);
                            flagLogout = true;
                        }
                    } catch (SQLException ex) {
                        Logger.getLogger(QLNV.class.getName()).log(Level.SEVERE, null, ex);
                    }

                    try {
                        Thread.sleep(30000);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(QLNV.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        }).start();
    }
    // </editor-fold>
}
