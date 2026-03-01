import java.util.Scanner;

public class ex_03 {

	public static void main(String[] args) {
		
		Scanner sc = new Scanner(System.in);

		int alc = 0;
		int gas = 0;
		int die = 0;
			
		int tipo = sc.nextInt();
		
		while (tipo != 4) {
				if (tipo == 1) {;
						alc = alc + 1;					
				}
				else if (tipo == 2) {
						gas = gas + 1;
				}
				else if (tipo ==3) {
						die = die + 1;
				}
				
				tipo = sc.nextInt();
		}		
				
		System.out.println("MUITO OBRIGADO");
		System.out.println("Alcool:" + alc);
		System.out.println("Gasolina:" + gas);
		System.out.println("Diesel:" + die);
				
		sc.close();
	}
}