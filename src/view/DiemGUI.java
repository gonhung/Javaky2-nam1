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
     * Ch???y th??? ch????ng tr??nh
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
     * T???o giao di???n qu???n l?? ??i???m
     */
    public DiemGUI() {
        trangThaiThem = 0;
        trangThaiSua = 0;

        // Thu???c t??nh c???a JFrame
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(true);
        setSize(1200,800);
        setExtendedState(JFrame.MAXIMIZED_BOTH);

        // T???o JPanel ch??nh
        contentPane = new JPanel();
        contentPane.setBorder(BorderFactory.createEmptyBorder(25,25,25,25));
        contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.Y_AXIS));
        setContentPane(contentPane);

        // ch???a th??ng tin in
        jTDetail = new JTextArea();
        contentPane.add(jTDetail);

        // JPanel ch???a ti??u ?????
        JPanel pnlTieuDe = new JPanel();
        pnlTieuDe.setBackground(SystemColor.controlHighlight);
        pnlTieuDe.setAlignmentX(Component.CENTER_ALIGNMENT);
        contentPane.add(pnlTieuDe);

        JLabel lblTieuDe = new JLabel("Qu???n L?? ??i???m");
        lblTieuDe.setFont(new Font("Open Sans", Font.BOLD, 25));
        lblTieuDe.setForeground(new Color(228, 123, 77));
        lblTieuDe.setHorizontalAlignment(SwingConstants.CENTER);
        pnlTieuDe.add(lblTieuDe);

        // JPanel ch???a n???i dung qu???n l?? v?? th??ng tin ??i???m
        JPanel mainPane = new JPanel();
        mainPane.setBackground(SystemColor.controlHighlight);
        mainPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        mainPane.setLayout(new GridLayout(1,2,1,1));
        contentPane.add(mainPane);

        JPanel pnlNoiDung = new JPanel();
        pnlNoiDung.setBackground(SystemColor.controlHighlight);
        pnlNoiDung.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"),
                "N???i dung qu???n l??", TitledBorder.LEADING, TitledBorder.TOP, null,
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


        
        JLabel lblDiem_Ma = new JLabel("M?? h???c sinh:");
       lblDiem_Ma.setForeground(new Color(228, 123, 77));
        lblDiem_Ma.setFont(new Font("Open Sans", Font.PLAIN, 18));
        pnlNoiDung.add(lblDiem_Ma,gbc);

        txtDiem_Ma = new JTextField(10);
        txtDiem_Ma.setFont(new Font("Open Sans", Font.PLAIN, 16));
        txtDiem_Ma.setEditable(false);
        gbc.gridx = 1;
        pnlNoiDung.add(txtDiem_Ma,gbc);

        JLabel lblTen = new JLabel("T??n h???c sinh:\r\n");
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

        JLabel lblDiem_Toan = new JLabel("To??n:\r\n");
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

        JLabel lblDiem_Van = new JLabel("V??n:\r\n");
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

        JLabel lblDiem_TongDiem = new JLabel("T???ng ??i???m:\r\n");
        lblDiem_TongDiem.setForeground(new Color(228, 123, 77));
        lblDiem_TongDiem.setFont(new Font("Open Sans", Font.PLAIN, 18));
        gbc.gridx = 6;
        pnlNoiDung.add(lblDiem_TongDiem,gbc);

        txtDiem_TongDiem = new JTextField(10);
        txtDiem_TongDiem.setFont(new Font("Open Sans", Font.PLAIN, 16));
        gbc.gridx = 7;
        pnlNoiDung.add(txtDiem_TongDiem,gbc);

        JLabel lblDiem_KetQua = new JLabel("K???t qu???:\r\n");
        lblDiem_KetQua.setForeground(new Color(228, 123, 77));
        lblDiem_KetQua.setFont(new Font("Open Sans", Font.PLAIN, 18));
        gbc.gridx = 0;
        gbc.gridy = 2;
        pnlNoiDung.add(lblDiem_KetQua,gbc);

        txtDiem_KetQua = new JTextField(10);
        txtDiem_KetQua.setFont(new Font("Open Sans", Font.PLAIN, 16));
        gbc.gridx = 1;
        pnlNoiDung.add(txtDiem_KetQua,gbc);

        JLabel lblDiem_GhiChu = new JLabel("Ghi ch??:\r\n");
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
               "STT", "M?? H???c Sinh", "T??n H???c Sinh", "??i???m To??n", "??i???m V??n", "??i???m Anh",
                "T???ng ??i???m", "K???t Qu???", "Ghi Ch??"
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
//in d??? li??u
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
                            "\t\n\t TH??NG TIN H???C SINH \n"+

                                    "\nM?? :\t\t" + tbModel.getValueAt(id, 1).toString()+
                                    "\nH??? T??N:\t\t" + tbModel.getValueAt(id, 2).toString()+
                                    "\n??I???M TO??N:\t\t" + tbModel.getValueAt(id, 3).toString()+
                                    "\n??I???M V??N\t\t" + tbModel.getValueAt(id, 4).toString()+
                                    "\n??I???M ANH:\t\t" + tbModel.getValueAt(id, 5).toString()+
                                    "\nT???NG ??I???M :\t\t" + tbModel.getValueAt(id, 6).toString()+
                                    "\nK???T QU???\t\t" + tbModel.getValueAt(id, 7).toString()) ;

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
                "T??m ki???m ??i???m", TitledBorder.LEADING, TitledBorder.TOP, null,
                new Color(228, 123, 77)));
        pnlTimKiem.setLayout(new GridBagLayout());
        contentPane.add(pnlTimKiem);

        GridBagConstraints gbc3 = new GridBagConstraints();
        gbc3.fill = GridBagConstraints.HORIZONTAL;
        gbc3.gridx = 0;
        gbc3.gridy = 0;
        gbc3.insets = new Insets(5,10,5,10);
        gbc3.anchor = GridBagConstraints.WEST;

        JLabel lblNhap = new JLabel("Nh???p th??ng tin t??m ki???m:");
        lblNhap.setForeground(new Color(228, 123, 77));
        lblNhap.setFont(new Font("Open Sans", Font.PLAIN, 18));
        pnlTimKiem.add(lblNhap,gbc3);

        cmbTim = new JComboBox();
        cmbTim.setFont(new Font("Open Sans", Font.PLAIN, 16));
        gbc3.gridx = 1;
        pnlTimKiem.add(cmbTim,gbc3);

        JButton btnTimKiem = new JButton("T??m ki???m");
        btnTimKiem.setFont(new Font("Open Sans", Font.PLAIN, 16));
        btnTimKiem.setBackground(Color.lightGray);
        gbc3.gridx = 2;
        pnlTimKiem.add(btnTimKiem,gbc3);

        JLabel lblTim = new JLabel("T??m theo:");
        lblTim.setForeground(new Color(228, 123, 77));
        lblTim.setFont(new Font("Open Sans", Font.PLAIN, 18));
        gbc3.gridx = 2;
        gbc3.gridy = 1;
        pnlTimKiem.add(lblTim,gbc3);
       // JRadioButton radSTT = new JRadioButton("STT");
        radMa = new JRadioButton("M?? h???c sinh");
        radMa.setSelected(true);
        radMa.setFont(new Font("Open Sans", Font.PLAIN, 18));
        gbc3.gridx = 3;
        pnlTimKiem.add(radMa,gbc3);


        radTenHS = new JRadioButton("T??n h???c sinh");
        radTenHS.setFont(new Font("Open Sans", Font.PLAIN, 18));
        gbc3.gridx = 4;
        pnlTimKiem.add(radTenHS,gbc3);

        radKetQua = new JRadioButton("K???t qu???");
        radKetQua.setFont(new Font("Open Sans", Font.PLAIN, 18));
        gbc3.gridx = 6;
        pnlTimKiem.add(radKetQua,gbc3);


        ButtonGroup group = new ButtonGroup();
        group.add(radMa);
        group.add(radTenHS);
        group.add(radKetQua);

        cboModeTim.addElement("T??m ki???m theo:");
        cboModeTim.addElement("T??m theo m?? h???c sinh.");
        cboModeTim.addElement("T??m theo t??n h???c sinh.");
        cboModeTim.addElement("T??m theo k???t qu???.");

        pnlChucNang = new JPanel();
        pnlChucNang.setBackground(SystemColor.controlHighlight);
        pnlChucNang.setAlignmentX(Component.CENTER_ALIGNMENT);
        contentPane.add(pnlChucNang);



        btnThem = new JButton("Th??m");
        btnThem.setFont(new Font("Open Sans", Font.PLAIN, 16));
        btnThem.setForeground(Color.WHITE);
        btnThem.setBackground(new Color(228, 123, 77));
        btnThem.setFocusable(false);
        btnThem.setBorder(BorderFactory.createEmptyBorder(5,15,5,15));
        pnlChucNang.add(btnThem);

        btnSua = new JButton("S???a");
        btnSua.setFont(new Font("Open Sans", Font.PLAIN, 16));
        btnSua.setForeground(Color.WHITE);
        btnSua.setBackground(new Color(228, 123, 77));
        btnSua.setFocusable(false);
        btnSua.setBorder(BorderFactory.createEmptyBorder(5,20,5,20));
        pnlChucNang.add(btnSua);

        btnLuu = new JButton("L??u");
        btnLuu.setFont(new Font("Open Sans", Font.PLAIN, 16));
        btnLuu.setForeground(Color.WHITE);
        btnLuu.setBackground(new Color(228, 123, 77));
        btnLuu.setFocusable(false);
        btnLuu.setBorder(BorderFactory.createEmptyBorder(5,20,5,20));
        pnlChucNang.add(btnLuu);

        btnXoa = new JButton("X??a");
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

        btnLamMoi = new JButton("L??m m???i");
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
                    btnSua.setText("S???a");
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
                btnXoa.setText("X??a");
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
                    btnThem.setText("Th??m");
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
     * D??ng ????? l???y d??? li???u t??? sql l??n b???ng
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
     * D??ng ????? hi???n th??? c??c text v?? combobox t??? b???ng l??n
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
     * M??? giao di??n s???a
     */
    public void sua()  {
        txtDiem_Ten.setEditable(true);
        txtDiem_Toan.setEditable(true);
        txtDiem_Van.setEditable(true);
        txtDiem_Anh.setEditable(true);
        txtDiem_TongDiem.setEditable(true);
        txtDiem_KetQua.setEditable(true);
        txtDiem_GhiChu.setEditable(true);

        btnSua.setText("H???y");
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
     * D??ng ????? l??u c??c c??c thu???c t??nh ???? th??m ho???c s???a
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
                JOptionPane.showMessageDialog(this, "Th??m th??nh c??ng !");
                btnThem.setText("Th??m");
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
                JOptionPane.showMessageDialog(this, "S???a Th??nh C??ng");
                btnThem.setEnabled(true);
                btnSua.setText("S???a");
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
     * M??? giao di???n th??m
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

        btnThem.setText("H???y");
        trangThaiThem = 1;

        btnSua.setEnabled(false);
        btnLuu.setEnabled(true);
    }

    /** Giao di???n in

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
     * D??ng ????? x??a d??? li???u b???ng
     */
    public void xoaTable() {
        tbModel.addRow(new Object[] {

        });
        DefaultTableModel tbl = (DefaultTableModel) tblDiem.getModel();
        tbl.getDataVector().removeAllElements();

    }

    /**
     * D??ng ????? l??m m???i giao di???n
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
        btnSua.setText("S???a");
        btnSua.setEnabled(true);
        trangThaiSua = 0;

        btnThem.setText("Th??m");
        btnThem.setEnabled(true);
        trangThaiThem = 0;
    }

    /**
     * D??ng ????? t??m ki???m ??i???m
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
            JOptionPane.showMessageDialog(this, "Kh??ng ???????c r???ng");
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
     * D??ng ki???m tra d??? li???u.
     * @return true n???u d??? li???u ????ng.
     * @return false n???u d??? li???u sai.
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
                JOptionPane.showMessageDialog(this, "Vui l??ng nh???p t??n h???c sinh !",
                        "Th??ng b??o !", JOptionPane.ERROR_MESSAGE, new ImageIcon("image\\warning.png"));
                txtDiem_Ten.requestFocus();
                txtDiem_Ten.selectAll();
                return false;
            }
            else if (toan.equals("")|| Float.parseFloat(toan) >= 10.0 ) {
                JOptionPane.showMessageDialog(this, "L???i nh???p ??i???m to??n !",
                        "Th??ng b??o !", JOptionPane.ERROR_MESSAGE, new ImageIcon("image\\warning.png"));
                txtDiem_Toan.requestFocus();
                txtDiem_Toan.selectAll();
                return false;
            }
            else if (van.equals("")|| Float.parseFloat(van) >= 10.0  ) {
                JOptionPane.showMessageDialog(this, "L???i nh???p ??i???m v??n !",
                        "Th??ng b??o !", JOptionPane.ERROR_MESSAGE, new ImageIcon("image\\warning.png"));
                txtDiem_Van.requestFocus();
                txtDiem_Van.selectAll();
                return false;

            }
            else if (anh.equals("") || Float.parseFloat(anh) >= 10.0 ) {
                JOptionPane.showMessageDialog(this, "L???i nh???p ??i???m anh !",
                        "Th??ng b??o !", JOptionPane.ERROR_MESSAGE, new ImageIcon("image\\warning.png"));
                txtDiem_Anh.requestFocus();
                txtDiem_Anh.selectAll();
                return false;
            }
//            else if (tongDiem.equals("")) {
//                JOptionPane.showMessageDialog(this, "Vui l??ng nh???p t???ng ??i???m !",
//                        "Th??ng b??o !", JOptionPane.ERROR_MESSAGE, new ImageIcon("image\\warning.png"));
//                txtDiem_TongDiem.requestFocus();
//                txtDiem_TongDiem.selectAll();
//                return false;
//            }
//            else if (ketQua.equals("")) {
//                JOptionPane.showMessageDialog(this, "Vui l??ng nh???p k???t qu???!",
//                        "Th??ng b??o !", JOptionPane.ERROR_MESSAGE, new ImageIcon("image\\warning.png"));
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
