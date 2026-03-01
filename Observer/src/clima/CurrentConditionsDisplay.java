package clima;

/**
 * ConcreteObserver: exibe as condições atuais.
 * Registra-se no Subject no construtor para receber atualizações automaticamente.
 */
public class CurrentConditionsDisplay implements Observer {
    private double temperature;
    private final Subject weatherData; // manter referência caso queira se desregistrar no futuro

    public CurrentConditionsDisplay(Subject weatherData) {
        this.weatherData = weatherData;
        // Registrar-se no subject para começar a receber atualizações
        weatherData.registerObserver(this);
    }

    @Override
    public void update(double temperature) {
        // Recebe a temperatura do subject e atualiza o estado local
        this.temperature = temperature;
        display();
    }

    public void display() {
        System.out.println("Current conditions: " + temperature + "°C");
    }
}
