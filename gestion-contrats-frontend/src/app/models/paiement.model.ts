export interface Paiement {
  id?: number;
  datePaiement?: string;
  montant: number;
  modePaiement: string;
  contratId: number;
  typeContrat?: string;
}

export interface PaiementRequest {
  contratId: number;
  montant: number;
  modePaiement: string;
  datePaiement?: string;
}
