package ma.enset.gestioncontrats.entities;

/**
 * Enum représentant le cycle de vie d'un contrat d'assurance.
 * On stocke le nom STRING en BDD (plus lisible que l'index numérique).
 */
public enum StatutContrat {
    BROUILLON,
    EN_ATTENTE,
    VALIDE,
    RESILIE,
    EXPIRE
}
