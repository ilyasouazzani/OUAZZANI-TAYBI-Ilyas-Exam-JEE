import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';
import { Client, ClientRequest } from '../models/client.model';
import { Page } from '../models/page.model';

@Injectable({ providedIn: 'root' })
export class ClientService {

  private apiUrl = `${environment.apiUrl}/clients`;

  constructor(private http: HttpClient) {}

  /**
   * GET /api/clients?page=0&size=10&sortBy=nom
   * Retourne un Observable<Page<Client>> — les composants s'abonnent avec .subscribe()
   */
  getAll(page = 0, size = 10, sortBy = 'nom'): Observable<Page<Client>> {
    const params = new HttpParams()
      .set('page', page)
      .set('size', size)
      .set('sortBy', sortBy);
    return this.http.get<Page<Client>>(this.apiUrl, { params });
  }

  search(keyword: string, page = 0, size = 10): Observable<Page<Client>> {
    const params = new HttpParams()
      .set('keyword', keyword)
      .set('page', page)
      .set('size', size);
    return this.http.get<Page<Client>>(`${this.apiUrl}/search`, { params });
  }

  getById(id: number): Observable<Client> {
    return this.http.get<Client>(`${this.apiUrl}/${id}`);
  }

  create(client: ClientRequest): Observable<Client> {
    return this.http.post<Client>(this.apiUrl, client);
  }

  update(id: number, client: ClientRequest): Observable<Client> {
    return this.http.put<Client>(`${this.apiUrl}/${id}`, client);
  }

  delete(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }
}
