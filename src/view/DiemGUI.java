package view;

import com.toedter.calendar.JDateChooser;
import com.toedter.calendar.JTextFieldDateEditor;
import dao.ConnectDatabase;
import dao.DiemDAO;
import entity.Diem;
import entity.KetQua;
import org.jdesktop.swingx.autocomplete.AutoCompleteDecorator;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.print.PrinterException;
import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;

import static dao.ConnectDatabase.con;
import static dao.ConnectDatabase.getConnection;

public class DiemGUI extends JFrame {

    public static JPanel contentPane;

    private JTextField txtDiem_Ma;
    private JTextField txtDiem_Ten;
    private JTextField txtDiem_Toan;
    private JTextField txtDiem_Van;
    private JTextField txtDiem_Anh;
    private JTextField txtDiem_TongDiem;
    private JTextField txtDiem_KetQua;
    private JTextField txtDiem_GhiChu;
    private JTable tblDiem;
    private JComboBox cmbTim;
    private JTextArea jTDetail;

    private DiemDAO dao = new DiemDAO();
    private DefaultTableModel tbModel;
    private JRadioButton radMa, radTenHS, radKetQua;
    private DefaultComboBoxModel cboModeTim = new DefaultComboBoxModel();
    private DefaultComboBoxModel cboModeMa = new DefaultComboBoxModel();
    private DefaultComboBoxModel cboModeTenHS = new DefaultComboBoxModel();
    private DefaultComboBoxModel cboModeTimKetQua = new DefaultComboBoxModel();

    private JButton btnLamMoi;
    private List<String> listMa = new ArrayList<>();
    private List<String> listTen = new ArrayList<>();
    private List<String> listTimKetQua = new ArrayList<>();

    public static JButton btnThem;
    public static JPanel pnlChucNang;
    public static JButton btnLuu;
    public static JButton btnSua;
    private int trangThaiThem;
    private int trangThaiSua;
    private static JButton btnIn;
    private static  JButton btnXoa;
    private  static JButton btnChat;

    Connection conn;
    PreparedStatement insert;
    Vector vdata = new Vector();
    Vector vtile = new Vector();
    DefaultTableModel tableModel = new DefaultTableModel(vdata, vtile);

