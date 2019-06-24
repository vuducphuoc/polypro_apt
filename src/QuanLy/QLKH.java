/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package QuanLy;

import DAO.ChuyenDeDAO;
import DAO.HocVienDAO;
import DAO.KhoaHocDAO;
import HeThong.DangNhapFrame;
import Helper.DateHelper;
import Helper.JdbcHelper;
import Helper.MailHelper;
import Model.ChuyenDe;
import Model.HocVien;
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
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.mail.MessagingException;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableRowSorter;
import polypro_apt2_main.MainFrame;

public class QLKH extends JPanel {

    final int ROLE_TRUONG_PHONG = 1;

    public static boolean doneLoad = false;

    JdbcHelper jdbcHelper = new JdbcHelper();

    public QLKH() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex) {
            Logger.getLogger(QLKH.class.getName()).log(Level.SEVERE, null, ex);
        }
        this.init();
        this.addControls();
        this.setDefault();
        this.refresh();
        this.addEvents();
        this.showWindow();
    }

    private void showWindow() {
        this.setSize(1100, 650);
    }

    private void init() {
        lblInformationTitle = new JLabel("Thông tin khóa học");
        lblNgayKhaiGiang = new JLabel("Ngày khai giảng");
        lblSearchCD = new JLabel("Chuyên đề");
        lblSearchNam = new JLabel("Năm học");
        lblCD = new JLabel("Chuyên đề");
        lblHocPhi = new JLabel("Học phí (x1000 VNĐ)");
        lblThoiLuong = new JLabel("Thời lượng (giờ)");
        lblGhiChu = new JLabel("Ghi chú");

        cbxCD = new JComboBox();
        cbxSearchCD = new JComboBox();
        cbxSearchNam = new JComboBox();
        txtHocPhi = new JTextField();
        txtThoiLuong = new JTextField();
        txtNgayKhaiGiang = new JTextField();
        txaGhiChu = new JTextArea();
        txaGhiChu.setLineWrap(true);
        scGhiChu = new JScrollPane(txaGhiChu, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        modelKhoaHoc = new DefaultTableModel();
        modelKhoaHoc.setColumnIdentifiers(new Object[]{"STT", "CHUYÊN ĐỀ", "HỌC PHÍ", "THỜI LƯỢNG", "NGÀY KHAI GIẢNG", "TÌNH TRẠNG"});
        tblKhoaHoc = new JTable(modelKhoaHoc) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }

        };
        scTblKhoaHoc = new JScrollPane(tblKhoaHoc, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        // căn giữa header
        tblHeader = tblKhoaHoc.getTableHeader();
        ((DefaultTableCellRenderer) tblHeader.getDefaultRenderer()).setHorizontalAlignment(JLabel.CENTER);

        // set độ rộng cột
        tblKhoaHoc.getColumnModel().getColumn(0).setPreferredWidth(50);
        tblKhoaHoc.getColumnModel().getColumn(1).setPreferredWidth(200);
        tblKhoaHoc.getColumnModel().getColumn(2).setPreferredWidth(125);
        tblKhoaHoc.getColumnModel().getColumn(3).setPreferredWidth(125);
        tblKhoaHoc.getColumnModel().getColumn(4).setPreferredWidth(155);
        tblKhoaHoc.getColumnModel().getColumn(5).setPreferredWidth(155);

        // độ cao dòng
        tblKhoaHoc.setRowHeight(30);
        tblKhoaHoc.setSelectionBackground(Color.decode("#3a4d8f"));

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
        renderer.setHorizontalAlignment((int) JTable.CENTER_ALIGNMENT);
        for (int i = 0; i < tblKhoaHoc.getColumnCount(); i++) {
            tblKhoaHoc.setDefaultRenderer(tblKhoaHoc.getColumnClass(i), renderer);
        }

        tblKhoaHoc.setRowSorter(new TableRowSorter(modelKhoaHoc));
        tblKhoaHoc.setAutoCreateRowSorter(true);

        btnNew = new JButton("Thêm mới");
        btnSave = new JButton("Lưu");
        btnEdit = new JButton("Sửa");
        btnDelete = new JButton("Xóa");
        btnCancel = new JButton("Bỏ qua");

        btnNew.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnSave.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnEdit.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnDelete.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnCancel.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // set Font
        lblInformationTitle.setFont(new Font("Segoe UI", 1, 20));
        lblNgayKhaiGiang.setFont(new Font("Segoe UI", 0, 16));
        lblGhiChu.setFont(new Font("Segoe UI", 0, 16));
        lblHocPhi.setFont(new Font("Segoe UI", 0, 16));
        lblCD.setFont(new Font("Segoe UI", 0, 16));
        lblSearchCD.setFont(new Font("Segoe UI", 0, 16));
        lblSearchNam.setFont(new Font("Segoe UI", 0, 16));
        lblThoiLuong.setFont(new Font("Segoe UI", 0, 16));

        txtHocPhi.setFont(new Font("Segoe UI", 0, 13));
        txtNgayKhaiGiang.setFont(new Font("Segoe UI", 0, 13));
        txtThoiLuong.setFont(new Font("Segoe UI", 0, 13));

        txaGhiChu.setFont(new Font("Segoe UI", 0, 14));
        tblKhoaHoc.setFont(new Font("Segoe UI", 0, 12));

        btnNew.setFont(new Font("Segoe UI", 0, 14));
        btnSave.setFont(new Font("Segoe UI", 0, 14));
        btnEdit.setFont(new Font("Segoe UI", 0, 14));
        btnDelete.setFont(new Font("Segoe UI", 0, 14));
        btnCancel.setFont(new Font("Segoe UI", 0, 14));

        // Size
        cbxSearchCD.setPreferredSize(new Dimension(300, 25));
        cbxSearchNam.setPreferredSize(new Dimension(100, 25));

        btnEdit.setPreferredSize(btnNew.getPreferredSize());
        btnSave.setPreferredSize(btnNew.getPreferredSize());
        btnDelete.setPreferredSize(btnNew.getPreferredSize());
        btnCancel.setPreferredSize(btnNew.getPreferredSize());

        //set phím tắt
        btnSave.setMnemonic(KeyEvent.VK_L);
        btnNew.setMnemonic(KeyEvent.VK_T);
        btnEdit.setMnemonic(KeyEvent.VK_S);
        btnDelete.setMnemonic(KeyEvent.VK_X);
        btnCancel.setMnemonic(KeyEvent.VK_B);

        // set tooltip
        btnSave.setToolTipText("Phím tắt Alt + S");
        btnNew.setToolTipText("Phím tắt Alt + N");
        btnEdit.setToolTipText("Phím tắt Alt + U");
        btnDelete.setToolTipText("Phím tắt Alt + D");
        btnCancel.setToolTipText("Phím tắt Alt + Z");

        ((JLabel) cbxSearchNam.getRenderer()).setHorizontalAlignment(JLabel.CENTER);
    }

    private void addControls() {

        //<editor-fold defaultstate="collapsed" desc="LEFT">
        JPanel pnSearch = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        pnSearch.add(lblSearchCD);
        pnSearch.add(cbxSearchCD);
        pnSearch.add(lblSearchNam);
        pnSearch.add(cbxSearchNam);
        pnSearch.setBorder(new TitledBorder(BorderFactory.createLineBorder(Color.BLACK), "Lọc", 1, 2, new Font("Segor UI", 1, 18)));

        JPanel pnTable = new JPanel(new BorderLayout());
        pnTable.add(scTblKhoaHoc, BorderLayout.CENTER);

        JPanel pnLeft = new JPanel(new BorderLayout(0, 10));
        pnLeft.add(pnSearch, BorderLayout.NORTH);
        pnLeft.add(pnTable, BorderLayout.CENTER);
        //</editor-fold>

        //<editor-fold defaultstate="collapsed" desc="RIGHT">
        JPanel pnInformation = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(5, 10, 5, 10);

        gbc.gridx = 0;
        gbc.gridy = 0;
        pnInformation.add(lblInformationTitle, gbc);

        gbc.ipady = 5;
        gbc.gridy = 1;
        pnInformation.add(lblCD, gbc);

        gbc.gridy = 2;
        pnInformation.add(cbxCD, gbc);

        gbc.gridy = 3;
        pnInformation.add(lblHocPhi, gbc);

        gbc.gridy = 4;
        pnInformation.add(txtHocPhi, gbc);

        gbc.gridy = 5;
        pnInformation.add(lblThoiLuong, gbc);

        gbc.gridy = 6;
        pnInformation.add(txtThoiLuong, gbc);

        gbc.gridy = 7;
        pnInformation.add(lblNgayKhaiGiang, gbc);

        gbc.gridy = 8;
        pnInformation.add(txtNgayKhaiGiang, gbc);

        gbc.gridy = 9;
        pnInformation.add(lblGhiChu, gbc);

        gbc.gridy = 10;
        gbc.ipady = 100;
        pnInformation.add(scGhiChu, gbc);

        JPanel pnButton = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        pnButton.add(btnNew);
        pnButton.add(btnEdit);
        if (DangNhapFrame.nvLogin.getVaiTro() == ROLE_TRUONG_PHONG) {
            pnButton.add(btnDelete);
        }
        pnButton.add(btnSave);
        pnButton.add(btnCancel);

        gbc.ipady = 5;
        gbc.insets = new Insets(20, 0, 0, 0);
        gbc.gridy = 11;
        pnInformation.add(pnButton, gbc);

        JPanel pnItems = new JPanel();

        JPanel pnRight = new JPanel();
        pnRight.add(pnInformation, BorderLayout.NORTH);
        pnRight.add(pnItems, BorderLayout.CENTER);
        //</editor-fold>

        //<editor-fold defaultstate="collapsed" desc="Container">
        this.setLayout(new BorderLayout(0, 5));
        this.add(pnLeft, BorderLayout.CENTER);
        this.add(pnRight, BorderLayout.EAST);

        //</editor-fold>
    }

    private void addEvents() {

        doneLoad = true;

        sendMailResultStudy();

        //<editor-fold defaultstate="collapsed" desc="JComboBox - Filter">
        cbxSearchCD.addItemListener((ItemEvent e) -> {
            if (cbxSearchCD.isFocusable()) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    loadDataToTable();
                    if (choose == 0) {
                        index = 0;
                        fillToForm(index);
                    }
                }
            }
        });

        cbxSearchNam.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    loadDataToTable();
                    if (choose == 0) {
                        index = 0;
                        fillToForm(index);
                    }
                }
            }
        });
        //</editor-fold>

        //<editor-fold defaultstate="collapsed" desc="New">
        btnNew.addActionListener((ActionEvent e) -> {
            clear();
            tblKhoaHoc.clearSelection();
            setStatus(true);
        });
        //</editor-fold>

        //<editor-fold defaultstate="collapsed" desc="Edit">
        btnEdit.addActionListener((ActionEvent e) -> {
            setStatus(false);
        });
        //</editor-fold>

        //<editor-fold defaultstate="collapsed" desc="Save">
        btnSave.addActionListener((ActionEvent e) -> {
            save();
            DangNhapFrame.pnTKDT.refresh();
            DangNhapFrame.pnQLHVKH.refresh();
        });
        //</editor-fold>

        //<editor-fold defaultstate="collapsed" desc="Delete">
        btnDelete.addActionListener((ActionEvent e) -> {
            if (flagNgayHH(khoaHoc) == true) {
                JOptionPane.showMessageDialog(null, "Khóa học " + khoaHoc + " đã kết thúc - Không thể xóa khóa học này !", "Cảnh báo!", JOptionPane.WARNING_MESSAGE);
            } else {
                delete();
            }
        });
        //</editor-fold>

        //<editor-fold defaultstate="collapsed" desc="Cancel">
        btnCancel.addActionListener((ActionEvent e) -> {
            int answer = JOptionPane.showConfirmDialog(null, messCancel, "Hỏi", JOptionPane.YES_NO_OPTION);

            if (answer == JOptionPane.NO_OPTION || answer == JOptionPane.CLOSED_OPTION) {
                return;
            }

            fillToForm(index);
            setDefault();
        });
        //</editor-fold>

        //<editor-fold defaultstate="collapsed" desc="Table MouseClick">
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
                //</editor-fold>

                //<editor-fold defaultstate="collapsed" desc="Click chuột trái">
                if (choose != 0) {
                    int answer = JOptionPane.showConfirmDialog(null, messCancel, "Hỏi", JOptionPane.YES_NO_OPTION);

                    if (answer == JOptionPane.YES_OPTION) {
                        index = tblKhoaHoc.getSelectedRow();
                        fillToForm(index);
                        setDefault();

                        if (e.getButton() == MouseEvent.BUTTON3) {
                            JPopupMenu popup = new QLKH.PopupmenuTblKH();
                            popup.show(e.getComponent(), e.getX(), e.getY());
                        }
                    } else {
                        tblKhoaHoc.clearSelection();
                    }

                } else {
                    index = tblKhoaHoc.getSelectedRow();
                    fillToForm(index);
                    setDefault();

                    if (e.getButton() == MouseEvent.BUTTON3) {
                        JPopupMenu popup = new QLKH.PopupmenuTblKH();
                        popup.show(e.getComponent(), e.getX(), e.getY());
                    }
                }
                //</editor-fold>

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
        //</editor-fold>

        //<editor-fold defaultstate="collapsed" desc="CbxCD.ItemListener">
        cbxCD.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    chuyenDe = listCD.get(cbxCD.getSelectedIndex());
                    txtHocPhi.setText("" + chuyenDe.getHocPhi());
                    txtThoiLuong.setText("" + chuyenDe.getThoiLuong());
                }
            }
        });
        //</editor-fold>

        //<editor-fold defaultstate="collapsed" desc="Key Listener">
        txtNgayKhaiGiang.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
            }

            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_UP) {
                    if (txtNgayKhaiGiang.getText().length() < 10) {
                        txtNgayKhaiGiang.setText(dateHelper.now());
                    }

                    UpDownDate(txtNgayKhaiGiang, 1);
                }

                if (e.getKeyCode() == KeyEvent.VK_DOWN) {
                    try {
                        if (txtNgayKhaiGiang.getText().length() < 10) {
                            txtNgayKhaiGiang.setText(dateHelper.now());
                        }

                        if (dateHelper.toDate(txtNgayKhaiGiang.getText()).getTime() - dateHelper.toDate(dateHelper.now()).getTime() > 0) {
                            UpDownDate(txtNgayKhaiGiang, -1);
                        }
                    } catch (ParseException ex) {
                        txtNgayKhaiGiang.setText(dateHelper.now());
                    }

                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
            }
        });

        tblKhoaHoc.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
            }

            @Override
            public void keyPressed(KeyEvent e) {
            }

            @Override
            public void keyReleased(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_UP) {
                    int n = tblKhoaHoc.getSelectedRow();
                    int stt = (int) tblKhoaHoc.getValueAt(n, 0);
                    khoaHoc = listKH.get(stt - 1);
                    setModel(khoaHoc);
                }

                if (e.getKeyCode() == KeyEvent.VK_DOWN) {
                    int n = tblKhoaHoc.getSelectedRow();
                    int stt = (int) tblKhoaHoc.getValueAt(n, 0);
                    khoaHoc = listKH.get(stt - 1);
                    setModel(khoaHoc);
                }
            }
        });
        //</editor-fold>

        fillToForm(index);

    }

    // METHOD
    public void refresh() {
        loadDataToCbx();
        loadDataToCbxNam();
        loadDataToTable();
    }

    private void reload() {
        loadDataToCbxNam();

        if (txtNgayKhaiGiang.getText().length() > 0) {
            int year = Integer.parseInt(khoaHoc.getNgayKG().substring(6));
            cbxSearchNam.setSelectedItem(year);
            cbxSearchCD.setSelectedItem(chuyenDe);
        } else {
        }
    }

    private void clear() {
        if (cbxCD.getItemCount() > 0) {
            cbxCD.setSelectedIndex(0);
            chuyenDe = (ChuyenDe) cbxCD.getSelectedItem();
            khoaHoc = new KhoaHoc(chuyenDe.getId(), chuyenDe.getHocPhi(), chuyenDe.getThoiLuong(), "", "");
            setModel(khoaHoc);
        }
    }

    private void save() {
        if (checkInfo()) {
            try {
                int n = -1;
                if (choose == 1) {
                    n = new KhoaHocDAO().insert(getModel());
                } else if (choose == 2) {
                    n = new KhoaHocDAO().update(getModel());
                }

                if (n > 0) {
                    reload();

                    for (KhoaHoc kh : listKH) {
                        if (getModel().getMaCD().equalsIgnoreCase(kh.getMaCD())
                                && kh.getNgayKG().equalsIgnoreCase(txtNgayKhaiGiang.getText())) {
                            index = listKH.indexOf(kh);
                            fillToForm(index);
                            break;
                        }
                    }
                    setDefault();
                    DangNhapFrame.pnQLHV.refresh();
                } else {
                    JOptionPane.showMessageDialog(null, "Không thành công! Vui lòng thử lại", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
                }
            } catch (SQLException ex) {
            }
        }

    }

    private void delete() {
        if (chkHV_IN_KH(khoaHoc)) {
            JOptionPane.showMessageDialog(null, "Khóa học " + khoaHoc + " đã có thành viên. Không thể xóa khóa học này!", "Cảnh báo!", JOptionPane.WARNING_MESSAGE);
        } else {
            int answer = JOptionPane.showConfirmDialog(null, "Bạn có muốn xóa khóa học " + khoaHoc + " này?", "Hỏi", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);

            if (answer == JOptionPane.YES_OPTION) {
                try {
                    int n = new KhoaHocDAO().delete(String.valueOf(khoaHoc.getId()));

                    if (n == 1) {
                        if (index == listKH.size() - 1) {
                            index = index - 1;
                        }

                        reload();
                        fillToForm(index);

                        DangNhapFrame.pnTKDT.refresh();
                        DangNhapFrame.pnQLHVKH.refresh();
                        DangNhapFrame.pnQLHV.refresh();
                    } else {
                        JOptionPane.showMessageDialog(null, "Không được xóa Khóa học này đang có học viên tham gia!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
                    }
                } catch (SQLException ex) {
                    System.out.println(ex.toString());
                }
            }
        }

    }

    private void setDefault() {
        cbxCD.setEnabled(false);
        txtHocPhi.setEnabled(false);
        txtThoiLuong.setEnabled(false);
        txtNgayKhaiGiang.setEnabled(false);
        txaGhiChu.setEnabled(false);

        btnSave.setVisible(false);
        btnCancel.setVisible(false);

        btnEdit.setVisible(true);
        btnNew.setVisible(true);
        btnDelete.setVisible(true);
        choose = 0;
    }

    private void setStatus(boolean insertable) {
        if (insertable) {
            choose = 1;
            messCancel = "Bạn có muốn hủy thao tác thêm mới khóa học không?";
        } else {
            choose = 2;
            messCancel = "Bạn có muốn hủy thao tác sửa thông tin khóa học không?";
        }

        btnSave.setVisible(true);
        btnNew.setVisible(false);
        btnEdit.setVisible(false);
        btnDelete.setVisible(!insertable);
        btnCancel.setVisible(true);

        cbxCD.setEnabled(true);
        txtNgayKhaiGiang.setEnabled(true);
        txaGhiChu.setEnabled(true);

        txtNgayKhaiGiang.requestFocus();
    }

    private KhoaHoc getModel() {
        ChuyenDe cd = (ChuyenDe) cbxCD.getSelectedItem();

        khoaHoc.setMaCD(((ChuyenDe) cbxCD.getSelectedItem()).getId());
        khoaHoc.setHocPhi(cd.getHocPhi());
        khoaHoc.setThoiLuong(cd.getThoiLuong());
        khoaHoc.setGhiChu(txaGhiChu.getText());

        String ngayKG = "";
        try {
            ngayKG = dateHelper.castDateForm1ToForm2(txtNgayKhaiGiang.getText());
        } catch (ParseException ex) {
            Logger.getLogger(QLKH.class.getName()).log(Level.SEVERE, null, ex);
        }

        khoaHoc.setNgayKG(ngayKG);
        return khoaHoc;
    }

    private void setModel(KhoaHoc kh) {
        cbxCD.setSelectedItem(chuyenDe);
        txtThoiLuong.setText(String.valueOf(kh.getThoiLuong()));
        txtHocPhi.setText(String.valueOf(kh.getHocPhi()));
        txtNgayKhaiGiang.setText(kh.getNgayKG());
        txaGhiChu.setText(kh.getGhiChu());
    }

    private void fillToForm(int index) {
        if (listKH.size() > 0) {
            tblKhoaHoc.setRowSelectionInterval(index, index);

            Rectangle rect = tblKhoaHoc.getCellRect(index, 0, true);
            tblKhoaHoc.scrollRectToVisible(rect);

            stt = (int) tblKhoaHoc.getValueAt(index, 0);
            khoaHoc = listKH.get(stt - 1);
            chuyenDe = (ChuyenDe) cbxSearchCD.getSelectedItem();
            setModel(khoaHoc);
        } else {
            clear();
        }
    }

    private void loadDataToCbx() {
        cbxCD.removeAllItems();
        cbxSearchCD.removeAllItems();

        try {
            listCD = new ChuyenDeDAO().getAll();

            for (ChuyenDe cd : listCD) {
                cbxCD.addItem(cd);
                cbxSearchCD.addItem(cd);
            }
        } catch (SQLException ex) {
            System.out.println(ex.toString());
        }
    }

    private void loadDataToCbxNam() {
        cbxSearchNam.removeAllItems();

        for (int year : getYear()) {
            cbxSearchNam.addItem(year);
        }
    }

    private void loadDataToTable() {
        modelKhoaHoc.setRowCount(0);
        if (cbxSearchCD.getItemCount() > 0 && cbxSearchNam.getItemCount() > 0) {

            try {
                ChuyenDe cd = (ChuyenDe) cbxSearchCD.getSelectedItem();
                listKH = new KhoaHocDAO().get(cd.getId(), cbxSearchNam.getSelectedItem());

                int i = 1;
                for (KhoaHoc kh : listKH) {
                    String strHH = "";
                    if (flagNgayHH(kh) == true) {
                        strHH = "Đã kết thúc";
                    } else {
                        strHH = "Chưa kết thúc";
                    }

                    modelKhoaHoc.addRow(new Object[]{
                        i, cd.getTenCD(), castIntToMoney(kh.getHocPhi() * 1000), kh.getThoiLuong(), kh.getNgayKG(), strHH
                    });
                    i++;
                }

            } catch (SQLException ex) {
                System.out.println(ex.toString());
            }
        }
    }

    private List<Integer> getYear() {
        List<Integer> listYear = new ArrayList<>();
        String sql = "SELECT DISTINCT YEAR(ngayKG) FROM dbo.KhoaHoc ORDER BY YEAR(ngayKG) DESC";

        try {
            ResultSet rs = new JdbcHelper().executeQuery(sql);
            while (rs.next()) {
                listYear.add(rs.getInt(1));
            }
        } catch (SQLException ex) {
        }

        return listYear;
    }

    private void UpDownDate(JTextField txt, int i) {
        String oldDate = txt.getText();

        try {
            String newDate = dateHelper.changeDate(oldDate, i);

            txt.setText(newDate);
        } catch (ParseException ex) {
            txt.setText(dateHelper.now());
        }
    }

    private String castIntToMoney(int money) {
        return numberFormat.format(money);
    }

    private boolean checkInfo() {
        if (txtNgayKhaiGiang.getText().length() == 0) {
            JOptionPane.showMessageDialog(null, "Vui lòng nhập ngày khai giảng!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            txtNgayKhaiGiang.requestFocus();
            return false;
        }

        if (!txtNgayKhaiGiang.getText().matches("^[0-9\\-]+$")) {
            JOptionPane.showMessageDialog(null, "Ngày khai giảng phải có định dạng ngày-tháng-năm!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            txtNgayKhaiGiang.requestFocus();
            return false;
        }

        try {
            Date NKG = dateHelper.toDate(txtNgayKhaiGiang.getText());
            Date dateCheck;

            if (choose == 1) {
                Calendar calendar = Calendar.getInstance();
                calendar.setTimeZone(TimeZone.getDefault());
                calendar.add(Calendar.DAY_OF_YEAR, 4);
                dateCheck = calendar.getTime();
            } else {
                String str = dateHelper.changeDate(khoaHoc.getNgayTao(), 5);
                dateCheck = dateHelper.toDate(str);
            }

            if (NKG.before(dateCheck)) {
                JOptionPane.showMessageDialog(null, "Ngày khai giảng không hợp lệ!\nNgày khai giảng phải lớn hơn 5 ngày so với ngày tạo", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
                txtNgayKhaiGiang.requestFocus();
                return false;
            }

        } catch (ParseException ex) {
            JOptionPane.showMessageDialog(null, "Ngày khai giảng không hợp lệ!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            txtNgayKhaiGiang.requestFocus();
            return false;
        }

        ChuyenDe cd = (ChuyenDe) cbxCD.getSelectedItem();
        for (KhoaHoc khoaHoc1 : listKH) {
            try {
                if (khoaHoc1.getMaCD().equalsIgnoreCase(cd.getId())
                        && dateHelper.toDate(khoaHoc1.getNgayKG()).compareTo(dateHelper.toDate(txtNgayKhaiGiang.getText())) == 0
                        && choose == 1) {
                    JOptionPane.showMessageDialog(null, "Khóa học này đã tồn tại!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
                    return false;
                }
            } catch (ParseException ex) {
                Logger.getLogger(QLKH.class.getName()).log(Level.SEVERE, null, ex);
                return false;
            }
        }

        return true;
    }

    public boolean flagNgayHH(KhoaHoc khoaHoc) {
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

    private boolean chkHV_IN_KH(KhoaHoc kh) {
        String sql = "SELECT COUNT(MAHV) FROM dbo.HocVien_KhoaHoc WHERE maKH = ?";
        int kq = -1;
        try {
            ResultSet rs = jdbcHelper.executeQuery(sql, kh.getId());
            while (rs.next()) {
                kq = rs.getInt(1);
            }

        } catch (SQLException ex) {
            Logger.getLogger(QLKH.class.getName()).log(Level.SEVERE, null, ex);
        }
        if (kq > 0) {
            return true;
        }
        return false;
    }

    class PopupmenuTblKH extends JPopupMenu {

        public PopupmenuTblKH() {
            JMenuItem mnuUpdate = new JMenuItem("Sửa");
            JMenuItem mnuDelete = new JMenuItem("Xóa");
            this.add(mnuUpdate);
            if (DangNhapFrame.nvLogin.getVaiTro() == ROLE_TRUONG_PHONG) {
                this.add(mnuDelete);
            }

            mnuDelete.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (flagNgayHH(khoaHoc) == true) {
                        JOptionPane.showMessageDialog(null, "Khóa học " + khoaHoc + " đã kết thúc - Không thể xóa khóa học này !", "Cảnh báo!", JOptionPane.WARNING_MESSAGE);
                    } else {
                        delete();
                    }
                }
            });

            mnuUpdate.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    setStatus(false);
                }
            });
        }
    }

    //<editor-fold defaultstate="collapsed" desc="Component">
    int choose = 0;
    int index = 0;
    int stt = -1;
    String messCancel = "";

    KhoaHoc khoaHoc = new KhoaHoc();
    ChuyenDe chuyenDe = new ChuyenDe();

    List<KhoaHoc> listKH = new ArrayList<>();
    List<ChuyenDe> listCD = new ArrayList<>();
    DateHelper dateHelper = new DateHelper();
    NumberFormat numberFormat = NumberFormat.getCurrencyInstance();

    JLabel lblSearchCD, lblSearchNam, lblHocPhi, lblThoiLuong, lblGhiChu, lblCD, lblInformationTitle, lblNgayKhaiGiang;
    JTextField txtHocPhi, txtThoiLuong, txtNgayKhaiGiang;
    JComboBox cbxCD, cbxSearchCD, cbxSearchNam;
    JTextArea txaGhiChu;
    JTable tblKhoaHoc;
    DefaultTableModel modelKhoaHoc;
    JScrollPane scGhiChu, scTblKhoaHoc;
    JButton btnNew, btnSave, btnEdit, btnDelete, btnCancel;
    JTableHeader tblHeader;
    //</editor-fold>

    // <editor-fold defaultstate="collapsed" desc="SEND KẾT QUẢ HỌC TẬP CHO KHÓA HỌC ĐÃ KẾT THÚC ">
    private void sendMailResultStudy() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                KhoaHocDAO khdao = new KhoaHocDAO();
                HocVienDAO hvdao = new HocVienDAO();
                MailHelper mailHelper = new MailHelper();

                while (true) {
                    try {
                        List<KhoaHoc> listKhoaHocs = khdao.getAll();
                        if (listKhoaHocs.size() > 0) {
                            for (KhoaHoc kh : listKhoaHocs) {
                                if (flagNgayHH(kh) == true && kh.isSendResult() == false) {
                                    System.out.println(" ===== Gửi mail cho khóa học " + kh.toString() + "========");

                                    List<HocVien_KhoaHoc> listHocVien_KhoaHoc = getListHvKhByMaKH(kh.getId());

                                    for (HocVien_KhoaHoc hvkh : listHocVien_KhoaHoc) {
                                        if (hvkh.isSendResult() == false) {
                                            String maHV = hvkh.getMaHV();

                                            HocVien hv = hvdao.getById(maHV);

                                            String to = hv.getEmail();
                                            String sub = "Thông báo kết quả học tập khóa học " + kh.toString();
                                            String content = "Chào " + hv.getHoTen() + "!"
                                                    + "\nChúng tôi gửi email này thông báo kết quả học tập của bạn trong khóa học " + kh.toString() + ". Thông tin chi tiết:"
                                                    + "\n- Họ và tên học viên: " + hv.getHoTen()
                                                    + "\n- Điểm: " + hvkh.getDiem()
                                                    + "\n- Xếp loại: " + hvkh.getXepLoai();
                                            mailHelper.sendMail(to, sub, content);

                                            jdbcHelper.executeUpdate("UPDATE dbo.HocVien_KhoaHoc SET sendResult = 1 WHERE id = ?", hvkh.getId());

                                            System.out.println("Đã gửi mail cho " + hv.getEmail() + " Thành công");
                                        }
                                    }

                                    jdbcHelper.executeUpdate("UPDATE dbo.KhoaHoc SET sendResult = 1 WHERE id = ?", kh.getId());
                                    System.out.println("Đã gửi mail cho " + kh.toString() + " Thành công");
                                }
                            }
                        }
                        Thread.sleep(1000);
                    } catch (InterruptedException | SQLException | MessagingException ex) {
                        Logger.getLogger(QLKH.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        }).start();
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="GET LIST KHÓA HỌC TỪ MÃ KHÓA HỌC ">
    private List<HocVien_KhoaHoc> getListHvKhByMaKH(int maKH) throws SQLException {
        String sql = "SELECT * FROM dbo.HocVien_KhoaHoc WHERE maKH = ?";

        ResultSet rs = jdbcHelper.executeQuery(sql, maKH);

        List<HocVien_KhoaHoc> listHocVien_KhoaHoc = new ArrayList<>();

        while (rs.next()) {
            HocVien_KhoaHoc hvkh = new HocVien_KhoaHoc(rs.getInt(1), rs.getInt(2), rs.getString(3), rs.getDouble(4), rs.getBoolean(5));
            listHocVien_KhoaHoc.add(hvkh);
        }

        return listHocVien_KhoaHoc;
    }
    // </editor-fold>
}
