import { inject } from '@angular/core';
import { CanActivateFn, Router } from '@angular/router';
import { AuthService } from '../services/auth.service';

/**
 * Guard de protection des routes — fonction Angular 17+.
 *
 * Vérifie si un token JWT est présent dans localStorage.
 * Si oui → accès autorisé à la route.
 * Si non → redirection vers /login.
 *
 * Usage dans app.routes.ts :
 *   { path: 'clients', component: ..., canActivate: [authGuard] }
 */
export const authGuard: CanActivateFn = (route, state) => {
  const authService = inject(AuthService);
  const router = inject(Router);

  if (authService.isLoggedIn()) {
    return true;
  }

  // Redirige vers login en mémorisant l'URL demandée (pour y revenir après login)
  return router.createUrlTree(['/login'], {
    queryParams: { returnUrl: state.url }
  });
};
