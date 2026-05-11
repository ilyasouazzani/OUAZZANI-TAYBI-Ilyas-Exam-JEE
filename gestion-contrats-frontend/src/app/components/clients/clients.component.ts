import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule, FormsModule, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { RouterLink } from '@angular/router';
import { ClientService } from '../../services/client.service';
import { AuthService } from '../../services/auth.service';
import { Client } from '../../models/client.model';
import { Page } from '../../models/page.model';

@Component({
  selector: 'app-clients',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, FormsModule, RouterLink],
  template: `
    <div class="page-header">
      <h1>👥 Gestion des Clients</h1>

      <!-- Barre de recherche + bouton Ajouter -->
      <div class="toolbar">
        <div class="search-bar">
          <input type="text" [(ngModel)]="keyword" (ngModel)="keyword"
                 placeholder="Rechercher par nom ou prénom..."
                 (input)="onSearch()" class="search-input">
        </div>
        <button *ngIf="isAdmin" class="btn-primary" (click)="openForm()">
          + Nouveau Client
        </button>
      </div>
    </div>

    <!-- Tableau des clients -->
    <div class="card">
      <table class="table" *ngIf="!loading">
        <thead>
          <tr>
            <th>ID</th><th>Nom complet</th><th>Email</th>
            <th>Téléphone</th><th>Contrats</th><th>Actions</th>
          </tr>
        </thead>
        <tbody>
          <tr *ngFor="let client of page?.content">
            <td>{{ client.id }}</td>
            <td><strong>{{ client.prenom }} {{ client.nom }}</strong></td>
            <td>{{ client.email }}</td>
            <td>{{ client.telephone || '—' }}</td>
            <td>
              <span class="badge">{{ client.nombreContrats }}</span>
            </td>
            <td class="actions">
              <a [routerLink]="['/contrats']"
                 [queryParams]="{clientId: client.id}"
                 class="btn-sm btn-info">Contrats</a>
              <button *ngIf="isAdmin" class="btn-sm btn-warning"
                      (click)="openForm(client)">Modifier</button>
              <button *ngIf="isAdmin" class="btn-sm btn-danger"
                      (click)="deleteClient(client.id!)">Supprimer</button>
            </td>
          </tr>
          <tr *ngIf="page?.content?.length === 0">
            <td colspan="6" class="empty">Aucun client trouvé</td>
          </tr>
        </tbody>
      </table>
      <div *ngIf="loading" class="loading">Chargement...</div>

      <!-- Pagination -->
      <div class="pagination" *ngIf="page && page.totalPages > 1">
        <button (click)="changePage(currentPage - 1)"
                [disabled]="page.first">◀ Préc.</button>
        <span>Page {{ currentPage + 1 }} / {{ page.totalPages }}</span>
        <button (click)="changePage(currentPage + 1)"
                [disabled]="page.last">Suiv. ▶</button>
      </div>
    </div>

    <!-- Modal Formulaire Créer/Modifier -->
    <div class="modal-overlay" *ngIf="showForm" (click)="closeForm()">
      <div class="modal" (click)="$event.stopPropagation()">
        <h3>{{ editingClient ? 'Modifier' : 'Nouveau' }} Client</h3>

        <form [formGroup]="clientForm" (ngSubmit)="saveClient()">
          <div class="form-row">
            <div class="form-group">
              <label>Nom *</label>
              <input type="text" formControlName="nom">
            </div>
            <div class="form-group">
              <label>Prénom *</label>
              <input type="text" formControlName="prenom">
            </div>
          </div>
          <div class="form-group">
            <label>Email *</label>
            <input type="email" formControlName="email">
          </div>
          <div class="form-group">
            <label>Téléphone</label>
            <input type="text" formControlName="telephone">
          </div>

          <div *ngIf="formError" class="alert-error">{{ formError }}</div>

          <div class="modal-actions">
            <button type="button" class="btn-secondary" (click)="closeForm()">Annuler</button>
            <button type="submit" class="btn-primary"
                    [disabled]="clientForm.invalid || saving">
              {{ saving ? 'Enregistrement...' : 'Enregistrer' }}
            </button>
          </div>
        </form>
      </div>
    </div>
  `,
  styles: [`
    .page-header { display: flex; justify-content: space-between; align-items: center; margin-bottom: 1.5rem; flex-wrap: wrap; gap: 1rem; }
    h1 { margin: 0; color: #1a237e; }
    .toolbar { display: flex; gap: 1rem; align-items: center; }
    .search-input { padding: 8px 14px; border: 1px solid #ccc; border-radius: 6px; width: 280px; font-size: 0.9rem; }
    .card { background: white; border-radius: 10px; padding: 1.5rem; box-shadow: 0 2px 10px rgba(0,0,0,0.08); }
    .table { width: 100%; border-collapse: collapse; }
    .table th, .table td { padding: 12px; text-align: left; border-bottom: 1px solid #f0f0f0; }
    .table th { background: #f5f5f5; font-weight: 600; color: #555; }
    .table tr:hover { background: #fafafa; }
    .badge { background: #e8eaf6; color: #1a237e; padding: 3px 10px; border-radius: 12px; font-size: 0.8rem; font-weight: bold; }
    .actions { display: flex; gap: 0.5rem; }
    .btn-sm { padding: 5px 12px; border: none; border-radius: 4px; cursor: pointer; font-size: 0.8rem; }
    .btn-info { background: #e3f2fd; color: #1565c0; }
    .btn-warning { background: #fff3e0; color: #e65100; }
    .btn-danger { background: #ffebee; color: #c62828; }
    .btn-primary { background: #1a237e; color: white; border: none; padding: 9px 18px; border-radius: 6px; cursor: pointer; }
    .btn-secondary { background: #f5f5f5; color: #333; border: none; padding: 9px 18px; border-radius: 6px; cursor: pointer; }
    .pagination { display: flex; justify-content: center; align-items: center; gap: 1rem; margin-top: 1.5rem; }
    .pagination button { padding: 6px 14px; border: 1px solid #ccc; background: white; border-radius: 4px; cursor: pointer; }
    .pagination button:disabled { opacity: 0.4; cursor: not-allowed; }
    .empty { text-align: center; color: #999; padding: 2rem; }
    .loading { text-align: center; padding: 2rem; color: #666; }
    .modal-overlay { position: fixed; inset: 0; background: rgba(0,0,0,0.5); display: flex; align-items: center; justify-content: center; z-index: 100; }
    .modal { background: white; padding: 2rem; border-radius: 12px; width: 460px; max-width: 95vw; }
    .modal h3 { margin-top: 0; color: #1a237e; }
    .form-row { display: grid; grid-template-columns: 1fr 1fr; gap: 1rem; }
    .form-group { margin-bottom: 1rem; }
    .form-group label { display: block; margin-bottom: 0.4rem; font-weight: 600; font-size: 0.9rem; }
    .form-group input { width: 100%; padding: 9px; border: 1px solid #ccc; border-radius: 6px; box-sizing: border-box; }
    .modal-actions { display: flex; justify-content: flex-end; gap: 1rem; margin-top: 1.5rem; }
    .alert-error { background: #ffebee; color: #c62828; padding: 10px; border-radius: 6px; margin-bottom: 1rem; font-size: 0.9rem; }
  `]
})
export class ClientsComponent implements OnInit {
  page: Page<Client> | null = null;
  loading = false;
  saving = false;
  keyword = '';
  currentPage = 0;

