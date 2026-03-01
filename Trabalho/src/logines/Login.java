package logines;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.io.InputStream;
import Trabalho.JanelaPrincipal;

public class Login extends JFrame {

    private JTextField txtLogin;
    private JPasswordField txtSenha;
    private Font pixelFont;
    private Image imagemFundo;

    public Login() {
        setUndecorated(true);  // Sem barra de título
        setExtendedState(JFrame.MAXIMIZED_BOTH);  // Maximizado ao abrir

        carregarFontePersonalizada();

        // Ajuste aqui o caminho da imagem do fundo conforme seu ambiente
        imagemFundo = new ImageIcon("C:\\Users\\mathe\\Downloads\\Untitled design.jpg").getImage();

        // Painel principal com imagem de fundo e deslocamento para direita
        JPanel painelFundo = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (imagemFundo != null) {
                    int imgWidth = imagemFundo.getWidth(this);
                    int imgHeight = imagemFundo.getHeight(this);
                    int deslocamentoDireita = 50; // ajuste para mover a imagem para direita
                    int x = (getWidth() - imgWidth) / 1 + deslocamentoDireita;
                    int y = (getHeight() - imgHeight) / 2;
                    g.drawImage(imagemFundo, x, y, this);
                }
            }
        };
        painelFundo.setLayout(new BorderLayout());
        setContentPane(painelFundo);

        // ===== Painel central com formulário =====
        JPanel painelCentro = new JPanel();
        painelCentro.setOpaque(false);
        painelCentro.setLayout(new GridBagLayout());
        painelFundo.add(painelCentro, BorderLayout.CENTER);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.CENTER;

        JPanel panelLogin = new JPanel();
        panelLogin.setLayout(null);
        panelLogin.setBackground(new Color(0, 0, 0, 150)); // Preto semi-transparente
        panelLogin.setPreferredSize(new Dimension(400, 300));

        JLabel lblLogin = new JLabel("Login");
        lblLogin.setFont(pixelFont.deriveFont(36f));
        lblLogin.setForeground(Color.WHITE);
        lblLogin.setBounds(0, 20, 400, 40);
        lblLogin.setHorizontalAlignment(SwingConstants.CENTER);
        panelLogin.add(lblLogin);

        txtLogin = new JTextField();
        txtLogin.setBounds(75, 70, 250, 35);
        panelLogin.add(txtLogin);

        JLabel lblSenha = new JLabel("Senha");
        lblSenha.setFont(pixelFont.deriveFont(36f));
        lblSenha.setForeground(Color.WHITE);
        lblSenha.setBounds(0, 120, 400, 40);
        lblSenha.setHorizontalAlignment(SwingConstants.CENTER);
        panelLogin.add(lblSenha);

        txtSenha = new JPasswordField();
        txtSenha.setBounds(75, 170, 250, 35);
        panelLogin.add(txtSenha);

        painelCentro.add(panelLogin, gbc);

        // ===== Painel lateral (START em cima, GIF animado, EXIT) =====
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
            String login = txtLogin.getText().trim();
            String senha = new String(txtSenha.getPassword()).trim();

            if (login.isEmpty() || senha.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Preencha Login e Senha!", "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (UsuarioDB.validarCredenciais(login, senha)) {
                new JanelaPrincipal().setVisible(true);
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Login ou senha incorretos!", "Falha", JOptionPane.ERROR_MESSAGE);
            }
        });
        painelLateral.add(startButton);

        painelLateral.add(Box.createRigidArea(new Dimension(0, 20)));

        // GIF animado
        JPanel arcadePlaceholder = new JPanel();
        arcadePlaceholder.setBackground(Color.DARK_GRAY);
        arcadePlaceholder.setPreferredSize(new Dimension(300, 250));

        JLabel lblGif = new JLabel();
        ImageIcon gifIcon = new ImageIcon("C:\\Users\\mathe\\Downloads\\elvis-gameculte2.gif"); // ajuste caminho
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
    }

    private void carregarFontePersonalizada() {
        try {
            InputStream is = getClass().getResourceAsStream("/TELADOIS/imagens/Pixels.ttf");
            if (is == null) {
                throw new RuntimeException("Arquivo Pixels.ttf não encontrado!");
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
                Login frame = new Login();
                frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}
