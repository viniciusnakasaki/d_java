package logines;

import javax.swing.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import conexão.ConexaoMySQL;

public class UsuarioDB {

    public static boolean validarCredenciais(String email, String senha) {
        try (Connection conn = ConexaoMySQL.conectar()) {
            String sql = "SELECT * FROM usuarios WHERE email = ? AND senha = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, email);
            ps.setString(2, senha);
            ResultSet rs = ps.executeQuery();
            return rs.next();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Erro ao conectar com o banco: " + e.getMessage());
            return false;
        }
    }

    public static boolean emailExiste(String email) {
        try (Connection conn = ConexaoMySQL.conectar()) {
            String sql = "SELECT * FROM usuarios WHERE email = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();
            return rs.next();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Erro ao verificar e-mail: " + e.getMessage());
            return false;
        }
    }

    public static void adicionarUsuario(Usuario usuario) {
        try (Connection conn = ConexaoMySQL.conectar()) {
            String sql = "INSERT INTO usuarios (email, senha) VALUES (?, ?)";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, usuario.getEmail());
            ps.setString(2, usuario.getSenha());
            ps.executeUpdate();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Erro ao cadastrar usuário: " + e.getMessage(),
                    "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
}
