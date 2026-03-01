package Trabalho;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import conexão.ConexaoMySQL;
import java.awt.*;
import java.sql.*;
import java.time.LocalDate;

public class RelatorioHistoricoCompras extends JPanel {

    private JTable table;
    private DefaultTableModel model;
    private JTextField campoDataInicio;
    private JTextField campoDataFim;
    private JTextField campoClienteOuProduto;

    private RelatorioCompra relatorioCompra;

    public RelatorioHistoricoCompras() {
        this(null);
    }

    public RelatorioHistoricoCompras(RelatorioCompra relatorioCompra) {
        this.relatorioCompra = relatorioCompra;

        setLayout(new BorderLayout());
        setBackground(Color.DARK_GRAY);

        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topPanel.setBackground(Color.DARK_GRAY);
        topPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        JLabel labelDe = new JLabel("De:");
        labelDe.setForeground(Color.WHITE);
        topPanel.add(labelDe);

        campoDataInicio = new JTextField(10);
        campoDataInicio.setText(LocalDate.now().minusMonths(1).toString());
        topPanel.add(campoDataInicio);

        JLabel labelAte = new JLabel("Até:");
        labelAte.setForeground(Color.WHITE);
        topPanel.add(labelAte);

        campoDataFim = new JTextField(10);
        campoDataFim.setText(LocalDate.now().toString());
        topPanel.add(campoDataFim);

        JLabel labelClienteProduto = new JLabel("Cliente ou Produto:");
        labelClienteProduto.setForeground(Color.WHITE);
        topPanel.add(labelClienteProduto);

        campoClienteOuProduto = new JTextField(15);
        topPanel.add(campoClienteOuProduto);

        JButton btnBuscar = new JButton("Buscar");
        topPanel.add(btnBuscar);

        JButton btnAtualizar = new JButton("Atualizar");
        topPanel.add(btnAtualizar);

        JButton btnExcluir = new JButton("Excluir");
        topPanel.add(btnExcluir);

        add(topPanel, BorderLayout.NORTH);

        model = new DefaultTableModel(
                new String[]{"ID", "Data", "Cliente", "SKU", "Produto", "Quantidade", "Descrição", "Valor", "Pagamento", "Status"}, 0);
        table = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.getViewport().setBackground(Color.WHITE);
        scrollPane.setPreferredSize(new Dimension(900, 400));

        JPanel painelCentral = new JPanel(new BorderLayout());
        painelCentral.setBackground(Color.DARK_GRAY);
        painelCentral.add(scrollPane, BorderLayout.CENTER);
        add(painelCentral, BorderLayout.CENTER);

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

        carregarHistorico();

        btnBuscar.addActionListener(e -> filtrarHistorico());
        btnAtualizar.addActionListener(e -> carregarHistorico());
        btnExcluir.addActionListener(e -> excluirRegistroSelecionado());
    }

    private void carregarHistorico() {
        model.setRowCount(0);
        String sql = "SELECT id, data, cliente, sku, produto, quantidade, descricao, preco AS valor, pagamento, status FROM historico_vendas ORDER BY data DESC";

        try (Connection conn = ConexaoMySQL.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                model.addRow(new Object[]{
                        rs.getInt("id"),
                        rs.getDate("data"),
                        rs.getString("cliente"),
                        rs.getInt("sku"),
                        rs.getString("produto"),
                        rs.getInt("quantidade"),
                        rs.getString("descricao"),
                        rs.getDouble("valor"),
                        rs.getString("pagamento"),
                        rs.getString("status")
                });
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Erro ao carregar histórico: " + e.getMessage());
        }
    }

    private void filtrarHistorico() {
        model.setRowCount(0);
        String inicio = campoDataInicio.getText().trim();
        String fim = campoDataFim.getText().trim();
        String termo = campoClienteOuProduto.getText().trim();

        if (!inicio.matches("\\d{4}-\\d{2}-\\d{2}") || !fim.matches("\\d{4}-\\d{2}-\\d{2}")) {
            JOptionPane.showMessageDialog(this, "Datas no formato inválido. Use YYYY-MM-DD");
            return;
        }

        String sql = "SELECT id, data, cliente, sku, produto, quantidade, descricao, preco AS valor, pagamento, status FROM historico_vendas WHERE data BETWEEN ? AND ?";
        boolean filtrarTermo = !termo.isEmpty();
        if (filtrarTermo) {
            sql += " AND (cliente LIKE ? OR produto LIKE ? OR descricao LIKE ? OR status LIKE ?)";
        }
        sql += " ORDER BY data DESC";

        try (Connection conn = ConexaoMySQL.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setDate(1, java.sql.Date.valueOf(LocalDate.parse(inicio)));
            stmt.setDate(2, java.sql.Date.valueOf(LocalDate.parse(fim)));

            if (filtrarTermo) {
                stmt.setString(3, "%" + termo + "%");
                stmt.setString(4, "%" + termo + "%");
                stmt.setString(5, "%" + termo + "%");
                stmt.setString(6, "%" + termo + "%");
            }

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    model.addRow(new Object[]{
                            rs.getInt("id"),
                            rs.getDate("data"),
                            rs.getString("cliente"),
                            rs.getInt("sku"),
                            rs.getString("produto"),
                            rs.getInt("quantidade"),
                            rs.getString("descricao"),
                            rs.getDouble("valor"),
                            rs.getString("pagamento"),
                            rs.getString("status")
                    });
                }
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Erro ao filtrar histórico: " + e.getMessage());
        }
    }

    private void excluirRegistroSelecionado() {
        int selectedRow = table.getSelectedRow();

        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Selecione uma linha para excluir.");
            return;
        }

        int id = (int) model.getValueAt(selectedRow, 0);
        int confirm = JOptionPane.showConfirmDialog(this,
                "Tem certeza que deseja excluir o registro ID: " + id + "?", "Confirmação", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            String sql = "DELETE FROM historico_vendas WHERE id = ?";

            try (Connection conn = ConexaoMySQL.conectar();
                 PreparedStatement stmt = conn.prepareStatement(sql)) {

                stmt.setInt(1, id);
                int rowsDeleted = stmt.executeUpdate();

                if (rowsDeleted > 0) {
                    JOptionPane.showMessageDialog(this, "Registro excluído com sucesso.");
                    carregarHistorico();

                    if (relatorioCompra != null) {
                        relatorioCompra.atualizarTabela();
                    }
                } else {
                    JOptionPane.showMessageDialog(this, "Registro não encontrado ou já excluído.");
                }

            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Erro ao excluir: " + e.getMessage());
            }
        }
    }

    public void atualizarRelatorio() {
        carregarHistorico();
    }

    public void setRelatorioCompra(RelatorioCompra relatorioCompra) {
        this.relatorioCompra = relatorioCompra;
    }
}
