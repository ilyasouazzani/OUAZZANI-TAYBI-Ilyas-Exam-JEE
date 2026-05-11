import { HttpInterceptorFn } from '@angular/common/http';
import { inject } from '@angular/core';
import { AuthService } from '../services/auth.service';

/**
 * Intercepteur JWT — fonction Angular 17+ (pas de classe).
 *
 * Comment ça marche :
 * Avant chaque requête HTTP, cet intercepteur :
 * 1. Récupère le token JWT depuis localStorage
 * 2. Clone la requête originale en ajoutant le header Authorization
 * 3. Passe la requête modifiée au prochain handler
 *
 * Le "clone" est nécessaire car les requêtes Angular sont immutables.
 *
 * Résultat : Angular ajoute automatiquement "Authorization: Bearer <token>"
 * sur TOUTES les requêtes vers le backend — plus besoin de le faire manuellement.
 */
export const jwtInterceptor: HttpInterceptorFn = (req, next) => {
  const authService = inject(AuthService);
  const token = authService.getToken();

  // Si pas de token (utilisateur non connecté), on laisse passer sans modification
  if (!token) {
    return next(req);
  }

  // On clone la requête avec le header Authorization ajouté
  const authReq = req.clone({
    setHeaders: {
      Authorization: `Bearer ${token}`
    }
  });

  return next(authReq);
};
