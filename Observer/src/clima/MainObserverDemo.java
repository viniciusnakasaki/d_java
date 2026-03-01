package clima;

/**
 * Demo unificado do padrão Observer.
 * Simula mudanças de temperatura e mostra como diferentes observers reagem.
 */
public class MainObserverDemo {
    public static void main(String[] args) {
        // Cria o Subject (a estação meteorológica)
        WeatherData weatherData = new WeatherData();

        // Cria observers concretos
        CurrentConditionsDisplay currentDisplay = new CurrentConditionsDisplay(weatherData);
        StatisticsDisplay statisticsDisplay = new StatisticsDisplay(weatherData);

        // Cria um observer anônimo
        Observer anonymousDisplay = new Observer() {
            @Override
            public void update(double temperature) {
                System.out.println("Anonymous observer: Nova temperatura recebida -> " + temperature + "°C");
            }
        };
        weatherData.registerObserver(anonymousDisplay);

        // Simulação de mudanças de temperatura
        System.out.println("\n--- Atualização 1 ---");
        weatherData.setTemperature(25.0);

        System.out.println("\n--- Atualização 2 ---");
        weatherData.setTemperature(27.5);

        System.out.println("\n--- Atualização 3 ---");
        weatherData.setTemperature(30.0);

        // Remover o observer anônimo — não receberá mais atualizações
        weatherData.removeObserver(anonymousDisplay);

        System.out.println("\n--- Atualização 4  ---");
        weatherData.setTemperature(22.0); // apenas observers concretos são notificados
    }
}
