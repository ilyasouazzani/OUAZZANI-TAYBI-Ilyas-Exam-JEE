import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router, ActivatedRoute } from '@angular/router';
import { AuthService } from '../../services/auth.service';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  template: `
    <div class="login-container">
      <div class="login-card">
        <h2>🛡️ Gestion des Contrats</h2>
        <h3>Connexion</h3>

        <form [formGroup]="loginForm" (ngSubmit)="onSubmit()">

          <div class="form-group">
            <label>Nom d'utilisateur</label>
            <input type="text" formControlName="username" placeholder="admin">
            <small *ngIf="loginForm.get('username')?.invalid && loginForm.get('username')?.touched"
                   class="error">Champ obligatoire</small>
          </div>

          <div class="form-group">
            <label>Mot de passe</label>
            <input type="password" formControlName="password" placeholder="••••••">
            <small *ngIf="loginForm.get('password')?.invalid && loginForm.get('password')?.touched"
                   class="error">Champ obligatoire</small>
          </div>

          <div *ngIf="errorMessage" class="alert-error">{{ errorMessage }}</div>

          <button type="submit" [disabled]="loginForm.invalid || loading" class="btn-login">
            {{ loading ? 'Connexion...' : 'Se connecter' }}
          </button>
        </form>

        <!-- Aide pour l'exam / soutenance -->
        <div class="demo-accounts">
          <p><strong>Comptes de test :</strong></p>
          <p>admin / admin123 &nbsp;|&nbsp; employe / employe123 &nbsp;|&nbsp; client / client123</p>
        </div>
      </div>
    </div>
  `,
  styles: [`
    .login-container {
      display: flex; justify-content: center; align-items: center;
      min-height: 100vh; background: #e8eaf6;
    }
    .login-card {
      background: white; padding: 2.5rem; border-radius: 12px;
      box-shadow: 0 4px 20px rgba(0,0,0,0.15); width: 380px;
    }
    h2 { text-align: center; color: #1a237e; margin-bottom: 0.25rem; }
    h3 { text-align: center; color: #666; margin-bottom: 1.5rem; }
    .form-group { margin-bottom: 1.25rem; }
    label { display: block; margin-bottom: 0.4rem; font-weight: 600; color: #333; }
    input {
      width: 100%; padding: 10px; border: 1px solid #ccc;
      border-radius: 6px; font-size: 0.95rem; box-sizing: border-box;
    }
    input:focus { outline: none; border-color: #1a237e; }
    .error { color: #e53935; font-size: 0.8rem; }
    .alert-error {
      background: #ffebee; color: #c62828;
      padding: 10px; border-radius: 6px; margin-bottom: 1rem;
      font-size: 0.9rem; text-align: center;
    }
    .btn-login {
      width: 100%; padding: 12px; background: #1a237e;
      color: white; border: none; border-radius: 6px;
      font-size: 1rem; cursor: pointer; transition: background 0.2s;
    }
    .btn-login:hover:not(:disabled) { background: #283593; }
    .btn-login:disabled { background: #9e9e9e; cursor: not-allowed; }
    .demo-accounts {
      margin-top: 1.5rem; padding: 1rem; background: #f5f5f5;
      border-radius: 6px; font-size: 0.82rem; color: #555;
    }
    .demo-accounts p { margin: 0.2rem 0; }
  `]
})
export class LoginComponent {
  loginForm: FormGroup;
  loading = false;
  errorMessage = '';

  constructor(
    private fb: FormBuilder,
    private authService: AuthService,
    private router: Router,
    private route: ActivatedRoute
  ) {
    // Si déjà connecté → redirect direct
    if (this.authService.isLoggedIn()) {
      this.router.navigate(['/clients']);
    }

    this.loginForm = this.fb.group({
      username: ['', Validators.required],
      password: ['', Validators.required]
    });
  }

  onSubmit(): void {
    if (this.loginForm.invalid) return;

    this.loading = true;
    this.errorMessage = '';

    this.authService.login(this.loginForm.value).subscribe({
      next: () => {
        // Récupère l'URL de retour si elle existe, sinon /clients
        const returnUrl = this.route.snapshot.queryParams['returnUrl'] || '/clients';
        this.router.navigate([returnUrl]);
      },
      error: (err) => {
        this.errorMessage = 'Identifiants incorrects. Vérifiez votre nom d\'utilisateur et mot de passe.';
        this.loading = false;
      }
    });
  }
}
