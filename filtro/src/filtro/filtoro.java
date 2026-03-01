package filtro;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;

public class filtoro extends JFrame {

    private JTable tabela;
    private JTextField campoFiltro;
    private TableRowSorter<TableModel> sorter;

    public filtoro() {
        setTitle("Exemplo de Filtro");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 400);
        setLocationRelativeTo(null);

        String[] colunas = {"Produto", "Cliente", "Quantidade"};
        Object[][] dados = {
            {"Café", "João", 30},
            {"Leite", "Maria", 10},
            {"Açúcar", "João", 5},
            {"Pão", "Carlos", 12}
        };

        DefaultTableModel modelo = new DefaultTableModel(dados, colunas);
        tabela = new JTable(modelo);
        sorter = new TableRowSorter<>(modelo);
        tabela.setRowSorter(sorter);

        campoFiltro = new JTextField(15);
        JButton botaoFiltrar = new JButton("Filtrar Cliente 'João'");

        botaoFiltrar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                sorter.setRowFilter(RowFilter.regexFilter("João", 1)); // coluna 1 = Cliente
            }
        });

        JButton limparFiltro = new JButton("Limpar Filtro");
        limparFiltro.addActionListener(e -> sorter.setRowFilter(null));

        JPanel painelFiltro = new JPanel();
        painelFiltro.add(botaoFiltrar);
        painelFiltro.add(limparFiltro);

        add(painelFiltro, BorderLayout.NORTH);
        add(new JScrollPane(tabela), BorderLayout.CENTER);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new filtoro().setVisible(true);
        });
    }
}