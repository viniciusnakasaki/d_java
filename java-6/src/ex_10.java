import java.util.Scanner;

public class ex_10 {
	
	public static void main(String[] args) {
			
		Scanner sc = new Scanner(System.in);
		
		int n =sc.nextInt();
		
		for (int i=1; i<=n; i++){							
				
				int z = i;
				int a = i * i;
				int b = i * i * i;
				System.out.printf("%d %d %d%n", z, a, b);				
		}
		
		sc.close();
	}
}