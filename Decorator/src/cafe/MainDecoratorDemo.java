package cafe;

import java.util.Locale;

/**
 * Demo do padrão Decorator com formatação correta dos preços
 * para evitar saídas como 1.3900000000000001.
 */
public class MainDecoratorDemo {
    public static void main(String[] args) {
        // 1) Bebida simples (sem decorator)
        Beverage beverage = new Espresso();
        // Formata com 2 casas decimais
        System.out.println(beverage.getDescription() + " $" + String.format(Locale.US, "%.2f", beverage.cost()));

        // 2) Decorando: Espresso com Mocha e Whip
        Beverage beverage2 = new Espresso();
        beverage2 = new Mocha(beverage2); // primeiro adiciona Mocha
        beverage2 = new Whip(beverage2);  // depois adiciona Whip
        // Usa printf (ou String.format) para imprimir com 2 casas decimais
        System.out.println(beverage2.getDescription() + " $" + String.format(Locale.US, "%.2f", beverage2.cost()));

        // 3) HouseBlend com dois Mochas e Whip
        Beverage beverage3 = new HouseBlend();
        beverage3 = new Mocha(beverage3); // primeiro Mocha
        beverage3 = new Mocha(beverage3); // encadeia outro Mocha
        beverage3 = new Whip(beverage3);  // e um Whip
        System.out.println(beverage3.getDescription() + " $" + String.format(Locale.US, "%.2f", beverage3.cost()));
    }
}



