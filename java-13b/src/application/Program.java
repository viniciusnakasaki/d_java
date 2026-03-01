package application;

import java.text.ParseException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;

import entities.ImportedProduct;
import entities.Product;
import entities.UsedProduct;

public class Program {

	public static void main(String[] args) throws ParseException {

		Locale.setDefault(Locale.US);
		Scanner sc = new Scanner(System.in);

		List<Product> list = new ArrayList<>();

		System.out.print("Enter the number of products: ");
		int n = sc.nextInt();

		for (int i = 1; i <= n; i++) {
			System.out.println("Product #" + i + " data:");
			System.out.print("Common, used or imported (c/u/i)? ");
			char ch = sc.next().charAt(0);	
			System.out.print("Name: ");
			sc.nextLine();
			String name = sc.nextLine();
			System.out.print("Price: ");
			double price = sc.nextDouble();			
			if(ch == 'c') {	
				//jeito 1
				list.add(new Product(name, price));	
			} 
			else if (ch == 'u') {				
				System.out.print("Manufacture data (DD/MM/YYYY): ");
				LocalDate manufactureDate = LocalDate.parse(sc.next(), DateTimeFormatter.ofPattern("dd/MM/yyyy"));								
				list.add(new UsedProduct(name, price, manufactureDate));
			} 
			else {		
				System.out.print("Customs fee: ");
				double customsFee = sc.nextDouble();
				//jeito 2
				Product imported = new ImportedProduct(name, price, customsFee);
				list.add(imported);
			}
		}
			System.out.println();
			System.out.println("PRICE TAGS: ");
			for (Product tags : list) {
				System.out.println(tags.priceTag());
			}
		
		sc.close();
	}
}
