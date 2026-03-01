package application;

import java.util.Locale;
import java.util.Scanner;

public class Program {

	public static void main(String[] args) {

		Locale.setDefault(Locale.US);
		Scanner sc = new Scanner(System.in);

		System.out.println("How many people will be entered?");
		int n = sc.nextInt();

		String[] name = new String[n];
		int[] age = new int[n];
		double[] height = new double[n];

		for (int i = 0; i < n; i++) {
			System.out.println((i + 1) + "st person data: ");
			System.out.println("Nome: ");
			name[i] = sc.next();
			System.out.println("Idade: ");
			age[i] = sc.nextInt();
			System.out.println("Altura: ");
			height[i] = sc.nextDouble();
		}

		double sum = 0.0;
		for (int i = 0; i < height.length; i++) {
			sum = sum + height[i];
		}
		double averageH = sum / n;
		System.out.println();
		System.out.printf("Average height: %.2f%n", averageH);

		double cont = 0;
		for (int i = 0; i < age.length; i++) {
			if (age[i] < 16) {
				cont = cont + 1;
			}
		}

		double percent = cont * 100.0 / n;
		;
		System.out.printf("People under 16: %.1f%%%n", percent);

		for (int i = 0; i < age.length; i++) {
			if (age[i] < 16) {
				System.out.println(name[i]);
			}
		}

		sc.close();
	}
}
