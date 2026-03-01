package Trabalho;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import conexão.ConexaoMySQL;
import java.awt.*;
import java.sql.*;
import java.time.LocalDate;

public class RelatorioCompra extends JPanel {

    private JTable historyTable;
    private DefaultTableModel tableModel;
    private JTextField dateStartField;
    private JTextField dateEndField;

    private RelatorioHistoricoCompras relatorioHistorico;

    public RelatorioCompra() {
        this(null);
    }

    public RelatorioCompra(RelatorioHistoricoCompras relatorioHistorico) {
        this.relatorioHistorico = relatorioHistorico;

        setLayout(new BorderLayout());
        setBackground(Color.DARK_GRAY);

        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.setBackground(Color.DARK_GRAY);
        centerPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        add(centerPanel, BorderLayout.CENTER);

        JPanel filterPanel = new JPanel(new BorderLayout());
        filterPanel.setBackground(Color.DARK_GRAY);

        JPanel dateFieldsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        dateFieldsPanel.setBackground(Color.DARK_GRAY);

        JLabel labelDe = new JLabel("De:");
        labelDe.setForeground(Color.WHITE);
        dateFieldsPanel.add(labelDe);

        dateStartField = new JTextField(8);
        dateStartField.setText(LocalDate.now().minusDays(7).toString());
        dateFieldsPanel.add(dateStartField);

        JLabel labelAte = new JLabel("Até:");
        labelAte.setForeground(Color.WHITE);
        dateFieldsPanel.add(labelAte);

        dateEndField = new JTextField(8);
        dateEndField.setText(LocalDate.now().toString());
        dateFieldsPanel.add(dateEndField);

        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 5));
        buttonsPanel.setBackground(Color.DARK_GRAY);

        JButton searchButton = new JButton("Buscar");
        JButton topClients = new JButton("Clientes Mais Valiosos");
        JButton topProducts = new JButton("Produtos Mais Vendidos");
        JButton deleteRow = new JButton("Excluir Venda");
        JButton refreshButton = new JButton("Atualizar Relatório");

        buttonsPanel.add(searchButton);
        buttonsPanel.add(topClients);
        buttonsPanel.add(topProducts);
        buttonsPanel.add(deleteRow);
        buttonsPanel.add(refreshButton);

        filterPanel.add(dateFieldsPanel, BorderLayout.NORTH);
        filterPanel.add(buttonsPanel, BorderLayout.CENTER);

        centerPanel.add(filterPanel, BorderLayout.NORTH);

        String[] columns = {"ID", "Data", "Cliente", "Produto", "Quantidade", "Valor", "Desconto Aplicado (R$)"};
        tableModel = new DefaultTableModel(columns, 0);
        historyTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(historyTable);
        centerPanel.add(scrollPane, BorderLayout.CENTER);
        scrollPane.getViewport().setBackground(Color.WHITE);

        carregarVendasDoBanco();

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

        JPanel arcadePlaceholder = new JPanel();
        arcadePlaceholder.setBackground(Color.DARK_GRAY);
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
        searchButton.addActionListener(e -> filtrarPorData());
        topClients.addActionListener(e -> gerarRelatorioClientes());
        topProducts.addActionListener(e -> gerarRelatorioProdutos());
        deleteRow.addActionListener(e -> excluirVendaSelecionada());
        refreshButton.addActionListener(e -> carregarVendasDoBanco());
    }

    public void atualizarTabela() {
        carregarVendasDoBanco();
    }

    private void carregarVendasDoBanco() {
        tableModel.setRowCount(0);
        String sql = "SELECT id, data, cliente, produto, quantidade, preco AS valor FROM historico_vendas";

        try (Connection con = ConexaoMySQL.conectar();
             PreparedStatement stmt = con.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                int id = rs.getInt("id");
                LocalDate data = rs.getDate("data").toLocalDate();
                String cliente = rs.getString("cliente");
                String produto = rs.getString("produto");
                int quantidade = rs.getInt("quantidade");
                double valor = rs.getDouble("valor");
                String descontoTexto = calcularDescontoAplicado(produto, data, valor);

                tableModel.addRow(new Object[]{id, data.toString(), cliente, produto, quantidade, valor, descontoTexto});
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Erro ao carregar vendas: " + e.getMessage());
        }
    }

    private String calcularDescontoAplicado(String produto, LocalDate dataVenda, double valor) {
        String sql = "SELECT desconto, periodo FROM promocoes WHERE sku = (SELECT sku FROM produtos1 WHERE nome = ?)";
        try (Connection con = ConexaoMySQL.conectar();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setString(1, produto);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                double desconto = rs.getDouble("desconto");
                String periodo = rs.getString("periodo");
                String[] datas = periodo.split("ATÉ");
                if (datas.length == 2) {
                    LocalDate inicio = LocalDate.parse(datas[0].trim());
                    LocalDate fim = LocalDate.parse(datas[1].trim());
                    if (!dataVenda.isBefore(inicio) && !dataVenda.isAfter(fim)) {
                        return String.format("R$ %.2f", desconto);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "—";
    }

    private void filtrarPorData() {
        String inicioTexto = dateStartField.getText().trim();
        String fimTexto = dateEndField.getText().trim();

        try {
            LocalDate inicio = LocalDate.parse(inicioTexto);
            LocalDate fim = LocalDate.parse(fimTexto);

            tableModel.setRowCount(0);
            String sql = "SELECT id, data, cliente, produto, quantidade, preco AS valor FROM historico_vendas WHERE data BETWEEN ? AND ?";

            try (Connection con = ConexaoMySQL.conectar();
                 PreparedStatement stmt = con.prepareStatement(sql)) {

                stmt.setDate(1, java.sql.Date.valueOf(inicio));
                stmt.setDate(2, java.sql.Date.valueOf(fim));
                ResultSet rs = stmt.executeQuery();

                while (rs.next()) {
                    int id = rs.getInt("id");
                    LocalDate data = rs.getDate("data").toLocalDate();
                    String cliente = rs.getString("cliente");
                    String produto = rs.getString("produto");
                    int quantidade = rs.getInt("quantidade");
                    double valor = rs.getDouble("valor");
                    String descontoTexto = calcularDescontoAplicado(produto, data, valor);

                    tableModel.addRow(new Object[]{id, data.toString(), cliente, produto, quantidade, valor, descontoTexto});
                }

            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Erro ao filtrar por data: " + e.getMessage());
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Datas inválidas! Use o formato: yyyy-MM-dd");
        }
    }

    private void gerarRelatorioClientes() {
        String sql = "SELECT cliente, COUNT(*) AS totalVendas, SUM(preco) AS valorTotalVendas, AVG(preco) AS mediaVenda " +
                     "FROM historico_vendas GROUP BY cliente ORDER BY valorTotalVendas DESC LIMIT 10";

        StringBuilder html = new StringBuilder();
        html.append("<html><body style='font-family:Arial,sans-serif;'>");
        html.append("<h2>Relatório Técnico - Clientes Mais Valiosos</h2>");
        html.append("<table border='1' cellpadding='6' cellspacing='0' style='border-collapse:collapse;'>");
        html.append("<tr style='background-color:#0d47a1; color:white; font-weight:bold;'>")
            .append("<th>Posição</th><th>Cliente</th><th>Total de Vendas</th><th>Valor Total (R$)</th><th>Média por Venda (R$)</th></tr>");

        try (Connection con = ConexaoMySQL.conectar();
             PreparedStatement stmt = con.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            int posicao = 1;
            while (rs.next()) {
                String cliente = rs.getString("cliente");
                int totalVendas = rs.getInt("totalVendas");
                double valorTotal = rs.getDouble("valorTotalVendas");
                double valorMedio = rs.getDouble("mediaVenda");

                String corLinha = (posicao <= 3) ? "#bbdefb" : "#ffffff";

                html.append("<tr style='background-color:" + corLinha + ";'>")
                    .append("<td>").append(posicao).append("º</td>")
                    .append("<td>").append(cliente).append("</td>")
                    .append("<td style='text-align:center;'>").append(totalVendas).append("</td>")
                    .append("<td style='text-align:right;'>").append(String.format("R$ %.2f", valorTotal)).append("</td>")
                    .append("<td style='text-align:right;'>").append(String.format("R$ %.2f", valorMedio)).append("</td>")
                    .append("</tr>");

                posicao++;
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Erro ao gerar relatório de clientes: " + e.getMessage());
            return;
        }

        html.append("</table></body></html>");
        mostrarRelatorioEmTelaHTML(html.toString(), "Clientes Mais Valiosos - Relatório Técnico");
    }

    private void gerarRelatorioProdutos() {
        String sql = "SELECT produto, COUNT(*) AS totalVendas, SUM(quantidade) AS quantidadeTotal, SUM(preco) AS valorTotalVenda " +
                     "FROM historico_vendas GROUP BY produto ORDER BY totalVendas DESC LIMIT 10";

        StringBuilder html = new StringBuilder();
        html.append("<html><body style='font-family:Arial,sans-serif;'>");
        html.append("<h2>Relatório Técnico - Produtos Mais Vendidos</h2>");
        html.append("<table border='1' cellpadding='6' cellspacing='0' style='border-collapse:collapse;'>");
        html.append("<tr style='background-color:#0d47a1; color:white; font-weight:bold;'>")
            .append("<th>Posição</th><th>Produto</th><th>Total de Vendas</th><th>Quantidade Total</th><th>Valor Total (R$)</th></tr>");

        try (Connection con = ConexaoMySQL.conectar();
             PreparedStatement stmt = con.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            int posicao = 1;
            while (rs.next()) {
                String produto = rs.getString("produto");
                int totalVendas = rs.getInt("totalVendas");
                int quantidadeTotal = rs.getInt("quantidadeTotal");
                double valorTotal = rs.getDouble("valorTotalVenda");

                String corLinha = (posicao <= 3) ? "#bbdefb" : "#ffffff";

                html.append("<tr style='background-color:" + corLinha + ";'>")
                    .append("<td>").append(posicao).append("º</td>")
                    .append("<td>").append(produto).append("</td>")
                    .append("<td style='text-align:center;'>").append(totalVendas).append("</td>")
                    .append("<td style='text-align:center;'>").append(quantidadeTotal).append("</td>")
                    .append("<td style='text-align:right;'>").append(String.format("R$ %.2f", valorTotal)).append("</td>")
                    .append("</tr>");

                posicao++;
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Erro ao gerar relatório de produtos: " + e.getMessage());
            return;
        }

        html.append("</table></body></html>");
        mostrarRelatorioEmTelaHTML(html.toString(), "Produtos Mais Vendidos - Relatório Técnico");
    }

    private void mostrarRelatorioEmTelaHTML(String htmlTexto, String titulo) {
        JEditorPane editorPane = new JEditorPane("text/html", htmlTexto);
        editorPane.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(editorPane);
        scrollPane.setPreferredSize(new Dimension(700, 400));

        JOptionPane.showMessageDialog(this, scrollPane, titulo, JOptionPane.INFORMATION_MESSAGE);
    }

    private void excluirVendaSelecionada() {
        int linha = historyTable.getSelectedRow();
        if (linha == -1) {
            JOptionPane.showMessageDialog(this, "Selecione uma venda para excluir.");
            return;
        }
        int id = (int) tableModel.getValueAt(linha, 0);
        String sql = "DELETE FROM historico_vendas WHERE id = ?";

        try (Connection con = ConexaoMySQL.conectar();
             PreparedStatement stmt = con.prepareStatement(sql)) {

            stmt.setInt(1, id);
            if (stmt.executeUpdate() > 0) {
                JOptionPane.showMessageDialog(this, "Venda excluída com sucesso.");
                carregarVendasDoBanco();

                if (relatorioHistorico != null) {
                    relatorioHistorico.atualizarRelatorio();
                }
            } else {
                JOptionPane.showMessageDialog(this, "Erro ao excluir venda.");
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Erro ao excluir venda: " + e.getMessage());
        }
    }

    public void setRelatorioHistorico(RelatorioHistoricoCompras relatorioHistorico) {
        this.relatorioHistorico = relatorioHistorico;
    }
}