    /**
     * Chạy thử chương trình
     * @param args
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    DiemGUI frame = new DiemGUI();
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * Tạo giao diện quản lý điểm
     */
    public DiemGUI() {
        trangThaiThem = 0;
        trangThaiSua = 0;

        // Thuộc tính của JFrame
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(true);
        setSize(1200,800);
        setExtendedState(JFrame.MAXIMIZED_BOTH);

        // Tạo JPanel chính
        contentPane = new JPanel();
        contentPane.setBorder(BorderFactory.createEmptyBorder(25,25,25,25));
        contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.Y_AXIS));
        setContentPane(contentPane);

        // chứa thông tin in
        jTDetail = new JTextArea();
        contentPane.add(jTDetail);

        // JPanel chứa tiêu đề
        JPanel pnlTieuDe = new JPanel();
        pnlTieuDe.setBackground(SystemColor.controlHighlight);
        pnlTieuDe.setAlignmentX(Component.CENTER_ALIGNMENT);
        contentPane.add(pnlTieuDe);

        JLabel lblTieuDe = new JLabel("Quản Lý Điểm");
        lblTieuDe.setFont(new Font("Open Sans", Font.BOLD, 25));
        lblTieuDe.setForeground(new Color(228, 123, 77));
        lblTieuDe.setHorizontalAlignment(SwingConstants.CENTER);
        pnlTieuDe.add(lblTieuDe);

        // JPanel chứa nội dung quản lý và thông tin điểm
        JPanel mainPane = new JPanel();
        mainPane.setBackground(SystemColor.controlHighlight);
        mainPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        mainPane.setLayout(new GridLayout(1,2,1,1));
        contentPane.add(mainPane);

        JPanel pnlNoiDung = new JPanel();
        pnlNoiDung.setBackground(SystemColor.controlHighlight);
        pnlNoiDung.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"),
                "Nội dung quản lý", TitledBorder.LEADING, TitledBorder.TOP, null,
                new Color(228, 123, 77)));
        pnlNoiDung.setLayout(new GridBagLayout());
        mainPane.add(pnlNoiDung);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        gbc.insets = new Insets(10,10,10,10);
        gbc.anchor = GridBagConstraints.WEST;


        
        JLabel lblDiem_Ma = new JLabel("Mã học sinh:");
       lblDiem_Ma.setForeground(new Color(228, 123, 77));
        lblDiem_Ma.setFont(new Font("Open Sans", Font.PLAIN, 18));
        pnlNoiDung.add(lblDiem_Ma,gbc);

        txtDiem_Ma = new JTextField(10);
        txtDiem_Ma.setFont(new Font("Open Sans", Font.PLAIN, 16));
        txtDiem_Ma.setEditable(false);
        gbc.gridx = 1;
        pnlNoiDung.add(txtDiem_Ma,gbc);

        JLabel lblTen = new JLabel("Tên học sinh:\r\n");
        lblTen.setForeground(new Color(228, 123, 77));
        lblTen.setFont(new Font("Open Sans", Font.PLAIN, 18));
        gbc.gridx = 2;
        gbc.gridwidth = 1;
        pnlNoiDung.add(lblTen,gbc);

        txtDiem_Ten = new JTextField(10);
        txtDiem_Ten.setFont(new Font("Open Sans", Font.PLAIN, 16));
        txtDiem_Ten.setEditable(false);
        gbc.gridx = 3;
        gbc.gridwidth = 3;
        pnlNoiDung.add(txtDiem_Ten,gbc);

        JLabel lblDiem_Toan = new JLabel("Toán:\r\n");
        lblDiem_Toan.setForeground(new Color(228, 123, 77));
        lblDiem_Toan.setFont(new Font("Open Sans", Font.PLAIN, 18));
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        pnlNoiDung.add(lblDiem_Toan,gbc);

        txtDiem_Toan = new JTextField(10);
        txtDiem_Toan.setFont(new Font("Open Sans", Font.PLAIN, 16));
        gbc.gridx = 1;
        pnlNoiDung.add(txtDiem_Toan,gbc);

        JLabel lblDiem_Van = new JLabel("Văn:\r\n");
        lblDiem_Van.setForeground(new Color(228, 123, 77));
        lblDiem_Van.setFont(new Font("Open Sans", Font.PLAIN, 18));
        gbc.gridx = 2;
        pnlNoiDung.add(lblDiem_Van,gbc);

        txtDiem_Van = new JTextField(10);
        txtDiem_Van.setFont(new Font("Open Sans", Font.PLAIN, 16));
        gbc.gridx = 3;
        pnlNoiDung.add(txtDiem_Van,gbc);

        JLabel lblDiem_Anh = new JLabel("Anh:\r\n");
        lblDiem_Anh.setForeground(new Color(228, 123, 77));
        lblDiem_Anh.setFont(new Font("Open Sans", Font.PLAIN, 18));
        gbc.gridx = 4;
        pnlNoiDung.add(lblDiem_Anh,gbc);

        txtDiem_Anh = new JTextField(10);
        txtDiem_Anh.setFont(new Font("Open Sans", Font.PLAIN, 16));
        gbc.gridx = 5;
        pnlNoiDung.add(txtDiem_Anh,gbc);

        JLabel lblDiem_TongDiem = new JLabel("Tổng điểm:\r\n");
        lblDiem_TongDiem.setForeground(new Color(228, 123, 77));
        lblDiem_TongDiem.setFont(new Font("Open Sans", Font.PLAIN, 18));
        gbc.gridx = 6;
        pnlNoiDung.add(lblDiem_TongDiem,gbc);

        txtDiem_TongDiem = new JTextField(10);
        txtDiem_TongDiem.setFont(new Font("Open Sans", Font.PLAIN, 16));
        gbc.gridx = 7;
        pnlNoiDung.add(txtDiem_TongDiem,gbc);

        JLabel lblDiem_KetQua = new JLabel("Kết quả:\r\n");
        lblDiem_KetQua.setForeground(new Color(228, 123, 77));
        lblDiem_KetQua.setFont(new Font("Open Sans", Font.PLAIN, 18));
        gbc.gridx = 0;
        gbc.gridy = 2;
        pnlNoiDung.add(lblDiem_KetQua,gbc);

        txtDiem_KetQua = new JTextField(10);
        txtDiem_KetQua.setFont(new Font("Open Sans", Font.PLAIN, 16));
        gbc.gridx = 1;
        pnlNoiDung.add(txtDiem_KetQua,gbc);

        JLabel lblDiem_GhiChu = new JLabel("Ghi chú:\r\n");
        lblDiem_GhiChu.setForeground(new Color(228, 123, 77));
        lblDiem_GhiChu.setFont(new Font("Open Sans", Font.PLAIN, 18));
        gbc.gridx = 2;
        pnlNoiDung.add(lblDiem_GhiChu,gbc);

        txtDiem_GhiChu = new JTextField(10);
        txtDiem_GhiChu.setFont(new Font("Open Sans", Font.PLAIN, 16));
        gbc.gridx = 3;
        pnlNoiDung.add(txtDiem_GhiChu,gbc);

        JPanel pnlBang = new JPanel();
        pnlBang.setBackground(SystemColor.controlHighlight);
        pnlBang.setBorder(new EmptyBorder(25,25,25,25));
        pnlBang.setLayout(new BorderLayout());


        JScrollPane scroll = new JScrollPane();
        scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        scroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
        pnlBang.add(scroll);

        String[] tb = {
               "STT", "Mã Học Sinh", "Tên Học Sinh", "Điểm Toán", "Điểm Văn", "Điểm Anh",
                "Tổng Điểm", "Kết Quả", "Ghi Chú"
        };

        tbModel = new DefaultTableModel(tb,0);

        tblDiem = new JTable(tbModel);
        tblDiem.setFont(new Font("Open Sans", Font.PLAIN, 14));
        tblDiem.setDefaultEditor(Object.class, null);
