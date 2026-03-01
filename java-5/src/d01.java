import java.util.Scanner;

public class d01 {

	public static void main(String[] args) {
		
		Scanner sc = new Scanner(System.in);
		int negativo;
		
		System.out.println("numero");
		negativo = sc.nextInt();
		
		if (negativo < 0) {		
			System.out.println("NEGATIVO");		
		}
		else {
			System.out.println("NÃO NEGATIVO");
		}
		
		sc.close();
	}
}
