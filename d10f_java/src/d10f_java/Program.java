package d10f_java;

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

		for (int i = 0; i < n; i++) {
			System.out.println((i + 1) + "st person data: ");
			System.out.println("Nome: ");
			name[i] = sc.next();
			System.out.println("Idade: ");
			age[i] = sc.nextInt();
		}

		int oldestAge = 0;
		String oldestPerson = "";
		
		for (int i = 0; i < age.length; i++) {
			if(age[i] > oldestAge) {
				oldestAge = age[i];
				oldestPerson = name[i];
			}
		}
		System.out.println("Older person: " + oldestPerson);
		
		sc.close();

	}

}
