/**
 * Interface générique qui mappe la réponse paginée de Spring Data.
 * Spring renvoie toujours ce format pour les Page<T>.
 * Le T générique permet de l'utiliser pour n'importe quelle entité :
 * Page<Client>, Page<Contrat>, etc.
 */
export interface Page<T> {
  content: T[];
  totalElements: number;
  totalPages: number;
  size: number;
  number: number;       // numéro de la page actuelle (0-based)
  first: boolean;
  last: boolean;
  empty: boolean;
}
