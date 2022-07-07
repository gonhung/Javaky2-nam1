package view;

import Security.AES;
import dao.TaiKhoanDAO;
import dao.TaoTaiKhoanDAO;
import entity.TaiKhoan;

import javax.security.auth.callback.Callback;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class TaoTaiKhoanGUI extends JFrame implements ActionListener {

    private JTextField txtTenTK;
    private JPasswordField txtMatKhau;
    private JPasswordField txtNhapLaiMK;
    private JButton btnDangKy;
    private AES aes;

    TaoTaiKhoanDAO taoTaiKhoanDAO = new TaoTaiKhoanDAO();
    TaiKhoan taiKhoan = new TaiKhoan();

    public TaoTaiKhoanGUI() {
        setTitle("Đăng ký");
        setIconImage(Toolkit.getDefaultToolkit().getImage("image\\logo.png"));
        setSize(800,600);
        setVisible(true);
        setResizable(false);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        JPanel contentPane = new JPanel();
        contentPane.setBorder(BorderFactory.createEmptyBorder(20,20,20,20));
        contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.Y_AXIS));
        setContentPane(contentPane);

        JLabel lblTieuDe = new JLabel("ĐĂNG KÝ TÀI KHOẢN");
        lblTieuDe.setFont(new Font("Open Sans",Font.BOLD,25));
        lblTieuDe.setForeground(new Color(228, 123, 77));
        lblTieuDe.setAlignmentX(Component.CENTER_ALIGNMENT);
        contentPane.add(lblTieuDe);

        JPanel mainPane = new JPanel();
        mainPane.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
        mainPane.setLayout(new GridBagLayout());
        contentPane.add(mainPane);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(10,10,10,10);
        gbc.anchor = GridBagConstraints.WEST;

        JLabel lblTenTK = new JLabel("Tên tài khoản:");
        lblTenTK.setFont(new Font("Open Sans",Font.PLAIN,18));
        mainPane.add(lblTenTK,gbc);

        txtTenTK = new JTextField(15);
        txtTenTK.setFont(new Font("Open Sans",Font.PLAIN,16));
        gbc.gridx = 1;
        mainPane.add(txtTenTK,gbc);

        JLabel lblMatKhau = new JLabel("Mật khẩu:");
        lblMatKhau.setFont(new Font("Open Sans",Font.PLAIN,18));
        gbc.gridx = 0;
        gbc.gridy = 1;
        mainPane.add(lblMatKhau,gbc);

        txtMatKhau = new JPasswordField(15);
        txtMatKhau.setEchoChar('•');
        txtMatKhau.setFont(new Font("Open Sans",Font.PLAIN,16));
        gbc.gridx = 1;
        mainPane.add(txtMatKhau,gbc);

        JLabel lblNhapLaiMK = new JLabel("Nhập lại mật khẩu:");
        lblNhapLaiMK.setFont(new Font("Open Sans",Font.PLAIN,18));
        gbc.gridx = 0;
        gbc.gridy = 2;
        mainPane.add(lblNhapLaiMK,gbc);

        txtNhapLaiMK = new JPasswordField(15);
        txtNhapLaiMK.setEchoChar('•');
        txtNhapLaiMK.setFont(new Font("Open Sans",Font.PLAIN,16));
        gbc.gridx = 1;
        mainPane.add(txtNhapLaiMK,gbc);

        btnDangKy = new JButton("Đăng ký");
        btnDangKy.setFont(new Font("Open Sans",Font.PLAIN,18));
        btnDangKy.setBackground(new Color(228, 123, 77));
        btnDangKy.setForeground(new Color(255,255,255));
        btnDangKy.setFocusable(false);
        btnDangKy.setBorder(BorderFactory.createEmptyBorder(5,27,5,27));
        btnDangKy.addActionListener(this);
        btnDangKy.setAlignmentX(CENTER_ALIGNMENT);
        contentPane.add(btnDangKy);

    }

    // cái phần kéo thả ở đâu
    // cso kéo tahr đâu m
    //vcl
    // m code tay á
    // m làm gì đấy
    // t làm hiện mỗi cái này thôi mà nó có hiện đâu

    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            if (txtTenTK.getText().equals("")) {
                JOptionPane.showMessageDialog(this, "Vui lòng nhập tên tài khoản !");
            }
            else if (txtMatKhau.getPassword()==null) {
                JOptionPane.showMessageDialog(this, "Vui lòng nhập mật khẩu !");
            }
            else if (txtTenTK.getText().equals(taiKhoan.getTenTaiKhoan())) {
                JOptionPane.showMessageDialog(this, "Tên tài khoản bị trùng. Vui lòng nhập lại tên tài khoản khác!");
                txtMatKhau.setText("");
                txtNhapLaiMK.setText("");
            }
            else if (!String.valueOf(txtMatKhau.getPassword()).equals(String.valueOf(txtNhapLaiMK.getPassword()))) {
                 JOptionPane.showMessageDialog(this, "Nhập lại mật khẩu không chính xác !");
            }
            else {
                String mahoa = new AES(String.valueOf(txtMatKhau.getPassword())).encrypt(String.valueOf(txtMatKhau.getPassword()));
                taoTaiKhoanDAO.taoTaiKhoan(txtTenTK.getText(), mahoa);
                JOptionPane.showMessageDialog(this, "Đăng ký tài khoản thành công !");
                DangNhapGUI gui = new DangNhapGUI();
                gui.setVisible(true);
                this.setVisible(false);
                }
//            }
        } catch (Exception e2) {
            e2.printStackTrace();
           JOptionPane.showMessageDialog(this,"Lỗi.");
        }
    }
}
