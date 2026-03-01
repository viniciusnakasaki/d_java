package clima;

/**
 * Interface Observer.
 * Define o método update que será chamado pelo Subject quando o estado mudar.
 */
public interface Observer {
    void update(double temperature);
}

