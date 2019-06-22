/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package QuanLy;

//<editor-fold defaultstate="collapsed" desc="IMPORT">
import DAO.HocVienDAO;
import DAO.HocVien_KhoaHocDAO;
import DAO.KhoaHocDAO;
import HeThong.DangNhapFrame;
import Helper.DateHelper;
import Helper.JdbcHelper;
import Model.HocVien;
import Model.HocVien_KhoaHoc;
import Model.KhoaHoc;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
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
import java.text.ParseException;
import java.util.ArrayList;
import java.util.EventObject;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BorderFactory;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.TitledBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableRowSorter;
//</editor-fold>

/**
 *
 * @author phuoc.bleach
 */
public class QLHVKH extends JPanel {

    public static boolean doneLoad = false;

    public QLHVKH() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            this.init();
            this.addControls();
            this.loadDataToCbxNam();
            this.loadDataToCbxKH();
            this.addEvents();
            this.showWindow();
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex) {
            Logger.getLogger(QLHVKH.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void init() {
        lblHocVien = new JLabel("Học Viên");
        txtHocVien = new JTextField();

        lblSearch = new JLabel("Lọc");
        lblSoLuong = new JLabel("Số lượng: ");
        lblNumber = new JLabel("0");
        lblSearchNam = new JLabel("Năm học");
        cbxSearchNam = new JComboBox();
        cbxSearchNam.setName("Khóa học");
        cbxSearchNam.setPreferredSize(new Dimension(80, 25));

        lblSearchKH = new JLabel("Khóa học");
        cbxSearchKH = new JComboBox();
        cbxSearchKH.setName("Khóa học");
        cbxSearchKH.setPreferredSize(new Dimension(320, 25));

        model = new DefaultTableModel();
        model.setColumnIdentifiers(new Object[]{"STT", "MÃ HỌC VIÊN", "HỌ VÀ TÊN", "EMAIL", "GIỚI TÍNH", "ĐIỂM"});

        tblHVKH = new JTable(model) {
            @Override
            public boolean isCellEditable(int row, int column) {
                if (column != model.getColumnCount() - 1) {
                    return false;
                }
                return super.isCellEditable(row, column); //To change body of generated methods, choose Tools | Templates.
            }

            @Override
            public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {
                JLabel c = (JLabel) super.prepareRenderer(renderer, row, column);
                if (column == 2 || column == 3) {
                    c.setHorizontalAlignment(JLabel.LEFT);
                } else {
                    c.setHorizontalAlignment(JLabel.CENTER);

                }
                return c;
            }
        };

        tblHVKH.getColumnModel().getColumn(0).setPreferredWidth(70);
        tblHVKH.getColumnModel().getColumn(1).setPreferredWidth(180);
        tblHVKH.getColumnModel().getColumn(2).setPreferredWidth(250);
        tblHVKH.getColumnModel().getColumn(3).setPreferredWidth(250);
        tblHVKH.getColumnModel().getColumn(4).setPreferredWidth(100);
        tblHVKH.getColumnModel().getColumn(5).setPreferredWidth(100);

        scTable = new JScrollPane(tblHVKH, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        tblHeader = tblHVKH.getTableHeader();
        ((DefaultTableCellRenderer) tblHeader.getDefaultRenderer()).setHorizontalAlignment(JLabel.CENTER);

        tblHVKH.setRowHeight(30);
        tblHVKH.setSelectionBackground(Color.decode("#3a4d8f"));
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
        for (int i = 0; i < tblHVKH.getColumnCount(); i++) {
            tblHVKH.setDefaultRenderer(tblHVKH.getColumnClass(i), renderer);
        }

        tblHVKH.setRowSorter(new TableRowSorter(model));
        tblHVKH.setAutoCreateRowSorter(true);

        // Font
        lblSearchKH.setFont(new Font("Segoe UI", 0, 16));
        lblSearchNam.setFont(new Font("Segoe UI", 0, 16));
        lblHocVien.setFont(new Font("Segoe UI", 0, 16));

        lblSoLuong.setFont(new Font("Segoe UI", 0, 14));
        lblNumber.setFont(new Font("Segoe UI", 0, 14));
        lblNumber.setPreferredSize(new Dimension(50, 20));

        cbxSearchKH.setFont(new Font("Segoe UI", 0, 14));
        cbxSearchNam.setFont(new Font("Segoe UI", 0, 14));

        tblHVKH.setFont(new Font("Segoe UI", 0, 14));
    }

    private void addControls() {

        //<editor-fold defaultstate="collapsed" desc="Header">
        JPanel pnHeader = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;

        gbc.insets = new Insets(5, 10, 5, 10);
        gbc.ipady = 5;
        gbc.ipadx = 25;

        gbc.gridx = 0;
        gbc.gridy = 0;
        pnHeader.add(lblSearchNam, gbc);

        gbc.gridx = 1;
        pnHeader.add(cbxSearchNam, gbc);

        gbc.gridx = 2;
        pnHeader.add(lblSearchKH, gbc);

        gbc.gridx = 3;
        pnHeader.add(cbxSearchKH, gbc);

        gbc.gridy = 1;
        gbc.gridx = 2;
        pnHeader.add(lblHocVien, gbc);

        gbc.gridx = 3;
        pnHeader.add(txtHocVien, gbc);

        pnHeader.setBorder(new TitledBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY), "Lọc", 1, 2, new Font("Segoe UI", 1, 18)));
        //</editor-fold>

        //<editor-fold defaultstate="collapsed" desc="Content">
        JPanel pnContent = new JPanel(new BorderLayout());;
        pnContent.add(scTable, BorderLayout.CENTER);

        JPanel pnItems = new JPanel(new FlowLayout(FlowLayout.TRAILING, 20, 5));
        pnItems.add(lblSoLuong);
        pnItems.add(lblNumber);
        pnContent.add(pnItems, BorderLayout.NORTH);
        //</editor-fold>

        //<editor-fold defaultstate="collapsed" desc="Container">
        this.setLayout(new BorderLayout(0, 10));
        this.add(pnHeader, BorderLayout.NORTH);
        this.add(pnContent, BorderLayout.CENTER);
        //</editor-fold>
    }

    private void addEvents() {
        loadDataToTable();

        doneLoad = true;

        //<editor-fold defaultstate="collapsed" desc="Search">
        cbxSearchKH.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    if (cbxSearchKH.getItemCount() > 0 && cbxSearchNam.getItemCount() > 0) {
                        loadDataToTable();
                    }
                }
            }
        });

        cbxSearchNam.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    if (cbxSearchNam.getItemCount() > 0) {
                        loadDataToCbxKH();
//                        loadDataToTable();
                    }
                }
            }
        });
        //</editor-fold>

        //<editor-fold defaultstate="collapsed" desc="Table MouseClick">
        tblHVKH.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                try {
                    // <editor-fold defaultstate="collapsed" desc="Click chuột phải ">
                    if (e.getButton() == MouseEvent.BUTTON3) {
                        int r = tblHVKH.rowAtPoint(e.getPoint());
                        if (r >= 0 && r < tblHVKH.getRowCount()) {
                            tblHVKH.setRowSelectionInterval(r, r);
                        } else {
                            tblHVKH.clearSelection();
                        }
                    }
                    //</editor-fold>

                    //<editor-fold defaultstate="collapsed" desc="Click chuột trái">
                    int row = tblHVKH.getSelectedRow();

                    hvkh = listHV_KH.get(((int) tblHVKH.getValueAt(row, 0)) - 1);
                    hv = getByID(hvkh.getMaHV());

                    if (new QLKH().flagNgayHH(khoaHoc) == false) {
                        if (e.getButton() == MouseEvent.BUTTON3) {
                            popup = new QLHVKH.PopupmenuTblHVKH();
                            popup.show(e.getComponent(), e.getX(), e.getY());
                        }
                    }

                    //</editor-fold>
                } catch (Exception ex) {
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
        //</editor-fold>

        //<editor-fold defaultstate="collapsed" desc="Update Mark On Table">
        model.addTableModelListener(new TableModelListener() {
            @Override
            public void tableChanged(TableModelEvent e) {
                int row = e.getFirstRow();
                int col = e.getColumn();

                // nếu thay đổi giá trị ở cột điểm
                if (col == model.getColumnCount() - 1
                        && !model.getValueAt(row, col).equals("-")
                        && !model.getValueAt(row, col).equals(hvkh.getDiem())) {
                    if (checkValidate()) {
                        try {
                            hvkh.setDiem(Double.parseDouble(model.getValueAt(row, col).toString()));
                            int n = new HocVien_KhoaHocDAO().update(hvkh);
                            if (n > 0) {
                                loadDataToTable();
                                DangNhapFrame.pnTHBĐ.refresh();
                                DangNhapFrame.pnQLHV.refresh();
                                DangNhapFrame.pnQLHV.refresh();
                            } else {
                                JOptionPane.showMessageDialog(null, "Cập nhật không thành công. Vui lòng kiểm tra lại!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
                            }

                        } catch (SQLException ex) {
                            System.out.println(ex.toString());
                        }
                    }
                }
            }
        });
        //</editor-fold>

        // <editor-fold defaultstate="collapsed" desc="Tìm kiếm HVKH với txtSearch ">
        txtHocVien.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                try {

                    if (txtHocVien.getText().length() > 0) {
                        listSearchHV = getListHV_Join_HVKH(txtHocVien.getText().trim(), khoaHoc.getId());
                        search(indexSearch);
                    } else {
                        indexSearch = 0;
                        tblHVKH.clearSelection();
                    }

                } catch (SQLException ex) {
                    Logger.getLogger(QLHVKH.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                try {

                    if (txtHocVien.getText().length() > 0) {
                        listSearchHV = getListHV_Join_HVKH(txtHocVien.getText().trim(), khoaHoc.getId());
                        search(indexSearch);
                    } else {
                        indexSearch = 0;
                        tblHVKH.clearSelection();
                    }

                } catch (SQLException ex) {
                    Logger.getLogger(QLHVKH.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

            @Override
            public void changedUpdate(DocumentEvent e) {

            }

        });

        txtHocVien.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
            }

            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER && listSearchHV.size() > 0 && txtHocVien.getText().length() > 0) {
                    search(indexSearch++);
                }

                if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                    txtHocVien.setText("");
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {

            }
        });
        // </editor-fold>

    }

    private void showWindow() {
        this.setSize(900, 650);
    }

    private void search(int i) {
        if (listSearchHV.size() > 0) {
            if (indexSearch == listSearchHV.size()) {
                indexSearch = 0;
            }

            HocVien hocVien = listSearchHV.get(indexSearch);
            for (HocVien_KhoaHoc hvkh : listHV_KH) {
                if (hvkh.getMaHV().equalsIgnoreCase(hocVien.getId())) {
                    index = listHV_KH.indexOf(hvkh);
                    tblHVKH.setRowSelectionInterval(index, index);
                    Rectangle rect = tblHVKH.getCellRect(index, 0, true);
                    tblHVKH.scrollRectToVisible(rect);
                    break;
                }
            }
        } else {
            indexSearch = 0;
            tblHVKH.clearSelection();
        }

    }

    private void addHV() {
        try {
            int n = new HocVien_KhoaHocDAO().insert(hvkh);
            if (n > 0) {
                loadDataToTable();
            } else {
                JOptionPane.showMessageDialog(null, "Thêm không thành công! Vui lòng kiểm tra lại", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            }

        } catch (SQLException ex) {
            System.out.println(ex.toString());
        }

        DangNhapFrame.pnTKDT.refresh();
        DangNhapFrame.pnTHBĐ.refresh();
    }

    private void delete() {
        if (hvkh.getDiem() >= 0) {
            JOptionPane.showMessageDialog(null, "Học viên " + hv + "đã có điểm! Không thể xóa học viên này", "Cảnh báo!", JOptionPane.WARNING_MESSAGE);
        } else {
            int answer = JOptionPane.showConfirmDialog(null, "Bạn có muốn xóa học viên " + hv + " khỏi khóa học " + khoaHoc + "?", "Hỏi", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);

            if (answer == JOptionPane.YES_OPTION) {
                try {
                    int n = new HocVien_KhoaHocDAO().delete(String.valueOf(hvkh.getId()));
                    if (n > 0) {
                        loadDataToTable();
                        DangNhapFrame.pnQLHV.refresh();
                    } else {
                        JOptionPane.showMessageDialog(null, "Xóa không thành công!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
                    }
                } catch (SQLException ex) {
                    System.out.println(ex.toString());
                }
            }
        }
        DangNhapFrame.pnTKDT.refresh();
        DangNhapFrame.pnTHBĐ.refresh();
    }

    private void loadDataToCbxKH() {
        cbxSearchKH.removeAllItems();
        listKH = getListKH((int) cbxSearchNam.getSelectedItem());

        for (KhoaHoc kh : listKH) {
            cbxSearchKH.addItem(kh);
        }
    }

    private void loadDataToCbxNam() {
        try {
            cbxSearchNam.removeAllItems();

            listHV = hvdao.getAll();
            listYear = getYear();

            for (int year : listYear) {
                cbxSearchNam.addItem(year);
            }

        } catch (SQLException ex) {
            System.out.println(ex.toString());
        }
    }

    private void loadDataToTable() {
        model.setRowCount(0);

        if (cbxSearchKH.getItemCount() > 0 && cbxSearchNam.getItemCount() > 0) {
            try {
                khoaHoc = (KhoaHoc) cbxSearchKH.getSelectedItem();
                int yearSearch = (int) cbxSearchNam.getSelectedItem();

                listHV_KH = hvkhdao.get(khoaHoc.getId(), yearSearch);
                int stt = 1;

                for (HocVien_KhoaHoc data : listHV_KH) {
                    HocVien hv = getByID(data.getMaHV());
                    String diem = "";
                    String gioiTinh = "";

                    if (data.getDiem() < 0) {
                        diem = "-";
                    } else {
                        diem = String.valueOf(data.getDiem());
                    }

                    if (hv.isGioiTinh() == true) {
                        gioiTinh = "Nam";
                    } else {
                        gioiTinh = "Nữ";
                    }

                    model.addRow(new Object[]{
                        stt, hv.getId(), hv.getHoTen(), hv.getEmail(), gioiTinh, diem
                    });

                    stt++;
                }

                lblNumber.setText("" + model.getRowCount());

            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }

    private List<Integer> getYear() {
        List<Integer> list = new ArrayList<>();
        String sql = "SELECT DISTINCT YEAR(ngayKG) FROM dbo.KhoaHoc ORDER BY YEAR(ngayKG) DESC";

        try {
            ResultSet rs = new JdbcHelper().executeQuery(sql);
            while (rs.next()) {
                list.add(rs.getInt(1));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return list;
    }

    private List<KhoaHoc> getListKH(int year) {
        List<KhoaHoc> listKhoaHoc = new ArrayList<>();

        String sql = "SELECT * FROM dbo.KhoaHoc WHERE YEAR(ngayKG)= ? ORDER BY ngayKG DESC";

        ResultSet rs;
        try {
            rs = new JdbcHelper().executeQuery(sql, year);
            while (rs.next()) {
                listKhoaHoc.add(khdao.readResultSet(rs));
            }
        } catch (SQLException ex) {
            Logger.getLogger(QLHVKH.class.getName()).log(Level.SEVERE, null, ex);
        }

        return listKhoaHoc;
    }

    public List<HocVien> getListHV_Join_HVKH(Object... args) throws SQLException {
        List<HocVien> listHocVien = new ArrayList<>();

        String sql = "SELECT dbo.HocVien.* FROM dbo.HocVien INNER JOIN dbo.HocVien_KhoaHoc ON HocVien_KhoaHoc.maHV = HocVien.id "
                + "WHERE (HocVien.id LIKE ? OR HocVien.hoTen LIKE ? OR HocVien.dienThoai LIKE ? OR HocVien.email LIKE ?) AND maKH = ?";

        ResultSet rs = jdbcHelper.executeQuery(sql, "%" + args[0] + "%", "%" + args[0] + "%", "%" + args[0] + "%", "%" + args[0] + "%", args[1]);

        while (rs.next()) {
            listHocVien.add(new HocVienDAO().readResultSet(rs));
        }

        return listHocVien;
    }

    public HocVien getByID(String id) throws SQLException {
        String sql = "SELECT * FROM dbo.HocVien WHERE id = ?";

        ResultSet rs = jdbcHelper.executeQuery(sql, id);
        HocVien hocVien = null;

        while (rs.next()) {
            hocVien = new HocVienDAO().readResultSet(rs);
        }

        return hocVien;
    }

    public void refresh() {
        try {
            loadDataToCbxNam();
            loadDataToCbxKH();
            loadDataToTable();
        } catch (Exception e) {
        }

    }

    private boolean checkValidate() {
        int row = tblHVKH.getSelectedRow();
        int col = model.getColumnCount() - 1;
        String strDiem = model.getValueAt(row, col).toString();

        if (strDiem.length() == 0) {
            hvkh.setDiem(-1);
            model.setValueAt(hvkh.getDiem(), row, col);
            return true;
        }

        if (!strDiem.matches("^\\d*\\.*\\d*$")) {
            JOptionPane.showMessageDialog(null, "Điểm phải là số từ 0 đến 10", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            if (hvkh.getDiem() == -1) {
                model.setValueAt("-", row, col);
            } else {
                model.setValueAt(hvkh.getDiem(), row, col);
            }
            return false;
        }

        if (Double.parseDouble(strDiem) < 0 || Double.parseDouble(strDiem) > 10) {
            JOptionPane.showMessageDialog(null, "Điểm phải nằm trong khoảng từ 0 - 10", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            if (hvkh.getDiem() == -1) {
                model.setValueAt("-", row, col);
            } else {
                model.setValueAt(hvkh.getDiem(), row, col);
            }
            return false;
        }

        if (new QLKH().flagNgayHH(khoaHoc) == true) {
            JOptionPane.showMessageDialog(null, "Khóa học " + khoaHoc + " đã kết thúc - Không thể sửa điểm cho học viên", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            if (hvkh.getDiem() == -1) {
                model.setValueAt("-", row, col);
            } else {
                model.setValueAt(hvkh.getDiem(), row, col);
            }
            return false;
        }
        return true;
    }

    class PopupmenuTblHVKH extends JPopupMenu {

        public PopupmenuTblHVKH() {
            JMenuItem mnuDelete = new JMenuItem("Xóa");
            JMenuItem mnuUpdate = new JMenuItem("Sửa");

            this.add(mnuUpdate);

            if (DangNhapFrame.nvLogin.getVaiTro() == 1) {
                this.add(mnuDelete);
            }

            mnuDelete.setVisible(true);
            mnuUpdate.setVisible(true);

            mnuDelete.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {

                    delete();
                }
            });

            mnuUpdate.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    int row = tblHVKH.getSelectedRow();
                    tblHVKH.setColumnSelectionInterval(model.getColumnCount() - 1, model.getColumnCount() - 1);
                    tblHVKH.editCellAt(row, model.getColumnCount() - 1, new EventObject(true));
                    tblHVKH.transferFocus();
                }
            });
        }
    }

    //<editor-fold defaultstate="collapsed" desc="COMPONENT">
    public static JPopupMenu popup;
    int index = 0;
    int indexSearch = 0;

    HocVien_KhoaHocDAO hvkhdao = new HocVien_KhoaHocDAO();
    KhoaHocDAO khdao = new KhoaHocDAO();
    HocVienDAO hvdao = new HocVienDAO();
    DateHelper dateHelper = new DateHelper();
    JdbcHelper jdbcHelper = new JdbcHelper();

    List<HocVien> listSearchHV = new ArrayList<>();
    List<KhoaHoc> listKH = new ArrayList<>();
    List<HocVien> listHV = new ArrayList<>();
    List<HocVien_KhoaHoc> listHV_KH = new ArrayList<>();
    List<HocVien_KhoaHoc> listHV_KH_Search = new ArrayList<>();

    HocVien_KhoaHoc hvkh = new HocVien_KhoaHoc();
    KhoaHoc khoaHoc = new KhoaHoc();
    HocVien hv = new HocVien();
    List<Integer> listYear = new ArrayList<>();

    JLabel lblSearch, lblSearchKH, lblSearchNam, lblNumber, lblSoLuong, lblHocVien;
    JTextField txtHocVien;

    JComboBox cbxSearchKH, cbxSearchNam;
    JTable tblHVKH;
    DefaultTableModel model;
    JScrollPane scTable;
    JTableHeader tblHeader;
    //</editor-fold>

}
