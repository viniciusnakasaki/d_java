package facu_1;

import java.util.Random;
import java.util.Scanner;

public class facu {

	public static void main(String[] args) {
		Scanner ler = new Scanner(System.in);

		String[] adjectives = { "Brilliant", "Wild", "Imposing", "Fast", "Mysterious", "Intense", "Powerful", "Agile",
                "Invincible", "Radiant" };

        String[] nouns = { "Lion", "Dragon", "Storm", "Wind", "Falcon", "Eagle", "Lightning", "Volcano", "Thunder",
                "Ocean" };

        String[] verbs = { "Strike", "Run", "Fly", "Conquer", "Defend", "Charge", "Roar", "Smash", "Dominate",
                "Unleash" };

        String[] colors = { "Red", "Blue", "Black", "White", "Gold", "Silver", "Green", "Purple", "Crimson", "Azure" };

        String[] places = { "Mountain", "Desert", "Forest", "Ocean", "City", "Arena", "Island", "Cave", "Castle",
                "Plain" };

        System.out.print("How many team names will be needed?: ");
        int n = ler.nextInt();

        Random random = new Random(); 

        for (int i = 0; i < n; i++) { 
            String randomAdjective = adjectives[random.nextInt(adjectives.length)];
            String randomNoun = nouns[random.nextInt(nouns.length)];
            String randomVerb = verbs[random.nextInt(verbs.length)];
            String randomColor = colors[random.nextInt(colors.length)];
            String randomPlace = places[random.nextInt(places.length)];

            System.out.println("Team Name " + (i + 1) + ": " + randomAdjective + " " + randomNoun + " - " + 
                               randomVerb + " in " + randomColor + " " + randomPlace);
        }

		ler.close();

	}

}
