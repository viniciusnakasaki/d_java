package cafe;

/**
 * ConcreteDecorator: adiciona o condimento "Mocha".
 * Encapsula um Beverage e estende comportamento.
 */
public class Mocha extends CondimentDecorator {
    public Mocha(Beverage beverage) {
        super(beverage);
    }

    @Override
    public String getDescription() {
        // concatena a descrição do componente encapsulado
        return beverage.getDescription() + ", Mocha";
    }

    @Override
    public double cost() {
        // adiciona o custo do condimento ao custo do componente encapsulado
        return 0.20 + beverage.cost();
    }
}


