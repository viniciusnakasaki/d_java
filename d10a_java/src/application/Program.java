package application;

import java.util.Locale;
import java.util.Scanner;

public class Program {

	public static void main(String[] args) {

		Locale.setDefault(Locale.US);
		Scanner sc = new Scanner(System.in);

		System.out.printf("How many numbers will you enter? ");
		int n = sc.nextInt();

		int[] vect = new int[n];

		for (int i = 0; i < n; i++) {
			System.out.print("Enter a number: ");
			vect[i] = sc.nextInt();
		}

		int cont = 0;
		System.out.print("\nEven numbers: ");
		for (int i = 0; i < n; i++) {
			if(vect[i] % 2 == 0) {
				System.out.printf("%d  ", vect[i]);
				cont++;
			}			
		}
		
		System.out.print("\nTotal even numbers: " + cont);

		sc.close();
	}
}
