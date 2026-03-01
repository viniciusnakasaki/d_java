package Trabalho;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.*;
import java.sql.*;
import conexão.ConexaoMySQL;

public class ProdutosPainel extends JPanel {

    private JTable productTable1;
    private DefaultTableModel tableModel1;
    private JTextField[] fields1;
    private JTextField searchField1;
    private JButton editButton1, deleteButton1;
    private JButton btnEstoqueBaixo;
    private String[] labels1 = {"SKU", "Nome", "Descrição", "Preço de Custo", "Preço de Venda", "Quantidade", "Categoria", "Fornecedor"};

    public ProdutosPainel() {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

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

        // Painel central
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setBackground(Color.GRAY);
        centerPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        add(centerPanel, BorderLayout.CENTER);

        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        searchPanel.setBackground(Color.DARK_GRAY);

        searchField1 = new JTextField("SKU ou Nome do Produto", 20);
        JButton searchButton = new JButton("Buscar");
        editButton1 = new JButton("Editar");
        deleteButton1 = new JButton("Excluir");
        JButton refreshButton = new JButton("Atualizar");

        searchButton.addActionListener(e -> buscarProduto1());
        editButton1.addActionListener(e -> editarProduto1());
        deleteButton1.addActionListener(e -> excluirProduto1());
        refreshButton.addActionListener(e -> carregarProdutosDoBanco());

        btnEstoqueBaixo = new JButton("Ver Estoque Baixo");
        btnEstoqueBaixo.addActionListener(e -> {
            java.util.List<String> baixos = produtosEstoqueBaixo();
            if (baixos.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Nenhum produto com estoque baixo.");
            } else {
                String msg = "Produtos com estoque baixo:\n" + String.join("\n", baixos);
                JOptionPane.showMessageDialog(this, msg, "Estoque Baixo", JOptionPane.WARNING_MESSAGE);
            }
        });

        searchPanel.add(searchField1);
        searchPanel.add(searchButton);
        searchPanel.add(editButton1);
        searchPanel.add(deleteButton1);
        searchPanel.add(refreshButton);
        searchPanel.add(btnEstoqueBaixo);

        centerPanel.add(searchPanel);

        tableModel1 = new DefaultTableModel(labels1, 0);
        productTable1 = new JTable(tableModel1);
        carregarProdutosDoBanco();
        JScrollPane scrollPane = new JScrollPane(productTable1);
        centerPanel.add(scrollPane);
        scrollPane.getViewport().setBackground(Color.WHITE);

        GridBagLayout gbl_formPanel = new GridBagLayout();
        gbl_formPanel.columnWidths = new int[]{0, 0, 0};
        gbl_formPanel.columnWeights = new double[]{0.0, 0.0, 0.0};
        gbl_formPanel.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0};
        JPanel formPanel = new JPanel(gbl_formPanel);
        formPanel.setBackground(Color.GRAY);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.VERTICAL;
        gbc.insets = new Insets(2, 5, 2, 5);

        fields1 = new JTextField[labels1.length];
        for (int i = 0; i < labels1.length; i++) {
            JLabel fieldLabel = new JLabel(labels1[i]);
            fields1[i] = new JTextField(20);

            AbstractDocument doc = (AbstractDocument) fields1[i].getDocument();
            if (labels1[i].equalsIgnoreCase("SKU") || labels1[i].equalsIgnoreCase("Quantidade")) {
                doc.setDocumentFilter(new IntegerFilter());
            } else if (labels1[i].equalsIgnoreCase("Preço de Custo") || labels1[i].equalsIgnoreCase("Preço de Venda")) {
                doc.setDocumentFilter(new DecimalFilter(2));
            }

            gbc.gridx = 0;
            gbc.gridy = i;
            formPanel.add(fieldLabel, gbc);
            gbc.gridx = 1;
            formPanel.add(fields1[i], gbc);
        }

        JButton saveButton = new JButton("Salvar");
        saveButton.addActionListener(e -> salvarProduto1());

        gbc.gridx = 2;
        gbc.gridy = 0;
        gbc.gridheight = 7;
        formPanel.add(saveButton, gbc);
        centerPanel.add(formPanel);

