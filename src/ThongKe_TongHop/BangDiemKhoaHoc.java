/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ThongKe_TongHop;

// <editor-fold defaultstate="collapsed" desc="IMPORT ">
import DAO.KhoaHocDAO;
import HeThong.DangNhapFrame;
import Helper.JdbcHelper;
import Model.BangDiem;
import Model.KhoaHoc;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.streaming.SXSSFCell;
import org.apache.poi.xssf.streaming.SXSSFRow;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
// </editor-fold>

/**
 *
 * @author MSI
 */
public class BangDiemKhoaHoc extends JPanel {

    JLabel lblTitle;
    JComboBox<KhoaHoc> cbxKhoaHoc;

    JTable tblBangDiemKhoaHoc;
    DefaultTableModel model;
    JScrollPane scTable;

    JdbcHelper jdbcHelper = new JdbcHelper();
    KhoaHocDAO khoaHocDAO = new KhoaHocDAO();

    JButton btnExport;
    
    public static boolean doneLoad = false;

    public BangDiemKhoaHoc() {
        addControls();
        addEvent();
    }

    // <editor-fold defaultstate="collapsed" desc="ADD CONTROLS ">
    private void addControls() {
        JPanel pnSearch = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 20));
        pnSearch.setPreferredSize(new Dimension(0, 60));
        lblTitle = new JLabel("Khóa học:");
        lblTitle.setPreferredSize(new Dimension(120, 30));

        cbxKhoaHoc = new JComboBox();
        cbxKhoaHoc.setPreferredSize(new Dimension(320, 30));

        lblTitle.setHorizontalAlignment(SwingConstants.CENTER);
        lblTitle.setFont(new Font("Segoe UI", 0, 18));

        pnSearch.add(lblTitle);
        pnSearch.add(cbxKhoaHoc);

        JPanel pnMain = new JPanel(new BorderLayout(0, 10));
        model = new DefaultTableModel();
        model.setColumnIdentifiers(new String[]{
            "STT", "MÃ HỌC VIÊN", "HỌ VÀ TÊN", "ĐIỂM", "XẾP LOẠI"
        });
        tblBangDiemKhoaHoc = new JTable(model) {
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
        tblBangDiemKhoaHoc.setAutoCreateRowSorter(true);
        scTable = new JScrollPane(tblBangDiemKhoaHoc, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

        //Set độ rộng cột
        JTableHeader header = tblBangDiemKhoaHoc.getTableHeader();
        ((DefaultTableCellRenderer) header.getDefaultRenderer()).setHorizontalAlignment(JLabel.CENTER);
        tblBangDiemKhoaHoc.getColumnModel().getColumn(0).setPreferredWidth(90);
        tblBangDiemKhoaHoc.getColumnModel().getColumn(1).setPreferredWidth(240);
        tblBangDiemKhoaHoc.getColumnModel().getColumn(2).setPreferredWidth(465);
        tblBangDiemKhoaHoc.getColumnModel().getColumn(3).setPreferredWidth(160);
        tblBangDiemKhoaHoc.getColumnModel().getColumn(4).setPreferredWidth(160);

        tblBangDiemKhoaHoc.setRowHeight(30);

        tblBangDiemKhoaHoc.setSelectionBackground(Color.decode("#3a4d8f"));
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
        for (int i = 0; i < tblBangDiemKhoaHoc.getColumnCount(); i++) {
            tblBangDiemKhoaHoc.setDefaultRenderer(tblBangDiemKhoaHoc.getColumnClass(i), renderer);
        }

        JPanel pnButton = new JPanel(new FlowLayout(FlowLayout.RIGHT));

        btnExport = new JButton("Xuất file Excel");
        pnButton.add(btnExport);

        pnMain.add(scTable, BorderLayout.CENTER);
        pnMain.add(pnSearch, BorderLayout.NORTH);
        pnMain.add(pnButton, BorderLayout.SOUTH);

        this.setLayout(new BorderLayout());
        this.add(pnMain, BorderLayout.CENTER);
    }
    // </editor-fold>

    private void addEvent() {
        loadDataToCbxKhoaHoc();

        //Load data mặc định
        if (cbxKhoaHoc.getItemCount() > 0) {
            int maKH = cbxKhoaHoc.getItemAt(0).getId();
            loadDataToTbl(maKH);
        }
        
        doneLoad    = true;

        // <editor-fold defaultstate="collapsed" desc="Sự kiện thay đổi khóa học ">
        cbxKhoaHoc.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    KhoaHoc kh = (KhoaHoc) cbxKhoaHoc.getSelectedItem();
                    int maKH = kh.getId();
                    loadDataToTbl(maKH);
                }
            }
        });
        // </editor-fold>

        // <editor-fold defaultstate="collapsed" desc="Sự kiện btnExport ">
        btnExport.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                export();
            }
        });
        // </editor-fold>
    }

    // <editor-fold defaultstate="collapsed" desc="LẤY DANH SÁCH KHÓA HỌC ">
    private List<KhoaHoc> getListKhoaHoc() {
        List<KhoaHoc> listKhoaHoc = new ArrayList<>();

        String sql = "SELECT * FROM dbo.KhoaHoc ORDER BY ngayKG DESC";

        try {
            ResultSet rs = jdbcHelper.executeQuery(sql);

            while (rs.next()) {
                listKhoaHoc.add(khoaHocDAO.readResultSet(rs));
            }
        } catch (SQLException ex) {
            Logger.getLogger(BangDiemKhoaHoc.class.getName()).log(Level.SEVERE, null, ex);
        }

        return listKhoaHoc;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="LOAD DỮ LIỆU VÀO cbxKhoaHoc ">
    private void loadDataToCbxKhoaHoc() {
        List<KhoaHoc> listKhoaHoc = getListKhoaHoc();

        for (KhoaHoc khoaHoc : listKhoaHoc) {
            cbxKhoaHoc.addItem(khoaHoc);
        }
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="LẤY DANH SÁCH BẢNG ĐIỂM THEO KHÓA HỌC ">
    private List<Object[]> getBangDiemTheoKhoaHoc(int maKH) {
        List<Object[]> listBangDiem = new ArrayList<>();

        String sql = "{call sp_bangDiem (?)}";

        try {
            ResultSet rs = jdbcHelper.executeQuery(sql, maKH);

            while (rs.next()) {
                listBangDiem.add(readRs(rs));
            }
        } catch (SQLException ex) {
            Logger.getLogger(BangDiemKhoaHoc.class.getName()).log(Level.SEVERE, null, ex);
        }

        return listBangDiem;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="ĐỌC DỮ LIỆU TỪ RS ">
    private Object[] readRs(ResultSet rs) throws SQLException {
        int stt = rs.getInt(1);
        String maHV = rs.getString(2);
        String tenHV = rs.getString(3);
        String diem = "";
        String xepLoai = "";
        if (rs.getDouble(4) < 0) {
            diem = "-";
            xepLoai = "-";
        } else {
            diem = rs.getString(4);
            xepLoai = rs.getString(5);
        }

        return new Object[]{stt, maHV, tenHV, diem, xepLoai};
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="LOAD DỮ LIỆU LÊN TABLE ">
    private void loadDataToTbl(int maKH) {
        List<Object[]> listBangDiem = getBangDiemTheoKhoaHoc(maKH);

        model.setRowCount(0);

        for (Object[] obj : listBangDiem) {
            model.addRow(obj);
        }
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="REFRESH DỮ LIỆU ">
    public void refresh() {
        loadDataToCbxKhoaHoc();
        int maKH = (int)((KhoaHoc)cbxKhoaHoc.getSelectedItem()).getId();
        loadDataToTbl(maKH);
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="EXPORT EXCEL ">
    private void exportExcel(File file) throws IOException {
        SXSSFWorkbook workbook = new SXSSFWorkbook();
        SXSSFSheet sheet = workbook.createSheet();

        sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 4));
        sheet.addMergedRegion(new CellRangeAddress(1, 1, 0, 4));
        sheet.addMergedRegion(new CellRangeAddress(5, 5, 0, 8));
        sheet.addMergedRegion(new CellRangeAddress(6, 6, 0, 8));
        sheet.addMergedRegion(new CellRangeAddress(9, 9, 1, 2));
        sheet.addMergedRegion(new CellRangeAddress(9, 9, 3, 5));
        sheet.addMergedRegion(new CellRangeAddress(9, 9, 7, 8));

        writeHeader(sheet);

        writeContent(sheet);

        FileOutputStream fos = new FileOutputStream(file);
        workbook.write(fos);

        fos.close();

        Desktop desktop = Desktop.getDesktop();
        if (file.exists()) {
            desktop.open(file);
        }
    }

    // <editor-fold defaultstate="collapsed" desc="WRITE HEADER ">
    private void writeHeader(SXSSFSheet sheet) throws FileNotFoundException, IOException {
        SXSSFRow row;
        SXSSFCell cell;

        row = sheet.createRow(0);
        cell = row.createCell(0);
        cell.setCellStyle(createCellStyle1(sheet));
        cell.setCellValue("HỆ THỐNG QUẢN LÝ KHÓA HỌC");

        row = sheet.createRow(1);
        cell = row.createCell(0);
        cell.setCellStyle(createCellStyle1(sheet));
        cell.setCellValue("NHÓM APT");

        row = sheet.createRow(5);
        cell = row.createCell(0);
        cell.setCellStyle(createCellStyle1(sheet));
        cell.setCellValue("TỔNG HỢP BẢNG ĐIỂM KHÓA HỌC ");

        row = sheet.createRow(6);
        cell = row.createCell(0);
        cell.setCellStyle(createCellStyle1(sheet));
        cell.setCellValue((cbxKhoaHoc.getSelectedItem()).toString().toUpperCase());

        row = sheet.createRow(9);
        cell = row.createCell(0);
        cell.setCellStyle(createCellStyle2(sheet));
        cell.setCellValue("STT");

        cell = row.createCell(1);
        cell.setCellStyle(createCellStyle2(sheet));
        cell.setCellValue("MÃ HỌC VIÊN");

        cell = row.createCell(2);
        cell.setCellStyle(createCellStyle2(sheet));

        cell = row.createCell(3);
        cell.setCellStyle(createCellStyle2(sheet));
        cell.setCellValue("HỌ VÀ TÊN");

        cell = row.createCell(4);
        cell.setCellStyle(createCellStyle2(sheet));

        cell = row.createCell(5);
        cell.setCellStyle(createCellStyle2(sheet));

        cell = row.createCell(6);
        cell.setCellStyle(createCellStyle2(sheet));
        cell.setCellValue("ĐIỂM");

        cell = row.createCell(7);
        cell.setCellStyle(createCellStyle2(sheet));
        cell.setCellValue("XẾP LOẠI");

        cell = row.createCell(8);
        cell.setCellStyle(createCellStyle2(sheet));

    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="WRITE CONTENT ">
    private void writeContent(SXSSFSheet sheet) {
        KhoaHoc kh = (KhoaHoc) cbxKhoaHoc.getSelectedItem();
        int maKH = kh.getId();
        List<Object[]> listBangDiem = getBangDiemTheoKhoaHoc(maKH);

        int indexRow = 10;

        SXSSFRow row;
        SXSSFCell cell;

        for (Object[] obj : listBangDiem) {
            sheet.addMergedRegion(new CellRangeAddress(indexRow, indexRow, 1, 2));
            sheet.addMergedRegion(new CellRangeAddress(indexRow, indexRow, 3, 5));
            sheet.addMergedRegion(new CellRangeAddress(indexRow, indexRow, 7, 8));

            row = sheet.createRow(indexRow);

            //STT
            cell = row.createCell(0, CellType.NUMERIC);
            cell.setCellStyle(createCellStyle3(sheet));
            cell.setCellValue((int) obj[0]);

            cell = row.createCell(1, CellType.STRING);
            cell.setCellStyle(createCellStyle3(sheet));
            cell.setCellValue(String.valueOf(obj[1]));

            cell = row.createCell(2);
            cell.setCellStyle(createCellStyle3(sheet));

            cell = row.createCell(3, CellType.STRING);
            cell.setCellStyle(createCellStyle3(sheet));
            cell.setCellValue(String.valueOf(obj[2]));

            cell = row.createCell(4);
            cell.setCellStyle(createCellStyle3(sheet));

            cell = row.createCell(5);
            cell.setCellStyle(createCellStyle3(sheet));

            if (String.valueOf(obj[3]).equals("-")) {
                cell = row.createCell(6, CellType.STRING);
                cell.setCellStyle(createCellStyle3(sheet));
                cell.setCellValue(String.valueOf(obj[3]));
            } else {
                cell = row.createCell(6, CellType.NUMERIC);
                cell.setCellStyle(createCellStyle3(sheet));
                cell.setCellValue(Double.parseDouble((String) obj[3]));
            }

            cell = row.createCell(7, CellType.STRING);
            cell.setCellStyle(createCellStyle3(sheet));
            cell.setCellValue(String.valueOf(obj[4]));

            cell = row.createCell(8);
            cell.setCellStyle(createCellStyle3(sheet));

            indexRow++;
        }
        
        sheet.addMergedRegion(new CellRangeAddress(indexRow+1, indexRow+1, 5, 7));
        row     = sheet.createRow(indexRow+1);
        cell    = row.createCell(5, CellType.STRING);
        cell.setCellValue("NGƯỜI TỔNG HỢP");
        cell.setCellStyle(createCellStyle1(sheet));
        
        sheet.addMergedRegion(new CellRangeAddress(indexRow+7, indexRow+7, 5, 7));
        row     = sheet.createRow(indexRow+7);
        cell    = row.createCell(5, CellType.STRING);
        cell.setCellValue(DangNhapFrame.nvLogin.getHoTen());
        cell.setCellStyle(createCellStyle1(sheet));
    }

    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="AUTO RESIZE ">
    private static void autosizeColumn(SXSSFSheet sheet, int lastColumn) {
        for (int columnIndex = 0; columnIndex < lastColumn; columnIndex++) {
            sheet.autoSizeColumn(columnIndex);
        }
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="CREATE CELLSTYLE1 ">
    private CellStyle createCellStyle1(SXSSFSheet sheet) {
        org.apache.poi.ss.usermodel.Font font = sheet.getWorkbook().createFont();
        font.setFontName("Times New Roman");
        font.setBold(true);
        font.setFontHeightInPoints((short) 13); // font size
        font.setColor(IndexedColors.BLACK.getIndex()); // text color

        // Create CellStyle
        CellStyle cellStyle = sheet.getWorkbook().createCellStyle();
        cellStyle.setFont(font);
        cellStyle.setAlignment(HorizontalAlignment.CENTER);
        return cellStyle;
    }

    // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="CREATE CELLSTYLE2 ">
    private CellStyle createCellStyle2(SXSSFSheet sheet) {
        org.apache.poi.ss.usermodel.Font font = sheet.getWorkbook().createFont();
        font.setFontName("Times New Roman");
        font.setBold(true);
        font.setFontHeightInPoints((short) 11); // font size
        font.setColor(IndexedColors.BLACK.getIndex()); // text color

        // Create CellStyle
        CellStyle cellStyle = sheet.getWorkbook().createCellStyle();
        cellStyle.setFont(font);
        cellStyle.setBorderBottom(BorderStyle.THIN);
        cellStyle.setBorderLeft(BorderStyle.THIN);
        cellStyle.setBorderRight(BorderStyle.THIN);
        cellStyle.setBorderTop(BorderStyle.THIN);
        cellStyle.setAlignment(HorizontalAlignment.CENTER);
        return cellStyle;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc=" DESCRIPTION ">
    private CellStyle createCellStyle3(SXSSFSheet sheet) {
        org.apache.poi.ss.usermodel.Font font = sheet.getWorkbook().createFont();
        font.setFontName("Times New Roman");
        font.setFontHeightInPoints((short) 11); // font size
        font.setColor(IndexedColors.BLACK.getIndex()); // text color

        // Create CellStyle
        CellStyle cellStyle = sheet.getWorkbook().createCellStyle();
        cellStyle.setFont(font);
        cellStyle.setBorderBottom(BorderStyle.THIN);
        cellStyle.setBorderLeft(BorderStyle.THIN);
        cellStyle.setBorderRight(BorderStyle.THIN);
        cellStyle.setBorderTop(BorderStyle.THIN);
        cellStyle.setAlignment(HorizontalAlignment.CENTER);
        return cellStyle;
    }
    // </editor-fold>
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="METHOD CHO btnExport">
    private void export() {
        if (tblBangDiemKhoaHoc.getRowCount() == 0) {
            JOptionPane.showMessageDialog(null, "Chưa có dữ liệu để xuất ra file Excel. Vui lòng kiểm tra lại!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

        int i = fileChooser.showSaveDialog(null);
        
        String path = fileChooser.getSelectedFile()+ "\\Tổng hợp bảng điểm khóa học " + cbxKhoaHoc.getSelectedItem().toString() + "_" + System.currentTimeMillis() + ".xlsx";
        if (i == fileChooser.APPROVE_OPTION) {
            try {
                File file = new File(path);
                
                exportExcel(file);
            } catch (IOException ex) {
                Logger.getLogger(BangDiemKhoaHoc.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    // </editor-fold>
}
