import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';
import {
  Contrat, ContratAutoRequest,
  ContratHabitationRequest, ContratSanteRequest, StatutContrat
} from '../models/contrat.model';
import { Page } from '../models/page.model';

@Injectable({ providedIn: 'root' })
export class ContratService {

  private apiUrl = `${environment.apiUrl}/contrats`;

  constructor(private http: HttpClient) {}

  getAll(page = 0, size = 10): Observable<Page<Contrat>> {
    const params = new HttpParams().set('page', page).set('size', size);
    return this.http.get<Page<Contrat>>(this.apiUrl, { params });
  }

  getById(id: number): Observable<Contrat> {
    return this.http.get<Contrat>(`${this.apiUrl}/${id}`);
  }

  getByClient(clientId: number, page = 0, size = 10): Observable<Page<Contrat>> {
    const params = new HttpParams().set('page', page).set('size', size);
    return this.http.get<Page<Contrat>>(`${this.apiUrl}/client/${clientId}`, { params });
  }

  getByStatut(statut: StatutContrat, page = 0, size = 10): Observable<Page<Contrat>> {
    const params = new HttpParams().set('page', page).set('size', size);
    return this.http.get<Page<Contrat>>(`${this.apiUrl}/statut/${statut}`, { params });
  }

  // Création des 3 types
  createAuto(data: ContratAutoRequest): Observable<Contrat> {
    return this.http.post<Contrat>(`${this.apiUrl}/auto`, data);
  }

  createHabitation(data: ContratHabitationRequest): Observable<Contrat> {
    return this.http.post<Contrat>(`${this.apiUrl}/habitation`, data);
  }

  createSante(data: ContratSanteRequest): Observable<Contrat> {
    return this.http.post<Contrat>(`${this.apiUrl}/sante`, data);
  }

  // Actions métier
  valider(id: number): Observable<Contrat> {
    return this.http.put<Contrat>(`${this.apiUrl}/${id}/valider`, {});
  }

  resilier(id: number): Observable<Contrat> {
    return this.http.put<Contrat>(`${this.apiUrl}/${id}/resilier`, {});
  }

  delete(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }
}
