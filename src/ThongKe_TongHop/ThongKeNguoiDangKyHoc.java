/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ThongKe_TongHop;

// <editor-fold defaultstate="collapsed" desc="IMPORT ">
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
import java.util.Vector;
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
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableRowSorter;
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
 * @author phuoc.bleach
 */
public class ThongKeNguoiDangKyHoc extends JPanel{
    JdbcHelper jdbcHelper   = new JdbcHelper();
    
    JButton btnExport;
    
    int numberFile = 1;
    
    public static boolean doneLoad = false;
    
    //<editor-fold defaultstate="collapsed" desc="Component">
    JLabel lblNam;
    JComboBox cbxNam;
    DefaultTableModel model;
    JTable tblTKNDH;
    JScrollPane scTable;
    JTableHeader tblHeader;
    //</editor-fold>
    
    public ThongKeNguoiDangKyHoc() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
             init();
            addControls();
            addEvents();
            showWindow();
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex) {
            Logger.getLogger(ThongKeNguoiDangKyHoc.class.getName()).log(Level.SEVERE, null, ex);
        }
       
    }
    
    // <editor-fold defaultstate="collapsed" desc="INIT ">
    private void init() {
        lblNam          = new JLabel("Năm học");
        cbxNam          = new JComboBox();
        cbxNam.setPreferredSize(new Dimension(100, 25));
        ((JLabel)cbxNam.getRenderer()).setHorizontalAlignment(JLabel.CENTER);
        
        model           = new DefaultTableModel();
        model.setColumnIdentifiers(new Object[]{
           "STT", "THÁNG", "SỐ LƯỢNG SINH VIÊN"
        });
        tblTKNDH        = new JTable(model){
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
            
        };
        scTable         = new JScrollPane(tblTKNDH, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        tblHeader = tblTKNDH.getTableHeader();
        ((DefaultTableCellRenderer)tblHeader.getDefaultRenderer()).setHorizontalAlignment(JLabel.CENTER);
        tblTKNDH.setRowSorter(new TableRowSorter(model));
        tblTKNDH.setAutoCreateRowSorter(true);
        
        tblTKNDH.setRowHeight(30);
        
        tblTKNDH.setSelectionBackground(Color.decode("#3a4d8f"));
        //căn giữa nội dung table
        DefaultTableCellRenderer renderer   = new DefaultTableCellRenderer(){
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                final Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                if (!isSelected) {
                    c.setBackground(row % 2 == 0 ? Color.decode("#e7e7e7") : Color.WHITE);
                }
                return c;
            }
        };
        renderer.setHorizontalAlignment((int) JTable.CENTER_ALIGNMENT);
        for (int i = 0; i < tblTKNDH.getColumnCount()-1; i++) {
            tblTKNDH.setDefaultRenderer(tblTKNDH.getColumnClass(i), renderer);
        }
        
        tblTKNDH.getColumnModel().getColumn(0).setPreferredWidth(90);
        tblTKNDH.getColumnModel().getColumn(1).setPreferredWidth(610);
        tblTKNDH.getColumnModel().getColumn(2).setPreferredWidth(460);
        
        lblNam.setFont(new Font("Segoe UI", 0, 16));
        cbxNam.setFont(new Font("Segoe UI", 0, 14));
        scTable.setFont(new Font("Segoe UI", 0, 14));
        
    }
