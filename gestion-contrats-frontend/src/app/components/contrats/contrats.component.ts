import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { ContratService } from '../../services/contrat.service';
import { AuthService } from '../../services/auth.service';
import { Contrat, StatutContrat } from '../../models/contrat.model';
import { Page } from '../../models/page.model';

@Component({
  selector: 'app-contrats',
  standalone: true,
  imports: [CommonModule],
  template: `
    <div class="page-header">
      <h1>📄 Gestion des Contrats</h1>

      <!-- Filtres par statut -->
      <div class="filters">
        <button *ngFor="let s of statuts"
                [class.active]="selectedStatut === s.value"
                (click)="filterByStatut(s.value)"
                class="filter-btn">
          {{ s.label }}
        </button>
      </div>
    </div>

    <div class="card">
      <table class="table" *ngIf="!loading">
        <thead>
          <tr>
            <th>ID</th><th>Type</th><th>Client</th><th>Montant</th>
            <th>Durée</th><th>Statut</th><th>Détails</th><th>Actions</th>
          </tr>
        </thead>
        <tbody>
          <tr *ngFor="let contrat of page?.content">
            <td>{{ contrat.id }}</td>
            <td>
              <span class="type-badge" [class]="'type-' + contrat.typeContrat.toLowerCase()">
                {{ contrat.typeContrat }}
              </span>
            </td>
            <td>{{ contrat.clientNomComplet }}</td>
            <td><strong>{{ contrat.montant | number:'1.0-0' }} MAD</strong></td>
            <td>{{ contrat.duree }} mois</td>
            <td>
              <span class="statut-badge" [class]="'statut-' + contrat.statut.toLowerCase()">
                {{ contrat.statut }}
              </span>
            </td>
            <td class="details">
              <span *ngIf="contrat.typeContrat === 'AUTO'">
                🚗 {{ contrat.marque }} {{ contrat.modele }} ({{ contrat.immatriculation }})
              </span>
              <span *ngIf="contrat.typeContrat === 'HABITATION'">
                🏠 {{ contrat.type }} — {{ contrat.superficie }}m² — {{ contrat.adresse }}
              </span>
              <span *ngIf="contrat.typeContrat === 'SANTE'">
                💊 {{ contrat.niveauCouverture }} — {{ contrat.nbPersonnes }} pers.
              </span>
            </td>
            <td class="actions">
              <button *ngIf="canActOnContrat && contrat.statut !== 'VALIDE' && contrat.statut !== 'RESILIE'"
                      class="btn-sm btn-success"
                      (click)="valider(contrat.id!)">✓ Valider</button>
              <button *ngIf="canActOnContrat && contrat.statut === 'VALIDE'"
                      class="btn-sm btn-danger"
                      (click)="resilier(contrat.id!)">✗ Résilier</button>
              <button *ngIf="isAdmin"
                      class="btn-sm btn-danger"
                      (click)="supprimer(contrat.id!)">🗑</button>
            </td>
          </tr>
          <tr *ngIf="page?.content?.length === 0">
            <td colspan="8" class="empty">Aucun contrat trouvé</td>
          </tr>
        </tbody>
      </table>
      <div *ngIf="loading" class="loading">Chargement...</div>

      <!-- Pagination -->
      <div class="pagination" *ngIf="page && page.totalPages > 1">
        <button (click)="changePage(currentPage - 1)" [disabled]="page.first">◀</button>
        <span>Page {{ currentPage + 1 }} / {{ page.totalPages }}</span>
        <button (click)="changePage(currentPage + 1)" [disabled]="page.last">▶</button>
      </div>
    </div>
  `,
  styles: [`
    .page-header { display: flex; justify-content: space-between; align-items: center; margin-bottom: 1.5rem; flex-wrap: wrap; gap: 1rem; }
    h1 { margin: 0; color: #1a237e; }
    .filters { display: flex; gap: 0.5rem; flex-wrap: wrap; }
    .filter-btn { padding: 6px 14px; border: 1px solid #ccc; background: white; border-radius: 20px; cursor: pointer; font-size: 0.85rem; }
    .filter-btn.active { background: #1a237e; color: white; border-color: #1a237e; }
    .card { background: white; border-radius: 10px; padding: 1.5rem; box-shadow: 0 2px 10px rgba(0,0,0,0.08); overflow-x: auto; }
    .table { width: 100%; border-collapse: collapse; }
    .table th, .table td { padding: 11px 12px; text-align: left; border-bottom: 1px solid #f0f0f0; font-size: 0.9rem; }
    .table th { background: #f5f5f5; font-weight: 600; color: #555; }
    .type-badge { padding: 3px 10px; border-radius: 12px; font-size: 0.78rem; font-weight: bold; }
    .type-auto { background: #e3f2fd; color: #1565c0; }
    .type-habitation { background: #e8f5e9; color: #2e7d32; }
    .type-sante { background: #fce4ec; color: #880e4f; }
    .statut-badge { padding: 3px 10px; border-radius: 12px; font-size: 0.78rem; font-weight: bold; }
    .statut-valide { background: #e8f5e9; color: #2e7d32; }
    .statut-brouillon { background: #f5f5f5; color: #757575; }
    .statut-en_attente { background: #fff8e1; color: #f57f17; }
    .statut-resilie { background: #ffebee; color: #c62828; }
    .statut-expire { background: #fafafa; color: #9e9e9e; }
    .details { max-width: 220px; font-size: 0.82rem; color: #555; }
    .actions { display: flex; gap: 0.4rem; }
    .btn-sm { padding: 4px 10px; border: none; border-radius: 4px; cursor: pointer; font-size: 0.8rem; }
    .btn-success { background: #e8f5e9; color: #2e7d32; }
    .btn-danger { background: #ffebee; color: #c62828; }
    .pagination { display: flex; justify-content: center; align-items: center; gap: 1rem; margin-top: 1.5rem; }
    .pagination button { padding: 6px 14px; border: 1px solid #ccc; background: white; border-radius: 4px; cursor: pointer; }
    .pagination button:disabled { opacity: 0.4; cursor: not-allowed; }
    .empty, .loading { text-align: center; padding: 2rem; color: #999; }
  `]
})
export class ContratsComponent implements OnInit {
  page: Page<Contrat> | null = null;
  loading = false;
  currentPage = 0;
  selectedStatut: StatutContrat | null = null;

