package Trabalho;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.sql.*;
import java.util.HashMap;
import conexão.ConexaoMySQL;

public class Vendas extends JPanel {

    private JTextField campoSKU, campoPrecoVenda, campoQuantidade, campoDesconto;
    private JComboBox<String> campoProduto, campoCliente, campoPagamento, campoStatus;
    private JTextArea campoDescricao;
    private DefaultListModel<String> modeloCarrinho;
    private JList<String> listaCarrinho;

    private HashMap<String, String> produtoPorSKU = new HashMap<>();
    private HashMap<String, Double> precosPorProduto = new HashMap<>();
    private HashMap<String, String> descricaoPorProduto = new HashMap<>();

    private RelatorioHistoricoCompras painelHistorico;
    private ClientePainel painelClientes;

    private Runnable onVendaFinalizada;

    public Vendas(RelatorioHistoricoCompras painelHistorico, ClientePainel painelClientes) {
        this.painelHistorico = painelHistorico;
        this.painelClientes = painelClientes;

        setLayout(new BorderLayout());

        JPanel painelPrincipal = new JPanel();
        painelPrincipal.setLayout(new BoxLayout(painelPrincipal, BoxLayout.Y_AXIS));
        painelPrincipal.setBackground(Color.GRAY);
        painelPrincipal.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        add(painelPrincipal, BorderLayout.CENTER);

        JPanel painelFormulario = new JPanel(new GridBagLayout());
        painelFormulario.setBackground(Color.GRAY);
        painelFormulario.setBorder(new EmptyBorder(10, 10, 10, 10));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        campoProduto = new JComboBox<>();
        campoSKU = new JTextField(20);
        campoSKU.setEditable(false);
        campoPrecoVenda = new JTextField(20);
        campoPrecoVenda.setEditable(false);
        campoQuantidade = new JTextField(20);
        campoDescricao = new JTextArea(3, 20);
        campoDescricao.setEditable(false);
        JScrollPane scrollDescricao = new JScrollPane(campoDescricao);
        campoCliente = new JComboBox<>();
        campoPagamento = new JComboBox<>(new String[]{"Débito", "Crédito", "Pix", "Boleto"});
        campoDesconto = new JTextField(20);
        campoStatus = new JComboBox<>(new String[]{"Finalizado", "Pendente", "Cancelado"});

        carregarProdutosEClientes();

        String[] labels = {"Produto:", "SKU:", "Preço:", "Quantidade:", "Descrição:", "Cliente:", "Pagamento:", "Desconto:", "Status:"};
        Component[] campos = {campoProduto, campoSKU, campoPrecoVenda, campoQuantidade, scrollDescricao, campoCliente, campoPagamento, campoDesconto, campoStatus};

        for (int i = 0; i < labels.length; i++) {
            gbc.gridx = 0;
            gbc.gridy = i;
            painelFormulario.add(new JLabel(labels[i]), gbc);

            gbc.gridx = 1;
            painelFormulario.add(campos[i], gbc);
        }

        painelPrincipal.add(painelFormulario);

        JPanel painelBotoes = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
        painelBotoes.setBackground(Color.DARK_GRAY);

        JButton btnAdicionar = new JButton("Adicionar ao Carrinho");
        JButton btnRemover = new JButton("Remover Item");
        JButton btnFinalizar = new JButton("Finalizar Venda");
        JButton btnAtualizar = new JButton("Atualizar");

        painelBotoes.add(btnAdicionar);
        painelBotoes.add(btnRemover);
        painelBotoes.add(btnFinalizar);
        painelBotoes.add(btnAtualizar);

        painelPrincipal.add(painelBotoes);

        modeloCarrinho = new DefaultListModel<>();
        listaCarrinho = new JList<>(modeloCarrinho);
        JScrollPane carrinhoScroll = new JScrollPane(listaCarrinho);
        carrinhoScroll.setPreferredSize(new Dimension(600, 150));

        JPanel painelCarrinho = new JPanel(new BorderLayout());
        painelCarrinho.setBackground(Color.GRAY);
        painelCarrinho.add(new JLabel("Carrinho de Compras:"), BorderLayout.NORTH);
        painelCarrinho.add(carrinhoScroll, BorderLayout.CENTER);

        painelPrincipal.add(painelCarrinho);

        // Painel lateral direito (Arcade)
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

        campoProduto.addActionListener(e -> atualizarPrecoESKU());
        btnAdicionar.addActionListener(e -> adicionarAoCarrinho());
        btnRemover.addActionListener(e -> removerItemCarrinho());
        btnFinalizar.addActionListener(e -> confirmarEFinalizarVenda());
        btnAtualizar.addActionListener(e -> carregarProdutosEClientes());
    }

