import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, tap } from 'rxjs';
import { environment } from '../../environments/environment';
import { LoginRequest, LoginResponse, RegisterRequest } from '../models/auth.model';

/**
 * Service d'authentification.
 *
 * providedIn: 'root' = singleton — une seule instance partagée dans toute l'app.
 * C'est la façon Angular 17+ de déclarer un service global sans NgModule.
 *
 * On stocke le token JWT dans localStorage pour qu'il persiste après rechargement.
 */
@Injectable({ providedIn: 'root' })
export class AuthService {

  private apiUrl = `${environment.apiUrl}/auth`;

  // Clés localStorage
  private TOKEN_KEY = 'jwt_token';
  private USER_KEY  = 'current_user';

  constructor(private http: HttpClient) {}

  /**
   * Appelle POST /api/auth/login.
   * tap() : opérateur RxJS qui effectue un effet de bord (stocker le token)
   * sans modifier les données du flux Observable.
   */
  login(request: LoginRequest): Observable<LoginResponse> {
    return this.http.post<LoginResponse>(`${this.apiUrl}/login`, request).pipe(
      tap(response => {
        localStorage.setItem(this.TOKEN_KEY, response.token);
        localStorage.setItem(this.USER_KEY, JSON.stringify(response));
      })
    );
  }

  register(request: RegisterRequest): Observable<any> {
    return this.http.post(`${this.apiUrl}/register`, request);
  }

  logout(): void {
    localStorage.removeItem(this.TOKEN_KEY);
    localStorage.removeItem(this.USER_KEY);
  }

  getToken(): string | null {
    return localStorage.getItem(this.TOKEN_KEY);
  }

  isLoggedIn(): boolean {
    return this.getToken() !== null;
  }

  getCurrentUser(): LoginResponse | null {
    const user = localStorage.getItem(this.USER_KEY);
    return user ? JSON.parse(user) : null;
  }

  hasRole(role: string): boolean {
    const user = this.getCurrentUser();
    return user?.roles?.includes(role) ?? false;
  }

  isAdmin(): boolean { return this.hasRole('ROLE_ADMIN'); }
  isEmploye(): boolean { return this.hasRole('ROLE_EMPLOYE'); }
  isClient(): boolean { return this.hasRole('ROLE_CLIENT'); }
}
