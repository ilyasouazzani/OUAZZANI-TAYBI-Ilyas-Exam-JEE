export interface LoginRequest {
  username: string;
  password: string;
}

/**
 * Réponse du backend après un login réussi.
 * On stocke le token dans localStorage pour le réutiliser.
 */
export interface LoginResponse {
  token: string;
  username: string;
  roles: string[];
}

export interface RegisterRequest {
  username: string;
  password: string;
  email: string;
  role?: string; // CLIENT, EMPLOYE, ADMIN
}