  showForm = false;
  editingClient: Client | null = null;
  clientForm!: FormGroup;
  formError = '';

  isAdmin = false;
  isEmploye = false;

  constructor(
    private clientService: ClientService,
    private authService: AuthService,
    private fb: FormBuilder
  ) {
    this.isAdmin = this.authService.isAdmin();
    this.isEmploye = this.authService.isEmploye();
  }

  ngOnInit(): void {
    this.loadClients();
    this.initForm();
  }

  initForm(client?: Client): void {
    this.clientForm = this.fb.group({
      nom:       [client?.nom || '',       Validators.required],
      prenom:    [client?.prenom || '',    Validators.required],
      email:     [client?.email || '',     [Validators.required, Validators.email]],
      telephone: [client?.telephone || '']
    });
  }

  loadClients(): void {
    this.loading = true;
    const obs = this.keyword
      ? this.clientService.search(this.keyword, this.currentPage)
      : this.clientService.getAll(this.currentPage);

    obs.subscribe({
      next: (data) => { this.page = data; this.loading = false; },
      error: ()    => { this.loading = false; }
    });
  }

  onSearch(): void {
    this.currentPage = 0;
    this.loadClients();
  }

  changePage(p: number): void {
    this.currentPage = p;
    this.loadClients();
  }

  openForm(client?: Client): void {
    this.editingClient = client || null;
    this.formError = '';
    this.initForm(client);
    this.showForm = true;
  }

  closeForm(): void {
    this.showForm = false;
    this.editingClient = null;
  }

  saveClient(): void {
    if (this.clientForm.invalid) return;
    this.saving = true;
    this.formError = '';

    const data = this.clientForm.value;
    const obs = this.editingClient
      ? this.clientService.update(this.editingClient.id!, data)
      : this.clientService.create(data);

    obs.subscribe({
      next: () => { this.saving = false; this.closeForm(); this.loadClients(); },
      error: (err) => {
        this.formError = err.error?.message || 'Erreur lors de l\'enregistrement';
        this.saving = false;
      }
    });
  }

  deleteClient(id: number): void {
    if (!confirm('Supprimer ce client et tous ses contrats ?')) return;
    this.clientService.delete(id).subscribe({
      next: () => this.loadClients(),
      error: (err) => alert(err.error?.message || 'Erreur lors de la suppression')
    });
  }
}
