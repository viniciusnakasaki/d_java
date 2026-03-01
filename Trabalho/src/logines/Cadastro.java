package logines;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import conexão.ConexaoMySQL;

public class Cadastro extends JFrame {

    private JTextField txtLogin;
    private JPasswordField txtSenha;
    private Font pixelFont;
    private Image imagemFundo;
    private boolean cadastroRealizado = false;

    public Cadastro() {
        setUndecorated(true); // sem barra superior
        setExtendedState(JFrame.MAXIMIZED_BOTH); // maximizado

        carregarFontePersonalizada();

        // Carrega imagem de fundo (ajuste o caminho conforme seu PC)
        imagemFundo = new ImageIcon("C:\\Users\\mathe\\Downloads\\Untitled design.jpg").getImage();

        JPanel painelFundo = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (imagemFundo != null) {
                    int imgWidth = imagemFundo.getWidth(this);
                    int imgHeight = imagemFundo.getHeight(this);
                    int deslocamentoDireita = 50;  // ajuste aqui para deslocar mais ou menos
                    int x = (getWidth() - imgWidth) / 1 + deslocamentoDireita;
                    int y = (getHeight() - imgHeight) / 2;
                    g.drawImage(imagemFundo, x, y, this);
                }
            }
        };
        painelFundo.setLayout(new BorderLayout());
        setContentPane(painelFundo);

        // Painel central com formulário
        JPanel painelCentro = new JPanel();
        painelCentro.setOpaque(false);
        painelCentro.setLayout(new GridBagLayout());
        painelFundo.add(painelCentro, BorderLayout.CENTER);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.NONE;

        JPanel panelCadastro = new JPanel();
        panelCadastro.setLayout(null);
        panelCadastro.setBackground(new Color(0, 0, 0, 150)); // semi-transparente preto
        panelCadastro.setPreferredSize(new Dimension(400, 300));

        JLabel lblLogin = new JLabel("Login (E-mail)");
        lblLogin.setFont(pixelFont.deriveFont(36f));
        lblLogin.setForeground(Color.WHITE);
        lblLogin.setBounds(0, 20, 400, 30);
        lblLogin.setHorizontalAlignment(SwingConstants.CENTER);
        panelCadastro.add(lblLogin);

        txtLogin = new JTextField();
        txtLogin.setBounds(75, 60, 250, 35);
        panelCadastro.add(txtLogin);

        JLabel lblSenha = new JLabel("Senha");
        lblSenha.setFont(pixelFont.deriveFont(36f));
        lblSenha.setForeground(Color.WHITE);
        lblSenha.setBounds(0, 100, 400, 30);
        lblSenha.setHorizontalAlignment(SwingConstants.CENTER);
        panelCadastro.add(lblSenha);

        txtSenha = new JPasswordField();
        txtSenha.setBounds(75, 140, 250, 35);
        panelCadastro.add(txtSenha);

        JButton btnCadastrar = new JButton("Cadastrar");
        btnCadastrar.setBounds(100, 190, 200, 35);
        panelCadastro.add(btnCadastrar);

        JButton btnJaTenhoCadastro = new JButton("Já possuo cadastro");
        btnJaTenhoCadastro.setBounds(100, 235, 200, 30);
        panelCadastro.add(btnJaTenhoCadastro);

        painelCentro.add(panelCadastro, gbc);

        // ===== Painel lateral (START em cima, GIF e EXIT abaixo) =====
        JPanel painelLateral = new JPanel();
        painelLateral.setPreferredSize(new Dimension(300, getHeight()));
        painelLateral.setBackground(Color.DARK_GRAY);
        painelLateral.setLayout(new BoxLayout(painelLateral, BoxLayout.Y_AXIS));
        painelLateral.setBorder(new EmptyBorder(20, 20, 20, 20));
        painelFundo.add(painelLateral, BorderLayout.EAST);

        // Botão START em cima do GIF
        JButton startButton = new JButton("START");
        startButton.setFont(new Font("Monospaced", Font.BOLD, 24));
        startButton.setBackground(new Color(0, 180, 0));
        startButton.setForeground(Color.WHITE);
        startButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        startButton.addActionListener(e -> {
            if (cadastroRealizado) {
                dispose();
                new Login().setVisible(true);
            } else {
                JOptionPane.showMessageDialog(this, "Por favor, realize o cadastro primeiro!", "Acesso Negado", JOptionPane.WARNING_MESSAGE);
            }
        });
        painelLateral.add(startButton);

        painelLateral.add(Box.createRigidArea(new Dimension(0, 20)));

        // GIF animado
        JPanel arcadePlaceholder = new JPanel();
        arcadePlaceholder.setBackground(Color.DARK_GRAY);
        arcadePlaceholder.setPreferredSize(new Dimension(150, 250));

        JLabel lblGif = new JLabel();
        ImageIcon gifIcon = new ImageIcon("C:\\Users\\mathe\\Downloads\\elvis-gameculte2.gif");
        lblGif.setIcon(gifIcon);
        arcadePlaceholder.add(lblGif);
        painelLateral.add(arcadePlaceholder);

        painelLateral.add(Box.createRigidArea(new Dimension(0, 20)));

        // Botão EXIT
        JButton exitButton = new JButton("EXIT");
        exitButton.setFont(new Font("SansSerif", Font.BOLD, 20));
        exitButton.setBackground(Color.RED);
        exitButton.setForeground(Color.WHITE);
        exitButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        exitButton.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(this, "Deseja sair?", "Sair", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                System.exit(0);
            }
        });
        painelLateral.add(exitButton);

        // Eventos botões do formulário
        btnJaTenhoCadastro.addActionListener(e -> {
            dispose();
            new Login().setVisible(true);
        });

        btnCadastrar.addActionListener(e -> {
            String login = txtLogin.getText().trim();
            String senha = new String(txtSenha.getPassword()).trim();

            if (login.isEmpty() || senha.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Preencha todos os campos!", "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (!login.matches("[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}")) {
                JOptionPane.showMessageDialog(this, "Formato de e-mail inválido!", "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }

            try (Connection conn = ConexaoMySQL.conectar()) {
                String sql = "INSERT INTO usuarios (email, senha) VALUES (?, ?)";
                PreparedStatement ps = conn.prepareStatement(sql);
                ps.setString(1, login);
                ps.setString(2, senha);
                ps.executeUpdate();
                JOptionPane.showMessageDialog(this, "Cadastro realizado com sucesso!");
                cadastroRealizado = true;
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Erro ao cadastrar: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Erro: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            }
        });
    }

    private void carregarFontePersonalizada() {
        try {
            InputStream is = getClass().getResourceAsStream("/TELADOIS/imagens/Pixels.ttf");
            if (is == null) {
                throw new RuntimeException("Fonte Pixels.ttf não encontrada!");
            }
            pixelFont = Font.createFont(Font.TRUETYPE_FONT, is);
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            ge.registerFont(pixelFont);
        } catch (Exception e) {
            System.out.println("Erro ao carregar fonte personalizada.");
            pixelFont = new Font("SansSerif", Font.PLAIN, 18);
        }
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                Cadastro frame = new Cadastro();
                frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}
