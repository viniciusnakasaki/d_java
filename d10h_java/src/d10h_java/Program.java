package d10h_java;

import java.util.Locale;
import java.util.Scanner;

public class Program {

	public static void main(String[] args) {
		Locale.setDefault(Locale.US);
		Scanner sc = new Scanner(System.in);
		
		System.out.println("How many students will be entered? ");
		int n = sc.nextInt();
		
		double[] height = new double[n];
		char [] letter = new char[n];
		
		for(int i = 0; i < n; i++) {
			System.out.printf("Height of the %dst person: %n", i + 1);
			height[i] = sc.nextDouble();
			System.out.printf("Gender of the %dst person: %n", i + 1);
			letter[i] = sc.next().charAt(0);
		}
		
		double tall = height[0]; 
		double small = height[0];
		double womensaverage = 0;
		double sumwomen = 0;
		int allmen = 0;
		int cont = 0;
		
		
		
		for(int i = 1; i < n; i++) {
			if(height[i] > tall) {
				tall = height[i];
			} 
			if(height[i] < small) {
				small = height[i];
			}
		}
		
		for(int i = 0; i < n; i++) {
			if(letter[i] == 'F') {
				sumwomen = sumwomen + height[i];
				cont++;
			} else {
				allmen++;
			}
		} 
		
		womensaverage = sumwomen / cont;
		
		System.out.println("Shortest height: " + small);
		System.out.println("Greater height: " + tall);
		System.out.printf("Average height of women: %.2f%n", womensaverage);
		System.out.println("Number of men: " + allmen);
		
		sc.close();
	}

}
