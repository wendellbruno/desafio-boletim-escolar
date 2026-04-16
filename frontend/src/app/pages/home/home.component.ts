import { ChangeDetectorRef, Component, OnDestroy, OnInit } from '@angular/core';
import { Subscription } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { AuthStateService } from '../../core/state/auth-state.service';
import { LoginService } from '../login/login.service';
import { Classroom, Discipline, GradeAuditEntry, HomeService, StudentGrade } from './home.service';

interface GradeColumn {
  evaluationId: number;
  evaluationName: string;
  evaluationWeight: number;
}

interface GradeCell {
  gradeId: number;
  value: number | null;
}

interface GradeRow {
  studentId: number;
  studentName: string;
  cells: Record<number, GradeCell>;
}

@Component({
  selector: 'app-home',
  standalone: false,
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.scss'],
  providers: [HomeService, LoginService]
})
export class HomeComponent implements OnInit, OnDestroy {
  classrooms: Classroom[] = [];
  disciplines: Discipline[] = [];
  columns: GradeColumn[] = [];
  rows: GradeRow[] = [];

  selectedClassroomId: number | null = null;
  selectedDisciplineId: number | null = null;

  isLoadingClassrooms = false;
  isLoadingDisciplines = false;
  isLoadingGrades = false;
  isSaving = false;
  isLoadingAudit = false;

  error = '';
  success = '';
  auditError = '';

  isAuditModalOpen = false;
  selectedAuditStudentName = '';
  selectedAuditRows: GradeAuditEntry[] = [];
  private classroomsRequest?: Subscription;
  private disciplinesRequest?: Subscription;
  private gradesRequest?: Subscription;
  private auditRequest?: Subscription;

  constructor(
    private cdr: ChangeDetectorRef,
    public authState: AuthStateService,
    private homeService: HomeService,
    private loginService: LoginService
  ) {}

  ngOnInit(): void {
    this.loadClassrooms();
  }

  ngOnDestroy(): void {
    this.classroomsRequest?.unsubscribe();
    this.disciplinesRequest?.unsubscribe();
    this.gradesRequest?.unsubscribe();
    this.auditRequest?.unsubscribe();
  }

  get currentUsername(): string {
    return this.authState.currentUser?.username ?? '';
  }

  loadClassrooms(): void {
    const user = this.authState.currentUser;

    if (!user) {
      this.isLoadingClassrooms = false;
      return;
    }

    this.error = '';
    this.success = '';
    this.isLoadingClassrooms = true;

    this.classroomsRequest?.unsubscribe();
    this.classroomsRequest = this.homeService
      .listClassrooms(user.id)
      .pipe(
        finalize(() => {
          this.isLoadingClassrooms = false;
          this.refreshView();
        })
      )
      .subscribe({
        next: (classrooms) => {
          this.classrooms = classrooms;

          if (classrooms.length > 0) {
            this.selectedClassroomId = classrooms[0].id;
            this.onClassroomChange();
          }
          this.refreshView();
        },
        error: (error: Error) => {
          this.error = error.message || 'Nao foi possivel carregar as turmas.';
          this.refreshView();
        }
      });
  }

  onClassroomChange(classroomId?: number | null): void {
    if (classroomId !== undefined) {
      this.selectedClassroomId = classroomId;
    }

    const user = this.authState.currentUser;
    if (!user || !this.selectedClassroomId) {
      this.isLoadingDisciplines = false;
      this.isLoadingGrades = false;
      return;
    }

    this.disciplinesRequest?.unsubscribe();
    this.gradesRequest?.unsubscribe();

    this.disciplines = [];
    this.selectedDisciplineId = null;
    this.columns = [];
    this.rows = [];
    this.error = '';
    this.success = '';
    this.isLoadingGrades = false;
    this.isLoadingDisciplines = true;

    this.disciplinesRequest = this.homeService
      .listDisciplines(user.id, this.selectedClassroomId)
      .pipe(
        finalize(() => {
          this.isLoadingDisciplines = false;
          this.refreshView();
        })
      )
      .subscribe({
        next: (disciplines) => {
          this.disciplines = disciplines;

          if (disciplines.length > 0) {
            this.selectedDisciplineId = disciplines[0].id;
            this.onDisciplineChange();
          }
          this.refreshView();
        },
        error: (error: Error) => {
          this.error = error.message || 'Nao foi possivel carregar as disciplinas.';
          this.refreshView();
        }
      });
  }

  onDisciplineChange(disciplineId?: number | null): void {
    if (disciplineId !== undefined) {
      this.selectedDisciplineId = disciplineId;
    }

    if (!this.selectedClassroomId || !this.selectedDisciplineId) {
      this.isLoadingGrades = false;
      return;
    }

    this.gradesRequest?.unsubscribe();
    this.columns = [];
    this.rows = [];
    this.error = '';
    this.success = '';
    this.isLoadingGrades = true;

    this.gradesRequest = this.homeService
      .listGrades(this.selectedClassroomId, this.selectedDisciplineId)
      .pipe(
        finalize(() => {
          this.isLoadingGrades = false;
          this.refreshView();
        })
      )
      .subscribe({
        next: (grades) => {
          this.buildGrid(grades);
          this.refreshView();
        },
        error: (error: Error) => {
          this.error = error.message || 'Nao foi possivel carregar as notas.';
          this.refreshView();
        }
      });
  }

