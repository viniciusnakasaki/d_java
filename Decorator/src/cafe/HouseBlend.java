package cafe;

/**
 * ConcreteComponent: outra implementação concreta de Beverage.
 * Representa um blend mais barato.
 */
public class HouseBlend implements Beverage {
    @Override
    public String getDescription() {
        return "House Blend Coffee";
    }

    @Override
    public double cost() {
        return 0.89;
    }
}