        JButton btnNewButton = new JButton("Limpar");
        btnNewButton.addActionListener(e -> {
            for (JTextField field : fields1) {
                field.setText("");
                field.setEditable(true);
            }
        });
        GridBagConstraints gbc_btnNewButton = new GridBagConstraints();
        gbc_btnNewButton.fill = GridBagConstraints.VERTICAL;
        gbc_btnNewButton.insets = new Insets(0, 0, 5, 0);
        gbc_btnNewButton.gridx = 2;
        gbc_btnNewButton.gridy = 7;
        gbc_btnNewButton.gridheight = 1;
        formPanel.add(btnNewButton, gbc_btnNewButton);
    }

    private void carregarProdutosDoBanco() {
        try (Connection conn = ConexaoMySQL.conectar();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM produtos1")) {
            tableModel1.setRowCount(0);
            while (rs.next()) {
                String[] linha = new String[labels1.length];
                for (int i = 0; i < labels1.length; i++) {
                    linha[i] = rs.getString(i + 1);
                }
                tableModel1.addRow(linha);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Erro ao carregar produtos: " + e.getMessage());
        }
    }

    private java.util.List<String> produtosEstoqueBaixo() {
        java.util.List<String> lista = new java.util.ArrayList<>();
        for (int i = 0; i < tableModel1.getRowCount(); i++) {
            try {
                int qtd = Integer.parseInt(tableModel1.getValueAt(i, 5).toString());
                if (qtd <= 5) {
                    String nome = tableModel1.getValueAt(i, 1).toString();
                    lista.add(nome + " (Qtd: " + qtd + ")");
                }
            } catch (NumberFormatException e) {
                // Ignorar erro
            }
        }
        return lista;
    }

    private boolean validarCampos1() {
        for (int i = 0; i < fields1.length; i++) {
            String valor = fields1[i].getText().trim();
            if (valor.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Preencha o campo: " + labels1[i]);
                return false;
            }

            if (labels1[i].equalsIgnoreCase("SKU") || labels1[i].equalsIgnoreCase("Quantidade")) {
                try {
                    int num = Integer.parseInt(valor);
                    if (num < 0) throw new NumberFormatException();
                } catch (NumberFormatException e) {
                    JOptionPane.showMessageDialog(this, labels1[i] + " inválido.");
                    return false;
                }
            } else if (labels1[i].contains("Preço")) {
                try {
                    double preco = Double.parseDouble(valor);
                    if (preco < 0) throw new NumberFormatException();
                } catch (NumberFormatException e) {
                    JOptionPane.showMessageDialog(this, labels1[i] + " inválido.");
                    return false;
                }
            }
        }

        try {
            double precoCusto = Double.parseDouble(fields1[3].getText().trim());
            double precoVenda = Double.parseDouble(fields1[4].getText().trim());
            if (precoCusto > precoVenda) {
                JOptionPane.showMessageDialog(this, "Preço de Custo não pode ser maior que o Preço de Venda.");
                return false;
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Erro ao validar preços.");
            return false;
        }

        return true;
    }

    private void salvarProduto1() {
        if (!validarCampos1()) return;

        try {
            int quantidade = Integer.parseInt(fields1[5].getText().trim());
            if (quantidade < 5) {
                int confirm = JOptionPane.showConfirmDialog(this,
                        "A quantidade cadastrada é inferior a 5 unidades. Deseja continuar mesmo assim?",
                        "Estoque Baixo", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
                if (confirm != JOptionPane.YES_OPTION) {
                    return;
                }
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Quantidade inválida.");
            return;
        }

        try (Connection conn = ConexaoMySQL.conectar()) {
            String sql = "REPLACE INTO produtos1 (sku, nome, descricao, preco_custo, preco_venda, quantidade, categoria, fornecedor) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement ps = conn.prepareStatement(sql);
            for (int i = 0; i < fields1.length; i++) {
                ps.setString(i + 1, fields1[i].getText().trim());
            }

            int realizar = JOptionPane.showConfirmDialog(this, "Deseja salvar?", "Salvar", JOptionPane.YES_NO_OPTION);
            if (realizar == JOptionPane.YES_OPTION) {
                ps.executeUpdate();
                JOptionPane.showMessageDialog(this, "Produto salvo!");
                carregarProdutosDoBanco();
                for (JTextField field : fields1) {
                    field.setText("");
                    field.setEditable(true);
                }
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Erro ao salvar: " + e.getMessage());
        }
    }

    private void editarProduto1() {
        int row = productTable1.getSelectedRow();
        if (row != -1) {
            for (int i = 0; i < fields1.length; i++) {
                fields1[i].setText(tableModel1.getValueAt(row, i).toString());
            }
            fields1[0].setEditable(false);
        } else {
            JOptionPane.showMessageDialog(this, "Selecione um produto para editar.");
        }
    }

    private void excluirProduto1() {
        int row = productTable1.getSelectedRow();
        if (row != -1) {
            String sku = tableModel1.getValueAt(row, 0).toString();
            int confirm = JOptionPane.showConfirmDialog(this, "Deseja excluir este produto?", "Confirmação", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                try (Connection conn = ConexaoMySQL.conectar()) {
                    PreparedStatement ps = conn.prepareStatement("DELETE FROM produtos1 WHERE sku = ?");
                    ps.setString(1, sku);
                    ps.executeUpdate();
                    tableModel1.removeRow(row);
                    JOptionPane.showMessageDialog(this, "Produto excluído.");
                } catch (SQLException e) {
                    JOptionPane.showMessageDialog(this, "Erro ao excluir: " + e.getMessage());
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, "Selecione um produto para excluir.");
        }
    }

    private void buscarProduto1() {
        String termo = searchField1.getText().trim().toLowerCase();
        if (termo.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Digite o SKU ou Nome.");
            return;
        }
        boolean encontrado = false;
        for (int i = 0; i < tableModel1.getRowCount(); i++) {
            String sku = tableModel1.getValueAt(i, 0).toString().toLowerCase();
            String nome = tableModel1.getValueAt(i, 1).toString().toLowerCase();
            if (sku.contains(termo) || nome.contains(termo)) {
                productTable1.setRowSelectionInterval(i, i);
                productTable1.scrollRectToVisible(productTable1.getCellRect(i, 0, true));
                encontrado = true;
                break;
            }
        }
        if (!encontrado) {
            JOptionPane.showMessageDialog(this, "Produto não encontrado.");
        }
    }

    public void atualizarListaProdutos() {
        carregarProdutosDoBanco();
    }

    public static class DecimalFilter extends DocumentFilter {
        private int decimalPlaces;
        public DecimalFilter(int decimalPlaces) {
            this.decimalPlaces = decimalPlaces;
        }
        private boolean isValid(String text) {
            if (text.isEmpty()) return true;
            try {
                if (text.equals(".")) return false;
                double val = Double.parseDouble(text);
                int index = text.indexOf(".");
                return index < 0 || text.length() - index - 1 <= decimalPlaces;
            } catch (NumberFormatException e) {
                return false;
            }
        }
        @Override
        public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr) throws BadLocationException {
            StringBuilder sb = new StringBuilder(fb.getDocument().getText(0, fb.getDocument().getLength()));
            sb.insert(offset, string);
            if (isValid(sb.toString())) {
                super.insertString(fb, offset, string, attr);
            }
        }
        @Override
        public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
            StringBuilder sb = new StringBuilder(fb.getDocument().getText(0, fb.getDocument().getLength()));
            sb.replace(offset, offset + length, text);
            if (isValid(sb.toString())) {
                super.replace(fb, offset, length, text, attrs);
            }
        }
    }

    public static class IntegerFilter extends DocumentFilter {
        private boolean isValid(String text) {
            if (text.isEmpty()) return true;
            try {
                Integer.parseInt(text);
                return true;
            } catch (NumberFormatException e) {
                return false;
            }
        }
        @Override
        public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr) throws BadLocationException {
            StringBuilder sb = new StringBuilder(fb.getDocument().getText(0, fb.getDocument().getLength()));
            sb.insert(offset, string);
            if (isValid(sb.toString())) {
                super.insertString(fb, offset, string, attr);
            }
        }
        @Override
        public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
            StringBuilder sb = new StringBuilder(fb.getDocument().getText(0, fb.getDocument().getLength()));
            sb.replace(offset, offset + length, text);
            if (isValid(sb.toString())) {
                super.replace(fb, offset, length, text, attrs);
            }
        }
    }

    public void recarregarTabela() {
        carregarProdutosDoBanco();
    }
}
