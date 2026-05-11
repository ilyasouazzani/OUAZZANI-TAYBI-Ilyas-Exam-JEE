import { Component } from '@angular/core';
import { Router, RouterLink } from '@angular/router';
import { CommonModule } from '@angular/common';
import { AuthService } from '../../services/auth.service';

@Component({
  selector: 'app-navbar',
  standalone: true,
  imports: [CommonModule, RouterLink],
  template: `
    <nav class="navbar">
      <div class="navbar-brand">🛡️ AssuranceApp</div>

      <div class="navbar-links">
        <a routerLink="/clients">Clients</a>
        <a routerLink="/contrats">Contrats</a>
      </div>

      <div class="navbar-user">
        <span class="username">👤 {{ username }}</span>
        <span class="role-badge" *ngFor="let role of roles">
          {{ role | slice:5 }}
        </span>
        <button class="btn-logout" (click)="logout()">Déconnexion</button>
      </div>
    </nav>
  `,
  styles: [`
    .navbar {
      display: flex;
      align-items: center;
      justify-content: space-between;
      padding: 0.75rem 2rem;
      background: #1a237e;
      color: white;
      box-shadow: 0 2px 8px rgba(0,0,0,0.3);
    }
    .navbar-brand { font-size: 1.2rem; font-weight: bold; }
    .navbar-links a {
      color: white; text-decoration: none;
      margin: 0 1rem; font-size: 0.95rem;
    }
    .navbar-links a:hover { text-decoration: underline; }
    .navbar-user { display: flex; align-items: center; gap: 0.75rem; }
    .username { font-size: 0.9rem; }
    .role-badge {
      background: #e8eaf6; color: #1a237e;
      padding: 2px 8px; border-radius: 12px;
      font-size: 0.75rem; font-weight: bold;
    }
    .btn-logout {
      background: #ef5350; color: white;
      border: none; padding: 6px 14px;
      border-radius: 4px; cursor: pointer;
      font-size: 0.85rem;
    }
    .btn-logout:hover { background: #c62828; }
  `]
})
export class NavbarComponent {
  username = '';
  roles: string[] = [];

  constructor(private authService: AuthService, private router: Router) {
    const user = this.authService.getCurrentUser();
    this.username = user?.username ?? '';
    this.roles = user?.roles ?? [];
  }

  logout(): void {
    this.authService.logout();
    this.router.navigate(['/login']);
  }
}
