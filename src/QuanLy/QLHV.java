/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package QuanLy;

// <editor-fold defaultstate="collapsed" desc="IMPORT ">
import DAO.HocVienDAO;
import DAO.HocVien_KhoaHocDAO;
import DAO.KhoaHocDAO;
import HeThong.DangNhapFrame;
import Helper.DateHelper;
import Helper.JdbcHelper;
import Helper.MailHelper;
import Model.HocVien;
import Model.HocVienEnjoyKhoaHoc;
import Model.HocVien_KhoaHoc;
import Model.KhoaHoc;
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
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.sql.SQLException;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.TreeSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.mail.MessagingException;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
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
public class QLHV extends JPanel {

    List<HocVien> listHocVien;

    List<HocVien> listHvSearch;
    int indexHvInListSearch = 0;

//    List<Integer> listYearSearch;
//    int indexInListYearSearch = 0;
    HocVienDAO hocVienDAO = new HocVienDAO();
    MailHelper mailHelper = new MailHelper();
    HocVien hvSelected = null;
    int indexHvSelectedInTable = -1;
    DateHelper dateHelper = new DateHelper();

    final int ROLE_TRUONG_PHONG = 1;
    final int FLAG_DEFAULT = 0;
    final int FLAG_INSERT = 1;
    final int FLAG_UPDATE = 2;
    final int ADD_KH = 3;
    int flagSave = FLAG_DEFAULT;

    final int SEARCH = 1;
    final int NO_SEARCH = 0;
    int flagSearch = NO_SEARCH;

    final int FOUND = 1;
    final int NOT_FOUND = 0;
    int flagFind = NOT_FOUND;

    public static boolean doneLoad = false;

    List<HocVienEnjoyKhoaHoc> listHvEnjoyKh;

    NumberFormat numberFormat = NumberFormat.getCurrencyInstance(new Locale("VI", "VN"));

    HocVien_KhoaHocDAO hvkhdao = new HocVien_KhoaHocDAO();

    //<editor-fold defaultstate="collapsed" desc="Component">
    JLabel lblSearchNam, lblSearchTitle, lblInformationTitle, lblHoTen, lblNgaySinh, lblGioiTinh, lblSDT, lblEmail, lblGhiChu, lblID, lblKhoaHoc, lblKhoaHocCua;
    JTextField txtSearchHV, txtHoTen, txtNgaySinh, txtSDT, txtEmail, txtID;
    JTextArea txaGhiChu;
    JComboBox cbxNam;
    JComboBox cbxKhoaHoc;
    JRadioButton rdoNam, rdoNu;
    ButtonGroup btgGioiTinh;
    JButton btnSearchContinue, btnSearch, btnNew, btnUpdate, btnSave, btnDelete, btnCancel, btnAddKH;
    JPanel pnButton2;

    DefaultTableModel model, dtmKH;
    JTable tblHocVien, tblKhoaHoc;
    JScrollPane scGhiChu, scTable, scTblKhoaHoc;
    JTableHeader tblHeader, tblHeaderKH;
    JCheckBox chkSearchAll;
    //</editor-fold>

