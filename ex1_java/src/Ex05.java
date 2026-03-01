import java.util.Locale;
import java.util.Scanner;

public class Ex05 {

	public static void main(String[] args) {
		
			Locale.setDefault(Locale.US);
			Scanner sc = new Scanner(System.in);
		
			int cod1, cod2, qte1 , qte2;
			double valor1, valor2, pagamento;
			
		cod1 = sc.nextInt();
		qte1 = sc.nextInt();
		valor1 = sc.nextDouble();
			
		cod2 = sc.nextInt();
		qte2 = sc.nextInt();
		valor2 = sc.nextDouble();
			
		pagamento = qte1 * valor1 + qte2 * valor2;
			
		System.out.printf("VALOR A PAGAR: R$ %.2f%n", pagamento);
			
		sc.close(); 
	}
}
