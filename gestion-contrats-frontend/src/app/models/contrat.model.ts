/**
 * Statuts possibles d'un contrat — même enum que côté Java.
 * TypeScript union type : plus léger qu'un enum TS pour l'exam.
 */
export type StatutContrat = 'BROUILLON' | 'EN_ATTENTE' | 'VALIDE' | 'RESILIE' | 'EXPIRE';
export type TypeContrat = 'AUTO' | 'HABITATION' | 'SANTE';

/**
 * Interface universelle pour la réponse contrat.
 * Tous les champs spécifiques (immatriculation, adresse...) sont optionnels
 * et remplis selon le typeContrat.
 */
export interface Contrat {
  id?: number;
  typeContrat: TypeContrat;
  dateSouscription?: string;
  statut: StatutContrat;
  dateValidation?: string;
  montant: number;
  duree: number;
  taux: number;
  clientId: number;
  clientNomComplet?: string;

  // Champs AUTO
  immatriculation?: string;
  marque?: string;
  modele?: string;

  // Champs HABITATION
  type?: string;
  adresse?: string;
  superficie?: number;

  // Champs SANTE
  niveauCouverture?: string;
  nbPersonnes?: number;
}

export interface ContratAutoRequest {
  clientId: number;
  montant: number;
  duree: number;
  taux: number;
  immatriculation: string;
  marque: string;
  modele: string;
}

export interface ContratHabitationRequest {
  clientId: number;
  montant: number;
  duree: number;
  taux: number;
  type: string;
  adresse: string;
  superficie: number;
}

export interface ContratSanteRequest {
  clientId: number;
  montant: number;
  duree: number;
  taux: number;
  niveauCouverture: string;
  nbPersonnes: number;
}