  isAdmin = false;
  canActOnContrat = false;

  statuts = [
    { value: null,          label: 'Tous' },
    { value: 'BROUILLON',   label: 'Brouillon' },
    { value: 'EN_ATTENTE',  label: 'En attente' },
    { value: 'VALIDE',      label: 'Validé' },
    { value: 'RESILIE',     label: 'Résilié' }
  ];

  constructor(
    private contratService: ContratService,
    private authService: AuthService,
    private route: ActivatedRoute
  ) {
    this.isAdmin = this.authService.isAdmin();
    this.canActOnContrat = this.authService.isAdmin() || this.authService.isEmploye();
  }

  ngOnInit(): void {
    // Support du queryParam clientId (depuis la liste clients)
    this.route.queryParams.subscribe(params => {
      this.loadContrats();
    });
  }

  loadContrats(): void {
    this.loading = true;
    const obs = this.selectedStatut
      ? this.contratService.getByStatut(this.selectedStatut, this.currentPage)
      : this.contratService.getAll(this.currentPage);

    obs.subscribe({
      next: (data) => { this.page = data; this.loading = false; },
      error: ()    => { this.loading = false; }
    });
  }

  filterByStatut(statut: any): void {
    this.selectedStatut = statut;
    this.currentPage = 0;
    this.loadContrats();
  }

  changePage(p: number): void {
    this.currentPage = p;
    this.loadContrats();
  }

  valider(id: number): void {
    this.contratService.valider(id).subscribe(() => this.loadContrats());
  }

  resilier(id: number): void {
    if (!confirm('Résilier ce contrat ?')) return;
    this.contratService.resilier(id).subscribe(() => this.loadContrats());
  }

  supprimer(id: number): void {
    if (!confirm('Supprimer définitivement ce contrat ?')) return;
    this.contratService.delete(id).subscribe(() => this.loadContrats());
  }
}
