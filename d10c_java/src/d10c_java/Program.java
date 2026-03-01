package d10c_java;

import java.util.Locale;
import java.util.Scanner;

public class Program {

	public static void main(String[] args) {
		Locale.setDefault(Locale.US);
		Scanner sc = new Scanner(System.in);

		System.out.println("How many values will each vector have?");
		int n = sc.nextInt();

		int[] vectorA = new int[n];
		int[] vectorB = new int[n];
		int[] vectorC = new int[n];

		for (int i = 0; i < n; i++) {
			System.out.print("Enter the values ​​of vector A: ");
			vectorA[i] = sc.nextInt();			
		}

		for (int i = 0; i < n; i++) {
			System.out.print("Enter the values ​​of vector B: ");
			vectorB[i] = sc.nextInt();			
		}
		
		for (int i = 0; i < n; i++) {
			vectorC[i] = vectorA[i] + vectorB[i];
			System.out.println("Resulting Vector: " + vectorC[i]);
		}
		
		
		sc.close();

	}

}
