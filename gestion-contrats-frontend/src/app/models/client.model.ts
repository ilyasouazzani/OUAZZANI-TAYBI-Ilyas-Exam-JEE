/**
 * Interface Client — miroir du ClientResponseDTO Java.
 * Le ? indique que le champ est optionnel (peut être absent dans certains contextes).
 */
export interface Client {
  id?: number;
  nom: string;
  prenom: string;
  email: string;
  telephone?: string;
  nombreContrats?: number;
}

export interface ClientRequest {
  nom: string;
  prenom: string;
  email: string;
  telephone?: string;
}
