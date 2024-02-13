package domain;

import java.time.LocalDate;

public interface ScontoStrategyInterface {
    float applicaSconto(float prezzo, int eta);
}