  updateCellValue(row: GradeRow, evaluationId: number, value: string | number): void {
    const cell = row.cells[evaluationId];
    if (!cell) {
      return;
    }

    if (value === '') {
      row.cells[evaluationId] = {
        ...cell,
        value: null
      };
      return;
    }

    const parsed = Number(value);
    if (Number.isNaN(parsed)) {
      return;
    }

    const clamped = Math.max(0, Math.min(10, parsed));
    row.cells[evaluationId] = {
      ...cell,
      value: Number(clamped.toFixed(2))
    };
  }

  getAverage(row: GradeRow): string {
    let totalWeight = 0;
    let weightedSum = 0;
    let validGrades = 0;

    for (const column of this.columns) {
      const cell = row.cells[column.evaluationId];
      if (!cell || cell.value === null) {
        continue;
      }

      if (!this.isValidWeight(column.evaluationWeight)) {
        continue;
      }

      totalWeight += column.evaluationWeight;
      weightedSum += cell.value * column.evaluationWeight;
      validGrades += 1;
    }

    if (!totalWeight || validGrades === 0) {
      return '-';
    }

    return (weightedSum / totalWeight).toFixed(2);
  }

  saveBatch(): void {
    if (!this.rows.length || !this.columns.length) {
      return;
    }

    const payload = this.rows.flatMap((row) =>
      this.columns
        .map((column) => row.cells[column.evaluationId])
        .filter((cell): cell is GradeCell => !!cell && cell.value !== null)
        .map((cell) => ({
          gradeId: cell.gradeId,
          gradeValue: Number(cell.value!.toFixed(2))
        }))
    );

    this.error = '';
    this.success = '';
    this.isSaving = true;

    this.homeService
      .saveGradesBatch(payload)
      .pipe(
        finalize(() => {
          this.isSaving = false;
          this.refreshView();
        })
      )
      .subscribe({
        next: () => {
          this.success = 'Notas salvas com sucesso.';
          this.refreshView();
        },
        error: (error: Error) => {
          this.error = error.message || 'Nao foi possivel salvar as notas.';
          this.refreshView();
        }
      });
  }

  logout(): void {
    this.loginService.logout();
  }

  openAuditModal(row: GradeRow): void {
    if (!this.selectedDisciplineId) {
      return;
    }

    this.isAuditModalOpen = true;
    this.selectedAuditStudentName = row.studentName;
    this.selectedAuditRows = [];
    this.auditError = '';
    this.isLoadingAudit = true;
    this.auditRequest?.unsubscribe();

    this.auditRequest = this.homeService
      .listGradeAudit(row.studentId, this.selectedDisciplineId)
      .pipe(
        finalize(() => {
          this.isLoadingAudit = false;
          this.refreshView();
        })
      )
      .subscribe({
        next: (rows) => {
          this.selectedAuditRows = rows;
          this.refreshView();
        },
        error: (error: Error) => {
          this.auditError = error.message || 'Nao foi possivel carregar a auditoria.';
          this.refreshView();
        }
      });
  }

  closeAuditModal(): void {
    this.isAuditModalOpen = false;
    this.selectedAuditStudentName = '';
    this.selectedAuditRows = [];
    this.auditError = '';
    this.auditRequest?.unsubscribe();
    this.isLoadingAudit = false;
  }

  private buildGrid(grades: StudentGrade[]): void {
    const columnsMap: Record<number, GradeColumn> = {};

    grades.forEach((student) => {
      student.evaluations.forEach((evaluation) => {
        columnsMap[evaluation.evaluationId] = {
          evaluationId: evaluation.evaluationId,
          evaluationName: evaluation.evaluationName,
          evaluationWeight: evaluation.evaluationWeight
        };
      });
    });

    this.columns = Object.values(columnsMap).sort((a, b) => a.evaluationId - b.evaluationId);
    this.rows = grades.map((student) => {
      const cells: Record<number, GradeCell> = {};
      student.evaluations.forEach((evaluation) => {
        cells[evaluation.evaluationId] = {
          gradeId: evaluation.gradeId,
          value: evaluation.gradeValue
        };
      });

      return {
        studentId: student.studentId,
        studentName: student.studentName,
        cells
      };
    });
  }

  private isValidWeight(weight: number): boolean {
    return Number.isFinite(weight) && weight >= 1 && weight <= 5;
  }

  private refreshView(): void {
    this.cdr.detectChanges();
  }

  formatAuditDate(value: string): string {
    const date = new Date(value);
    if (Number.isNaN(date.getTime())) {
      return value;
    }

    return date.toLocaleString('pt-BR');
  }
}
