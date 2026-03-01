package clima;

import java.util.ArrayList;
import java.util.List;

/**
 * ConcreteSubject: mantém uma lista de observers e notifica quando a temperatura muda.
 * Implementação simples (não-thread-safe). Em ambientes concorrentes, sincronize ou use
 * cópias defensivas/thread-safe collections.
 */
public class WeatherData implements Subject {
    private final List<Observer> observers;
    private double temperature;

    public WeatherData() {
        observers = new ArrayList<>();
    }

    @Override
    public void registerObserver(Observer o) {
        if (o != null && !observers.contains(o)) {
            observers.add(o);
        }
    }

    @Override
    public void removeObserver(Observer o) {
        observers.remove(o);
    }

    @Override
    public void notifyObservers() {
        // Notifica todos os observers com o estado atual
        for (Observer o : observers) {
            o.update(temperature);
        }
    }

    /**
     * Método público para atualizar a temperatura.
     * Sempre que o estado muda, chamamos measurementsChanged() para notificar observers.
     */
    public void setTemperature(double temperature) {
        this.temperature = temperature;
        measurementsChanged();
    }

    private void measurementsChanged() {
        notifyObservers();
    }

    // Getter (opcional, caso observers precisem consultar)
    public double getTemperature() {
        return temperature;
    }
}

