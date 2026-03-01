package conexão;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexaoMySQL {

    private static final String URL = "jdbc:mysql://localhost:3306/eletronico?useSSL=false&serverTimezone=UTC";
    private static final String USUARIO = "root";
    private static final String SENHA = "70782020";

    public static Connection conectar() {
        Connection conn = null;
        try {
            
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Realizar a conexão
            conn = DriverManager.getConnection(URL, USUARIO, SENHA);
            System.out.println("✅ Conexão com o banco de dados realizada com sucesso!");
        } catch (ClassNotFoundException e) {
            System.out.println("❌ Driver JDBC não encontrado: " + e.getMessage());
        } catch (SQLException e) {
            System.out.println("❌ Erro ao conectar no banco: " + e.getMessage());
        }
        return conn;
    }

    // Teste de conexão
    public static void main(String[] args) {
        conectar();
    }
}
