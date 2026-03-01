package Trabalho;

import javax.swing.*;
import java.awt.*;

public class ImagemInicial extends JPanel {

    private Image imagemOriginal;

    public ImagemInicial() {
        setBackground(Color.DARK_GRAY);
        setLayout(new BorderLayout());

        // ===== Caminho absoluto (somente para desenvolvimento local)
        // Use getClass().getResource(...) se for mover o projeto
        imagemOriginal = new ImageIcon("C:\\Users\\mathe\\Downloads\\Captura_de_tela_2025-06-17_191022-removebg-preview.png").getImage();

        // JLabel personalizado que redimensiona a imagem
        JLabel imagemLabel = new JLabel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (imagemOriginal != null) {
                    Image imagemRedimensionada = imagemOriginal.getScaledInstance(
                            getWidth(), getHeight(), Image.SCALE_SMOOTH);
                    g.drawImage(imagemRedimensionada, 0, 0, getWidth(), getHeight(), this);
                }
            }
        };
        imagemLabel.setIcon(new ImageIcon("C:\\Users\\mathe\\Downloads\\Untitled design.jpg"));

        imagemLabel.setHorizontalAlignment(SwingConstants.CENTER);
        imagemLabel.setVerticalAlignment(SwingConstants.CENTER);

        add(imagemLabel, BorderLayout.CENTER);
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(800, 600);
    }
}
