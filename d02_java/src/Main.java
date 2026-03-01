import java.util.Scanner;

public class Main {

	public static void main(String[] args) {
		
		Scanner sc = new Scanner(System.in);
		
		String x;
		x = sc.next();
		System.out.println("Voce digitou: " + x);
		
		int y;
		y = sc.nextInt();
		System.out.println("Voce diitou: " + y);
		
		double z;
		z = sc.nextDouble();
		System.out.println("Voce digiou: " + z);
		
		sc.close();
	}

}