// </editor-fold>
    
    
    private void addControls() {
        
        //<editor-fold defaultstate="collapsed" desc="Header">
        JPanel pnHeader     = new JPanel(new FlowLayout(FlowLayout.CENTER, 30, 10));
        pnHeader.add(lblNam);
        pnHeader.add(cbxNam);
//        pnHeader.setBorder(new TitledBorder(BorderFactory.createLineBorder(Color.BLACK), "Lọc", 1, 2, new Font("Segor UI", 1, 18)));
        //</editor-fold>
        
        //<editor-fold defaultstate="collapsed" desc="Content">
        JPanel pnContent    = new JPanel(new BorderLayout());
        pnContent.add(scTable);
        //</editor-fold>
        
        JPanel pnButton     = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnExport           = new JButton("Xuất file Excel");
        pnButton.add(btnExport);
        
        //<editor-fold defaultstate="collapsed" desc="Container">
        this.setLayout(new BorderLayout(0, 10));
        this.add(pnHeader, BorderLayout.NORTH);
        this.add(pnContent, BorderLayout.CENTER);
        this.add(pnButton, BorderLayout.SOUTH);
        //</editor-fold>
        
    }
    
    private void showWindow() {
        this.setSize(900, 600);
    }

    private void addEvents() {
        //load data to cbxYear
        loadDataToCbxYear();
        
        //load dữ liệu thống kê mặc định
        if (cbxNam.getItemCount() > 0) {
            loadDataToTbl(getListThongKe((int) cbxNam.getItemAt(0)));
        }
        
        doneLoad    = true;
        
        // <editor-fold defaultstate="collapsed" desc="Sự kiện thay đổi năm cbxYear ">
        cbxNam.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    int year = (int) cbxNam.getSelectedItem();
                    loadDataToTbl(getListThongKe(year));
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
    
    // <editor-fold defaultstate="collapsed" desc="GET LIST NĂM HỌC VIÊN ĐĂNG KÝ ">
    private List<Integer> getListYear(){
        List<Integer> listYear  = new ArrayList<>();
        String sql              = "SELECT DISTINCT YEAR(ngayDK) FROM dbo.HocVien ORDER BY YEAR(ngayDK) DESC";
        
        try {
            ResultSet rs            = jdbcHelper.executeQuery(sql);
            
            while (rs.next()) {                
                listYear.add(rs.getInt(1));
            }
        } catch (SQLException ex) {
            Logger.getLogger(ThongKeNguoiDangKyHoc.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return listYear;
    }
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="LAOD DỮ LIỆU VÀO cbxYear ">
    private void loadDataToCbxYear(){
        cbxNam.removeAllItems();
        List<Integer> listYear = getListYear();
        
        for (Integer integer : listYear) {
            cbxNam.addItem(integer);
        }
    }
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="GET LIST THỐNG KÊ NGƯỜI ĐĂNG KÝ HỌC ">
    private List<Object[]> getListThongKe(int year){
        List<Object[]> listThongKe    = new ArrayList<>();
        
        String sql = "{call sp_thongKeNguoiDangKyHoc (?) }";
        
        try {
            ResultSet rs = jdbcHelper.executeQuery(sql, year);
            
            while (rs.next()) {                
                listThongKe.add(readRs(rs));
            }
        } catch (SQLException ex) {
            Logger.getLogger(ThongKeNguoiDangKyHoc.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return listThongKe;
    }
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="ĐỌC DỮ LIỆU TỪ RS ">
    private Object[] readRs(ResultSet rs) throws SQLException{
        return new Object[]{rs.getInt(1),rs.getInt(2),rs.getInt(3)};
    }
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="LOAD DỮ LIỆU VÀO TABLE ">
    private void loadDataToTbl(List<Object[]> listThongKe){
        model.setRowCount(0);
        for (Object[] object : listThongKe) {
            model.addRow(object);
        }
    }
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="REFRESH DỮ LIỆU ">
    public void refresh(){
        loadDataToCbxYear();
        loadDataToTbl(getListThongKe((int) cbxNam.getItemAt(0)));
    }
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="EXPORT EXCEL ">
    private void exportExcel(File file) throws IOException {
        SXSSFWorkbook workbook = new SXSSFWorkbook();
        SXSSFSheet sheet = workbook.createSheet();

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
        sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 4));
        sheet.addMergedRegion(new CellRangeAddress(1, 1, 0, 4));
        sheet.addMergedRegion(new CellRangeAddress(5, 5, 0, 8));
        sheet.addMergedRegion(new CellRangeAddress(6, 6, 0, 8));
        sheet.addMergedRegion(new CellRangeAddress(9, 9, 1, 4));
        sheet.addMergedRegion(new CellRangeAddress(9, 9, 5, 8));
        
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
        cell.setCellValue("THỐNG KÊ NGƯỜI ĐĂNG KÝ HỌC NĂM " + cbxNam.getSelectedItem());

        row = sheet.createRow(9);
        cell = row.createCell(0);
        cell.setCellStyle(createCellStyle2(sheet));
        cell.setCellValue("STT");

        cell = row.createCell(1);
        cell.setCellStyle(createCellStyle2(sheet));
        cell.setCellValue("THÁNG");

        cell = row.createCell(2);
        cell.setCellStyle(createCellStyle2(sheet));

        cell = row.createCell(3);
        cell.setCellStyle(createCellStyle2(sheet));

        cell = row.createCell(4);
        cell.setCellStyle(createCellStyle2(sheet));

        cell = row.createCell(5);
        cell.setCellStyle(createCellStyle2(sheet));
        cell.setCellValue("SỐ LƯỢNG HỌC VIÊN");

        cell = row.createCell(6);
        cell.setCellStyle(createCellStyle2(sheet));

        cell = row.createCell(7);
        cell.setCellStyle(createCellStyle2(sheet));

        cell = row.createCell(8);
        cell.setCellStyle(createCellStyle2(sheet));

    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="WRITE CONTENT ">
    private void writeContent(SXSSFSheet sheet) {
        int year = (int) cbxNam.getSelectedItem();
        List<Object[]> listThongKe = getListThongKe(year);

        int indexRow = 10;

        SXSSFRow row;
        SXSSFCell cell;

        for (Object[] obj : listThongKe) {
            sheet.addMergedRegion(new CellRangeAddress(indexRow, indexRow, 1, 4));
            sheet.addMergedRegion(new CellRangeAddress(indexRow, indexRow, 5, 8));

            row = sheet.createRow(indexRow);
            
            cell = row.createCell(0,CellType.NUMERIC);
            cell.setCellStyle(createCellStyle3(sheet));
            cell.setCellValue((int) obj[0]);

            cell = row.createCell(1);
            cell.setCellStyle(createCellStyle3(sheet));
            cell.setCellValue((int) obj[1]);

            cell = row.createCell(2);
            cell.setCellStyle(createCellStyle3(sheet));

            cell = row.createCell(3);
            cell.setCellStyle(createCellStyle3(sheet));

            cell = row.createCell(4);
            cell.setCellStyle(createCellStyle3(sheet));

            cell = row.createCell(5);
            cell.setCellStyle(createCellStyle3(sheet));
            cell.setCellValue((int) obj[2]);

            cell = row.createCell(6);
            cell.setCellStyle(createCellStyle3(sheet));

            cell = row.createCell(7);
            cell.setCellStyle(createCellStyle3(sheet));

            cell = row.createCell(8);
            cell.setCellStyle(createCellStyle3(sheet));

            indexRow++;
        }
        
        sheet.addMergedRegion(new CellRangeAddress(indexRow+1, indexRow+1, 5, 7));
        row     = sheet.createRow(indexRow+1);
        cell    = row.createCell(5, CellType.STRING);
        cell.setCellValue("NGƯỜI THỐNG KÊ");
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

    // <editor-fold defaultstate="collapsed" desc="CREATE CELLSTYLE3 ">
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
        if (tblTKNDH.getRowCount() == 0) {
            JOptionPane.showMessageDialog(null, "Chưa có dữ liệu để xuất ra file Excel. Vui lòng kiểm tra lại!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            return;
        }
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        int i = fileChooser.showSaveDialog(null);

        String path = fileChooser.getSelectedFile() + "\\Thống kê người đăng ký học năm " + cbxNam.getSelectedItem() + "_" + System.currentTimeMillis() + " .xlsx";

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
