import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';
import { Paiement, PaiementRequest } from '../models/paiement.model';

@Injectable({ providedIn: 'root' })
export class PaiementService {

  private apiUrl = `${environment.apiUrl}/paiements`;

  constructor(private http: HttpClient) {}

  create(data: PaiementRequest): Observable<Paiement> {
    return this.http.post<Paiement>(this.apiUrl, data);
  }

  getByContrat(contratId: number): Observable<Paiement[]> {
    return this.http.get<Paiement[]>(`${this.apiUrl}/contrat/${contratId}`);
  }

  getByClient(clientId: number): Observable<Paiement[]> {
    return this.http.get<Paiement[]>(`${this.apiUrl}/client/${clientId}`);
  }

  getTotalPaye(contratId: number): Observable<{ totalPaye: number }> {
    return this.http.get<{ totalPaye: number }>(`${this.apiUrl}/contrat/${contratId}/total`);
  }
}
