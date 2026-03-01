package clima;

public class StatisticsDisplay implements Observer {
    private double maxTemp = Double.MIN_VALUE;
    private double minTemp = Double.MAX_VALUE;

    public StatisticsDisplay(Subject weatherData) {
        weatherData.registerObserver(this);
    }

    @Override
    public void update(double temperature) {
        if (temperature > maxTemp) maxTemp = temperature;
        if (temperature < minTemp) minTemp = temperature;
        display();
    }

    public void display() {
        System.out.println("Máxima: " + maxTemp + "°C / Mínima: " + minTemp + "°C");
    }
}

