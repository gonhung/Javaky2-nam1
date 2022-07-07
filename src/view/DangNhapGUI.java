package view;

import dao.TaiKhoanDAO;
import entity.TaiKhoan;
import Security.AES;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class DangNhapGUI extends JFrame implements ActionListener {

    private JLabel lblTitle;
    private JLabel lblTenTK;
    private JLabel lblMatKhau;
    private JTextField txtTenTK;
    private JPasswordField txtMatKhau;
    private JButton btnDangNhap, btnDangKy;
    private JRadioButton radHienMatKhau;
    private AES aes = null;

    TaiKhoanDAO taiKhoanDAO = new TaiKhoanDAO();
    public DangNhapGUI() {
        // Cài đặt các thuộc tính cho JFrame
        setTitle("Đăng nhập");
        setIconImage(Toolkit.getDefaultToolkit().getImage("image\\logo.png"));
        setSize(800,600);
        setVisible(true);
        setResizable(false);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        // Tạo JPanel chính
        JPanel mainPane = new JPanel();
        mainPane.setBorder(BorderFactory.createEmptyBorder(25,25,40,25));
        mainPane.setLayout(new BoxLayout(mainPane, BoxLayout.Y_AXIS));
        setContentPane(mainPane);

        mainPane.add(Box.createVerticalBox());

        lblTitle = new JLabel("QUẢN LÝ ĐIỂM",JLabel.CENTER);
        lblTitle.setFont(new Font("Open Sans",Font.BOLD,25));
        lblTitle.setForeground(new Color(228, 123, 77));
        lblTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainPane.add(lblTitle);
        // thôi chịu
        // Tạo JPanel chứa nội dung chính
        JPanel contentPane = new JPanel();
        contentPane.setBorder(BorderFactory.createEmptyBorder(20,20,20,20));
        contentPane.setLayout(new GridBagLayout());
        mainPane.add(contentPane);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(10,10,10,10);
        gbc.anchor = GridBagConstraints.WEST;

        lblTenTK = new JLabel("Tên tài khoản:");
        lblTenTK.setFont(new Font("Open Sans",Font.PLAIN,18));
        contentPane.add(lblTenTK,gbc);

        txtTenTK = new JTextField(15);
        txtTenTK.setFont(new Font("Open Sans",Font.PLAIN,16));
        gbc.gridx = 1;
        contentPane.add(txtTenTK,gbc);

        lblMatKhau = new JLabel("Mật khẩu:");
        lblMatKhau.setFont(new Font("Open Sans",Font.PLAIN,18));
        gbc.gridx = 0;
        gbc.gridy = 1;
        contentPane.add(lblMatKhau,gbc);

        txtMatKhau = new JPasswordField(15);
        txtMatKhau.setEchoChar('•');
        txtMatKhau.setFont(new Font("Open Sans",Font.PLAIN,16));
        gbc.gridx = 1;
        contentPane.add(txtMatKhau,gbc);

        radHienMatKhau = new JRadioButton("Hiển thị mật khẩu");
        radHienMatKhau.setFont(new Font("Open Sans",Font.PLAIN,16));
        radHienMatKhau.addActionListener(this);
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        contentPane.add(radHienMatKhau,gbc);

        btnDangNhap = new JButton();
        btnDangNhap.setText("ĐĂNG NHẬP");
        btnDangNhap.setFont(new Font("Open Sans",Font.PLAIN,18));
        btnDangNhap.setBackground(new Color(228, 123, 77));
        btnDangNhap.setForeground(new Color(255,255,255));
        btnDangNhap.setFocusable(false);
        btnDangNhap.setBorder(BorderFactory.createEmptyBorder(5,27,5,27));
        btnDangNhap.addActionListener(this);
        btnDangNhap.setAlignmentX(CENTER_ALIGNMENT);
        mainPane.add(btnDangNhap);

        mainPane.add(Box.createVerticalStrut(10));

        btnDangKy = new JButton();
        btnDangKy.setText("Tạo tài khoản mới");
        btnDangKy.setFont(new Font("Open Sans",Font.PLAIN,18));
        btnDangKy.setBackground(Color.lightGray);
        btnDangKy.setFocusable(false);
        btnDangKy.setBorder(BorderFactory.createEmptyBorder(5,10,5,10));
        btnDangKy.addActionListener(this);
        btnDangKy.setAlignmentX(CENTER_ALIGNMENT);
        mainPane.add(btnDangKy);

        mainPane.add(Box.createVerticalBox());



    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource().equals(btnDangNhap)) {
            try {
                aes = new AES(String.valueOf(txtMatKhau.getPassword()));
                String mahoa = aes.encrypt(String.valueOf(txtMatKhau.getPassword()));
                TaiKhoan taiKhoan = taiKhoanDAO.kiemTraDangNhap(txtTenTK.getText(), mahoa);
                if(taiKhoan == null) {
                    JOptionPane.showMessageDialog(null, "Tài khoản hoặc mật khẩu không đúng!");
                }else{
                    DiemGUI dg = new DiemGUI();
                    dg.setVisible(true);
                }
            } catch (Exception e2) {
                // TODO: handle exception
                JOptionPane.showMessageDialog(this,"Lỗi.");
                return;
            }
        }
        if(e.getSource().equals(btnDangKy)) {
            this.dispose();
            TaoTaiKhoanGUI gui = new TaoTaiKhoanGUI();
            gui.setVisible(true);
        }

        if(radHienMatKhau.isSelected()) {
            radHienMatKhau.setText("Ẩn mật khẩu");
            txtMatKhau.setEchoChar((char)0);
        } else {
            radHienMatKhau.setText("Hiển thị mật khẩu");
            txtMatKhau.setEchoChar('•');
        }
    }
}
