package app;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import ent.Product2;

public class Program2 {

	public static void main(String[] args) {

		List<Product2> list = new ArrayList<>();

		list.add(new Product2("Tv", 900.00));
		list.add(new Product2("Mouse", 50.00));
		list.add(new Product2("Tablet", 350.50));
		list.add(new Product2("HD Case", 80.90));

		List<String> names = list.stream().map(p -> p.getName().toUpperCase()).collect(Collectors.toList());
		
		names.forEach(System.out::println);
	}
}
