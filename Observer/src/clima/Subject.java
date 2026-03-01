package clima;

/**
 * Interface Subject (sujeito) do padrão Observer.
 * Define métodos para registro, remoção e notificação de observers.
 */
public interface Subject {
    void registerObserver(Observer o);
    void removeObserver(Observer o);
    void notifyObservers();
}
