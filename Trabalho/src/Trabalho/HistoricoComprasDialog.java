package Trabalho;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import conexão.ConexaoMySQL;

public class HistoricoComprasDialog extends JDialog {

    private JTable tabela;
    private DefaultTableModel model;

    public HistoricoComprasDialog(Window parent, String nomeCliente) {
        super(parent, "Histórico de Compras - " + nomeCliente, ModalityType.APPLICATION_MODAL);

        setLayout(new BorderLayout());

        String[] colunas = {"Produto", "Descrição", "Quantidade", "Preço", "Desconto", "Data", "Status"};
        model = new DefaultTableModel(colunas, 0) {
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tabela = new JTable(model);
        tabela.setRowHeight(25);
        JScrollPane scroll = new JScrollPane(tabela);
        add(scroll, BorderLayout.CENTER);

        carregarHistorico(nomeCliente);

        setSize(700, 400);
        setLocationRelativeTo(parent);
    }

    private void carregarHistorico(String nomeCliente) {
        String sql = "SELECT produto, descricao, quantidade, preco, desconto, data, status " +
                     "FROM historico_vendas WHERE cliente = ? ORDER BY data DESC";

        try (Connection con = ConexaoMySQL.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, nomeCliente);

            try (ResultSet rs = ps.executeQuery()) {
                boolean encontrou = false;
                while (rs.next()) {
                    encontrou = true;
                    String produto = rs.getString("produto");
                    String descricao = rs.getString("descricao");
                    int quantidade = rs.getInt("quantidade");
                    double preco = rs.getDouble("preco");
                    double desconto = rs.getDouble("desconto");
                    String data = rs.getString("data");
                    String status = rs.getString("status");

                    model.addRow(new Object[]{produto, descricao, quantidade, preco, desconto, data, status});
                }

                if (!encontrou) {
                    JOptionPane.showMessageDialog(this, "Nenhuma compra encontrada para este cliente.",
                            "Histórico Vazio", JOptionPane.INFORMATION_MESSAGE);
                    dispose();
                }
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro ao carregar histórico: " + e.getMessage(),
                    "Erro", JOptionPane.ERROR_MESSAGE);
            dispose();
        }
    }
}
