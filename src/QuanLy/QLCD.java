/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package QuanLy;

import DAO.ChuyenDeDAO;
import HeThong.DangNhapFrame;
import Helper.JdbcHelper;
import Model.ChuyenDe;
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
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BorderFactory;
import javax.swing.JButton;
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
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableRowSorter;
import polypro_apt2_main.MainFrame;

public class QLCD extends JPanel {

    public static boolean doneLoad = false;

    final int ROLE_TRUONG_PHONG = 1;

    public QLCD() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex) {
            Logger.getLogger(QLCD.class.getName()).log(Level.SEVERE, null, ex);
        }

        this.init();
        this.addControls();
        this.setDefault();
        this.loadDataToTable();
        this.addEvents();
        this.showWindow();
    }

    private void init() {
        lblInformationTitle = new JLabel("Thông tin chuyên đề");
        lblSearchTitle = new JLabel("Tìm kiếm chuyên đề");
        lblSearch = new JLabel("Chuyên đề");
        txtSearch = new JTextField();
        btnSearch = new JButton("Tìm kiếm");

        lblMaCD = new JLabel("Mã Chuyên đề");
        txtMaCD = new JTextField();

        lblTenCD = new JLabel("Tên Chuyên đề");
        txtTenCD = new JTextField();

        lblHocPhi = new JLabel("Học phí (x1000 VNĐ)");
        txtHocPhi = new JTextField();

        lblThoiLuong = new JLabel("Thời lượng (giờ)");
        txtThoiLuong = new JTextField();

        lblMoTa = new JLabel("Mô tả");
        txaMoTa = new JTextArea();
        txaMoTa.setLineWrap(true);
        scMoTa = new JScrollPane(txaMoTa, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        model = new DefaultTableModel();
        model.setColumnIdentifiers(new Object[]{
            "STT", "MÃ CHUYÊN ĐỀ", "TÊN CHUYÊN ĐỀ", "HỌC PHÍ", "THỜI LƯỢNG"
        });

        tblCD = new JTable(model) {
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
        scTable = new JScrollPane(tblCD, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        tblHeader = tblCD.getTableHeader();
        ((DefaultTableCellRenderer) tblHeader.getDefaultRenderer()).setHorizontalAlignment(JLabel.CENTER);

        tblHeader.getColumnModel().getColumn(0).setPreferredWidth(90);
        tblHeader.getColumnModel().getColumn(1).setPreferredWidth(140);
        tblHeader.getColumnModel().getColumn(2).setPreferredWidth(325);
        tblHeader.getColumnModel().getColumn(3).setPreferredWidth(140);
        tblHeader.getColumnModel().getColumn(4).setPreferredWidth(140);

        tblCD.setRowHeight(30);
        tblCD.setSelectionBackground(Color.decode("#3a4d8f"));
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
        for (int i = 0; i < tblCD.getColumnCount(); i++) {
            tblCD.setDefaultRenderer(tblCD.getColumnClass(i), renderer);
        }

        tblCD.setRowSorter(new TableRowSorter(model));
        tblCD.setAutoCreateRowSorter(true);

        btnNew = new JButton("Thêm mới");
        btnSave = new JButton("Lưu");
        btnEdit = new JButton("Sửa");
        btnDelete = new JButton("Xóa");
        btnCancel = new JButton("Bỏ qua");
        btnNextSearch = new JButton("Tìm tiếp");

        btnSearch.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnNew.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnSave.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnEdit.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnDelete.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnCancel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnNextSearch.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // set Font
        lblInformationTitle.setFont(new Font("Segoe UI", 1, 20));
        lblSearchTitle.setFont(new Font("Segoe UI", 0, 16));
        lblSearch.setFont(new Font("Segoe UI", 0, 16));
        lblMaCD.setFont(new Font("Segoe UI", 0, 16));
        lblTenCD.setFont(new Font("Segoe UI", 0, 16));
        lblHocPhi.setFont(new Font("Segoe UI", 0, 16));
        lblThoiLuong.setFont(new Font("Segoe UI", 0, 16));
        lblMoTa.setFont(new Font("Segoe UI", 0, 16));

        txtMaCD.setFont(new Font("Segoe UI", 0, 13));
        txtTenCD.setFont(new Font("Segoe UI", 0, 13));
        txtHocPhi.setFont(new Font("Segoe UI", 0, 13));
        txtThoiLuong.setFont(new Font("Segoe UI", 0, 13));
        txtSearch.setFont(new Font("Segoe UI", 0, 13));
        txaMoTa.setFont(new Font("Segoe UI", 0, 13));

        btnNew.setFont(new Font("Segoe UI", 0, 14));
        btnSave.setFont(new Font("Segoe UI", 0, 14));
        btnEdit.setFont(new Font("Segoe UI", 0, 14));
        btnDelete.setFont(new Font("Segoe UI", 0, 14));
        btnSearch.setFont(new Font("Segoe UI", 0, 14));
        btnCancel.setFont(new Font("Segoe UI", 0, 14));
        btnNextSearch.setFont(new Font("Segoe UI", 0, 14));

        tblCD.setFont(new Font("Segoe UI", 0, 12));

        btnEdit.setPreferredSize(btnNew.getPreferredSize());
        btnSave.setPreferredSize(btnNew.getPreferredSize());
        btnDelete.setPreferredSize(btnNew.getPreferredSize());
        btnSearch.setPreferredSize(new Dimension(0, 0));
        btnCancel.setPreferredSize(btnNew.getPreferredSize());
        btnNextSearch.setPreferredSize(btnNew.getPreferredSize());

        //set phím tắt
        btnSave.setMnemonic(KeyEvent.VK_L);
        btnNew.setMnemonic(KeyEvent.VK_T);
        btnEdit.setMnemonic(KeyEvent.VK_S);
        btnDelete.setMnemonic(KeyEvent.VK_X);
        btnCancel.setMnemonic(KeyEvent.VK_B);
        btnSearch.setMnemonic(KeyEvent.VK_F);

        // set tooltip
        btnSave.setToolTipText("Phím tắt Alt + S");
        btnNew.setToolTipText("Phím tắt Alt + N");
        btnEdit.setToolTipText("Phím tắt Alt + U");
        btnDelete.setToolTipText("Phím tắt Alt + D");
        btnCancel.setToolTipText("Phím tắt Alt + Z");

        try {
            list = new ChuyenDeDAO().getAll();
        } catch (SQLException ex) {
        }

    }

    private void showWindow() {
        this.setSize(1100, 650);
    }

    private void addControls() {

        //<editor-fold defaultstate="collapsed" desc="LEFT">
        JPanel pnSearch = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        pnSearch.add(lblSearch);
        pnSearch.add(txtSearch);
        txtSearch.setPreferredSize(new Dimension(300, 25));
        pnSearch.add(btnNextSearch);
        pnSearch.add(btnSearch);
        pnSearch.setBorder(new TitledBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY), "Tìm kiếm", 1, 2, new Font("Segor UI", 1, 18)));

        JPanel pnTable = new JPanel(new BorderLayout());
        pnTable.add(scTable, BorderLayout.CENTER);

        JPanel pnLeft = new JPanel(new BorderLayout(0, 15));
        pnLeft.add(pnSearch, BorderLayout.NORTH);
        pnLeft.add(pnTable, BorderLayout.CENTER);
        //</editor-fold>

        //<editor-fold defaultstate="collapsed" desc="RIGHT">
        JPanel pnInformation = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(5, 10, 5, 10);
        gbc.ipady = 5;

        gbc.gridx = 0;
        gbc.gridy = 0;
        pnInformation.add(lblInformationTitle, gbc);

        gbc.gridy = 1;
        pnInformation.add(lblMaCD, gbc);

        gbc.gridy = 2;
        pnInformation.add(txtMaCD, gbc);

        gbc.gridy = 3;
        pnInformation.add(lblTenCD, gbc);

        gbc.gridy = 4;
        pnInformation.add(txtTenCD, gbc);

        gbc.gridy = 5;
        pnInformation.add(lblHocPhi, gbc);

        gbc.gridy = 6;
        pnInformation.add(txtHocPhi, gbc);

        gbc.gridy = 7;
        pnInformation.add(lblThoiLuong, gbc);

        gbc.gridy = 8;
        pnInformation.add(txtThoiLuong, gbc);

        gbc.gridy = 9;
        pnInformation.add(lblMoTa, gbc);

        gbc.gridy = 10;
        gbc.ipady = 100;
        pnInformation.add(scMoTa, gbc);

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

        JPanel pnRight = new JPanel(new BorderLayout());
        pnRight.add(pnInformation, BorderLayout.NORTH);
        pnRight.add(pnItems, BorderLayout.CENTER);
        //</editor-fold>

        //<editor-fold defaultstate="collapsed" desc="Panel Main">
        JPanel pnMain = new JPanel();
        pnMain.setLayout(new BorderLayout(5, 0));
        pnMain.add(pnLeft, BorderLayout.CENTER);
        pnMain.add(pnRight, BorderLayout.EAST);

        // Container
        this.setLayout(new BorderLayout());
        this.add(pnMain, BorderLayout.CENTER);
        //</editor-fold>
    }

    private void addEvents() {

        //<editor-fold defaultstate="collapsed" desc="Load Chuyên đề đầu tiên lên form">
        fillToForm(index);
        //</editor-fold>

        doneLoad = true;

        //<editor-fold defaultstate="collapsed" desc="Sự kiện THÊM MỚI">
        btnNew.addActionListener((ActionEvent e) -> {
            clear();
            tblCD.clearSelection();
            setStatus(true);
            txtMaCD.requestFocus();
        });
        //</editor-fold>

        //<editor-fold defaultstate="collapsed" desc="Sự kiên SỬA">
        btnEdit.addActionListener((ActionEvent e) -> {
            setStatus(false);
            txtTenCD.requestFocus();
        });
        //</editor-fold>

        //<editor-fold defaultstate="collapsed" desc="Sự kiên LƯU">
        btnSave.addActionListener((ActionEvent e) -> {
            save();
            DangNhapFrame.pnQLKH.refresh();
        });
        //</editor-fold>

        //<editor-fold defaultstate="collapsed" desc="Sự kiện XÓA">
        btnDelete.addActionListener((ActionEvent e) -> {
            delete();
        });
        //</editor-fold>

        //<editor-fold defaultstate="collapsed" desc="Sự kiện BỎ QUA">
        btnCancel.addActionListener((ActionEvent e) -> {
            int answer = JOptionPane.showConfirmDialog(null, messCancel, "Hỏi", JOptionPane.YES_NO_OPTION);

            if (answer == JOptionPane.NO_OPTION || answer == JOptionPane.CLOSED_OPTION) {
                return;
            }

            fillToForm(index);
            setDefault();
        });
        //</editor-fold>

        //<editor-fold defaultstate="collapsed" desc="Sự kiện TABLE MOUSE CLICK">
        tblCD.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {

                // <editor-fold defaultstate="collapsed" desc="Click chuột phải ">
                if (e.getButton() == MouseEvent.BUTTON3) {
                    int r = tblCD.rowAtPoint(e.getPoint());
                    if (r >= 0 && r < tblCD.getRowCount()) {
                        tblCD.setRowSelectionInterval(r, r);
                    } else {
                        tblCD.clearSelection();
                    }
                }
                //</editor-fold>

                //<editor-fold defaultstate="collapsed" desc="Clcik chuột trái">
                if (choose != 0) {
                    int answer = JOptionPane.showConfirmDialog(null, messCancel, "Hỏi", JOptionPane.YES_NO_OPTION);

                    if (answer == JOptionPane.YES_OPTION) {
                        setDefault();
                        index = tblCD.getSelectedRow();
                        fillToForm(index);

                        if (e.getButton() == MouseEvent.BUTTON3) {
                            JPopupMenu popup = new QLCD.PopupmenuTblHV();
                            popup.show(e.getComponent(), e.getX(), e.getY());
                        }
                    } else {
                        tblCD.clearSelection();
                    }
                } else {
                    setDefault();
                    index = tblCD.getSelectedRow();
                    fillToForm(index);

                    if (e.getButton() == MouseEvent.BUTTON3) {
                        JPopupMenu popup = new QLCD.PopupmenuTblHV();
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

        //<editor-fold defaultstate="collapsed" desc="Sự kiên TABLE KEY EVENT">
        tblCD.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
            }

            @Override
            public void keyPressed(KeyEvent e) {
            }

            @Override
            public void keyReleased(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_UP) {
                    index = tblCD.getSelectedRow();
                    fillToForm(index);
                }

                if (e.getKeyCode() == KeyEvent.VK_DOWN) {
                    index = tblCD.getSelectedRow();
                    fillToForm(index);
                }
            }
        });
        //</editor-fold>

        // <editor-fold defaultstate="collapsed" desc="Sự kiện btnSearch ">
        btnSearch.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                txtSearch.requestFocus();
            }
        });
        // </editor-fold>

        // <editor-fold defaultstate="collapsed" desc="Tìm kiếm CD với txtSearch ">
        txtSearch.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                try {
                    if (txtSearch.getText().length() > 0) {
                        listSearch = new ChuyenDeDAO().get(txtSearch.getText().trim());
                        search(indexSearch);

                        if (listSearch.size() > 0) {
                            btnNew.setEnabled(true);
                            btnEdit.setEnabled(true);
                            btnDelete.setEnabled(true);
                        } else {
                            btnNew.setEnabled(false);
                            btnEdit.setEnabled(false);
                            btnDelete.setEnabled(false);
                        }

                    } else {
                        indexSearch = 0;
                        index = 0;
                        fillToForm(index);

                    }
                } catch (SQLException ex) {
                    Logger.getLogger(QLCD.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                try {
                    if (txtSearch.getText().length() > 0) {
                        listSearch = new ChuyenDeDAO().get(txtSearch.getText().trim());
                        search(indexSearch);
                        if (listSearch.size() > 0) {
                            btnNew.setEnabled(true);
                            btnEdit.setEnabled(true);
                            btnDelete.setEnabled(true);
                        } else {
                            btnNew.setEnabled(false);
                            btnEdit.setEnabled(false);
                            btnDelete.setEnabled(false);
                        }
                    } else {
                        indexSearch = 0;
                        index = 0;
                        fillToForm(index);
                    }
                } catch (SQLException ex) {
                    Logger.getLogger(QLCD.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

            @Override
            public void changedUpdate(DocumentEvent e) {

            }
        });

        txtSearch.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
            }

            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER && listSearch.size() > 0 && txtSearch.getText().length() > 0) {
                    search(indexSearch++);
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {

            }
        });

        // </editor-fold>
        // <editor-fold defaultstate="collapsed" desc="Sự kiện clear txtSearch với phím ESC ">
        txtSearch.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
            }

            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                    txtSearch.setText("");
                    loadDataToTable();
                    fillToForm(0);
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
            }
        });
        // </editor-fold>

        //<editor-fold defaultstate="collapsed" desc="Sự kiện TÌM CD TIẾP THEO VỚI btnNextSearch">
        btnNextSearch.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (txtSearch.getText().length() > 0) {
                    search(indexSearch++);
                }
            }
        });
        //</editor-fold>

    }

    public void refresh() {
        loadDataToTable();
    }

    // CLEAR FORM
    private void clear() {
        chuyenDe = new ChuyenDe();
        setModel(chuyenDe);
    }

    // LẤY CHUYÊN ĐỀ TỪ FORM CHI TIẾT
    private ChuyenDe getModel() {
        chuyenDe.setId(txtMaCD.getText().toUpperCase());
        chuyenDe.setTenCD(txtTenCD.getText());
        chuyenDe.setHocPhi(Integer.parseInt(txtHocPhi.getText()));
        chuyenDe.setThoiLuong(Integer.parseInt(txtThoiLuong.getText()));
        chuyenDe.setMoTa(txaMoTa.getText());
        return chuyenDe;
    }

    // SET FORM CHI TIẾT TỪ CHUYÊN ĐỀ
    private void setModel(ChuyenDe cd) {
        txtMaCD.setText(cd.getId());
        txtTenCD.setText(cd.getTenCD());
        txtHocPhi.setText(String.valueOf(cd.getHocPhi()));
        txtThoiLuong.setText(String.valueOf(cd.getThoiLuong()));
        txaMoTa.setText(cd.getMoTa());
    }

    // TÌM KIẾM CHUYÊN ĐỀ
    private void search(int i) {
        if (listSearch.size() > 0) {

            if (indexSearch == listSearch.size()) {
                indexSearch = 0;
            }

            for (ChuyenDe chuyenDe1 : list) {
                if (chuyenDe1.getId().equalsIgnoreCase(listSearch.get(indexSearch).getId())) {
                    index = list.indexOf(chuyenDe1);
                    fillToForm(index);
                    break;
                }
            }
        } else {
            clear();
            indexSearch = 0;
            index = -1;
            tblCD.clearSelection();
        }
    }

    /* 
        LƯU CHUYÊN ĐỀ 
        1 : THÊM MỚI
        2 : CẬP NHẬT
     */
    private void save() {
        if (checkInfor()) {
            try {
                int n = -1;

                if (choose == 1) {
                    n = new ChuyenDeDAO().insert(getModel());
                } else if (choose == 2) {
                    n = new ChuyenDeDAO().update(getModel());
                }

                if (n > 0) {
                    setDefault();
                    loadDataToTable();
                    for (ChuyenDe cd : list) {
                        if (cd.getId().equalsIgnoreCase(getModel().getId())) {
                            index = list.indexOf(cd);
                            fillToForm(index);
                        }
                    }

                } else {
                    JOptionPane.showMessageDialog(null, "Không thành công! Vui lòng thử lại", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
                }
            } catch (SQLException ex) {
            }
        }
    }

    // XÓA CHUYÊN ĐỀ
    private void delete() {
        if (chkKH_IN_CD(chuyenDe)) {
            JOptionPane.showMessageDialog(null, "Đã tồn tại khóa học thuộc chuyên đề " + chuyenDe + ".\nKhông thể xóa chuyên đề này", "Cảnh báo!", JOptionPane.WARNING_MESSAGE);
        } else {
            int answer = JOptionPane.showConfirmDialog(null, "Bạn có chắc muốn xóa chuyên đề " + chuyenDe + " ?", "Hỏi", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
            if (answer == JOptionPane.YES_OPTION) {
                try {
                    int n = new ChuyenDeDAO().delete(chuyenDe.getId());

                    if (n == 1) {

                        if (index == list.size() - 1) {
                            index = index - 1;
                        }

                        loadDataToTable();
                        fillToForm(index);
                        
                        DangNhapFrame.pnQLKH.refresh();
                    } else {
                        JOptionPane.showMessageDialog(null, "Xóa không thành công!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
                    }
                } catch (SQLException ex) {
                }
            }
        }

    }

    // RESET FORM MẶC ĐỊNH
    private void setDefault() {
        txtMaCD.setEnabled(false);
        txtTenCD.setEnabled(false);
        txtHocPhi.setEnabled(false);
        txtThoiLuong.setEnabled(false);
        txaMoTa.setEnabled(false);

        btnSave.setVisible(false);
        btnCancel.setVisible(false);

        btnEdit.setVisible(true);
        btnNew.setVisible(true);
        btnDelete.setVisible(true);
        btnNextSearch.setEnabled(true);

        txtSearch.setEnabled(true);
        txtSearch.requestFocus();
        choose = 0;
    }

    // TRUE - THÊM MỚI / FALSE - SỬA
    private void setStatus(boolean insertable) {
        if (insertable) {
            choose = 1;
            messCancel = "Bạn có muốn bỏ qua thao tác thêm mới chuyên đề không?";
        } else {
            choose = 2;
            messCancel = "Bạn có muốn bỏ qua thao tác sửa thông tin chuyên đề không?";
        }

        btnSave.setVisible(true);
        btnNew.setVisible(false);
        btnEdit.setVisible(false);
        btnDelete.setVisible(!insertable);
        btnCancel.setVisible(true);

        txtMaCD.setEnabled(insertable);
        txtTenCD.setEnabled(true);
        txtHocPhi.setEnabled(true);
        txtThoiLuong.setEnabled(true);
        txaMoTa.setEnabled(true);

        txtSearch.setEnabled(false);
        btnNextSearch.setEnabled(false);
    }

    private void fillToForm(int index) {
        if (list.size() > 0) {
            tblCD.setRowSelectionInterval(index, index);
            Rectangle rect = tblCD.getCellRect(index, 0, true);
            tblCD.scrollRectToVisible(rect);
//            if (choose == 0) {
            stt = Integer.parseInt(tblCD.getValueAt(index, 0).toString());
            chuyenDe = list.get(stt - 1);
            setModel(chuyenDe);
//            }
        }
    }

    private void loadDataToTable() {
        model.setRowCount(0);
        try {
            list = new ChuyenDeDAO().getAll();
            int i = 1;
            for (ChuyenDe cd : list) {
                model.addRow(new Object[]{
                    i, cd.getId(), cd.getTenCD(), castIntToMoney(cd.getHocPhi() * 1000), cd.getThoiLuong()
                });
                i++;
            }
        } catch (SQLException ex) {
        }
    }

    //Chuyển số sang str tiền tệ
    private String castIntToMoney(int money) {
        return numberFormat.format(money);
    }

    // Check Validate
    private boolean checkInfor() {
//        if (txtMaCD.getText().length() == 0) {
//            JOptionPane.showMessageDialog(null, "Vui lòng nhập mã chuyên đề!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
//            txtMaCD.requestFocus();
//            return false;
//        }

//        if (txtMaCD.getText().length() > 7) {
//            JOptionPane.showMessageDialog(null, "Vui lòng nhập mã chuyên đề có độ dài nhỏ hơn 7 ký tự!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
//            txtMaCD.requestFocus();
//            return false;
//        }
//        if (txtTenCD.getText().length() == 0) {
//            JOptionPane.showMessageDialog(null, "Vui lòng nhập tên chuyên đề!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
//            txtTenCD.requestFocus();
//            return false;
//        }
        if (!txtMaCD.getText().matches("^[A-Za-zÀÁÂÃÈÉÊÌÍÒÓÔÕÙÚÝàáâãèéêìíòóôõùúýĂăĐđĨĩŨũƠơƯưẠ-ỹ[0-9]]{5,7}$")) {
            JOptionPane.showMessageDialog(null, "Vui lòng nhập mã chuyên đề có độ dài nhỏ hơn 5-7 ký tự, không chứa các ký tự đặc biệt \n, . / ; ' < > ? : \" { } - = _ + ` ~ ! @ $ % ^ & * ( ) \\ |", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            txtMaCD.requestFocus();
            return false;
        }
        
        try {
            ChuyenDe cd = new ChuyenDeDAO().getById(txtMaCD.getText().trim());
            if (cd != null && choose != 2) {
                JOptionPane.showMessageDialog(null, "Chuyên đề đã tồn tại! Vui lòng nhập lại!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
                txtMaCD.requestFocus();
                return false;
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        if (!txtTenCD.getText().matches("^[A-Za-z0-9ÀÁÂÃÈÉÊÌÍÒÓÔÕÙÚÝàáâãèéêìíòóôõùúýĂăĐđĨĩŨũƠơƯưẠ-ỹ\\s]{5,100}$")) {
            JOptionPane.showMessageDialog(null, "Vui lòng nhập tên chuyên đề, không chứa các ký tự đặc biệt \n, . / ; ' < > ? : \" { } - = _ + ` ~ ! @ $ % ^ & * ( ) \\ |", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            txtTenCD.requestFocus();
            return false;
        }
        
        try {
            ChuyenDe cd = new ChuyenDeDAO().getByName(txtTenCD.getText().trim());
            if (cd != null && choose != 2) {
                JOptionPane.showMessageDialog(null, "Tên chuyên đề đã tồn tại! Vui lòng nhập lại!", "Cảnh báo báo", JOptionPane.WARNING_MESSAGE);
                txtTenCD.requestFocus();
                return false;
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        if (!txtHocPhi.getText().matches("^\\d+$")) {
            JOptionPane.showMessageDialog(null, "Học phí không hợp lệ. Học phí phải là một số nguyên dương lớn hơn 0!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            txtHocPhi.requestFocus();
            return false;
        }

        if (Integer.parseInt(txtHocPhi.getText()) < 500) {
            JOptionPane.showMessageDialog(null, "Học phí ít nhất là 500.000 đ! Vui lòng nhập lại!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            txtHocPhi.requestFocus();
            return false;
        }

        if (!txtThoiLuong.getText().matches("^\\d+$")) {
            JOptionPane.showMessageDialog(null, "Thời lượng không hợp lệ. Thời lượng phải là một số nguyên dương lớn hơn 0!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            txtThoiLuong.requestFocus();
            return false;
        }

        if (Integer.parseInt(txtThoiLuong.getText()) < 20) {
            JOptionPane.showMessageDialog(null, "Thời lượng phải lớn hơn 20! Vui lòng nhập lại", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            txtThoiLuong.requestFocus();
            return false;
        }

        return true;
    }

    private boolean chkKH_IN_CD(ChuyenDe cd) {
        String sql = "Select COUNT(*) FROM KHOAHOC WHERE MACD = ?";
        int kq = -1;
        try {
            ResultSet rs = new JdbcHelper().executeQuery(sql, cd.getId());
            while (rs.next()) {
                kq = rs.getInt(1);
            }
        } catch (SQLException ex) {
            Logger.getLogger(QLCD.class.getName()).log(Level.SEVERE, null, ex);
        }
        if (kq > 0) {
            return true;
        } else {
            return false;
        }
    }

    // <editor-fold defaultstate="collapsed" desc="CREATE POPUPMENU ">
    class PopupmenuTblHV extends JPopupMenu {

        public PopupmenuTblHV() {
            JMenuItem mnuUpdate = new JMenuItem("Sửa");
            JMenuItem mnuDelete = new JMenuItem("Xóa");
            this.add(mnuUpdate);
            if (DangNhapFrame.nvLogin.getVaiTro() == ROLE_TRUONG_PHONG) {
                this.add(mnuDelete);
            }

            mnuDelete.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    delete();
                }
            });

            mnuUpdate.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    setStatus(false);
                    txtTenCD.requestFocus();
                }
            });
        }
    }
    // </editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Component">
    int choose;
    int stt = -1;
    int index = 0;
    int indexSearch = 0;

    String messCancel = "";

    ChuyenDe chuyenDe;
    List<ChuyenDe> list = new ArrayList<>();
    List<ChuyenDe> listSearch = new ArrayList<>();
    NumberFormat numberFormat = NumberFormat.getCurrencyInstance();

    JLabel lblInformationTitle, lblSearchTitle, lblSearch, lblMaCD, lblTenCD, lblHocPhi, lblThoiLuong, lblMoTa;
    JTextField txtMaCD, txtTenCD, txtHocPhi, txtThoiLuong, txtSearch;
    JTextArea txaMoTa;
    JButton btnNew, btnSave, btnDelete, btnEdit, btnSearch, btnCancel, btnNextSearch;
    DefaultTableModel model;
    JTable tblCD;
    JScrollPane scMoTa, scTable;
    JTableHeader tblHeader;
    //</editor-fold>
}
