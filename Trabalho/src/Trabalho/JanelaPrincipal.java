package Trabalho;

import javax.swing.*;
import java.awt.*;

public class JanelaPrincipal extends JFrame {

    private JPanel painelTopo;
    private JPanel painelConteudo;

    private ClientePainel clientePainel;
    private ProdutosPainel produtosPainel;
    private RelatorioCompra relatorioCompra;
    private RelatorioHistoricoCompras relatorioHistorico;
    private Vendas vendasPainel;
    private Promocoes promocoesPainel;
    private RelatorioLucratividade lucratividadePainel;

    public JanelaPrincipal() {
        setUndecorated(true);

        GraphicsEnvironment.getLocalGraphicsEnvironment()
            .getDefaultScreenDevice()
            .setFullScreenWindow(this);

        setBackground(Color.DARK_GRAY);
        setTitle("Tela Principal");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        painelTopo = new JPanel(new FlowLayout());
        painelTopo.setBackground(Color.DARK_GRAY);

        JButton btnCliente = new JButton("Cliente");
        JButton btnProduto = new JButton("Produto");
        JButton btnRelatorioCompra = new JButton("Relatório Compra");
        JButton btnRelatorioHistoricoCompras = new JButton("Histórico Compras");
        JButton btnVendas = new JButton("Vendas");
        JButton btnPromocoes = new JButton("Promoções");
        JButton btnRelatorioLucratividade = new JButton("Lucratividade");

        Color btnBg = Color.GRAY;
        btnCliente.setBackground(btnBg);
        btnProduto.setBackground(btnBg);
        btnRelatorioCompra.setBackground(btnBg);
        btnRelatorioHistoricoCompras.setBackground(btnBg);
        btnVendas.setBackground(btnBg);
        btnPromocoes.setBackground(btnBg);
        btnRelatorioLucratividade.setBackground(btnBg);

        painelTopo.add(btnCliente);
        painelTopo.add(btnProduto);
        painelTopo.add(btnRelatorioCompra);
        painelTopo.add(btnRelatorioHistoricoCompras);
        painelTopo.add(btnVendas);
        painelTopo.add(btnPromocoes);
        painelTopo.add(btnRelatorioLucratividade);

        painelConteudo = new JPanel(new BorderLayout());
        painelConteudo.setBackground(Color.DARK_GRAY);

        getContentPane().add(painelTopo, BorderLayout.NORTH);
        getContentPane().add(painelConteudo, BorderLayout.CENTER);

        relatorioCompra = new RelatorioCompra(null);
        relatorioHistorico = new RelatorioHistoricoCompras(relatorioCompra);
        relatorioCompra.setRelatorioHistorico(relatorioHistorico);

        clientePainel = new ClientePainel();
        produtosPainel = new ProdutosPainel();

        vendasPainel = new Vendas(relatorioHistorico, clientePainel);

        promocoesPainel = new Promocoes();
        lucratividadePainel = new RelatorioLucratividade();

        vendasPainel.setOnVendaFinalizada(() -> {
            SwingUtilities.invokeLater(() -> {
                relatorioHistorico.atualizarRelatorio();
                relatorioCompra.atualizarTabela();  // <-- Aqui atualiza o relatório de compras
                abrirPainel(relatorioHistorico);
            });
        });

        promocoesPainel.setRelatorioCompra(relatorioCompra);

        btnCliente.addActionListener(e -> abrirPainel(clientePainel));
        btnProduto.addActionListener(e -> abrirPainel(produtosPainel));
        btnRelatorioCompra.addActionListener(e -> abrirPainel(relatorioCompra));
        btnRelatorioHistoricoCompras.addActionListener(e -> {
            relatorioHistorico.atualizarRelatorio();
            abrirPainel(relatorioHistorico);
        });
        btnVendas.addActionListener(e -> abrirPainel(vendasPainel));
        btnPromocoes.addActionListener(e -> abrirPainel(promocoesPainel));
        btnRelatorioLucratividade.addActionListener(e -> abrirPainel(lucratividadePainel));

        abrirPainel(new ImagemInicial()); // Tela inicial, se existir
    }

    private void abrirPainel(JPanel novoPainel) {
        painelConteudo.removeAll();
        painelConteudo.add(novoPainel, BorderLayout.CENTER);
        painelConteudo.revalidate();
        painelConteudo.repaint();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JanelaPrincipal janela = new JanelaPrincipal();
            janela.setVisible(true);
        });
    }
}
