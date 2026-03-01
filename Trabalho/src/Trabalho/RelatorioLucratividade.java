package Trabalho;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;
import conexão.ConexaoMySQL;

public class RelatorioLucratividade extends JPanel {

    private JTable tabela;
    private JTextField campoData;
    private JComboBox<String> comboCategoria;
    private DefaultTableModel modeloTabela;

    public RelatorioLucratividade() {
        setLayout(new BorderLayout(10, 10));
        setBackground(Color.GRAY);
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Painel superior (Filtros)
        JPanel painelTopo = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        painelTopo.setBackground(Color.DARK_GRAY);

        campoData = new JTextField("YYYY-MM-DD", 10);
        campoData.addFocusListener(new java.awt.event.FocusAdapter() {
            @Override
            public void focusGained(java.awt.event.FocusEvent evt) {
                if (campoData.getText().equals("YYYY-MM-DD")) {
                    campoData.setText("");
                }
            }

            @Override
            public void focusLost(java.awt.event.FocusEvent evt) {
                if (campoData.getText().isEmpty()) {
                    campoData.setText("YYYY-MM-DD");
                }
            }
        });
        painelTopo.add(campoData);

        JButton btnBuscarData = new JButton("🔍");
        painelTopo.add(btnBuscarData);

        comboCategoria = new JComboBox<>();
        comboCategoria.addItem("Todas");
        carregarCategorias();
        painelTopo.add(comboCategoria);

        JButton btnBuscarCategoria = new JButton("🔍");
        painelTopo.add(btnBuscarCategoria);

        add(painelTopo, BorderLayout.NORTH);

        // Tabela
        String[] colunas = {"Código", "Cliente", "Data de Venda", "Descrição", "Quantidade", "Preço Unitário",
                "Desconto", "Pagamento", "Status", "Categoria"};
        modeloTabela = new DefaultTableModel(colunas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tabela = new JTable(modeloTabela);
        JScrollPane scrollPane = new JScrollPane(tabela);
        scrollPane.setPreferredSize(new Dimension(900, 400));
        add(scrollPane, BorderLayout.CENTER);
        scrollPane.getViewport().setBackground(Color.WHITE);

        carregarHistoricoDoBanco();

        // Painel inferior
        JPanel painelInferior = new JPanel();
        painelInferior.setLayout(new BoxLayout(painelInferior, BoxLayout.X_AXIS));
        painelInferior.setBackground(Color.DARK_GRAY);

        JPanel painelBotoes = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        painelBotoes.setBackground(Color.DARK_GRAY);

        JButton btnAnalise = new JButton("Análise de Lucratividade");
        painelBotoes.add(btnAnalise);

        JButton btnExcluir = new JButton("Excluir Registro");
        painelBotoes.add(btnExcluir);

        JButton btnAtualizar = new JButton("Atualizar Relatório");
        painelBotoes.add(btnAtualizar);

        painelInferior.add(painelBotoes);
        add(painelInferior, BorderLayout.SOUTH);

        // Painel lateral direito
        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
        rightPanel.setBackground(Color.DARK_GRAY);
        rightPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        rightPanel.setPreferredSize(new Dimension(300, 600));

        JButton startButton = new JButton("START");
        startButton.setFont(new Font("Monospaced", Font.BOLD, 24));
        startButton.setBackground(new Color(0, 180, 0));
        startButton.setForeground(Color.WHITE);
        startButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPanel arcadePlaceholder = new JPanel(new GridBagLayout());
        arcadePlaceholder.setBackground(Color.GRAY);
        arcadePlaceholder.setPreferredSize(new Dimension(150, 250));
        JLabel lblNewLabel = new JLabel(new ImageIcon("C:\\Users\\mathe\\Downloads\\elvis-gameculte2.gif"));
        arcadePlaceholder.add(lblNewLabel);

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

        rightPanel.add(startButton);
        rightPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        rightPanel.add(arcadePlaceholder);
        rightPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        rightPanel.add(exitButton);

        add(rightPanel, BorderLayout.EAST);

        // Ações dos botões
        btnBuscarData.addActionListener(e -> buscarPorData());
        btnBuscarCategoria.addActionListener(e -> buscarPorCategoria());
        btnAnalise.addActionListener(e -> mostrarAnaliseLucro());
        btnExcluir.addActionListener(e -> excluirRegistroSelecionado());
        btnAtualizar.addActionListener(e -> carregarHistoricoDoBanco());
    }

    private void carregarCategorias() {
        String sql = "SELECT DISTINCT categoria FROM produtos1 WHERE categoria IS NOT NULL";

        try (Connection conn = ConexaoMySQL.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                comboCategoria.addItem(rs.getString("categoria"));
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Erro ao carregar categorias: " + e.getMessage());
        }
    }

    private void carregarHistoricoDoBanco() {
        modeloTabela.setRowCount(0);
        String sql = "SELECT id, cliente, data, descricao, quantidade, preco, desconto, pagamento, status, categoria FROM historico_vendas";

        try (Connection con = ConexaoMySQL.conectar();
             PreparedStatement stmt = con.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                modeloTabela.addRow(new Object[]{
                        rs.getInt("id"),
                        rs.getString("cliente"),
                        rs.getDate("data"),
                        rs.getString("descricao"),
                        rs.getInt("quantidade"),
                        rs.getDouble("preco"),
                        rs.getDouble("desconto"),
                        rs.getString("pagamento"),
                        rs.getString("status"),
                        rs.getString("categoria")
                });
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Erro ao carregar dados: " + e.getMessage());
        }
    }

    private void buscarPorData() {
        String data = campoData.getText().trim();
        if (data.isEmpty() || data.equals("YYYY-MM-DD")) {
            JOptionPane.showMessageDialog(this, "Digite uma data válida para buscar.", "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }

        modeloTabela.setRowCount(0);
        String sql = "SELECT id, cliente, data, descricao, quantidade, preco, desconto, pagamento, status, categoria FROM historico_vendas WHERE data = ?";

        try (Connection con = ConexaoMySQL.conectar();
             PreparedStatement stmt = con.prepareStatement(sql)) {

            stmt.setDate(1, java.sql.Date.valueOf(data));
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    modeloTabela.addRow(new Object[]{
                            rs.getInt("id"),
                            rs.getString("cliente"),
                            rs.getDate("data"),
                            rs.getString("descricao"),
                            rs.getInt("quantidade"),
                            rs.getDouble("preco"),
                            rs.getDouble("desconto"),
                            rs.getString("pagamento"),
                            rs.getString("status"),
                            rs.getString("categoria")
                    });
                }
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Erro ao buscar por data: " + e.getMessage());
        }
    }

    private void buscarPorCategoria() {
        String categoriaSelecionada = (String) comboCategoria.getSelectedItem();
        if (categoriaSelecionada == null || categoriaSelecionada.equals("Todas")) {
            carregarHistoricoDoBanco();
            return;
        }

        modeloTabela.setRowCount(0);
        String sql = "SELECT id, cliente, data, descricao, quantidade, preco, desconto, pagamento, status, categoria FROM historico_vendas WHERE categoria = ?";

        try (Connection con = ConexaoMySQL.conectar();
             PreparedStatement stmt = con.prepareStatement(sql)) {

            stmt.setString(1, categoriaSelecionada);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    modeloTabela.addRow(new Object[]{
                            rs.getInt("id"),
                            rs.getString("cliente"),
                            rs.getDate("data"),
                            rs.getString("descricao"),
                            rs.getInt("quantidade"),
                            rs.getDouble("preco"),
                            rs.getDouble("desconto"),
                            rs.getString("pagamento"),
                            rs.getString("status"),
                            rs.getString("categoria")
                    });
                }
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Erro ao buscar por categoria: " + e.getMessage());
        }
    }

    private void excluirRegistroSelecionado() {
        int linhaSelecionada = tabela.getSelectedRow();

        if (linhaSelecionada == -1) {
            JOptionPane.showMessageDialog(this, "Selecione uma linha para excluir.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this, "Tem certeza que deseja excluir este registro?", "Confirmação", JOptionPane.YES_NO_OPTION);
        if (confirm != JOptionPane.YES_OPTION) {
            return;
        }

        int idRegistro = (int) modeloTabela.getValueAt(linhaSelecionada, 0);

        String sql = "DELETE FROM historico_vendas WHERE id = ?";

        try (Connection conn = ConexaoMySQL.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idRegistro);
            int linhasAfetadas = stmt.executeUpdate();

            if (linhasAfetadas > 0) {
                modeloTabela.removeRow(linhaSelecionada);
                JOptionPane.showMessageDialog(this, "Registro excluído com sucesso.");
            } else {
                JOptionPane.showMessageDialog(this, "Falha ao excluir o registro.");
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Erro ao excluir: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void mostrarAnaliseLucro() {
        double lucroBrutoTotal = 0;
        double descontoTotal = 0;
        double lucroLiquidoTotal = 0;
        double receitaTotal = 0;
        int quantidadeTotal = 0;

        StringBuilder relatorio = new StringBuilder("<html><body style='font-family:sans-serif;'>");
        relatorio.append("<h2 style='color: darkblue;'>Análise Detalhada de Lucratividade por Produto</h2>");
        relatorio.append("<table border='1' cellpadding='6' cellspacing='0' style='border-collapse: collapse; width: 100%;'>");
        relatorio.append("<tr style='background-color:#4CAF50; color: white;'>");
        relatorio.append("<th>Produto</th><th>Qtd Vendida</th><th>Preço Unitário (R$)</th><th>Preço Custo (R$)</th><th>Desconto Total (R$)</th><th>Lucro Bruto Unit. (R$)</th><th>Lucro Líquido (R$)</th><th>% Lucro Líquido</th></tr>");

        for (int i = 0; i < modeloTabela.getRowCount(); i++) {
            try {
                String nomeProduto = modeloTabela.getValueAt(i, 3).toString();
                int quantidade = Integer.parseInt(modeloTabela.getValueAt(i, 4).toString());
                double precoVenda = Double.parseDouble(modeloTabela.getValueAt(i, 5).toString());
                double precoCompra = obterPrecoCompra(nomeProduto);
                double desconto = Double.parseDouble(modeloTabela.getValueAt(i, 6).toString());

                double lucroBrutoUnit = precoVenda - precoCompra;
                double lucroBrutoTotalProduto = lucroBrutoUnit * quantidade;
                double descontoTotalProduto = desconto * quantidade;
                double lucroLiquidoProduto = lucroBrutoTotalProduto - descontoTotalProduto;
                double percentualLucroLiquido = precoVenda != 0 ? (lucroLiquidoProduto / (precoVenda * quantidade)) * 100 : 0;

                lucroBrutoTotal += lucroBrutoTotalProduto;
                descontoTotal += descontoTotalProduto;
                lucroLiquidoTotal += lucroLiquidoProduto;
                receitaTotal += precoVenda * quantidade;
                quantidadeTotal += quantidade;

                String corLinha = (i % 2 == 0) ? "#f9f9f9" : "white";

                relatorio.append("<tr style='background-color:").append(corLinha).append(";'>")
                         .append("<td>").append(nomeProduto).append("</td>")
                         .append("<td align='center'>").append(quantidade).append("</td>")
                         .append("<td align='right'>").append(String.format("%.2f", precoVenda)).append("</td>")
                         .append("<td align='right'>").append(String.format("%.2f", precoCompra)).append("</td>")
                         .append("<td align='right' style='color:red;'>").append(String.format("-%.2f", descontoTotalProduto)).append("</td>")
                         .append("<td align='right'>").append(String.format("%.2f", lucroBrutoTotalProduto)).append("</td>")
                         .append("<td align='right' style='font-weight:bold;'>").append(String.format("%.2f", lucroLiquidoProduto)).append("</td>")
                         .append("<td align='right'>").append(String.format("%.2f%%", percentualLucroLiquido)).append("</td>")
                         .append("</tr>");

            } catch (NumberFormatException ex) {
                relatorio.append("<tr><td colspan='8' style='color:red;'>Erro ao calcular lucro na linha ").append(i + 1).append("</td></tr>");
            }
        }

        relatorio.append("<tr style='background-color:#ddd; font-weight:bold;'>")
                 .append("<td>Total Geral</td>")
                 .append("<td align='center'>").append(quantidadeTotal).append("</td>")
                 .append("<td></td><td></td>")
                 .append("<td align='right' style='color:red;'>").append(String.format("-%.2f", descontoTotal)).append("</td>")
                 .append("<td align='right'>").append(String.format("%.2f", lucroBrutoTotal)).append("</td>")
                 .append("<td align='right'>").append(String.format("%.2f", lucroLiquidoTotal)).append("</td>")
                 .append("<td align='right'>").append(quantidadeTotal > 0 ? String.format("%.2f%%", (lucroLiquidoTotal / receitaTotal) * 100) : "0.00%").append("</td>")
                 .append("</tr>");

        relatorio.append("</table></body></html>");

        JEditorPane editorPane = new JEditorPane("text/html", relatorio.toString());
        editorPane.setEditable(false);
        editorPane.setCaretPosition(0);

        JScrollPane scrollPane = new JScrollPane(editorPane);
        scrollPane.setPreferredSize(new Dimension(800, 400));

        JOptionPane.showMessageDialog(this, scrollPane, "Relatório Detalhado de Lucratividade", JOptionPane.INFORMATION_MESSAGE);
    }

    private double obterPrecoCompra(String nomeProduto) {
        String sql = "SELECT preco_custo FROM produtos1 WHERE nome = ?";
        try (Connection con = ConexaoMySQL.conectar();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setString(1, nomeProduto);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getDouble("preco_custo");
                }
            }
        } catch (SQLException e) {
            System.err.println("Erro ao obter preço de custo: " + e.getMessage());
        }
        return 0;
    }
}