    private void carregarProdutosEClientes() {
        try (Connection conn = ConexaoMySQL.conectar()) {
            Statement stmt = conn.createStatement();

            ResultSet rsProdutos = stmt.executeQuery("SELECT sku, nome, preco_venda, descricao FROM produtos1");
            campoProduto.removeAllItems();
            produtoPorSKU.clear();
            precosPorProduto.clear();
            descricaoPorProduto.clear();

            while (rsProdutos.next()) {
                String sku = rsProdutos.getString("sku");
                String nomeProduto = rsProdutos.getString("nome");
                double preco = rsProdutos.getDouble("preco_venda");
                String descricao = rsProdutos.getString("descricao");

                campoProduto.addItem(nomeProduto);
                produtoPorSKU.put(sku, nomeProduto);
                precosPorProduto.put(nomeProduto, preco);
                descricaoPorProduto.put(nomeProduto, descricao);
            }

            ResultSet rsClientes = stmt.executeQuery("SELECT nome FROM clientes");
            campoCliente.removeAllItems();
            while (rsClientes.next()) {
                campoCliente.addItem(rsClientes.getString("nome"));
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Erro ao carregar dados: " + e.getMessage());
        }
    }

    private void atualizarPrecoESKU() {
        String produtoSelecionado = (String) campoProduto.getSelectedItem();
        if (produtoSelecionado != null) {
            Double preco = precosPorProduto.get(produtoSelecionado);
            campoPrecoVenda.setText(String.format("%.2f", preco));

            for (String sku : produtoPorSKU.keySet()) {
                if (produtoPorSKU.get(sku).equals(produtoSelecionado)) {
                    campoSKU.setText(sku);
                    break;
                }
            }
            campoDescricao.setText(descricaoPorProduto.getOrDefault(produtoSelecionado, ""));
        }
    }

    private void adicionarAoCarrinho() {
        String sku = campoSKU.getText().trim();
        String produto = (String) campoProduto.getSelectedItem();
        String precoStr = campoPrecoVenda.getText().trim().replace(",", ".");
        String quantidadeStr = campoQuantidade.getText().trim();
        String descontoStr = campoDesconto.getText().trim().replace(",", ".");
        String pagamento = (String) campoPagamento.getSelectedItem();
        String clienteSelecionado = (String) campoCliente.getSelectedItem();

        if (produto == null || produto.isEmpty() || sku.isEmpty() || precoStr.isEmpty() || quantidadeStr.isEmpty() || clienteSelecionado == null) {
            JOptionPane.showMessageDialog(this, "Preencha todos os campos obrigatórios antes de adicionar.");
            return;
        }

        try {
            int qtd = Integer.parseInt(quantidadeStr);
            double precoUnit = Double.parseDouble(precoStr);
            double desconto = descontoStr.isEmpty() ? 0 : Double.parseDouble(descontoStr);

            if (desconto > precoUnit) {
                JOptionPane.showMessageDialog(this, "Desconto não pode ser maior que o preço unitário.");
                return;
            }

            if (desconto == precoUnit) {
                int confirm = JOptionPane.showConfirmDialog(
                    this,
                    "O desconto é igual ao valor do produto.\nO produto sairá de graça.\nTem certeza disso?",
                    "Confirmação de Desconto Total",
                    JOptionPane.YES_NO_OPTION
                );
                if (confirm != JOptionPane.YES_OPTION) {
                    return;
                }
            }

            double total = (precoUnit * qtd) - desconto;
            if (total < 0) total = 0;

            String item = String.format(
                "sku: %s | Produto: %s | Qtd: %d | Preço: R$ %.2f | Desc: R$ %.2f | Pagto: %s | Total: R$ %.2f",
                sku, produto, qtd, precoUnit, desconto, pagamento, total
            );

            modeloCarrinho.addElement(item);
            campoQuantidade.setText("");
            campoDesconto.setText("");

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Quantidade ou desconto inválido.");
        }
    }

    private void removerItemCarrinho() {
        int index = listaCarrinho.getSelectedIndex();
        if (index >= 0) {
            int confirm = JOptionPane.showConfirmDialog(this, "Remover item selecionado?", "Remover Item", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                modeloCarrinho.remove(index);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Selecione um item no carrinho para remover.");
        }
    }

    private void confirmarEFinalizarVenda() {
        if (modeloCarrinho.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Carrinho vazio. Adicione produtos antes de finalizar.");
            return;
        }

        String clienteSelecionado = (String) campoCliente.getSelectedItem();
        if (clienteSelecionado == null || clienteSelecionado.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Selecione um cliente.");
            return;
        }

        int confirmar = JOptionPane.showConfirmDialog(this, "Deseja finalizar a venda?", "Confirmar Venda", JOptionPane.YES_NO_OPTION);
        if (confirmar == JOptionPane.YES_OPTION) {
            realizarVenda();
            modeloCarrinho.clear();

            if (painelHistorico != null) painelHistorico.atualizarRelatorio();
            if (painelClientes != null) painelClientes.atualizarListaClientes();
            if (onVendaFinalizada != null) onVendaFinalizada.run();
        }
    }

    private void realizarVenda() {
        try (Connection conn = ConexaoMySQL.conectar()) {
            conn.setAutoCommit(false);

            for (int i = 0; i < modeloCarrinho.size(); i++) {
                String item = modeloCarrinho.get(i);
                String[] partes = item.split("\\|");

                String sku = partes[0].split(":")[1].trim();
                String produto = partes[1].split(":")[1].trim();
                int quantidade = Integer.parseInt(partes[2].split(":")[1].trim());
                double precoUnit = Double.parseDouble(partes[3].split(":")[1].trim().replace("R$", "").replace(",", "."));
                double desconto = Double.parseDouble(partes[4].split(":")[1].trim().replace("R$", "").replace(",", "."));
                String pagamento = partes[5].split(":")[1].trim();
                String cliente = (String) campoCliente.getSelectedItem();
                String categoria = buscarCategoriaProduto(sku);

                PreparedStatement psVerificarEstoque = conn.prepareStatement("SELECT quantidade FROM produtos1 WHERE sku = ?");
                psVerificarEstoque.setString(1, sku);
                ResultSet rsEstoque = psVerificarEstoque.executeQuery();
                if (rsEstoque.next()) {
                    int estoqueAtual = rsEstoque.getInt("quantidade");
                    if (estoqueAtual < quantidade) {
                        JOptionPane.showMessageDialog(this, "Estoque insuficiente para o SKU: " + sku);
                        conn.rollback();
                        return;
                    }
                } else {
                    JOptionPane.showMessageDialog(this, "Produto com SKU " + sku + " não encontrado no estoque.");
                    conn.rollback();
                    return;
                }

                PreparedStatement psEstoque = conn.prepareStatement(
                    "UPDATE produtos1 SET quantidade = quantidade - ? WHERE sku = ?"
                );
                psEstoque.setInt(1, quantidade);
                psEstoque.setString(2, sku);
                psEstoque.executeUpdate();

                PreparedStatement psVenda = conn.prepareStatement(
                    "INSERT INTO historico_vendas (sku, cliente, data, produto, descricao, quantidade, preco, desconto, pagamento, status, categoria) VALUES (?, ?, NOW(), ?, ?, ?, ?, ?, ?, ?, ?)"
                );
                psVenda.setString(1, sku);
                psVenda.setString(2, cliente);
                psVenda.setString(3, produto);
                psVenda.setString(4, descricaoPorProduto.getOrDefault(produto, ""));
                psVenda.setInt(5, quantidade);
                psVenda.setDouble(6, precoUnit);
                psVenda.setDouble(7, desconto);
                psVenda.setString(8, pagamento);
                psVenda.setString(9, (String) campoStatus.getSelectedItem());
                psVenda.setString(10, categoria);
                psVenda.executeUpdate();
            }

            conn.commit();
            JOptionPane.showMessageDialog(this, "Venda realizada com sucesso!");

            carregarProdutosEClientes();

        } catch (SQLException | NumberFormatException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Erro ao salvar a venda: " + e.getMessage());
        }
    }

    private String buscarCategoriaProduto(String sku) {
        String sql = "SELECT categoria FROM produtos1 WHERE sku = ?";
        try (Connection conn = ConexaoMySQL.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, sku);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("categoria");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "Sem Categoria";
    }

    public void setOnVendaFinalizada(Runnable callback) {
        this.onVendaFinalizada = callback;
    }
}
