package d10e_java;

import java.util.Locale;
import java.util.Scanner;

public class Program {

	public static void main(String[] args) {

		Locale.setDefault(Locale.US);
		Scanner sc = new Scanner(System.in);

		System.out.println("How many elements will the vector have?");
		int n = sc.nextInt();

		double[] vector = new double[n];

		double average = 0;
		double sum = 0;
		double cont = 0;
		for (int i = 0; i < n; i++) {
			System.out.print("Enter a number: ");
			vector[i] = sc.nextDouble();
			if (vector[i] % 2 == 0) {
				sum = sum + vector[i];
				cont++;
			}
		}
		average = sum / cont;
		if (cont < 0) {
			System.out.print("No evens numbers");
		} else {
			System.out.print("Evens average:  " + average);
		}
		
		sc.close();
	}

}
