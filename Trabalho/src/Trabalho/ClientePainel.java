package Trabalho;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;
import javax.swing.text.MaskFormatter;

import conexão.ConexaoMySQL;

import java.awt.*;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class ClientePainel extends JPanel {

    private JTable tabela;
    private DefaultTableModel model;
    private JTextField campoBusca;
    private JTextField[] campos;
    private JButton btnEditar, btnExcluir;
    private int linhaSelecionada = -1;

    public ClientePainel() {
        setLayout(new BorderLayout());

        JPanel painelPrincipal = new JPanel();
        painelPrincipal.setLayout(new BoxLayout(painelPrincipal, BoxLayout.Y_AXIS));
        painelPrincipal.setBackground(Color.GRAY);
        painelPrincipal.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        add(painelPrincipal, BorderLayout.CENTER);

        JPanel painelBusca = new JPanel(new FlowLayout(FlowLayout.CENTER));
        painelBusca.setBackground(Color.DARK_GRAY);

        campoBusca = new JTextField(20);
        painelBusca.add(campoBusca);

        JButton btnBuscar = new JButton("Buscar");
        painelBusca.add(btnBuscar);

        btnEditar = new JButton("Editar");
        btnEditar.setEnabled(false);
        painelBusca.add(btnEditar);

        btnExcluir = new JButton("Excluir");
        btnExcluir.setEnabled(false);
        painelBusca.add(btnExcluir);

        painelPrincipal.add(painelBusca);

        String[] colunas = {"Id", "Nome", "Endereço", "Telefone", "Email"};
        model = new DefaultTableModel(colunas, 0) {
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tabela = new JTable(model);
        tabela.setRowHeight(25);
        tabela.setBackground(Color.WHITE);
        JScrollPane scrollPane = new JScrollPane(tabela);
        scrollPane.getViewport().setBackground(Color.WHITE);

        JPanel painelTabela = new JPanel(new BorderLayout());
        painelTabela.add(scrollPane, BorderLayout.CENTER);
        painelPrincipal.add(painelTabela);

        tabela.getSelectionModel().addListSelectionListener(e -> {
            linhaSelecionada = tabela.getSelectedRow();
            btnEditar.setEnabled(linhaSelecionada >= 0);
            btnExcluir.setEnabled(linhaSelecionada >= 0);
        });

        JPanel painelFormulario = new JPanel(new GridBagLayout());
        painelFormulario.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        painelFormulario.setBackground(Color.GRAY);
        painelPrincipal.add(painelFormulario);

        String[] labels = {"Id", "Nome", "Endereço", "Telefone", "E-mail"};
        campos = new JTextField[labels.length];
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        for (int i = 0; i < labels.length; i++) {
            gbc.gridx = 0;
            gbc.gridy = i;
            painelFormulario.add(new JLabel(labels[i] + ":"), gbc);

            gbc.gridx = 1;

            if (i == 3) {  // Campo Telefone com máscara
                try {
                    MaskFormatter telefoneMask = new MaskFormatter("(##)#####-####");
                    telefoneMask.setPlaceholderCharacter('_');
                    JFormattedTextField ftfTelefone = new JFormattedTextField(telefoneMask);
                    ftfTelefone.setColumns(20);
                    campos[i] = ftfTelefone;
                    painelFormulario.add(ftfTelefone, gbc);
                } catch (Exception e) {
                    e.printStackTrace();
                    // fallback para JTextField comum se der erro
                    campos[i] = new JTextField(20);
                    painelFormulario.add(campos[i], gbc);
                }
            } else {
                campos[i] = new JTextField(20);
                painelFormulario.add(campos[i], gbc);
            }
        }

        // Campo ID só aceita números
        ((AbstractDocument) campos[0].getDocument()).setDocumentFilter(new NumeroDocumentFilter());

        gbc.gridx = 1;
        gbc.gridy = labels.length;
        JButton btnSalvar = new JButton("Salvar");
        painelFormulario.add(btnSalvar, gbc);

        gbc.gridy++;
        JButton btnHistorico = new JButton("Histórico de Compra");
        painelFormulario.add(btnHistorico, gbc);

        gbc.gridy++;
        JButton btnLimpar = new JButton("Limpar");
        painelFormulario.add(btnLimpar, gbc);

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

        carregarClientesDoBanco();

        btnBuscar.addActionListener(e -> buscarCliente());
        btnEditar.addActionListener(e -> editarCliente());
        btnExcluir.addActionListener(e -> excluirCliente());
        btnSalvar.addActionListener(e -> salvarCliente());
        btnHistorico.addActionListener(e -> mostrarHistorico());
        btnLimpar.addActionListener(e -> limparCampos());
    }

    private void carregarClientesDoBanco() {
        try (Connection con = ConexaoMySQL.conectar();
             Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM clientes")) {

            while (rs.next()) {
                model.addRow(new Object[]{
                        rs.getString("id"),
                        rs.getString("nome"),
                        rs.getString("endereco"),
                        rs.getString("telefone"),
                        rs.getString("email")
                });
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro ao carregar clientes: " + e.getMessage());
        }
    }

    private void buscarCliente() {
        String nomeBusca = campoBusca.getText().trim().toLowerCase();
        if (nomeBusca.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Digite um nome para buscar.");
            return;
        }

        boolean encontrado = false;
        for (int i = 0; i < model.getRowCount(); i++) {
            String nomeTabela = model.getValueAt(i, 1).toString().toLowerCase();
            if (nomeTabela.contains(nomeBusca)) {
                tabela.setRowSelectionInterval(i, i);
                encontrado = true;
                break;
            }
        }

        if (!encontrado) {
            JOptionPane.showMessageDialog(this, "Cliente não encontrado.");
        }
    }

    private void editarCliente() {
        if (linhaSelecionada >= 0) {
            for (int i = 0; i < campos.length; i++) {
                campos[i].setText(model.getValueAt(linhaSelecionada, i).toString());
            }
            campos[0].setEditable(false); // Bloqueia o campo ID ao editar
        }
    }

    private void excluirCliente() {
        if (linhaSelecionada >= 0) {
            int confirm = JOptionPane.showConfirmDialog(this, "Excluir cliente?", "Confirmação", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                String id = model.getValueAt(linhaSelecionada, 0).toString();
                try (Connection con = ConexaoMySQL.conectar();
                     var ps = con.prepareStatement("DELETE FROM clientes WHERE id = ?")) {
                    ps.setString(1, id);
                    ps.executeUpdate();

                    model.removeRow(linhaSelecionada);
                    linhaSelecionada = -1;
                    btnEditar.setEnabled(false);
                    btnExcluir.setEnabled(false);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Erro ao excluir cliente: " + ex.getMessage());
                }
            }
        }
    }

    private void salvarCliente() {
        String[] dadosCliente = new String[campos.length];
        for (int i = 0; i < campos.length; i++) {
            dadosCliente[i] = campos[i].getText().trim();
            if (dadosCliente[i].isEmpty()) {
                JOptionPane.showMessageDialog(this, "Preencha todos os campos.");
                return;
            }
        }

        // Validar telefone removendo máscara e verificando números
        String telefoneNumeros = dadosCliente[3].replaceAll("[^0-9]", "");
        if (!telefoneNumeros.matches("\\d{10,11}")) {
            JOptionPane.showMessageDialog(this, "Telefone inválido. Use o formato (XX)XXXXX-XXXX.");
            return;
        }

        String idDigitado = dadosCliente[0];
        boolean idExiste = false;
        for (int i = 0; i < model.getRowCount(); i++) {
            if (model.getValueAt(i, 0).toString().equals(idDigitado)) {
                if (linhaSelecionada == -1 || i != linhaSelecionada) {
                    idExiste = true;
                    break;
                }
            }
        }

        if (idExiste) {
            JOptionPane.showMessageDialog(this, "Já existe um cliente com esse ID.");
            return;
        }

        try (Connection con = ConexaoMySQL.conectar()) {
            boolean atualizar = (linhaSelecionada >= 0);

            if (atualizar) {
                var ps = con.prepareStatement("UPDATE clientes SET nome=?, endereco=?, telefone=?, email=? WHERE id=?");
                ps.setString(1, dadosCliente[1]);
                ps.setString(2, dadosCliente[2]);
                ps.setString(3, dadosCliente[3]);
                ps.setString(4, dadosCliente[4]);
                ps.setString(5, dadosCliente[0]);
                ps.executeUpdate();

                for (int i = 0; i < campos.length; i++) {
                    model.setValueAt(dadosCliente[i], linhaSelecionada, i);
                }

                JOptionPane.showMessageDialog(this, "Cliente atualizado com sucesso.");
            } else {
                var ps = con.prepareStatement("INSERT INTO clientes (id, nome, endereco, telefone, email) VALUES (?, ?, ?, ?, ?)");
                for (int i = 0; i < campos.length; i++) {
                    ps.setString(i + 1, dadosCliente[i]);
                }
                int realizar = JOptionPane.showConfirmDialog(this, "Deseja salvar?", "Salvar", JOptionPane.YES_NO_OPTION);
                if (realizar == JOptionPane.YES_OPTION) {
                    ps.executeUpdate();
                    model.addRow(dadosCliente);
                    JOptionPane.showMessageDialog(this, "Novo cliente adicionado com sucesso.");
                }
            }

            limparCampos();

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erro ao salvar cliente: " + ex.getMessage());
        }
    }

    private void limparCampos() {
        // Libera edição do campo ID
        campos[0].setEditable(true);

        // Reseta documento do campo ID para garantir limpeza total
        campos[0].setDocument(new javax.swing.text.PlainDocument());
        ((AbstractDocument) campos[0].getDocument()).setDocumentFilter(new NumeroDocumentFilter());

        // Limpa os demais campos normalmente
        for (int i = 1; i < campos.length; i++) {
            campos[i].setText("");
        }

        tabela.clearSelection();
        linhaSelecionada = -1;
        btnEditar.setEnabled(false);
        btnExcluir.setEnabled(false);
    }

    private void mostrarHistorico() {
        if (linhaSelecionada >= 0) {
            String clienteNome = model.getValueAt(linhaSelecionada, 1).toString();
            HistoricoComprasDialog dialog = new HistoricoComprasDialog(SwingUtilities.getWindowAncestor(this), clienteNome);
            dialog.setVisible(true);
        } else {
            JOptionPane.showMessageDialog(this, "Selecione um cliente primeiro.");
        }
    }

    // Método público para atualizar a lista de clientes externamente
    public void atualizarListaClientes() {
        model.setRowCount(0);
        carregarClientesDoBanco();
    }

    static class NumeroDocumentFilter extends DocumentFilter {
        @Override
        public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr) throws BadLocationException {
            if (string != null && string.matches("\\d+")) {
                super.insertString(fb, offset, string, attr);
            }
        }

        @Override
        public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
            if (text != null && text.matches("\\d+")) {
                super.replace(fb, offset, length, text, attrs);
            }
        }
    }
}
