package logines;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.io.InputStream;

public class RedefinirSenha extends JFrame {

    private JTextField txtLogin;
    private JPasswordField txtNovaSenha;
    private JPasswordField txtRepitaSenha;
    private Font pixelFont;
    private Image imagemFundo;

    public RedefinirSenha() {
        setUndecorated(true);
        setExtendedState(JFrame.MAXIMIZED_BOTH);

        carregarFontePersonalizada();

        // Ajuste o caminho da imagem para seu ambiente
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

        // Painel central com campos (preto semi-transparente)
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

        JPanel panelRedefinir = new JPanel();
        panelRedefinir.setLayout(null);
        panelRedefinir.setBackground(new Color(0, 0, 0, 150));
        panelRedefinir.setPreferredSize(new Dimension(400, 400));

        JLabel lblLogin = new JLabel("Login");
        lblLogin.setFont(pixelFont.deriveFont(36f));
        lblLogin.setForeground(Color.WHITE);
        lblLogin.setBounds(0, 10, 400, 40);
        lblLogin.setHorizontalAlignment(SwingConstants.CENTER);
        panelRedefinir.add(lblLogin);

        txtLogin = new JTextField();
        txtLogin.setBounds(75, 60, 250, 35);
        panelRedefinir.add(txtLogin);

        JLabel lblNovaSenha = new JLabel("Nova Senha");
        lblNovaSenha.setFont(pixelFont.deriveFont(36f));
        lblNovaSenha.setForeground(Color.WHITE);
        lblNovaSenha.setBounds(0, 110, 400, 30);
        lblNovaSenha.setHorizontalAlignment(SwingConstants.CENTER);
        panelRedefinir.add(lblNovaSenha);

        txtNovaSenha = new JPasswordField();
        txtNovaSenha.setBounds(75, 150, 250, 35);
        panelRedefinir.add(txtNovaSenha);

        JLabel lblRepitaSenha = new JLabel("Repita Senha");
        lblRepitaSenha.setFont(pixelFont.deriveFont(36f));
        lblRepitaSenha.setForeground(Color.WHITE);
        lblRepitaSenha.setBounds(0, 200, 400, 30);
        lblRepitaSenha.setHorizontalAlignment(SwingConstants.CENTER);
        panelRedefinir.add(lblRepitaSenha);

        txtRepitaSenha = new JPasswordField();
        txtRepitaSenha.setBounds(75, 240, 250, 35);
        panelRedefinir.add(txtRepitaSenha);

        JButton btnRedefinir = new JButton("Redefinir Senha");
        btnRedefinir.setBounds(100, 290, 200, 35);
        panelRedefinir.add(btnRedefinir);

        gbc.gridy = 0;
        painelCentro.add(panelRedefinir, gbc);

        // ===== Painel lateral (START em cima, GIF animado, EXIT) =====
        JPanel painelLateral = new JPanel();
        painelLateral.setPreferredSize(new Dimension(300, getHeight()));
        painelLateral.setBackground(Color.DARK_GRAY);
        painelLateral.setLayout(new BoxLayout(painelLateral, BoxLayout.Y_AXIS));
        painelLateral.setBorder(new EmptyBorder(20, 20, 20, 20));
        painelFundo.add(painelLateral, BorderLayout.EAST);

        // Botão START em cima do GIF (voltar para Login)
        JButton startButton = new JButton("START");
        startButton.setFont(new Font("Monospaced", Font.BOLD, 24));
        startButton.setBackground(new Color(0, 180, 0));
        startButton.setForeground(Color.WHITE);
        startButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        startButton.addActionListener(e -> {
            dispose();
            new Login().setVisible(true);
        });
        painelLateral.add(startButton);

        painelLateral.add(Box.createRigidArea(new Dimension(0, 20)));

        // GIF animado
        JPanel arcadePlaceholder = new JPanel();
        arcadePlaceholder.setBackground(Color.DARK_GRAY);
        arcadePlaceholder.setPreferredSize(new Dimension(150, 250));

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

        // Evento botão redefinir
        btnRedefinir.addActionListener(e -> {
            String login = txtLogin.getText().trim();
            String novaSenha = new String(txtNovaSenha.getPassword());
            String confirmaSenha = new String(txtRepitaSenha.getPassword());

            if (login.isEmpty() || novaSenha.isEmpty() || confirmaSenha.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Preencha todos os campos!", "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (!novaSenha.equals(confirmaSenha)) {
                JOptionPane.showMessageDialog(this, "As senhas não coincidem!", "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }

            JOptionPane.showMessageDialog(this, "Senha redefinida com sucesso!");
            dispose();
            new Login().setVisible(true);
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
            pixelFont = new Font("SansSerif", Font.PLAIN, 20);
        }
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                RedefinirSenha frame = new RedefinirSenha();
                frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}
