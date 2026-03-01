package cafe;

/**
 * Decorator abstrato: implementa a mesma interface do componente
 * e mantém uma referência para um Beverage. Subclasses concretas
 * adicionam comportamento adicional.
 */
public abstract class CondimentDecorator implements Beverage {
    protected Beverage beverage; // componente que está sendo decorado

    public CondimentDecorator(Beverage beverage) {
        this.beverage = beverage;
    }

    // getDescription() e cost() devem ser implementados nas classes concretas
}

