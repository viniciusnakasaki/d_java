package application;

import java.util.Locale;
import java.util.Scanner;

public class Program {

	public static void main(String[] args) {

		Locale.setDefault(Locale.US);
		Scanner sc = new Scanner(System.in);

		System.out.println("How many numbers will you enter?");
		int n = sc.nextInt();

		double[] vetor = new double[n];

		double more = 0;
		int posmore = 0;
		for (int i = 0; i < n; i++) {
			System.out.print("Digite um numero: ");
			vetor[i] = sc.nextDouble();
			if(vetor[i] > more) {
				more = vetor[i];
				posmore = i;
			}
		}
		
		System.out.print("\nGreatest value: " + more);
		System.out.print("\nPosition of the greatest value: " + posmore);
		
		sc.close();
	}
}
