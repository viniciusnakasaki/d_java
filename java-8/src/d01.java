import java.util.Locale;
import java.util.Scanner;

public class d01 {

	public static void main(String[] args) {
		
		Locale.setDefault(Locale.US);
		Scanner sc = new Scanner(System.in);
				
		System.out.println("Enter the measure of triangle X:");		
		double xA = sc.nextDouble();
		double xB = sc.nextDouble();
		double xC = sc.nextDouble();		
		System.out.println("Enter the measure of triangle Y:");		
		double yA = sc.nextDouble();
		double yB = sc.nextDouble();
		double yC = sc.nextDouble();
		
		double p = (xA + xB + xC)/2;
		double area = Math.sqrt(p*(p-xA)*(p-xB)*(p-xC));
						
		double py = (yA + yB + yC)/2;
		double areay = Math.sqrt(py*(py-yA)*(py-yB)*(py-yC));
		
		System.out.printf("Triangle X area: %.4f%n", area);
		System.out.printf("Triangle Y area: %.4f%n", areay);
		
		if (area > areay) {
			System.out.println("Larger area: X");		
		}
		else {
			System.out.println("Larger area: Y");
		}
					
		sc.close();
	}
}
