package domain;

import java.time.DayOfWeek;
import java.time.LocalDate;

public class ScontoStrategy1 implements ScontoStrategyInterface {

    @Override
    public float applicaSconto(float prezzo, int eta) {
        float prezzoScontato;
        if (eta >= 0 && eta <= 6) {
            // Bimbi (0-6 anni): Sconto del 100%
            prezzoScontato = (float) (prezzo - (prezzo * 1));
        } else if (eta >= 7 && eta <= 17) {
            // Ragazzi (7-17 anni): Sconto del 50%
            prezzoScontato = (float) (prezzo - (prezzo * 0.5));
        } else if (eta >= 65) {
            // Anziani (65 anni o più): Sconto del 70%
            prezzoScontato = (float) (prezzo - (prezzo * 0.7));
        } else {
            // Adulti: costo massimo.
            prezzoScontato = (float) (prezzo - (prezzo * 0));
        }
        return prezzoScontato;
    }
}