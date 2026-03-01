package d10d_java;

import java.util.Locale;
import java.util.Scanner;

public class Program {

	public static void main(String[] args) {
		
		Locale.setDefault(Locale.US);
		Scanner sc = new Scanner(System.in);

		System.out.println("How many elements will the vector have?");
		int n = sc.nextInt();

		double[] vector = new double[n];
		
		for (int i = 0; i < n; i++) {
			System.out.print("Enter a number: ");
			vector[i] = sc.nextDouble();			
		}
		System.out.println();
		
		double average = 0;
		double sum = 0;
		for (int i = 0; i < n; i++) {
			sum = sum + vector[i];		
		}		
		average = sum / n; 
		System.out.printf("Vector media = %.3f%n", average);
		
		for (int i = 0; i < n; i++) {
			if (vector[i] < average) {		
				System.out.println("Numbers below average: " + vector[i]);
			}
		}		
		
		sc.close();
	}

}
