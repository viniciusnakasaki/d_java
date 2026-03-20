package app;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import ent.Product1;

public class Program1 {

	public static void main(String[] args) {

		List<Product1> list = new ArrayList<>();

		list.add(new Product1("Tv", 900.00));
		list.add(new Product1("Mouse", 50.00));
		list.add(new Product1("Tablet", 350.50));
		list.add(new Product1("HD Case", 80.90));

		double factor = 1.1;
		
		list.forEach(p -> p.setPrice(p.getPrice() * factor));

		list.forEach(System.out::println);
	}
}