//        tblDiem.setFillsViewportHeight(true);
        tblDiem.setBackground(Color.WHITE);
        scroll.setViewportView(tblDiem);
       getContentPane().add(scroll);

        tblDiem.addMouseListener(new MouseListener() {
//in dữ liêu
            @Override
            public void mouseReleased(MouseEvent e) {
                // TODO Auto-generated method stub
                try {
                    hienTable();
                    int id = tblDiem.rowAtPoint(e.getPoint());

                    jTDetail.setEditable(false);
                    jTDetail.setText(" ");
                    System.out.print( tbModel.getValueAt(id, 1).toString());
                    txtDiem_Ma.setText(tbModel.getValueAt(id, 1).toString());
                    jTDetail.append(
                            "\t\n\t THÔNG TIN HỌC SINH \n"+

                                    "\nMÃ :\t\t" + tbModel.getValueAt(id, 1).toString()+
                                    "\nHỌ TÊN:\t\t" + tbModel.getValueAt(id, 2).toString()+
                                    "\nĐIỂM TOÁN:\t\t" + tbModel.getValueAt(id, 3).toString()+
                                    "\nĐIỂM VĂN\t\t" + tbModel.getValueAt(id, 4).toString()+
                                    "\nĐIỂM ANH:\t\t" + tbModel.getValueAt(id, 5).toString()+
                                    "\nTỔNG ĐIỂM :\t\t" + tbModel.getValueAt(id, 6).toString()+
                                    "\nKẾT QUẢ\t\t" + tbModel.getValueAt(id, 7).toString()) ;

                } catch (Exception e2) {
                    // TODO: handle exception
                }


            }


            @Override
            public void mousePressed(MouseEvent e) {
                // TODO Auto-generated method stub

            }

            @Override
            public void mouseExited(MouseEvent e) {
                // TODO Auto-generated method stub

            }

            @Override
            public void mouseEntered(MouseEvent e) {
                // TODO Auto-generated method stub

            }

            @Override
            public void mouseClicked(MouseEvent e) {
                // TODO Auto-generated method stub

            }


        });



        JPanel pnlTimKiem = new JPanel();
        pnlTimKiem.setBackground(SystemColor.controlHighlight);
        pnlTimKiem.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"),
                "Tìm kiếm điểm", TitledBorder.LEADING, TitledBorder.TOP, null,
                new Color(228, 123, 77)));
        pnlTimKiem.setLayout(new GridBagLayout());
        contentPane.add(pnlTimKiem);

        GridBagConstraints gbc3 = new GridBagConstraints();
        gbc3.fill = GridBagConstraints.HORIZONTAL;
        gbc3.gridx = 0;
        gbc3.gridy = 0;
        gbc3.insets = new Insets(5,10,5,10);
        gbc3.anchor = GridBagConstraints.WEST;

        JLabel lblNhap = new JLabel("Nhập thông tin tìm kiếm:");
        lblNhap.setForeground(new Color(228, 123, 77));
        lblNhap.setFont(new Font("Open Sans", Font.PLAIN, 18));
        pnlTimKiem.add(lblNhap,gbc3);

        cmbTim = new JComboBox();
        cmbTim.setFont(new Font("Open Sans", Font.PLAIN, 16));
        gbc3.gridx = 1;
        pnlTimKiem.add(cmbTim,gbc3);

        JButton btnTimKiem = new JButton("Tìm kiếm");
        btnTimKiem.setFont(new Font("Open Sans", Font.PLAIN, 16));
        btnTimKiem.setBackground(Color.lightGray);
        gbc3.gridx = 2;
        pnlTimKiem.add(btnTimKiem,gbc3);

        JLabel lblTim = new JLabel("Tìm theo:");
        lblTim.setForeground(new Color(228, 123, 77));
        lblTim.setFont(new Font("Open Sans", Font.PLAIN, 18));
        gbc3.gridx = 2;
        gbc3.gridy = 1;
        pnlTimKiem.add(lblTim,gbc3);
       // JRadioButton radSTT = new JRadioButton("STT");
        radMa = new JRadioButton("Mã học sinh");
        radMa.setSelected(true);
        radMa.setFont(new Font("Open Sans", Font.PLAIN, 18));
        gbc3.gridx = 3;
        pnlTimKiem.add(radMa,gbc3);


        radTenHS = new JRadioButton("Tên học sinh");
        radTenHS.setFont(new Font("Open Sans", Font.PLAIN, 18));
        gbc3.gridx = 4;
        pnlTimKiem.add(radTenHS,gbc3);

        radKetQua = new JRadioButton("Kết quả");
        radKetQua.setFont(new Font("Open Sans", Font.PLAIN, 18));
        gbc3.gridx = 6;
        pnlTimKiem.add(radKetQua,gbc3);


        ButtonGroup group = new ButtonGroup();
        group.add(radMa);
        group.add(radTenHS);
        group.add(radKetQua);

        cboModeTim.addElement("Tìm kiếm theo:");
        cboModeTim.addElement("Tìm theo mã học sinh.");
        cboModeTim.addElement("Tìm theo tên học sinh.");
        cboModeTim.addElement("Tìm theo kết quả.");

        pnlChucNang = new JPanel();
        pnlChucNang.setBackground(SystemColor.controlHighlight);
        pnlChucNang.setAlignmentX(Component.CENTER_ALIGNMENT);
        contentPane.add(pnlChucNang);



        btnThem = new JButton("Thêm");
        btnThem.setFont(new Font("Open Sans", Font.PLAIN, 16));
        btnThem.setForeground(Color.WHITE);
        btnThem.setBackground(new Color(228, 123, 77));
        btnThem.setFocusable(false);
        btnThem.setBorder(BorderFactory.createEmptyBorder(5,15,5,15));
        pnlChucNang.add(btnThem);

        btnSua = new JButton("Sửa");
        btnSua.setFont(new Font("Open Sans", Font.PLAIN, 16));
        btnSua.setForeground(Color.WHITE);
        btnSua.setBackground(new Color(228, 123, 77));
        btnSua.setFocusable(false);
        btnSua.setBorder(BorderFactory.createEmptyBorder(5,20,5,20));
        pnlChucNang.add(btnSua);

        btnLuu = new JButton("Lưu");
        btnLuu.setFont(new Font("Open Sans", Font.PLAIN, 16));
        btnLuu.setForeground(Color.WHITE);
        btnLuu.setBackground(new Color(228, 123, 77));
        btnLuu.setFocusable(false);
        btnLuu.setBorder(BorderFactory.createEmptyBorder(5,20,5,20));
        pnlChucNang.add(btnLuu);

        btnXoa = new JButton("Xóa");
        btnXoa.setFont(new Font("Open Sans", Font.PLAIN, 16));
        btnXoa.setForeground(Color.WHITE);
        btnXoa.setBackground(new Color(228, 123, 77));
        btnXoa.setFocusable(false);
        btnXoa.setBorder(BorderFactory.createEmptyBorder(5,20,5,20));
        pnlChucNang.add(btnXoa);

        btnChat = new JButton("CHAT");
        btnChat.setFont(new Font("Open Sans", Font.PLAIN, 16));
        btnChat.setForeground(Color.WHITE);
        btnChat.setBackground(new Color(228, 123, 77));
        btnChat.setFocusable(false);
        btnChat.setBorder(BorderFactory.createEmptyBorder(5,20,5,20));
        pnlChucNang.add(btnChat);

        btnLamMoi = new JButton("Làm mới");
        btnLamMoi.setFont(new Font("Open Sans", Font.PLAIN, 16));
        btnLamMoi.setForeground(Color.WHITE);
        btnLamMoi.setBackground(new Color(228, 123, 77));
        btnLamMoi.setFocusable(false);
        btnLamMoi.setBorder(BorderFactory.createEmptyBorder(5,10,5,10));
        pnlChucNang.add(btnLamMoi);

        btnIn = new JButton("In");
        btnIn.setFont(new Font("Open Sans", Font.PLAIN, 16));
        btnIn.setForeground(Color.WHITE);
        btnIn.setBackground(new Color(228, 123, 77));
        btnIn.setFocusable(false);
        btnIn.setBorder(BorderFactory.createEmptyBorder(5,10,5,10));
        pnlChucNang.add(btnIn);




        btnSua.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                // TODO Auto-generated method stub
                if(trangThaiSua == 0) {
                    sua();
                }
                else {
                    btnSua.setText("Sửa");
                    lamMoi();
                    trangThaiSua = 0;
                    btnThem.setEnabled(true);
                    btnLuu.setEnabled(false);
                }
            }
        });

        btnXoa.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            // TODO Auto-generated method stub
            if (e.getSource() == btnXoa) {
              Xoa();
              lamMoi();
            }
            else {
                btnXoa.setText("Xóa");
                xoaTable();
                int trangthaixoa = 0;

            }
        }});

        btnIn.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                // TODO Auto-generated method stub
                try {
				jTDetail.print();
			} catch (PrinterException ex) {
				// TODO Auto-generated catch block
				ex.printStackTrace();
			}


                }
        });

        btnLamMoi.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                // TODO Auto-generated method stub
                lamMoi();
            }
        });

        btnLuu.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                // TODO Auto-generated method stub
                try {
                    luu();
                } catch (ParseException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                }
            }
        });

        btnThem.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                // TODO Auto-generated method stub

                if(trangThaiThem == 0) {
                    them();
                }
                else {
                    btnThem.setText("Thêm");
                    lamMoi();
                    trangThaiThem = 0;
                    btnSua.setEnabled(true);
                    btnLuu.setEnabled(false);
                }
            }
        });

        btnTimKiem.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                // TODO Auto-generated method stub
                timKiem();
            }
        });

        radMa.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                // TODO Auto-generated method stub
                cmbTim.setModel(cboModeMa);
            }
        });

        radTenHS.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                // TODO Auto-generated method stub
                cmbTim.setModel(cboModeTenHS);
            }
        });

        radKetQua.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                // TODO Auto-generated method stub
                cmbTim.setModel(cboModeTimKetQua);
            }
        });

        AutoCompleteDecorator.decorate(cmbTim);
        docDuLieu();
    }

    /**
     * Dùng để lấy dự liệu từ sql lên bảng
     */

    public void docDuLieu() {


        try{
            int d = 1;
            List<Diem> list = dao.getDiem();
            for(Diem x:list) {

                tbModel.addRow(new Object[] {
                        d++, x.getMa(), x.getTen(), x.getToan(), x.getVan(), x.getAnh(), x.getTongDiem(), x.getKetQua(), x.getGhiChu()

                });



                if(!listMa.contains(x.getMa())) {
                    cboModeMa.addElement(x.getMa());
                    listMa.add(x.getMa());
                }
                if(!listTen.contains(x.getTen())) {
                    cboModeTenHS.addElement(x.getTen());
                    listTen.add(x.getTen());
                }
                if(!listTimKetQua.contains(x.getKetQua())) {
                    cboModeTimKetQua.addElement(x.getKetQua());
                    listTimKetQua.add(x.getKetQua());
                }
            }
        } catch (IllegalAccessError ex){
            JOptionPane.showMessageDialog(tblDiem, ex);
        }

    }

    /**
     * Dùng để hiện thị các text và combobox từ bảng lên
     */
    public void hienTable() {
        int row;
        int rowCount;
        row = tblDiem.getSelectedRow();
        txtDiem_Ma.setText(tbModel.getValueAt(row, 1).toString());
        txtDiem_Ten.setText(tbModel.getValueAt(row, 2).toString());
        txtDiem_Toan.setText(tbModel.getValueAt(row, 3).toString());
        txtDiem_Van.setText(tbModel.getValueAt(row, 4).toString());
        txtDiem_Anh.setText(tbModel.getValueAt(row, 5).toString());
        txtDiem_TongDiem.setText(tbModel.getValueAt(row, 6).toString());
        txtDiem_KetQua.setText(tbModel.getValueAt(row, 7).toString());
        txtDiem_GhiChu.setText(tbModel.getValueAt(row, 8).toString());

//        cmbDonViTinh.setSelectedItem(tbModel.getValueAt(row, 11));
    }

    /**
     * Mở giao diên sửa
     */
    public void sua()  {
        txtDiem_Ten.setEditable(true);
        txtDiem_Toan.setEditable(true);
        txtDiem_Van.setEditable(true);
        txtDiem_Anh.setEditable(true);
        txtDiem_TongDiem.setEditable(true);
        txtDiem_KetQua.setEditable(true);
        txtDiem_GhiChu.setEditable(true);

        btnSua.setText("Hủy");
        trangThaiSua = 1;
        btnThem.setEnabled(false);
        btnLuu.setEnabled(true);
    }


    public  void  Xoa() {
        String Ma = txtDiem_Ma.getText();
        DiemDAO diemDAO = new DiemDAO();
        diemDAO.Xoa(Ma);


    }

    /**
     * Dùng để lưu các các thuộc tính đã thêm hoặc sửa
     * @throws ParseException
     */
    public void luu() throws ParseException {
        String Ma = txtDiem_Ma.getText();
        String ten = txtDiem_Ten.getText();
        String toan = txtDiem_Toan.getText();
//
        String van = txtDiem_Van.getText();
        String anh = txtDiem_Anh.getText();
        String tongDiem = txtDiem_TongDiem.getText();
        String ketQua = txtDiem_KetQua.getText();
        String ghiChu = txtDiem_GhiChu.getText();
        if(trangThaiThem != 0 && trangThaiSua == 0) {
            if(kiemTra()) {
                dao.them(Ma,ten, Float.parseFloat(toan), Float.parseFloat(van), Float.parseFloat(anh));
                JOptionPane.showMessageDialog(this, "Thêm thành công !");
                btnThem.setText("Thêm");
                btnSua.setEnabled(true);
                btnLuu.setEnabled(false);
                trangThaiThem = 0;
            } else {


            }
        }

        else if(trangThaiThem == 0 && trangThaiSua != 0) {
            if(kiemTra()) {
                String ma = txtDiem_Ma.getText();
                dao.sua(ma, ten, Float.parseFloat(toan), Float.parseFloat(van), Float.parseFloat(anh),
                        Float.parseFloat(tongDiem), ketQua, ghiChu);
                JOptionPane.showMessageDialog(this, "Sửa Thành Công");
                btnThem.setEnabled(true);
                btnSua.setText("Sửa");
                btnLuu.setEnabled(false);
                trangThaiSua = 0;
            }
            else {
                return;
            }
        }
        lamMoi();
    }

    /**
     * Mở giao diện thêm
     */
    public void them() {
        lamMoi();
        txtDiem_Ma.setEditable(true);
        txtDiem_Ten.setEditable(true);
        txtDiem_Toan.setEditable(true);
        txtDiem_Van.setEditable(true);
        txtDiem_Anh.setEditable(true);
        txtDiem_TongDiem.setEditable(true);
        txtDiem_KetQua.setEditable(true);
        txtDiem_GhiChu.setEditable(true);

        btnThem.setText("Hủy");
        trangThaiThem = 1;

        btnSua.setEnabled(false);
        btnLuu.setEnabled(true);
    }

    /** Giao diện in

     */
    public void In(){
        try {
            jTDetail.print();
        } catch (PrinterException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }


    /**
     * Dùng để xóa dữ liệu bảng
     */
    public void xoaTable() {
        tbModel.addRow(new Object[] {

        });
        DefaultTableModel tbl = (DefaultTableModel) tblDiem.getModel();
        tbl.getDataVector().removeAllElements();

    }

    /**
     * Dùng để làm mới giao diện
     */
    public void lamMoi() {
        txtDiem_Ma.setText("");
        txtDiem_Ma.setEditable(false);

        txtDiem_Ten.setText("");
        txtDiem_Ten.setEditable(false);

        txtDiem_Toan.setText("");
        txtDiem_Toan.setEditable(false);


        txtDiem_Van.setText("");
        txtDiem_Van.setEditable(false);

        txtDiem_Anh.setText("");
        txtDiem_Anh.setEditable(false);

        txtDiem_TongDiem.setText("");
        txtDiem_TongDiem.setEditable(false);

        txtDiem_KetQua.setText("");
        txtDiem_KetQua.setEditable(false);

        txtDiem_GhiChu.setText("");
        txtDiem_GhiChu.setEditable(false);

        xoaTable();
        docDuLieu();
        btnSua.setText("Sửa");
        btnSua.setEnabled(true);
        trangThaiSua = 0;

        btnThem.setText("Thêm");
        btnThem.setEnabled(true);
        trangThaiThem = 0;
    }

    /**
     * Dùng để tìm kiếm điểm
     */
    public void timKiem() {
        lamMoi();
        int d = 1;
        String tim = "";
        try {
            tim = cmbTim.getSelectedItem().toString();
        } catch (NullPointerException e) {
            // TODO: handle exception

        }

        if(tim.equalsIgnoreCase("")) {
            JOptionPane.showMessageDialog(this, "Không được rỗng");
            return;
        }

        if(radMa.isSelected()){
            xoaTable();
            Diem x = dao.getDiembyMa((tim));
            tbModel.addRow(new Object[] {
                    d++, x.getMa(), x.getTen(), x.getToan(), x.getVan(), x.getAnh(), x.getKetQua(), x.getGhiChu()
            });
        }
        else if(radKetQua.isSelected()) {
            xoaTable();
            List<Diem> list = dao.getDiembyKetQua(tim);
            for(Diem x:list) {
                tbModel.addRow(new Object[] {
                        d++, x.getMa(), x.getTen(), x.getToan(), x.getVan(), x.getAnh(), x.getKetQua(), x.getGhiChu()
                });
            }
        }
        else if(radTenHS.isSelected()){
            xoaTable();
            List<Diem> list = dao.getDiembyTen(tim);
            for(Diem x:list) {
                tbModel.addRow(new Object[] {
                        d++, x.getMa(), x.getTen(), x.getToan(), x.getVan(), x.getAnh(), x.getKetQua(), x.getGhiChu()
                });
            }
        }
    }

    /**
     * Dùng kiểm tra dữ liệu.
     * @return true nếu dữ liệu đúng.
     * @return false nếu dữ liệu sai.
     */
    public boolean kiemTra()  {
        try {
            String ten = txtDiem_Ten.getText();
            String toan = txtDiem_Toan.getText();
            String van = txtDiem_Van.getText();
            String anh = txtDiem_Anh.getText();
            String tongDiem = txtDiem_TongDiem.getText();
            String ketQua = txtDiem_KetQua.getText();
            String ghiChu = txtDiem_GhiChu.getText();

            if (ten.equals("")) {
                JOptionPane.showMessageDialog(this, "Vui lòng nhập tên học sinh !",
                        "Thông báo !", JOptionPane.ERROR_MESSAGE, new ImageIcon("image\\warning.png"));
                txtDiem_Ten.requestFocus();
                txtDiem_Ten.selectAll();
                return false;
            }
            else if (toan.equals("")|| Float.parseFloat(toan) >= 10.0 ) {
                JOptionPane.showMessageDialog(this, "Lỗi nhập điểm toán !",
                        "Thông báo !", JOptionPane.ERROR_MESSAGE, new ImageIcon("image\\warning.png"));
                txtDiem_Toan.requestFocus();
                txtDiem_Toan.selectAll();
                return false;
            }
            else if (van.equals("")|| Float.parseFloat(van) >= 10.0  ) {
                JOptionPane.showMessageDialog(this, "Lỗi nhập điểm văn !",
                        "Thông báo !", JOptionPane.ERROR_MESSAGE, new ImageIcon("image\\warning.png"));
                txtDiem_Van.requestFocus();
                txtDiem_Van.selectAll();
                return false;

            }
            else if (anh.equals("") || Float.parseFloat(anh) >= 10.0 ) {
                JOptionPane.showMessageDialog(this, "Lỗi nhập điểm anh !",
                        "Thông báo !", JOptionPane.ERROR_MESSAGE, new ImageIcon("image\\warning.png"));
                txtDiem_Anh.requestFocus();
                txtDiem_Anh.selectAll();
                return false;
            }
//            else if (tongDiem.equals("")) {
//                JOptionPane.showMessageDialog(this, "Vui lòng nhập tổng điểm !",
//                        "Thông báo !", JOptionPane.ERROR_MESSAGE, new ImageIcon("image\\warning.png"));
//                txtDiem_TongDiem.requestFocus();
//                txtDiem_TongDiem.selectAll();
//                return false;
//            }
//            else if (ketQua.equals("")) {
//                JOptionPane.showMessageDialog(this, "Vui lòng nhập kết quả!",
//                        "Thông báo !", JOptionPane.ERROR_MESSAGE, new ImageIcon("image\\warning.png"));
//                txtDiem_KetQua.requestFocus();
//                txtDiem_KetQua.selectAll();
//                return false;
//            }
        } catch (Exception e) {
            // TODO: handle exception
        }
        return true;
    }
}
