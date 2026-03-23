package hechizos;

import personajes.Personajes;
import personajes.Sacerdote;
import estados.Renovar;

// CURACIÓN EN EL TIEMPO (HoT) - Aplica el estado Renovar a un aliado.
// Ciri usa:   Aura de la Vieja Sangre
// Eredin usa: Presencia del Rey
//
// No cura al impactar; solo inyecta el estado Renovar al objetivo.
// Si el lanzador es Sacerdote, Renovar cura 20 HP extra por turno
// (se pasa true al constructor especial de Renovar).

public class CuraciónEnElTiempo extends Hechizos {

    public CuraciónEnElTiempo(String nombre, int costeMana, int cooldownMaximo) {
        // potenciaBase = 0 porque la curación la gestiona el estado Renovar
        super(nombre, costeMana, 0, cooldownMaximo, TipoObjetivo.ALIADO_UNICO);
    }

    @Override
    public void lanzar(Personajes lanzador, Personajes blanco) {
        if (!puedeUsarse(lanzador))
            return;

        System.out.println(" " + lanzador.getNombre() + " lanza «" + nombre
                + "» sobre " + blanco.getNombre() + ". ¡Estado Renovar aplicado!");

        // Si lo lanza un Sacerdote, Renovar cura +20 HP extra por turno
        if (lanzador instanceof Sacerdote) {
            Renovar renovarMejorado = new Renovar(true); // true = aplicado por Sacerdote
            blanco.aplicarEstados(renovarMejorado);
            System.out.println("  (Renovar potenciado: +20 HP extra por turno gracias al Sacerdote)");
        } else {
            Renovar renovar = new Renovar(); // 40 HP por turno, 4 turnos
            blanco.aplicarEstados(renovar);
        }

        ponerEnCooldown(lanzador);
    }
}
