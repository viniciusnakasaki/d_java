package Trabalho;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;
import java.util.HashMap;
import java.util.Map;

import conexão.ConexaoMySQL;

public class Promocoes extends JPanel {

    private JComboBox<String> comboProduto;
    private JTextField campoSKU;
    private JTextField campoPreco;
    private JTextField campoDesconto;
    private JTextField campoDataInicio;
    private JTextField campoDataFim;
    private JTable tabelaPromocoes;
    private DefaultTableModel modeloTabela;
    private Map<String, String> produtoParaSKU = new HashMap<>();
    private Map<String, Double> produtoParaPreco = new HashMap<>();
    private RelatorioCompra relatorioCompra;

    public void setRelatorioCompra(RelatorioCompra rc) {
        this.relatorioCompra = rc;
    }

    public Promocoes() {
        setLayout(new BorderLayout(10, 10));
        setBackground(Color.DARK_GRAY);
        setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10));

        JPanel painelCentro = new JPanel();
        painelCentro.setLayout(new BoxLayout(painelCentro, BoxLayout.Y_AXIS));
        painelCentro.setBackground(Color.GRAY);
        painelCentro.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        painelCentro.add(Box.createVerticalStrut(10));
        painelCentro.add(criarPainelFormulario());

        painelCentro.add(Box.createVerticalStrut(10));
        JPanel painelBotoes = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        painelBotoes.setBackground(Color.GRAY);
        JButton botaoSalvar = new JButton("Salvar");
        JButton botaoExcluir = new JButton("Excluir");
        botaoSalvar.addActionListener(e -> salvarPromocao());
        botaoExcluir.addActionListener(e -> excluirPromocao());
        painelBotoes.add(botaoSalvar);
        painelBotoes.add(botaoExcluir);
        painelCentro.add(painelBotoes);

        painelCentro.add(Box.createVerticalStrut(10));
        painelCentro.add(criarTabelaPromocoes());
        carregarTabelaPromocoes();

        add(painelCentro, BorderLayout.CENTER);

        JPanel painelLateral = new JPanel();
        painelLateral.setLayout(new BoxLayout(painelLateral, BoxLayout.Y_AXIS));
        painelLateral.setBackground(Color.DARK_GRAY);
        painelLateral.setBorder(new EmptyBorder(20, 20, 20, 20));
        painelLateral.setPreferredSize(new Dimension(300, 600));

        JButton botaoStart = new JButton("START");
        botaoStart.setFont(new Font("Monospaced", Font.BOLD, 24));
        botaoStart.setBackground(new Color(0, 180, 0));
        botaoStart.setForeground(Color.WHITE);
        botaoStart.setAlignmentX(Component.CENTER_ALIGNMENT);
        painelLateral.add(botaoStart);
        painelLateral.add(Box.createRigidArea(new Dimension(0, 20)));

        JPanel arcadePlaceholder = new JPanel();
        arcadePlaceholder.setBackground(Color.DARK_GRAY);
        arcadePlaceholder.setPreferredSize(new Dimension(150, 250));
        JLabel lblNewLabel = new JLabel("New label");
        lblNewLabel.setIcon(new ImageIcon("C:\\Users\\mathe\\Downloads\\elvis-gameculte2.gif"));
        arcadePlaceholder.add(lblNewLabel);
        painelLateral.add(arcadePlaceholder);
        painelLateral.add(Box.createRigidArea(new Dimension(0, 20)));

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

        add(painelLateral, BorderLayout.EAST);
    }

    private JPanel criarPainelFormulario() {
        JPanel painelFormulario = new JPanel(new GridBagLayout());
        painelFormulario.setBackground(Color.GRAY);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0;
        gbc.gridy = 0;
        painelFormulario.add(new JLabel("Produto:"), gbc);

        gbc.gridx = 1;
        comboProduto = new JComboBox<>();
        painelFormulario.add(comboProduto, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        painelFormulario.add(new JLabel("SKU:"), gbc);

        gbc.gridx = 1;
        campoSKU = new JTextField(15);
        campoSKU.setEditable(false);
        painelFormulario.add(campoSKU, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        painelFormulario.add(new JLabel("Preço:"), gbc);

        gbc.gridx = 1;
        campoPreco = new JTextField(15);
        campoPreco.setEditable(false);
        painelFormulario.add(campoPreco, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        painelFormulario.add(new JLabel("Desconto (R$):"), gbc);

        gbc.gridx = 1;
        campoDesconto = new JTextField(10);
        painelFormulario.add(campoDesconto, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        painelFormulario.add(new JLabel("De:"), gbc);

        gbc.gridx = 1;
        campoDataInicio = new JTextField("2025-06-01", 15);
        painelFormulario.add(campoDataInicio, gbc);

        gbc.gridx = 0;
        gbc.gridy = 5;
        painelFormulario.add(new JLabel("Até:"), gbc);

        gbc.gridx = 1;
        campoDataFim = new JTextField("2025-06-30", 15);
        painelFormulario.add(campoDataFim, gbc);

        carregarProdutos();

        comboProduto.addActionListener(e -> {
            String produtoSelecionado = (String) comboProduto.getSelectedItem();
            if (produtoSelecionado != null) {
                campoSKU.setText(produtoParaSKU.get(produtoSelecionado.toLowerCase()));
                Double preco = produtoParaPreco.get(produtoSelecionado.toLowerCase());
                campoPreco.setText(preco != null ? String.format("%.2f", preco) : "");
            } else {
                campoSKU.setText("");
                campoPreco.setText("");
            }
        });

        return painelFormulario;
    }

    private JPanel criarTabelaPromocoes() {
        JPanel painelTabela = new JPanel(new BorderLayout());
        painelTabela.setBackground(Color.GRAY);

        painelTabela.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Color.GRAY),
                "Promoções Salvas",
                javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION,
                javax.swing.border.TitledBorder.DEFAULT_POSITION,
                null,
                Color.BLACK
        ));

        String[] colunas = {"Produto", "SKU", "Preço", "Desconto (R$)", "Período"};
        modeloTabela = new DefaultTableModel(colunas, 0);
        tabelaPromocoes = new JTable(modeloTabela);
        tabelaPromocoes.setBackground(Color.WHITE);
        tabelaPromocoes.setForeground(Color.BLACK);
        JScrollPane scrollPane = new JScrollPane(tabelaPromocoes);
        scrollPane.getViewport().setBackground(Color.WHITE);

        painelTabela.add(scrollPane, BorderLayout.CENTER);
        return painelTabela;
    }

    private void carregarProdutos() {
        try (Connection conn = ConexaoMySQL.conectar()) {
            String sql = "SELECT nome, sku, preco_venda FROM produtos1 ORDER BY nome";
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            produtoParaSKU.clear();
            produtoParaPreco.clear();
            comboProduto.removeAllItems();

            while (rs.next()) {
                String nome = rs.getString("nome");
                String sku = rs.getString("sku");
                double preco = rs.getDouble("preco_venda");

                produtoParaSKU.put(nome.toLowerCase(), sku);
                produtoParaPreco.put(nome.toLowerCase(), preco);
                comboProduto.addItem(nome);
            }

            if (comboProduto.getItemCount() > 0) {
                comboProduto.setSelectedIndex(0);
                String produtoSelecionado = (String) comboProduto.getSelectedItem();
                campoSKU.setText(produtoParaSKU.get(produtoSelecionado.toLowerCase()));
                Double preco = produtoParaPreco.get(produtoSelecionado.toLowerCase());
                campoPreco.setText(preco != null ? String.format("%.2f", preco) : "");
            }

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Erro ao carregar produtos!", "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void salvarPromocao() {
        String produto = (String) comboProduto.getSelectedItem();
        String sku = campoSKU.getText();
        String descontoTexto = campoDesconto.getText().trim();
        String dataInicio = campoDataInicio.getText().trim();
        String dataFim = campoDataFim.getText().trim();
        String precoTexto = campoPreco.getText().replace(",", ".").trim();

        if (produto == null || sku.isEmpty() || descontoTexto.isEmpty() || dataInicio.isEmpty() || dataFim.isEmpty() || precoTexto.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Preencha todos os campos!", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            double desconto = Double.parseDouble(descontoTexto);
            double preco = Double.parseDouble(precoTexto);

            if (desconto > preco) {
                JOptionPane.showMessageDialog(this,
                        "Desconto inválido!\nO desconto não pode ser maior que o preço de venda.",
                        "Erro no Desconto",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (desconto == preco) {
                int resposta = JOptionPane.showConfirmDialog(this,
                        "O desconto é igual ao preço!\nO produto vai sair de graça!\nDeseja continuar mesmo assim?",
                        "Atenção: Produto de graça",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.WARNING_MESSAGE);

                if (resposta != JOptionPane.YES_OPTION) {
                    return;
                }
            }

            String periodo = dataInicio + " ATÉ " + dataFim;

            try (Connection conn = ConexaoMySQL.conectar()) {
                String sql = "INSERT INTO promocoes (nome_produto, sku, desconto, periodo) VALUES (?, ?, ?, ?)";
                PreparedStatement ps = conn.prepareStatement(sql);
                ps.setString(1, produto);
                ps.setString(2, sku);
                ps.setDouble(3, desconto);
                ps.setString(4, periodo);

                int linhas = ps.executeUpdate();
                if (linhas > 0) {
                    JOptionPane.showMessageDialog(this, "Promoção salva com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                    campoDesconto.setText("");
                    carregarTabelaPromocoes();

                    if (relatorioCompra != null) {
                        relatorioCompra.atualizarTabela();
                    }
                } else {
                    JOptionPane.showMessageDialog(this, "Falha ao salvar promoção.", "Erro", JOptionPane.ERROR_MESSAGE);
                }

            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Erro no banco de dados!", "Erro", JOptionPane.ERROR_MESSAGE);
            }

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Valor de desconto inválido! Digite um número válido.", "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void excluirPromocao() {
        int linhaSelecionada = tabelaPromocoes.getSelectedRow();
        if (linhaSelecionada == -1) {
            JOptionPane.showMessageDialog(this, "Selecione uma promoção para excluir!", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String sku = (String) tabelaPromocoes.getValueAt(linhaSelecionada, 1);

        int confirm = JOptionPane.showConfirmDialog(this, "Deseja excluir a promoção com SKU: " + sku + "?", "Confirmar Exclusão", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            try (Connection conn = ConexaoMySQL.conectar()) {
                String sql = "DELETE FROM promocoes WHERE sku = ?";
                PreparedStatement ps = conn.prepareStatement(sql);
                ps.setString(1, sku);

                int linhasAfetadas = ps.executeUpdate();
                if (linhasAfetadas > 0) {
                    JOptionPane.showMessageDialog(this, "Promoção excluída com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                    carregarTabelaPromocoes();

                    if (relatorioCompra != null) {
                        relatorioCompra.atualizarTabela();
                    }
                } else {
                    JOptionPane.showMessageDialog(this, "Promoção não encontrada.", "Erro", JOptionPane.ERROR_MESSAGE);
                }
            } catch (SQLException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Erro ao excluir promoção!", "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void carregarTabelaPromocoes() {
        modeloTabela.setRowCount(0);
        try (Connection conn = ConexaoMySQL.conectar()) {
            String sql = "SELECT nome_produto, sku, desconto, periodo FROM promocoes ORDER BY id DESC";
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Object[] linha = {
                        rs.getString("nome_produto"),
                        rs.getString("sku"),
                        "Consultar Preço",
                        String.format("R$ %.2f", rs.getDouble("desconto")),
                        rs.getString("periodo")
                };
                modeloTabela.addRow(linha);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Erro ao carregar promoções!", "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
}
