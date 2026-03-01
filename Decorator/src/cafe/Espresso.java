package cafe;

/**
 * ConcreteComponent: implementação concreta de Beverage.
 * Representa um tipo básico de bebida (Espresso).
 */
public class Espresso implements Beverage {
    @Override
    public String getDescription() {
        return "Espresso";
    }

    @Override
    public double cost() {
        // custo base do espresso
        return 1.99;
    }
}

