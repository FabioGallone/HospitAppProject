package TestHospitApp;

import domain.ScontoStrategy1;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class TestScontoStrategy1 {

    @Test
    public void testApplicaSconto(){
    ScontoStrategy1 st=new ScontoStrategy1();
        // Bimbi (0-6 anni): Sconto del 100%
        // Ragazzi (7-17 anni): Sconto del 50%
        // Adulti: costo massimo.
        // Anziani (65 anni o più): Sconto del 70%

    assertEquals(0, st.applicaSconto(50, 5), 0);
    assertEquals(25, st.applicaSconto(50, 15), 0);
    assertEquals(50, st.applicaSconto(50, 50), 0);
    assertEquals(15, st.applicaSconto(50, 70), 0);

    }
}
