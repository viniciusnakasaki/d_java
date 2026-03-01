package d10g_java;

import java.util.Locale;
import java.util.Scanner;

public class Program {

	public static void main(String[] args) {		
		Locale.setDefault(Locale.US);
		Scanner sc = new Scanner(System.in);
		
		System.out.println("How many students will be entered? ");
		int n = sc.nextInt();
		sc.nextLine();
		
		String[] names = new String[n];
		double[] grade1 = new double[n];
		double[] grade2 = new double[n];
		double[] grade = new double[n];
		
		for(int i = 0; i < n; i++) {
			System.out.printf("Enter the name, first and second grade of the %dst student: %n", i + 1);
			names[i] = sc.nextLine();
			grade1[i] = sc.nextDouble();
			grade2[i] = sc.nextDouble();
			sc.nextLine();
		}
		
		System.out.println("Approved Students: ");
		
		for(int i = 0; i < n; i++) {
			grade[i] = (grade1[i] + grade2[i]) / 2;
		}
			
		for(int i = 0; i < n; i++) {
			if(grade[i] >= 6.0) {
				System.out.println(names[i]);
			}
		}
		
		sc.close();
	}

}