    public QLHV() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            this.init();
            this.addControls();
            this.addEvents();
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex) {
            Logger.getLogger(QLHV.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void init() {
        //<editor-fold defaultstate="collapsed" desc="init">
        lblSearchNam = new JLabel("Năm");
        cbxNam = new JComboBox();

        cbxKhoaHoc = new JComboBox<>();

        lblSearchTitle = new JLabel("Tìm kiếm học viên");
        lblInformationTitle = new JLabel("Thông tin học viên");
        lblHoTen = new JLabel("Họ tên");
        lblNgaySinh = new JLabel("Ngày sinh");
        lblGioiTinh = new JLabel("Giới tính");
        lblSDT = new JLabel("Số điện thoại");
        lblEmail = new JLabel("Email");
        lblGhiChu = new JLabel("Ghi chú");
        lblID = new JLabel("Mã học viên");
        lblKhoaHoc = new JLabel("Thêm vào Khóa học");
        lblKhoaHocCua = new JLabel("");

        txtSearchHV = new JTextField();
        txtHoTen = new JTextField();
        txtNgaySinh = new JTextField();
        txtSDT = new JTextField();
        txtEmail = new JTextField();
        txaGhiChu = new JTextArea();
        txaGhiChu.setLineWrap(true);
        scGhiChu = new JScrollPane(txaGhiChu, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        txtID = new JTextField();

        rdoNam = new JRadioButton("Nam");
        rdoNam.setSelected(true);
        rdoNu = new JRadioButton("Nữ");
        btgGioiTinh = new ButtonGroup();
        btgGioiTinh.add(rdoNam);
        btgGioiTinh.add(rdoNu);

        btnSearchContinue = new JButton("Tìm tiếp");
        btnSearch = new JButton("Tìm kiếm");
        btnNew = new JButton("Thêm mới");
        btnSave = new JButton("Lưu");
        btnUpdate = new JButton("Sửa");
        btnDelete = new JButton("Xóa");
        btnCancel = new JButton("Bỏ qua");
        btnAddKH = new JButton("Thêm khóa học");

        model = new DefaultTableModel();
        model.setColumnIdentifiers(new Object[]{
            "STT", "MÃ HỌC VIÊN", "HỌ VÀ TÊN", "GIỚI TÍNH", "NGÀY SINH", "SỐ ĐIỆN THOẠI", "NGÀY ĐĂNG KÝ"
        });

        tblHocVien = new JTable(model) {
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
        scTable = new JScrollPane(tblHocVien, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        tblHeader = tblHocVien.getTableHeader();
        ((DefaultTableCellRenderer) tblHeader.getDefaultRenderer()).setHorizontalAlignment(JLabel.CENTER);
        tblHocVien.getColumnModel().getColumn(0).setPreferredWidth(50);
        tblHocVien.getColumnModel().getColumn(1).setPreferredWidth(100);
        tblHocVien.getColumnModel().getColumn(2).setPreferredWidth(210);
        tblHocVien.getColumnModel().getColumn(3).setPreferredWidth(80);
        tblHocVien.getColumnModel().getColumn(4).setPreferredWidth(130);
        tblHocVien.getColumnModel().getColumn(5).setPreferredWidth(120);
        tblHocVien.getColumnModel().getColumn(6).setPreferredWidth(120);

        tblHocVien.setRowHeight(30);
        tblHocVien.setSelectionBackground(Color.decode("#3a4d8f"));
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

        tblHocVien.setRowSorter(new TableRowSorter(model));
        tblHocVien.setAutoCreateRowSorter(true);

        chkSearchAll = new JCheckBox("Tìm trong danh sách tất cả học viên                                           ");

        // Font
        lblInformationTitle.setFont(new Font("Segoe UI", 1, 18));
        lblSearchTitle.setFont(new Font("Segoe UI", 1, 18));
        lblKhoaHocCua.setFont(new Font("Segoe UI", 1, 15));

        lblEmail.setFont(new Font("Segoe UI", 0, 14));
        lblID.setFont(new Font("Segoe UI", 0, 14));
        lblGhiChu.setFont(new Font("Segoe UI", 0, 14));
        lblGioiTinh.setFont(new Font("Segoe UI", 0, 14));
        lblHoTen.setFont(new Font("Segoe UI", 0, 14));
        lblNgaySinh.setFont(new Font("Segoe UI", 0, 14));
        lblSDT.setFont(new Font("Segoe UI", 0, 14));
        lblSearchNam.setFont(new Font("Segoe UI", 0, 14));
        lblKhoaHoc.setFont(new Font("Segoe UI", 0, 14));
        rdoNam.setFont(new Font("Segoe UI", 0, 14));
        rdoNu.setFont(new Font("Segoe UI", 0, 14));

        btnNew.setFont(new Font("Segoe UI", 0, 13));
        btnSave.setFont(new Font("Segoe UI", 0, 13));
        btnUpdate.setFont(new Font("Segoe UI", 0, 13));
        btnDelete.setFont(new Font("Segoe UI", 0, 13));
        btnSearch.setFont(new Font("Segoe UI", 0, 13));
        btnSearchContinue.setFont(new Font("Segoe UI", 0, 13));
        btnCancel.setFont(new Font("Segoe UI", 0, 13));

        tblHocVien.setFont(new Font("Segoe UI", 0, 12));

        btnUpdate.setPreferredSize(btnNew.getPreferredSize());
        btnSave.setPreferredSize(btnNew.getPreferredSize());
        btnDelete.setPreferredSize(btnNew.getPreferredSize());
        btnSearch.setPreferredSize(new Dimension(0, 0));
        btnSearchContinue.setPreferredSize(btnNew.getPreferredSize());
        btnCancel.setPreferredSize(btnNew.getPreferredSize());

        txtSearchHV.setPreferredSize(new Dimension(300, 25));

        //Cursor
        btnSearchContinue.setCursor(new Cursor(Cursor.HAND_CURSOR));
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
        btnSearch.setToolTipText("Phím tắt Alt + F");

        txtSearchHV.setToolTipText("Tìm kiếm học viên theo mã, tên, số điện thoại hoặc email");
        txtNgaySinh.setToolTipText("dd-MM-yyyy");
        txtSDT.setToolTipText("0xxxxxxxxx/+84xxxxxxxxx");
        cbxNam.setToolTipText("Năm học viên đăng ký học");

        //Size
        cbxNam.setPreferredSize(new Dimension(100, 25));
        ((JLabel) cbxNam.getRenderer()).setHorizontalAlignment(JLabel.CENTER);
        rdoNam.setMargin(new Insets(0, 20, 0, 20));

        dtmKH = new DefaultTableModel();
        dtmKH.setColumnIdentifiers(new Object[]{"STT", "TÊN KHÓA HỌC", "THỜI LƯỢNG", "HỌC PHÍ", "NGÀY KHAI GIẢNG", "ĐIỂM"});
        tblKhoaHoc = new JTable(dtmKH) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }

            @Override
            public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {
                JLabel c = (JLabel) super.prepareRenderer(renderer, row, column);
                if (column == 1) {
                    c.setHorizontalAlignment(JLabel.LEFT);
                } else {
                    c.setHorizontalAlignment(JLabel.CENTER);

                }
                return c;
            }
        };
        scTblKhoaHoc = new JScrollPane(tblKhoaHoc, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        for (int i = 0; i < tblKhoaHoc.getColumnCount(); i++) {
            tblKhoaHoc.setDefaultRenderer(tblKhoaHoc.getColumnClass(i), renderer);
        }

        for (int i = 0; i < tblHocVien.getColumnCount(); i++) {
            tblHocVien.setDefaultRenderer(tblHocVien.getColumnClass(i), renderer);
        }

        tblKhoaHoc.setRowHeight(30);
        tblKhoaHoc.setSelectionBackground(Color.decode("#3a4d8f"));

        tblHeaderKH = tblKhoaHoc.getTableHeader();
        ((DefaultTableCellRenderer) tblHeaderKH.getDefaultRenderer()).setHorizontalAlignment(JLabel.CENTER);

        tblKhoaHoc.getColumnModel().getColumn(0).setPreferredWidth(50);
        tblKhoaHoc.getColumnModel().getColumn(1).setPreferredWidth(290);
        tblKhoaHoc.getColumnModel().getColumn(2).setPreferredWidth(160);
        tblKhoaHoc.getColumnModel().getColumn(3).setPreferredWidth(100);
        tblKhoaHoc.getColumnModel().getColumn(4).setPreferredWidth(125);
        tblKhoaHoc.getColumnModel().getColumn(5).setPreferredWidth(80);
        //</editor-fold>
    }

    private void addControls() {

        //<editor-fold defaultstate="collapsed" desc="LEFT">
        JPanel pnHeaderLeft = new JPanel(new BorderLayout());

        JPanel pnFilter = new JPanel();
        pnFilter.setPreferredSize(new Dimension(250, 95));
        pnFilter.add(lblSearchNam);
        pnFilter.add(cbxNam);
        pnFilter.setBorder(new TitledBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY), "Lọc", 1, 2, new Font("Segor UI", 1, 18)));

        JPanel pnSearch = new JPanel();
        pnSearch.add(txtSearchHV);
        pnSearch.add(btnSearch);
        pnSearch.add(btnSearchContinue);
        pnSearch.add(chkSearchAll);
        pnSearch.setBorder(new TitledBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY), "Tìm kiếm", 1, 2, new Font("Segor UI", 1, 18)));

        pnHeaderLeft.add(pnFilter, BorderLayout.WEST);
        pnHeaderLeft.add(pnSearch, BorderLayout.CENTER);

        JPanel pnContentLeft = new JPanel(new BorderLayout());
        pnContentLeft.add(scTable, BorderLayout.CENTER);

        JPanel pnKhoaHoc = new JPanel(new BorderLayout());
        pnKhoaHoc.add(scTblKhoaHoc, BorderLayout.CENTER);

        JPanel pnKhoaHocCua = new JPanel();
        pnKhoaHocCua.add(lblKhoaHocCua);

        pnKhoaHoc.add(pnKhoaHocCua, BorderLayout.NORTH);
        pnKhoaHoc.setPreferredSize(new Dimension(0, 210));

        pnContentLeft.add(pnKhoaHoc, BorderLayout.SOUTH);

        JPanel pnLeft = new JPanel(new BorderLayout(0, 10));
        pnLeft.add(pnHeaderLeft, BorderLayout.NORTH);
        pnLeft.add(pnContentLeft, BorderLayout.CENTER);
        //</editor-fold>

        //<editor-fold defaultstate="collapsed" desc="RIGHT">
        JPanel pnInformation = new JPanel(new GridBagLayout());
//        pnInformation.setPreferredSize(new Dimension(345, 600));
        GridBagConstraints gbc = new GridBagConstraints();

        gbc.insets = new Insets(0, 15, 5, 15);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.ipady = 4;

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 999;
        pnInformation.add(lblInformationTitle, gbc);

        gbc.gridy = 1;
        pnInformation.add(lblID, gbc);

        gbc.gridy = 2;
        pnInformation.add(txtID, gbc);

        gbc.gridy = 3;
//        pnInformation.add(, gbc);

        gbc.gridy = 4;
        pnInformation.add(lblHoTen, gbc);

        gbc.gridy = 5;
        pnInformation.add(txtHoTen, gbc);

        gbc.gridy = 6;
        pnInformation.add(lblNgaySinh, gbc);

        gbc.gridy = 7;
        pnInformation.add(txtNgaySinh, gbc);

        JPanel pnGioiTinh = new JPanel(new FlowLayout(FlowLayout.LEADING, 0, 0));
        pnGioiTinh.add(lblGioiTinh);
        pnGioiTinh.add(rdoNam);
        pnGioiTinh.add(rdoNu);

        gbc.gridy = 8;
        pnInformation.add(pnGioiTinh, gbc);

        gbc.gridy = 9;
        pnInformation.add(lblSDT, gbc);

        gbc.gridy = 10;
        pnInformation.add(txtSDT, gbc);

        gbc.gridy = 11;
        pnInformation.add(lblEmail, gbc);

        gbc.gridy = 12;
        pnInformation.add(txtEmail, gbc);

        gbc.gridy = 13;
        pnInformation.add(lblGhiChu, gbc);

        gbc.gridy = 14;
        gbc.ipady = 80;
        pnInformation.add(scGhiChu, gbc);

        gbc.gridy = 15;
        gbc.ipady = 4;
        pnInformation.add(lblKhoaHoc, gbc);

        gbc.gridy = 16;
        gbc.ipady = 4;
        pnInformation.add(cbxKhoaHoc, gbc);

        JPanel pnButton1 = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        pnButton1.add(btnNew);
        pnButton1.add(btnUpdate);
        pnButton1.add(btnSave);
        if (DangNhapFrame.nvLogin.getVaiTro() == ROLE_TRUONG_PHONG) {
            pnButton1.add(btnDelete);
        }
        pnButton1.add(btnCancel);

        pnButton2 = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
        pnButton2.add(btnAddKH);

        gbc.ipady = 4;
        gbc.gridy = 17;
        pnInformation.add(pnButton1, gbc);

        gbc.gridy = 18;
        pnInformation.add(pnButton2, gbc);

        JPanel pnItems = new JPanel();

        JPanel pnRight = new JPanel(new BorderLayout());
        pnRight.add(pnInformation, BorderLayout.NORTH);
        pnRight.add(pnItems, BorderLayout.CENTER);

        //</editor-fold>
        //<editor-fold defaultstate="collapsed" desc="CONTAINER">
        this.setLayout(new BorderLayout());
        this.add(pnLeft, BorderLayout.CENTER);
        this.add(pnRight, BorderLayout.EAST);
        //</editor-fold>

    }

    private void addEvents() {
        lockFormInfo();
        buttonStatus0();

        //load dữ liệu vào cbx năm
        try {
            loadDataToCbxYear();
        } catch (SQLException ex) {
            Logger.getLogger(QLHV.class.getName()).log(Level.SEVERE, null, ex);
        }

        //load dữ liệu lên table
        if (cbxNam.getItemCount() != 0) {
            try {
                loadDataToTable((int) cbxNam.getSelectedItem());
            } catch (SQLException ex) {
                Logger.getLogger(QLHV.class.getName()).log(Level.SEVERE, null, ex);
            } catch (ParseException ex) {
                Logger.getLogger(QLHV.class.getName()).log(Level.SEVERE, null, ex);
            }

            //load học viên đầu tiên lên form
            if (listHocVien.size() > 0) {
                hvSelected = listHocVien.get(0);

                loadDataToForm();

                lblKhoaHocCua.setText(hvSelected.getHoTen() + " đã tham gia khóa học");

                try {
                    loadDataToTblKH();

                    loadDataToCbxKhoaHoc();
                } catch (SQLException ex) {
                    Logger.getLogger(QLHV.class.getName()).log(Level.SEVERE, null, ex);
                } catch (ParseException ex) {
                    Logger.getLogger(QLHV.class.getName()).log(Level.SEVERE, null, ex);
                }

                tblHocVien.setRowSelectionInterval(0, 0);

                indexHvSelectedInTable = 0;
            }
        }

        doneLoad = true;

        // <editor-fold defaultstate="collapsed" desc="key listener txt họ tên ">
        txtHoTen.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
            }

            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_DOWN) {
                    txtNgaySinh.requestFocus();
                }

                if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                    try {
                        cancelSave();
                    } catch (SQLException ex) {
                        Logger.getLogger(QLHV.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (ParseException ex) {
                        Logger.getLogger(QLHV.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
            }
        });
        // </editor-fold>

        // <editor-fold defaultstate="collapsed" desc="Key listener ngày sinh">
        txtNgaySinh.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
            }

            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_UP && e.isShiftDown()) {
                    UpDownDate(txtNgaySinh, 1);
                    return;
                }

                if (e.getKeyCode() == KeyEvent.VK_DOWN && e.isShiftDown()) {
                    UpDownDate(txtNgaySinh, -1);
                    return;
                }

                if (e.getKeyCode() == KeyEvent.VK_DOWN) {
                    if (rdoNam.isSelected()) {
                        rdoNam.requestFocus();
                    } else {
                        rdoNu.requestFocus();
                    }
                }

                if (e.getKeyCode() == KeyEvent.VK_UP) {
                    txtHoTen.requestFocus();
                }

                if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                    try {
                        cancelSave();
                    } catch (SQLException ex) {
                        Logger.getLogger(QLHV.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (ParseException ex) {
                        Logger.getLogger(QLHV.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
            }
        });
        // </editor-fold>

        // <editor-fold defaultstate="collapsed" desc="Key listener rdoNam">
        rdoNam.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
            }

            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_UP) {
                    txtNgaySinh.requestFocus();
                }

                if (e.getKeyCode() == KeyEvent.VK_DOWN) {
                    txtSDT.requestFocus();
                }

                if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                    try {
                        cancelSave();
                    } catch (SQLException ex) {
                        Logger.getLogger(QLHV.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (ParseException ex) {
                        Logger.getLogger(QLHV.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
            }
        });
        // </editor-fold>

        // <editor-fold defaultstate="collapsed" desc="Key listener rdoNu ">
        rdoNu.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
            }

            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_UP) {
                    txtNgaySinh.requestFocus();
                }

                if (e.getKeyCode() == KeyEvent.VK_DOWN) {
                    txtSDT.requestFocus();
                }

                if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                    try {
                        cancelSave();
                    } catch (SQLException ex) {
                        Logger.getLogger(QLHV.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (ParseException ex) {
                        Logger.getLogger(QLHV.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
            }
        });
        // </editor-fold>

        // <editor-fold defaultstate="collapsed" desc="key listener txt số điện thoại">
        txtSDT.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
            }

            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_DOWN) {
                    txtEmail.requestFocus();
                }

                if (e.getKeyCode() == KeyEvent.VK_UP) {
                    if (rdoNam.isSelected()) {
                        rdoNam.requestFocus();
                    } else {
                        rdoNu.requestFocus();
                    }
                }

                if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                    try {
                        cancelSave();
                    } catch (SQLException ex) {
                        Logger.getLogger(QLHV.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (ParseException ex) {
                        Logger.getLogger(QLHV.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
            }
        });
        // </editor-fold>

        // <editor-fold defaultstate="collapsed" desc="key listener txt email">
        txtEmail.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
            }

            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_DOWN) {
                    txaGhiChu.requestFocus();
                }

                if (e.getKeyCode() == KeyEvent.VK_UP) {
                    txtSDT.requestFocus();
                }

                if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                    try {
                        cancelSave();
                    } catch (SQLException ex) {
                        Logger.getLogger(QLHV.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (ParseException ex) {
                        Logger.getLogger(QLHV.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
            }
        });
        // </editor-fold>

        // <editor-fold defaultstate="collapsed" desc="key listener txt ghi chú">
        txaGhiChu.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
            }

            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_UP) {
                    txtEmail.requestFocus();
                }

                if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                    try {
                        cancelSave();
                    } catch (SQLException ex) {
                        Logger.getLogger(QLHV.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (ParseException ex) {
                        Logger.getLogger(QLHV.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
            }
        });
        // </editor-fold>

        // <editor-fold defaultstate="collapsed" desc="Sự kiện nút btnNew ">
        btnNew.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (chkSearchAll.isSelected()) {
                    chkSearchAll.setSelected(false); // xem lại cái lỗi đó nha xem có sửa được k. Khi đăng nhập sai bấm Enter n vẫn nhận 
                }
                cbxNam.setSelectedIndex(0);
                unlockFormInfo();
                cbxKhoaHoc.setEnabled(true);
                buttonStatus1();
                resetForm();
                tblHocVien.clearSelection();
                txtHoTen.requestFocus();
                flagSave = FLAG_INSERT;
                dtmKH.setRowCount(0);
                lblKhoaHocCua.setText("");
                chkSearchAll.setEnabled(false);
                try {
                    loadDataToCbxKhoaHoc();
                } catch (SQLException ex) {
                    Logger.getLogger(QLHV.class.getName()).log(Level.SEVERE, null, ex);
                } catch (ParseException ex) {
                    Logger.getLogger(QLHV.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

        });
        // </editor-fold>

        // <editor-fold defaultstate="collapsed" desc="Sự kiện nút btnUpdate ">
        btnUpdate.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                unlockFormInfo();
                buttonStatus1();
                txtHoTen.requestFocus();
                flagSave = FLAG_UPDATE;
                chkSearchAll.setEnabled(false);
            }

        });
        // </editor-fold>

        // <editor-fold defaultstate="collapsed" desc="Sự kiện nút btnCancel ">
        btnCancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    cancelSave();
                } catch (SQLException ex) {
                    Logger.getLogger(QLHV.class.getName()).log(Level.SEVERE, null, ex);
                } catch (ParseException ex) {
                    Logger.getLogger(QLHV.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

        });
        // </editor-fold>

        // <editor-fold defaultstate="collapsed" desc="Sự kiện nút btnSave ">
        btnSave.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                switch (flagSave) {
                    // <editor-fold defaultstate="collapsed" desc="case 1: insert ">
                    case 1:
                        try {
                            insertHV();
                        } catch (ParseException ex) {
                            Logger.getLogger(QLHV.class.getName()).log(Level.SEVERE, null, ex);
                        } catch (SQLException ex) {
                            Logger.getLogger(QLHV.class.getName()).log(Level.SEVERE, null, ex);
                        } catch (Exception ex) {
                            Logger.getLogger(QLHV.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        break;
                    // </editor-fold>

                    // <editor-fold defaultstate="collapsed" desc="case 2: update ">
                    case 2:
                        try {
                            updateHV();
                        } catch (SQLException ex) {
                            Logger.getLogger(QLHV.class.getName()).log(Level.SEVERE, null, ex);
                        } catch (ParseException ex) {
                            Logger.getLogger(QLHV.class.getName()).log(Level.SEVERE, null, ex);
                        } catch (Exception ex) {
                            Logger.getLogger(QLHV.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        break;
                    // </editor-fold>

                    // <editor-fold defaultstate="collapsed" desc="case 3: addKH ">
                    case 3:
                        insertHvToKh();
                        break;
                    // </editor-fold>

                    default:
                        break;
                }

                DangNhapFrame.pnTKHV.refresh();
                DangNhapFrame.pnQLHVKH.refresh();
            }
        });
        // </editor-fold>

        // <editor-fold defaultstate="collapsed" desc="Sự kiện lọc theo năm ">
        cbxNam.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (flagSearch == NO_SEARCH) {
                    if (e.getStateChange() == ItemEvent.SELECTED) {
                        try {
                            loadDataToTable((int) cbxNam.getSelectedItem());
                        } catch (SQLException ex) {
                            Logger.getLogger(QLHV.class.getName()).log(Level.SEVERE, null, ex);
                        } catch (ParseException ex) {
                            Logger.getLogger(QLHV.class.getName()).log(Level.SEVERE, null, ex);
                        }

                        int stt = (int) tblHocVien.getValueAt(0, 0) - 1;
                        hvSelected = listHocVien.get(stt);
                        loadDataToForm();

                        try {
                            loadDataToTblKH();

                            loadDataToCbxKhoaHoc();
                        } catch (SQLException ex) {
                            Logger.getLogger(QLHV.class.getName()).log(Level.SEVERE, null, ex);
                        } catch (ParseException ex) {
                            Logger.getLogger(QLHV.class.getName()).log(Level.SEVERE, null, ex);
                        }

                        lblKhoaHocCua.setText(hvSelected.getHoTen() + " đã tham gia khóa học");
                    }
                } else {
                    try {
                        loadDataToTable((int) cbxNam.getSelectedItem());

                        indexHvInListSearch = 0;

                        listHvSearch = hocVienDAO.getByYear(txtSearchHV.getText().trim(), cbxNam.getSelectedItem());

                        searchHV();

                        try {
                            loadDataToTblKH();

                            loadDataToCbxKhoaHoc();
                        } catch (SQLException ex) {
                            Logger.getLogger(QLHV.class.getName()).log(Level.SEVERE, null, ex);
                        } catch (ParseException ex) {
                            Logger.getLogger(QLHV.class.getName()).log(Level.SEVERE, null, ex);
                        }

                        if (flagFind == NOT_FOUND) {
                            dtmKH.setRowCount(0);

                            lblKhoaHocCua.setText("");

                            btnUpdate.setEnabled(false);
                            btnDelete.setEnabled(false);
                            btnAddKH.setEnabled(false);
                        } else {
                            lblKhoaHocCua.setText(hvSelected.getHoTen() + " đã tham gia khóa học");
                            btnUpdate.setEnabled(true);
                            btnDelete.setEnabled(true);
                            btnAddKH.setEnabled(true);
                        }

                    } catch (SQLException ex) {
                        Logger.getLogger(QLHV.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (ParseException ex) {
                        Logger.getLogger(QLHV.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }

            }
        });
        // </editor-fold>

        // <editor-fold defaultstate="collapsed" desc="Sự kiện click vào hàng trong bảng học viên">
        tblHocVien.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                // <editor-fold defaultstate="collapsed" desc="Click chuột phải ">
                if (e.getButton() == MouseEvent.BUTTON3) {
                    int r = tblHocVien.rowAtPoint(e.getPoint());
                    if (r >= 0 && r < tblHocVien.getRowCount()) {
                        tblHocVien.setRowSelectionInterval(r, r);
                    } else {
                        tblHocVien.clearSelection();
                    }
                }
                // </editor-fold>
                if (flagSave != 0) {
                    String mess = "";
                    switch (flagSave) {
                        case FLAG_INSERT:
                            mess = "Bạn có muốn bỏ qua thao tác thêm mới học viên không?";
                            break;
                        case FLAG_UPDATE:
                            mess = "Bạn có muốn bỏ qua thao tác cập nhật thông tin học viên không?";
                            break;
                        case ADD_KH:
                            mess = "Bạn có muốn bỏ qua thao tác thêm học viên vào khóa học không?";
                            break;
                    }

                    int choose = JOptionPane.showConfirmDialog(null, mess, "Hỏi", JOptionPane.YES_NO_OPTION);

                    if (choose == JOptionPane.YES_OPTION) {
                        indexHvSelectedInTable = tblHocVien.getSelectedRow();
                        int indexInList = (int) tblHocVien.getValueAt(indexHvSelectedInTable, 0) - 1;//vị trí hv được chọn trong mảng

                        hvSelected = listHocVien.get(indexInList);

                        lblKhoaHocCua.setText(hvSelected.getHoTen() + " đã tham gia khóa học");

                        loadDataToForm();

                        lockFormInfo();
                        flagSave = FLAG_DEFAULT;
                        buttonStatus0();

                        if (e.getButton() == MouseEvent.BUTTON3) {
                            JPopupMenu popup = new PopupmenuTblHV();
                            popup.show(e.getComponent(), e.getX(), e.getY());
                        }

                        try {
                            loadDataToTblKH();

                            loadDataToCbxKhoaHoc();
                        } catch (SQLException ex) {
                            Logger.getLogger(QLHV.class.getName()).log(Level.SEVERE, null, ex);
                        } catch (ParseException ex) {
                            Logger.getLogger(QLHV.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                } else {
                    if (!chkSearchAll.isSelected()) {
                        indexHvSelectedInTable = tblHocVien.getSelectedRow();
                        int indexInList = (int) tblHocVien.getValueAt(tblHocVien.getSelectedRow(), 0) - 1;//vị trí hv được chọn trong mảng
                        hvSelected = listHocVien.get(indexInList);
                    } else {
                        indexHvSelectedInTable = tblHocVien.getSelectedRow();
                        int indexInList = (int) tblHocVien.getValueAt(tblHocVien.getSelectedRow(), 0) - 1;//vị trí hv được chọn trong mảng
                        hvSelected = listHvSearch.get(indexInList);
                    }

                    lblKhoaHocCua.setText(hvSelected.getHoTen() + " đã tham gia khóa học");

                    loadDataToForm();

                    lockFormInfo();

                    flagSave = FLAG_DEFAULT;
                    buttonStatus0();

                    if (e.getButton() == MouseEvent.BUTTON3) {
                        JPopupMenu popup = new PopupmenuTblHV();
                        popup.show(e.getComponent(), e.getX(), e.getY());
                    }

                    try {
                        loadDataToTblKH();

                        loadDataToCbxKhoaHoc();
                    } catch (SQLException ex) {
                        Logger.getLogger(QLHV.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (ParseException ex) {
                        Logger.getLogger(QLHV.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                btnAddKH.setEnabled(true);

                if (chkSearchAll.isSelected()) {
                    btnSearchContinue.setEnabled(false);
                    cbxNam.setEnabled(false);
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

        // <editor-fold defaultstate="collapsed" desc="Sự kiện nút btnSearch ">
        btnSearch.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                txtSearchHV.requestFocus();
            }
        });
        // </editor-fold>

        // <editor-fold defaultstate="collapsed" desc="Tìm kiếm HV với txtSearchHv ">
        txtSearchHV.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                if (txtSearchHV.getText().length() > 0) {
                    flagSearch = SEARCH;

                    if (chkSearchAll.isSelected() == false) {
                        indexHvInListSearch = 0;

                        try {
                            listHvSearch = hocVienDAO.getByYear(txtSearchHV.getText().trim(), cbxNam.getSelectedItem());

                            searchHV();

                            loadDataToTblKH();
                            loadDataToCbxKhoaHoc();
                        } catch (SQLException ex) {
                            Logger.getLogger(QLHV.class.getName()).log(Level.SEVERE, null, ex);
                        } catch (ParseException ex) {
                            Logger.getLogger(QLHV.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    } else {
                        try {
                            listHvSearch = hocVienDAO.get(txtSearchHV.getText().trim());

                            int stt = 1;
                            model.setRowCount(0);
                            for (HocVien hv : listHvSearch) {
                                model.addRow(new Object[]{stt, hv.getId(), hv.getHoTen(), (hv.isGioiTinh() ? "Nam" : "Nữ"), hv.getNgaySinh(), hv.getDienThoai(), hv.getNgayDK()});
                                stt++;
                            }

                            searchHV();
                            loadDataToTblKH();
                            loadDataToCbxKhoaHoc();
                            loadDataToForm();
                        } catch (SQLException ex) {
                            Logger.getLogger(QLHV.class.getName()).log(Level.SEVERE, null, ex);
                        } catch (ParseException ex) {
                            Logger.getLogger(QLHV.class.getName()).log(Level.SEVERE, null, ex);
                        }

                        if (flagFind == NOT_FOUND) {
                            dtmKH.setRowCount(0);
                            model.setRowCount(0);
                            resetForm();
                        }
                    }
                }

                if (flagFind == NOT_FOUND) {
                    btnUpdate.setEnabled(false);
                    btnNew.setEnabled(false);
                    btnDelete.setEnabled(false);
                    btnAddKH.setEnabled(false);
                    lblKhoaHocCua.setText("");
                    dtmKH.setRowCount(0);
                } else {
                    btnUpdate.setEnabled(true);
                    btnNew.setEnabled(true);
                    btnDelete.setEnabled(true);
                    btnAddKH.setEnabled(true);
                    lblKhoaHocCua.setText(hvSelected.getHoTen() + " đã tham gia khóa học");
                }
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                if (txtSearchHV.getText().length() > 0) {
                    flagSearch = SEARCH;
                    if (chkSearchAll.isSelected() == false) {
                        indexHvInListSearch = 0;

                        try {
                            listHvSearch = hocVienDAO.getByYear(txtSearchHV.getText().trim(), cbxNam.getSelectedItem());

                            searchHV();

                            loadDataToTblKH();

                            loadDataToCbxKhoaHoc();

                            lblKhoaHocCua.setText(hvSelected.getHoTen() + " đã tham gia khóa học");
                        } catch (SQLException ex) {
                            Logger.getLogger(QLHV.class.getName()).log(Level.SEVERE, null, ex);
                        } catch (ParseException ex) {
                            Logger.getLogger(QLHV.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    } else {
                        try {
                            listHvSearch = hocVienDAO.get(txtSearchHV.getText().trim());

                            int stt = 1;
                            model.setRowCount(0);
                            for (HocVien hv : listHvSearch) {
                                model.addRow(new Object[]{stt, hv.getId(), hv.getHoTen(), (hv.isGioiTinh() ? "Nam" : "Nữ"), hv.getNgaySinh(), hv.getDienThoai(), hv.getNgayDK()});
                                stt++;
                            }

                            searchHV();
                            loadDataToTblKH();
                            loadDataToCbxKhoaHoc();
                            loadDataToForm();
                        } catch (SQLException ex) {
                            Logger.getLogger(QLHV.class.getName()).log(Level.SEVERE, null, ex);
                        } catch (ParseException ex) {
                            Logger.getLogger(QLHV.class.getName()).log(Level.SEVERE, null, ex);
                        }

                        if (flagFind == NOT_FOUND) {
                            btnUpdate.setEnabled(false);
                            btnNew.setEnabled(false);
                            btnDelete.setEnabled(false);
                            btnAddKH.setEnabled(false);
                            dtmKH.setRowCount(0);
                            model.setRowCount(0);
                            resetForm();
                            lblKhoaHocCua.setText("");
                        } else {
                            btnUpdate.setEnabled(true);
                            btnNew.setEnabled(true);
                            btnDelete.setEnabled(true);
                            btnAddKH.setEnabled(true);
                            lblKhoaHocCua.setText(hvSelected.getHoTen() + " đã tham gia khóa học");
                        }
                    }
                } else {
                    flagSearch = NO_SEARCH;

                    if (chkSearchAll.isSelected() == false) {
                        try {
                            loadDataToTable((int) cbxNam.getSelectedItem());

                            indexHvSelectedInTable = tblHocVien.getSelectedRow();
                            int indexInList = (int) tblHocVien.getValueAt(indexHvSelectedInTable, 0) - 1;//vị trí hv được chọn trong mảng

                            hvSelected = listHocVien.get(indexInList);

                            loadDataToTblKH();
                        } catch (SQLException ex) {
                            Logger.getLogger(QLHV.class.getName()).log(Level.SEVERE, null, ex);
                        } catch (ParseException ex) {
                            Logger.getLogger(QLHV.class.getName()).log(Level.SEVERE, null, ex);
                        }

                        Rectangle rect = tblHocVien.getCellRect(0, 0, true);
                        tblHocVien.scrollRectToVisible(rect);
                    } else {
                        cbxNam.setEnabled(false);
                        btnSearchContinue.setEnabled(false);
                        resetForm();
                        dtmKH.setRowCount(0);
                        model.setRowCount(0);
                        btnUpdate.setEnabled(false);
                        btnNew.setEnabled(false);
                        btnDelete.setEnabled(false);
                        btnAddKH.setEnabled(false);
                        lblKhoaHocCua.setText("");
                    }
                }

                if (flagFind == NOT_FOUND) {
                    btnUpdate.setEnabled(false);
                    btnNew.setEnabled(false);
                    btnDelete.setEnabled(false);
                    btnAddKH.setEnabled(false);
                } else {
                    btnUpdate.setEnabled(true);
                    btnNew.setEnabled(true);
                    btnDelete.setEnabled(true);
                    btnAddKH.setEnabled(true);
                }
            }

            @Override
            public void changedUpdate(DocumentEvent e) {

            }
        });
        // </editor-fold>

        // <editor-fold defaultstate="collapsed" desc="Sự kiện clear txtSearchHv với phím esc ">
        txtSearchHV.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
            }

            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                    if (chkSearchAll.isSelected() == false) {
                        try {
                            loadDataToTable((int) cbxNam.getSelectedItem());
                        } catch (SQLException ex) {
                            Logger.getLogger(QLHV.class.getName()).log(Level.SEVERE, null, ex);
                        } catch (ParseException ex) {
                            Logger.getLogger(QLHV.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        txtSearchHV.setText("");
                        flagSearch = NO_SEARCH;

                        indexHvSelectedInTable = tblHocVien.getSelectedRow();
                        int indexInList = (int) tblHocVien.getValueAt(indexHvSelectedInTable, 0) - 1;//vị trí hv được chọn trong mảng

                        hvSelected = listHocVien.get(indexInList);
                        loadDataToForm();

                        lblKhoaHocCua.setText(hvSelected.getHoTen() + " đã tham gia khóa học");

                        try {
                            loadDataToTblKH();
                        } catch (SQLException ex) {
                            Logger.getLogger(QLHV.class.getName()).log(Level.SEVERE, null, ex);
                        } catch (ParseException ex) {
                            Logger.getLogger(QLHV.class.getName()).log(Level.SEVERE, null, ex);
                        }

                        Rectangle rect = tblHocVien.getCellRect(0, 0, true);
                        tblHocVien.scrollRectToVisible(rect);

                        btnUpdate.setEnabled(true);
                        btnNew.setEnabled(true);
                        btnDelete.setEnabled(true);
                        btnAddKH.setEnabled(true);
                    } else {
                        dtmKH.setRowCount(0);
                        model.setRowCount(0);
                        resetForm();
                        txtSearchHV.setText("");
                        btnNew.setEnabled(false);
                        btnUpdate.setEnabled(false);
                        btnDelete.setEnabled(false);
                        btnAddKH.setEnabled(false);
                        lblKhoaHocCua.setText("");
                    }
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
            }
        });
        // </editor-fold>

        // <editor-fold defaultstate="collapsed" desc="Sự kiện nút btnSearchContinue ">
        btnSearchContinue.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    continueSearch();
                } catch (SQLException ex) {
                    Logger.getLogger(QLHV.class.getName()).log(Level.SEVERE, null, ex);
                } catch (ParseException ex) {
                    Logger.getLogger(QLHV.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        // </editor-fold>

        // <editor-fold defaultstate="collapsed" desc="Tiếp tục tìm khi nhấn enter trong txtSearch ">
        txtSearchHV.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
            }

            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER && chkSearchAll.isSelected() == false) {
                    try {
                        continueSearch();
                    } catch (SQLException ex) {
                        Logger.getLogger(QLHV.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (ParseException ex) {
                        Logger.getLogger(QLHV.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
            }
        });
        // </editor-fold>

        // <editor-fold defaultstate="collapsed" desc="Sự kiện nút btnDelete ">
        btnDelete.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    deleteHV();
                } catch (SQLException ex) {
                    Logger.getLogger(QLHV.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        // </editor-fold>

        // <editor-fold defaultstate="collapsed" desc="Sự kiện up down table ">
        tblHocVien.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
            }

            @Override
            public void keyPressed(KeyEvent e) {
            }

            @Override
            public void keyReleased(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_UP) {
                    int stt = (int) tblHocVien.getValueAt(tblHocVien.getSelectedRow(), 0) - 1;
                    hvSelected = listHocVien.get(stt);

                    loadDataToForm();

                    lblKhoaHocCua.setText(hvSelected.getHoTen() + " đã tham gia khóa học");

                    try {
                        loadDataToCbxKhoaHoc();
                        loadDataToTblKH();
                    } catch (SQLException ex) {
                        Logger.getLogger(QLHV.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (ParseException ex) {
                        Logger.getLogger(QLHV.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }

                if (e.getKeyCode() == KeyEvent.VK_DOWN) {
                    int stt = (int) tblHocVien.getValueAt(tblHocVien.getSelectedRow(), 0) - 1;
                    hvSelected = listHocVien.get(stt);

                    loadDataToForm();
                    lblKhoaHocCua.setText(hvSelected.getHoTen() + " đã tham gia khóa học");
                    try {
                        loadDataToCbxKhoaHoc();
                        loadDataToTblKH();
                    } catch (SQLException ex) {
                        Logger.getLogger(QLHV.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (ParseException ex) {
                        Logger.getLogger(QLHV.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }

                indexHvSelectedInTable = tblHocVien.getSelectedRow();
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

        // <editor-fold defaultstate="collapsed" desc="Sự kiện click vào hàng trong bảng khóa học ">
        tblKhoaHoc.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                // <editor-fold defaultstate="collapsed" desc="Click chuột phải ">
                if (e.getButton() == MouseEvent.BUTTON3) {
                    int r = tblKhoaHoc.rowAtPoint(e.getPoint());
                    if (r >= 0 && r < tblKhoaHoc.getRowCount()) {
                        tblKhoaHoc.setRowSelectionInterval(r, r);
                    } else {
                        tblKhoaHoc.clearSelection();
                    }
                }
                // </editor-fold>

                if (flagSave != 0) {
                    String mess = "";
                    switch (flagSave) {
                        case FLAG_INSERT:
                            mess = "Bạn có muốn bỏ qua thao tác thêm mới học viên không?";
                            break;
                        case FLAG_UPDATE:
                            mess = "Bạn có muốn bỏ qua thao tác cập nhật thông tin học viên không?";
                            break;
                        case ADD_KH:
                            mess = "Bạn có muốn bỏ qua thao tác thêm học viên vào khóa học không?";
                            break;
                    }

                    int choose = JOptionPane.showConfirmDialog(null, mess, "Hỏi", JOptionPane.YES_NO_OPTION);

                    if (choose == JOptionPane.YES_OPTION) {
                        indexHvSelectedInTable = tblHocVien.getSelectedRow();
                        int indexInList = (int) tblHocVien.getValueAt(indexHvSelectedInTable, 0) - 1;//vị trí hv được chọn trong mảng

                        hvSelected = listHocVien.get(indexInList);

                        loadDataToForm();

                        lockFormInfo();
                        flagSave = FLAG_DEFAULT;
                        buttonStatus0();

                        if (e.getButton() == MouseEvent.BUTTON3) {
                            JPopupMenu popup = new PopupmenuTblKH();
                            popup.show(e.getComponent(), e.getX(), e.getY());
                        }
                    }
                } else {
                    if (!chkSearchAll.isSelected()) {
                        indexHvSelectedInTable = tblHocVien.getSelectedRow();
                        int indexInList = (int) tblHocVien.getValueAt(tblHocVien.getSelectedRow(), 0) - 1;//vị trí hv được chọn trong mảng
                        hvSelected = listHocVien.get(indexInList);
                    } else {
                        indexHvSelectedInTable = tblHocVien.getSelectedRow();
                        int indexInList = (int) tblHocVien.getValueAt(tblHocVien.getSelectedRow(), 0) - 1;//vị trí hv được chọn trong mảng
                        hvSelected = listHvSearch.get(indexInList);
                    }

                    loadDataToForm();

                    lockFormInfo();

                    flagSave = FLAG_DEFAULT;
                    buttonStatus0();

                    if (e.getButton() == MouseEvent.BUTTON3) {
                        JPopupMenu popup = new PopupmenuTblKH();
                        popup.show(e.getComponent(), e.getX(), e.getY());
                    }
                }
                if (chkSearchAll.isSelected()) {
                    btnSearchContinue.setEnabled(false);
                    cbxNam.setEnabled(false);
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

        // <editor-fold defaultstate="collapsed" desc="Sự kiện nút thêm khóa học ">
        btnAddKH.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cbxKhoaHoc.setEnabled(true);
                txtSearchHV.setEditable(false);
                flagSave = ADD_KH;
                cbxKhoaHoc.requestFocus();
                buttonStatus1();
            }
        });
        // </editor-fold>

        // <editor-fold defaultstate="collapsed" desc="Sự kiện check vào tìm trong tất cả học viên ">
        chkSearchAll.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    cbxNam.setEnabled(false);
                    btnSearchContinue.setEnabled(false);
                    resetForm();
                    dtmKH.setRowCount(0);
                    model.setRowCount(0);
                    btnUpdate.setEnabled(false);
                    btnNew.setEnabled(false);
                    btnDelete.setEnabled(false);
                    btnAddKH.setEnabled(false);

                    if (flagSearch == SEARCH) {
                        try {
                            listHvSearch = hocVienDAO.get(txtSearchHV.getText().trim());

                            int stt = 1;
                            for (HocVien hv : listHvSearch) {
                                model.addRow(new Object[]{stt, hv.getId(), hv.getHoTen(), (hv.isGioiTinh() ? "Nam" : "Nữ"), hv.getNgaySinh(), hv.getDienThoai(), hv.getNgayDK()});
                                stt++;
                            }

                            searchHV();

                            loadDataToTblKH();
                            loadDataToCbxKhoaHoc();
                            loadDataToForm();
                            lblKhoaHocCua.setText(hvSelected.getHoTen() + " đã tham gia khóa học");
                        } catch (SQLException ex) {
                            Logger.getLogger(QLHV.class.getName()).log(Level.SEVERE, null, ex);
                        } catch (ParseException ex) {
                            Logger.getLogger(QLHV.class.getName()).log(Level.SEVERE, null, ex);
                        }

                        if (flagFind == NOT_FOUND) {
                            btnUpdate.setEnabled(false);
                            btnNew.setEnabled(false);
                            btnDelete.setEnabled(false);
                            btnAddKH.setEnabled(false);
                            lblKhoaHocCua.setText("");
                            dtmKH.setRowCount(0);
                        } else {
                            btnUpdate.setEnabled(true);
                            btnNew.setEnabled(true);
                            btnDelete.setEnabled(true);
                            btnAddKH.setEnabled(true);
                            lblKhoaHocCua.setText(hvSelected.getHoTen() + " đã tham gia khóa học");
                        }
                    } else {
                        lblKhoaHocCua.setText("");
                    }
                } else if (e.getStateChange() == ItemEvent.DESELECTED) {
                    cbxNam.setEnabled(true);
                    btnSearchContinue.setEnabled(true);
                    btnUpdate.setEnabled(true);
                    btnNew.setEnabled(true);
                    btnDelete.setEnabled(true);
                    btnAddKH.setEnabled(true);
                    dtmKH.setRowCount(0);
                    model.setRowCount(0);

                    if (flagSearch == SEARCH) {
                        try {
                            loadDataToTable((int) cbxNam.getSelectedItem());

                            listHvSearch = hocVienDAO.getByYear(txtSearchHV.getText().trim(), cbxNam.getSelectedItem());

                            searchHV();

                            loadDataToTblKH();

                            loadDataToCbxKhoaHoc();
                        } catch (SQLException ex) {
                            Logger.getLogger(QLHV.class.getName()).log(Level.SEVERE, null, ex);
                        } catch (ParseException ex) {
                            Logger.getLogger(QLHV.class.getName()).log(Level.SEVERE, null, ex);
                        }

                        if (flagFind == NOT_FOUND) {
                            btnUpdate.setEnabled(false);
                            btnNew.setEnabled(false);
                            btnDelete.setEnabled(false);
                            btnAddKH.setEnabled(false);
                            lblKhoaHocCua.setText("");
                            dtmKH.setRowCount(0);
                        } else {
                            btnUpdate.setEnabled(true);
                            btnNew.setEnabled(true);
                            btnDelete.setEnabled(true);
                            btnAddKH.setEnabled(true);
                            lblKhoaHocCua.setText(hvSelected.getHoTen() + " đã tham gia khóa học");
                        }
                    } else {
                        //load dữ liệu lên table
                        if (cbxNam.getItemCount() != 0) {
                            try {
                                loadDataToTable((int) cbxNam.getSelectedItem());
                            } catch (SQLException ex) {
                                Logger.getLogger(QLHV.class.getName()).log(Level.SEVERE, null, ex);
                            } catch (ParseException ex) {
                                Logger.getLogger(QLHV.class.getName()).log(Level.SEVERE, null, ex);
                            }

                            //load học viên đầu tiên lên form
                            if (listHocVien.size() > 0) {
                                hvSelected = listHocVien.get(0);
                                loadDataToForm();

                                try {
                                    loadDataToTblKH();

                                    loadDataToCbxKhoaHoc();
                                } catch (SQLException ex) {
                                    Logger.getLogger(QLHV.class.getName()).log(Level.SEVERE, null, ex);
                                } catch (ParseException ex) {
                                    Logger.getLogger(QLHV.class.getName()).log(Level.SEVERE, null, ex);
                                }

                                tblHocVien.setRowSelectionInterval(0, 0);

                                indexHvSelectedInTable = 0;

                                lblKhoaHocCua.setText(hvSelected.getHoTen() + " đã tham gia khóa học");
                            } else {
                                lblKhoaHocCua.setText("");
                                dtmKH.setRowCount(0);
                            }
                        }
                    }
                }
            }
        });
        // </editor-fold>
    }

    // <editor-fold defaultstate="collapsed" desc="KHÓA FORM NHẬP THÔNG TIN VÀ NÚT LƯU SỬA XÓA ">
    private void lockFormInfo() {
        txtSearchHV.setEditable(true);
        btnSearchContinue.setEnabled(true);
        btnSearch.setEnabled(true);
        txtID.setEditable(false);
        txtHoTen.setEditable(false);
        txtNgaySinh.setEditable(false);
        rdoNam.setEnabled(false);
        rdoNu.setEnabled(false);
        txtSDT.setEditable(false);
        txtEmail.setEditable(false);
        txaGhiChu.setEditable(false);
        cbxKhoaHoc.setEnabled(false);
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="MỞ KHÓA FORM NHẬP THÔNG TIN ">
    private void unlockFormInfo() {
        txtSearchHV.setEditable(false);
        btnSearchContinue.setEnabled(false);
        btnSearch.setEnabled(false);
        txtHoTen.setEditable(true);
        txtNgaySinh.setEditable(true);
        rdoNam.setEnabled(true);
        rdoNu.setEnabled(true);
        txtSDT.setEditable(true);
        txtEmail.setEditable(true);
        txaGhiChu.setEditable(true);
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="BUTTON STATUS MẶC ĐỊNH ">
    private void buttonStatus0() {
        btnSave.setEnabled(false);
        btnCancel.setEnabled(false);
        btnDelete.setEnabled(true);
        btnNew.setEnabled(true);
        btnUpdate.setEnabled(true);

        btnSave.setVisible(false);
        btnCancel.setVisible(false);
        btnDelete.setVisible(true);
        btnNew.setVisible(true);
        btnUpdate.setVisible(true);
        btnAddKH.setVisible(true);

        cbxNam.setEnabled(true);
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="BUTTON STATUS SAVE">
    private void buttonStatus1() {
        btnSave.setEnabled(true);
        btnCancel.setEnabled(true);
        btnDelete.setEnabled(false);
        btnNew.setEnabled(false);
        btnUpdate.setEnabled(false);

        btnSave.setVisible(true);
        btnCancel.setVisible(true);
        btnDelete.setVisible(false);
        btnNew.setVisible(false);
        btnUpdate.setVisible(false);
        btnAddKH.setVisible(false);

        cbxNam.setEnabled(false);
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="CLEAR FOMR">
    private void resetForm() {
        txtID.setText("");
        txtHoTen.setText("");
        txtEmail.setText("");
        txtNgaySinh.setText("");
        rdoNam.setSelected(true);
        txtSDT.setText("");
        txaGhiChu.setText("");
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="LOAD DỮ LIỆU VÀO TABLE ">
    private void loadDataToTable(int year) throws SQLException, ParseException {
        listHocVien = hocVienDAO.getByYearDK(year);

        model.setRowCount(0);

        int stt = 1;
        for (HocVien hocVien : listHocVien) {
            model.addRow(new Object[]{stt,
                hocVien.getId(),
                hocVien.getHoTen(),
                hocVien.isGioiTinh() == true ? "Nam" : "Nữ",
                hocVien.getNgaySinh(),
                hocVien.getDienThoai(),
                dateHelper.castDateForm3ToForm1(hocVien.getNgayDK())});
            stt++;
        }

        tblHocVien.setRowSelectionInterval(0, 0);
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc=" LOAD DỮ LIỆU VÀO cbxYear ">
    private void loadDataToCbxYear() throws SQLException {
        List<Integer> listYear = hocVienDAO.getListYearDK();

        for (Integer integer : listYear) {
            cbxNam.addItem(integer);
        }
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="LOAD THÔNG TIN LÊN FORM ">
    private void loadDataToForm() {
        txtID.setText(hvSelected.getId());
        txtHoTen.setText(hvSelected.getHoTen());
        txtNgaySinh.setText(hvSelected.getNgaySinh());

        if (hvSelected.isGioiTinh()) {
            rdoNam.setSelected(true);
        } else {
            rdoNu.setSelected(true);
        }

        txtSDT.setText(hvSelected.getDienThoai());
        txtEmail.setText(hvSelected.getEmail());
        txaGhiChu.setText(hvSelected.getGhiChu());
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="TÌM KIẾM HỌC VIÊN ">
    private void searchHV() throws SQLException, ParseException {
        if (listHvSearch.size() > 0) {
            if (chkSearchAll.isSelected() == false) {
                HocVien hvInListSearch = listHvSearch.get(indexHvInListSearch);

                for (HocVien hocVien : listHocVien) {
                    if (hocVien.getId().equals(hvInListSearch.getId())) {
                        hvSelected = hocVien;

                        loadDataToForm();

                        for (int i = 0; i < tblHocVien.getRowCount(); i++) {
                            if (tblHocVien.getValueAt(i, 1).equals(hvSelected.getId())) {
                                tblHocVien.setRowSelectionInterval(i, i);
                                flagFind = FOUND;

                                Rectangle rect = tblHocVien.getCellRect(i, 0, true);
                                tblHocVien.scrollRectToVisible(rect);
                                return;
                            }
                        }
                    }
                }
            } else {
                flagFind = FOUND;

                hvSelected = listHvSearch.get(0);
                loadDataToForm();
                tblHocVien.setRowSelectionInterval(0, 0);
            }
            lblKhoaHocCua.setText(hvSelected.getHoTen() + " đã tham gia khóa học");
        } else {
            flagFind = NOT_FOUND;
            resetForm();
            tblHocVien.clearSelection();
            dtmKH.setRowCount(0);
            lblKhoaHocCua.setText("");
        }
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="CHECK INFO HỌC VIÊN ">
    private boolean checkInfo() throws SQLException, Exception {
        if (!txtHoTen.getText().matches("^[A-Za-zÀÁÂÃÈÉÊÌÍÒÓÔÕÙÚÝàáâãèéêìíòóôõùúýĂăĐđĨĩŨũƠơƯưẠ-ỹ\\s\']{5,100}$")) {
            JOptionPane.showMessageDialog(null, "Vui lòng nhập họ tên đầy đủ, không chứa số và các ký tự đặc biệt \n, . / ; < > ? : \" { } - = _ + ` ~ ! @ $ % ^ & * ( ) \\ |", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            txtHoTen.requestFocus();
            return false;
        }

        if (!txtNgaySinh.getText().matches("^[0-9\\-]+$")) {
            JOptionPane.showMessageDialog(null, "Ngày sinh phải có định dạng ngày-tháng-năm!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            txtNgaySinh.requestFocus();
            return false;
        }

        try {
            Date ngaySinh = dateHelper.toDate(txtNgaySinh.getText());

            if (ngaySinh.getYear() > (new Date()).getYear() - 18) {
                JOptionPane.showMessageDialog(null, "Học viên phải từ 18 tuổi trở lên!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
                txtNgaySinh.requestFocus();
                return false;
            }
        } catch (ParseException ex) {
//            Logger.getLogger(QLHV.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(null, "Ngày sinh phải có định dạng ngày-tháng-năm!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            txtNgaySinh.requestFocus();
            return false;
        }

        if (txtSDT.getText().length() == 0) {
            JOptionPane.showMessageDialog(null, "Vui lòng nhập số điện thoại!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            txtSDT.requestFocus();
            return false;
        }

        if (!txtSDT.getText().matches("^(0|\\+84){1}(9|3|7|8|5){1}\\d{8}$")) {
            JOptionPane.showMessageDialog(null, "Số điện thoại không hợp lệ!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            txtSDT.requestFocus();
            return false;
        }

        if (hocVienDAO.checkPhoneExistsInDb(txtSDT.getText())) {
            JOptionPane.showMessageDialog(null, "Số điện thoại đã được sử dụng để đăng ký học!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            txtSDT.requestFocus();
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

        if (mailHelper.checkEmail(txtEmail.getText()).equals("false")) {
            JOptionPane.showMessageDialog(null, "Email không tồn tại. Vui lòng kiểm tra lại!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            txtEmail.requestFocus();
            return false;
        }

        if (flagSave == FLAG_INSERT) {
            if (hocVienDAO.checkEmailExistsInDb(txtEmail.getText().trim())) {
                JOptionPane.showMessageDialog(null, "Email đã được sử dụng để đăng ký học!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
                txtEmail.requestFocus();
                return false;
            }
        }

        if (flagSave == FLAG_INSERT) {
            if (hocVienDAO.checkHvExists(txtHoTen.getText(), rdoNam.isSelected(), dateHelper.castDateForm1ToForm2(txtNgaySinh.getText()))) {
                String hoTen = txtHoTen.getText();
                String gtinh = rdoNam.isSelected() == true ? "Nam" : "Nữ";
                String ngaySinh = txtNgaySinh.getText();
                int choose = JOptionPane.showConfirmDialog(null, "Đã tồn tại Học viên " + hoTen + " - " + gtinh + " (" + ngaySinh + ").\n Bạn có muốn kiểm tra lại không?", "Hỏi", JOptionPane.YES_NO_OPTION);

                if (choose == JOptionPane.YES_OPTION || choose == JOptionPane.CLOSED_OPTION) {
                    return false;
                }
            }
        }

        return true;
    }

    // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="TẠO MÃ HỌC VIÊN ">
    private String createID() throws SQLException {
        String lastID = hocVienDAO.getLastIdHocVien();

        if (lastID != null) {
            StringBuilder ID = new StringBuilder();
            ID.append("HV");

            int pathNumber = Integer.parseInt(lastID.substring(2)) + 1;

            for (int i = 0; i < 5 - String.valueOf(pathNumber).length(); i++) {
                ID.append("0");
            }

            ID.append(pathNumber);

            return ID.toString();
        } else {
            return "HV00001";
        }
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="THÊM HỌC VIÊN MỚI ">
    private void insertHV() throws ParseException, SQLException, Exception {
        if (checkInfo()) {
            String ngaySinh = dateHelper.castDateForm1ToForm2(txtNgaySinh.getText());
            boolean gt = true;

            if (rdoNu.isSelected()) {
                gt = false;
            }

            HocVien hv = new HocVien(createID(), txtHoTen.getText(), ngaySinh, gt, txtSDT.getText(), txtEmail.getText(), txaGhiChu.getText());

            int i = hocVienDAO.insert(hv);

            if (i > 0) {
                loadDataToTable((int) cbxNam.getSelectedItem());
                lockFormInfo();
                buttonStatus0();
                flagSave = 0;
                hvSelected = listHocVien.get(listHocVien.size() - 1);
                loadDataToForm();

                for (int j = 0; j < tblHocVien.getRowCount(); j++) {
                    if (tblHocVien.getValueAt(j, 1).equals(hvSelected.getId())) {
                        tblHocVien.setRowSelectionInterval(j, j);
                        break;
                    }
                }

                lblKhoaHocCua.setText(hvSelected.getHoTen() + " đã tham gia khóa học");

                //send mail thông báo đăng ký thành công
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        String to = hvSelected.getEmail();
                        String sub = "Thông báo đăng ký học viên tại Lập trình City";
                        String content = "Chào " + hvSelected.getHoTen() + "!"
                                + "\nBạn đã được đăng ký tham gia học tại Lập trình City thành công. Chúng tôi gửi mail xác nhận thông tin của bạn:"
                                + "\n- Mã học viên: " + hvSelected.getId()
                                + "\n- Họ và tên: " + hvSelected.getHoTen()
                                + "\n- Ngày sinh: " + hvSelected.getNgaySinh()
                                + "\n- Giới tính: " + (hvSelected.isGioiTinh() == true ? "Nam" : "Nữ")
                                + "\n- Số điện thoại: " + hvSelected.getDienThoai()
                                + "\n- Email: " + hvSelected.getEmail()
                                + "\n  Nếu thông tin của bạn bị sai. Vui lòng phản hồi để chúng tôi cập nhật lại thông tin cá nhân cho bạn.";
                        try {
                            mailHelper.sendMail(to, sub, content);
                        } catch (MessagingException ex) {
                            Logger.getLogger(QLHV.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                }).start();

                indexHvSelectedInTable = tblHocVien.getSelectedRow();

                insertHvToKh();
            } else {
                JOptionPane.showMessageDialog(null, "Thêm học viên không thành công.Vui lòng kiểm tra lại!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            }
        }
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="XÓA HỌC VIÊN ">
    private void deleteHV() throws SQLException {
        int choose = JOptionPane.showConfirmDialog(null, "Bạn có muốn xóa học viên " + hvSelected.getHoTen() + " (" + hvSelected.getId() + ") không?", "Hỏi", JOptionPane.YES_NO_OPTION);

        if (choose == JOptionPane.NO_OPTION || choose == JOptionPane.CLOSED_OPTION) {
            return;
        }

        try {
            String id = hvSelected.getId();

            int i = hocVienDAO.delete(id);

            if (i == 0) {
                JOptionPane.showMessageDialog(null, "Không được xóa học viên đã tham gia học!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            } else {
                if (chkSearchAll.isSelected()) {
                    dtmKH.setRowCount(0);
                    model.setRowCount(0);

                    try {
                        listHvSearch = hocVienDAO.get(txtSearchHV.getText().trim());

                        int stt = 1;
                        for (HocVien hv : listHvSearch) {
                            model.addRow(new Object[]{stt, hv.getId(), hv.getHoTen(), (hv.isGioiTinh() ? "Nam" : "Nữ"), hv.getNgaySinh(), hv.getDienThoai(), hv.getNgayDK()});
                            stt++;
                        }
                    } catch (SQLException ex) {
                        Logger.getLogger(QLHV.class.getName()).log(Level.SEVERE, null, ex);
                    }

                    if (flagFind == NOT_FOUND) {
                        btnUpdate.setEnabled(false);
                        btnNew.setEnabled(false);
                        btnDelete.setEnabled(false);
                        btnAddKH.setEnabled(false);
                        lblKhoaHocCua.setText("");
                    } else {
                        btnUpdate.setEnabled(true);
                        btnNew.setEnabled(true);
                        btnDelete.setEnabled(true);
                        btnAddKH.setEnabled(true);
                        lblKhoaHocCua.setText(hvSelected.getHoTen() + " đã tham gia khóa học");
                    }
                } else {
                    loadDataToTable((int) cbxNam.getSelectedItem());
                }

                resetForm();
                lockFormInfo();
                loadDataToFormAfterDelete();
                loadDataToCbxKhoaHoc();
                loadDataToTblKH();
                flagSave = 0;
                btnCancel.setEnabled(true);
                btnUpdate.setEnabled(true);
                btnDelete.setEnabled(true);
            }
        } catch (SQLException ex) {
            Logger.getLogger(QLHV.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ParseException ex) {
            Logger.getLogger(QLHV.class.getName()).log(Level.SEVERE, null, ex);
        }

        DangNhapFrame.pnTKHV.refresh();
        DangNhapFrame.pnQLHVKH.refresh();
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="CẬP NHẬT HỌC VIÊN ">
    private void updateHV() throws SQLException, ParseException, Exception {
        if (checkInfo()) {
            boolean gt = true;
            if (rdoNu.isSelected()) {
                gt = false;
            }
            String ngaySinh = dateHelper.castDateForm1ToForm2(txtNgaySinh.getText());

            hvSelected.setHoTen(txtHoTen.getText());
            hvSelected.setNgaySinh(ngaySinh);
            hvSelected.setGioiTinh(gt);
            hvSelected.setDienThoai(txtSDT.getText());
            hvSelected.setEmail(txtEmail.getText());
            hvSelected.setGhiChu(txaGhiChu.getText());

            int i = hocVienDAO.update(hvSelected);

            if (i > 0) {
                if (!chkSearchAll.isSelected()) {
                    loadDataToTable((int) cbxNam.getSelectedItem());

                    for (int j = 0; j < tblHocVien.getRowCount(); j++) {
                        if (tblHocVien.getValueAt(j, 1).equals(hvSelected.getId())) {
                            tblHocVien.setRowSelectionInterval(j, j);
                            break;
                        }
                    }

                    indexHvSelectedInTable = tblHocVien.getSelectedRow();
                } else {
                    resetForm();
                    dtmKH.setRowCount(0);
                    model.setRowCount(0);

                    if (flagSearch == SEARCH) {
                        try {
                            listHvSearch = hocVienDAO.get(txtSearchHV.getText().trim());

                            int stt = 1;
                            for (HocVien hv : listHvSearch) {
                                model.addRow(new Object[]{stt, hv.getId(), hv.getHoTen(), (hv.isGioiTinh() ? "Nam" : "Nữ"), hv.getNgaySinh(), hv.getDienThoai(), hv.getNgayDK()});
                                stt++;
                            }

                            loadDataToTblKH();
                            loadDataToCbxKhoaHoc();
                            loadDataToForm();
                        } catch (SQLException ex) {
                            Logger.getLogger(QLHV.class.getName()).log(Level.SEVERE, null, ex);
                        } catch (ParseException ex) {
                            Logger.getLogger(QLHV.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }

                    cbxNam.setEnabled(false);
                    btnSearchContinue.setEnabled(false);

                    for (int j = 0; j < tblHocVien.getRowCount(); j++) {
                        if (tblHocVien.getValueAt(j, 1).equals(hvSelected.getId())) {
                            tblHocVien.setRowSelectionInterval(j, j);
                            break;
                        }
                    }

                    indexHvSelectedInTable = tblHocVien.getSelectedRow();
                }

                lockFormInfo();
                buttonStatus0();
                flagSave = 0;

            } else {
                JOptionPane.showMessageDialog(null, "Cập nhật thông tin học viên không thành công.Vui lòng kiểm tra lại!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            }
        }

    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="UP DOWN DATE ">
    private void UpDownDate(JTextField txt, int i) {
        String oldDate = txt.getText();

        try {
            String newDate = dateHelper.changeDate(oldDate, i);

            txt.setText(newDate);
        } catch (ParseException ex) {
            String now = dateHelper.now();
            String birthday = now.substring(0, now.lastIndexOf("-") + 1) + (Integer.parseInt(now.substring(now.lastIndexOf("-") + 1)) - 18);

            txt.setText(birthday);
        }
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="KIỂM TRA HV SAU HV ĐƯỢC CHỌN CÓ TỒN TẠI KHÔNG ">
    private boolean checkAfterHvselectedExists() {
        try {
            if (tblHocVien.getValueAt(indexHvSelectedInTable, 0) != null) {
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
            if (chkSearchAll.isSelected() && listHvSearch.size() > 0) {
                int stt = (int) tblHocVien.getValueAt(indexHvSelectedInTable, 0) - 1;
                hvSelected = listHvSearch.get(stt);

                loadDataToForm();

                tblHocVien.setRowSelectionInterval(indexHvSelectedInTable, indexHvSelectedInTable);

                indexHvSelectedInTable = tblHocVien.getSelectedRow();
            } else if (chkSearchAll.isSelected() == false && listHocVien.size() > 0) {
                int stt = (int) tblHocVien.getValueAt(indexHvSelectedInTable, 0) - 1;
                hvSelected = listHocVien.get(stt);

                loadDataToForm();

                tblHocVien.setRowSelectionInterval(indexHvSelectedInTable, indexHvSelectedInTable);

                indexHvSelectedInTable = tblHocVien.getSelectedRow();
            } else {
                lblKhoaHocCua.setText("");
            }
        } else {
            if (chkSearchAll.isSelected() && listHvSearch.size() > 0) {
                int stt = (int) tblHocVien.getValueAt(indexHvSelectedInTable - 1, 0) - 1;
                hvSelected = listHvSearch.get(stt);

                loadDataToForm();

                tblHocVien.setRowSelectionInterval(indexHvSelectedInTable - 1, indexHvSelectedInTable - 1);

                indexHvSelectedInTable = tblHocVien.getSelectedRow();
            } else if (chkSearchAll.isSelected() == false && listHocVien.size() > 0) {
                int stt = (int) tblHocVien.getValueAt(indexHvSelectedInTable - 1, 0) - 1;
                hvSelected = listHocVien.get(stt);

                loadDataToForm();

                tblHocVien.setRowSelectionInterval(indexHvSelectedInTable - 1, indexHvSelectedInTable - 1);

                indexHvSelectedInTable = tblHocVien.getSelectedRow();
            } else {
                lblKhoaHocCua.setText("");
            }
        }

        if (chkSearchAll.isSelected()) {
            if (listHvSearch.size() > 0) {
                lblKhoaHocCua.setText(hvSelected.getHoTen() + " đã tham gia khóa học");
            } else {
                lblKhoaHocCua.setText("");
            }
        } else {
            if (listHocVien.size() > 0) {
                lblKhoaHocCua.setText(hvSelected.getHoTen() + " đã tham gia khóa học");
            } else {
                lblKhoaHocCua.setText("");
            }
        }
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="CREATE POPUPMENU ">
    class PopupmenuTblHV extends JPopupMenu {

        public PopupmenuTblHV() {
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
                        deleteHV();
                    } catch (SQLException ex) {
                        Logger.getLogger(QLHV.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            });

            mnuUpdate.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    unlockFormInfo();
                    btnDelete.setEnabled(false);
                    btnCancel.setEnabled(true);
                    btnSave.setEnabled(true);
                    btnNew.setEnabled(false);
                    btnUpdate.setEnabled(false);
                    txtHoTen.requestFocus();
                    flagSave = 2;
                }
            });
        }
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="CANCEL SAVE">
    private void cancelSave() throws SQLException, ParseException {
        String mess = "";
        switch (flagSave) {
            case FLAG_INSERT:
                mess = "Bạn có muốn bỏ qua thao tác thêm mới học viên không?";
                break;
            case FLAG_UPDATE:
                mess = "Bạn có muốn bỏ qua thao tác cập nhật thông tin học viên không?";
                break;
            case ADD_KH:
                mess = "Bạn có muốn bỏ qua thao tác thêm học viên vào khóa học không?";
                break;
        }
        int choose = JOptionPane.showConfirmDialog(null, mess, "Thông báo", JOptionPane.YES_NO_OPTION);

        if (choose == JOptionPane.NO_OPTION || choose == JOptionPane.CLOSED_OPTION) {
            return;
        }

        lockFormInfo();
        buttonStatus0();
        chkSearchAll.setEnabled(true);
        if (flagSave == 1) {
            resetForm();
            tblHocVien.setRowSelectionInterval(indexHvSelectedInTable, indexHvSelectedInTable);
            hvSelected = listHocVien.get((int) tblHocVien.getValueAt(indexHvSelectedInTable, 0) - 1);
            loadDataToForm();
        }
        flagSave = FLAG_DEFAULT;

        loadDataToTblKH();

        loadDataToCbxKhoaHoc();
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="LOAD DỮ LIỆU VÀO BẢNG KHÓA HỌC MÀ HỌC VIÊN ĐÃ THAM GIA ">
    private void loadDataToTblKH() throws SQLException, ParseException {
        listHvEnjoyKh = hocVienDAO.getListKhByHV(hvSelected.getId());

        dtmKH.setRowCount(0);
        int stt = 1;
        for (HocVienEnjoyKhoaHoc hvekh : listHvEnjoyKh) {
            dtmKH.addRow(new Object[]{stt,
                hvekh.getTenCĐ(),
                hvekh.getThoiLuong() + " giờ",
                numberFormat.format(hvekh.getHocPhi() * 1000),
                dateHelper.castDateForm3ToForm1(hvekh.getNgayKG()),
                hvekh.getDiem() >= 0 ? hvekh.getDiem() : "-"});
            stt++;
        }
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="LOAD DỮ LIỆU VÀO CBX KHÓA HỌC ">
    private void loadDataToCbxKhoaHoc() throws SQLException, ParseException {
        cbxKhoaHoc.removeAllItems();

        String maHV = "";

        if (flagSave != 1) {
            maHV = hvSelected.getId();
        }

        List<KhoaHoc> listKH = hocVienDAO.getListKhForHV(maHV);

        cbxKhoaHoc.addItem("-- Chọn khóa học --");

        for (KhoaHoc khoaHoc : listKH) {
            cbxKhoaHoc.addItem(khoaHoc);
        }
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="THÊM HỌC VIÊN VÀO KHÓA HỌC">
    private void insertHvToKh() {
        try {
            KhoaHoc kh = (KhoaHoc) cbxKhoaHoc.getSelectedItem();

            String maHV = hvSelected.getId();
            int maKH = kh.getId();

            HocVien_KhoaHoc hvkh = new HocVien_KhoaHoc(maKH, maHV, -1);

            int i = hvkhdao.insert(hvkh);

            if (i > 0) {
                loadDataToCbxKhoaHoc();
                loadDataToTblKH();

//                loadDataToTable((int) cbxNam.getSelectedItem());
                lockFormInfo();
                buttonStatus0();
                flagSave = 0;

                for (int j = 0; j < tblHocVien.getRowCount(); j++) {
                    if (tblHocVien.getValueAt(j, 1).equals(hvSelected.getId())) {
                        tblHocVien.setRowSelectionInterval(j, j);
                        break;
                    }
                }

                indexHvSelectedInTable = tblHocVien.getSelectedRow();

                DangNhapFrame.pnQLHVKH.refresh();
                DangNhapFrame.pnTHBĐ.refresh();
                DangNhapFrame.pnTKDT.refresh();

                //send mail thông báo đăng ký thành công
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        String to = hvSelected.getEmail();
                        String sub = "Thông báo đăng ký khóa học tại Lập trình City";
                        String content = "Chào " + hvSelected.getHoTen() + "!"
                                + "\nBạn đã được đăng ký vào khóa học tại Lập trình City thành công. Thông tin khóa học:"
                                + "\n- Tên khóa học: " + kh.toString().substring(0, kh.toString().indexOf("("))
                                + "\n- Ngày khai giảng: " + kh.toString().substring(kh.toString().indexOf("("))
                                + "\n  Chúng tôi gửi mail này để thông báo. Bạn vui lòng sắp xếp thời gian để đi học.";
                        try {
                            mailHelper.sendMail(to, sub, content);
                        } catch (MessagingException ex) {
                            Logger.getLogger(QLHV.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                }).start();
            }
        } catch (Exception e) {
        }
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="REFRESH ">
    public void refresh() {
        try {
            loadDataToTblKH();
            loadDataToCbxKhoaHoc();
        } catch (SQLException ex) {
            Logger.getLogger(QLHV.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ParseException ex) {
            Logger.getLogger(QLHV.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="CREAT POPUP TABLE KHÓA HỌC ">
    class PopupmenuTblKH extends JPopupMenu {

        public PopupmenuTblKH() {
            JMenuItem mnuDelete = new JMenuItem("Xóa");

            if (DangNhapFrame.nvLogin.getVaiTro() == 1) {
                this.add(mnuDelete);
            }

            mnuDelete.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    try {
                        deleteHV_KH();
                    } catch (SQLException ex) {
                        Logger.getLogger(QLHV.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            });
        }
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="XÓA HỌC VIÊN KHỎI KHÓA HỌC ">
    public void deleteHV_KH() throws SQLException {
        HocVienEnjoyKhoaHoc hvkh = listHvEnjoyKh.get((int) tblKhoaHoc.getValueAt(tblKhoaHoc.getSelectedRow(), 0) - 1);

        KhoaHoc khoaHoc = new KhoaHocDAO().getKhoaHocByID(hvkh.getMaKH());

        if (!flagNgayHH(khoaHoc)) {
            int answer = JOptionPane.showConfirmDialog(null, "Bạn có muốn xóa học viên " + hvSelected.getHoTen() + " (" + hvSelected.getId() + ") "
                    + "ra khỏi khóa học " + tblKhoaHoc.getValueAt(tblKhoaHoc.getSelectedRow(), 1) + " (" + tblKhoaHoc.getValueAt(tblKhoaHoc.getSelectedRow(), 4) + ") không?", "Thông báo", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);

            if (answer == JOptionPane.YES_OPTION) {
                try {
                    int n = new HocVien_KhoaHocDAO().delete(String.valueOf(hvkh.getMaHV_KH()));
                    if (n > 0) {
                        loadDataToTblKH();
                        loadDataToCbxKhoaHoc();

                        DangNhapFrame.pnTKDT.refresh();
                        DangNhapFrame.pnTHBĐ.refresh();
                        DangNhapFrame.pnQLHVKH.refresh();
                    } else {
                        JOptionPane.showMessageDialog(null, "Không thể xóa học viên đã có điểm trong khóa học!", "Cảnh báo!", JOptionPane.WARNING_MESSAGE);
                    }
                } catch (SQLException ex) {
                    Logger.getLogger(QLHV.class.getName()).log(Level.SEVERE, null, ex);
                } catch (ParseException ex) {
                    Logger.getLogger(QLHV.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        } else {
            JOptionPane.showMessageDialog(null, "Không thể xóa học viên khỏi khóa học đã kết thúc!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
        }

    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="TÌM TIẾP ">
    private void continueSearch() throws SQLException, ParseException {
        if (flagSearch == SEARCH) {
            if (indexHvInListSearch < listHvSearch.size() - 1) {
                indexHvInListSearch++;
            } else {
                indexHvInListSearch = 0;
            }
            searchHV();

            loadDataToTblKH();

            loadDataToCbxKhoaHoc();

            if (listHvSearch.size() > 0) {
                lblKhoaHocCua.setText(hvSelected.getHoTen() + " đã tham gia khóa học");
            } else {
                lblKhoaHocCua.setText("");
                btnNew.setEnabled(false);
                btnUpdate.setEnabled(false);
                btnDelete.setEnabled(false);
                btnAddKH.setEnabled(false);
            }
        }
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="CHECK KHÓA HỌC HẾT HẠN ">
    private boolean flagNgayHH(KhoaHoc khoaHoc) {
        int time = khoaHoc.getThoiLuong();
        int day = time / 2;
        int numberDay = (day / 3) * 7 + day % 3;

        try {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(dateHelper.toDate(khoaHoc.getNgayKG()));
            calendar.add(Calendar.DAY_OF_YEAR, numberDay);
            Date HH = calendar.getTime();

            if (new Date().after(HH)) {
                return true;
            }
        } catch (ParseException ex) {
        }
        return false;
    }
    // </editor-fold>
}
