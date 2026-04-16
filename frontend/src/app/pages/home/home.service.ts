import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

import { HttpAdapter } from '../../core/infra/libs/http';

export interface Classroom {
  id: number;
  name: string;
}

export interface Discipline {
  id: number;
  name: string;
}

export interface GradeEvaluation {
  gradeId: number;
  evaluationId: number;
  evaluationName: string;
  evaluationWeight: number;
  disciplineId: number;
  disciplineName: string;
  gradeValue: number;
}

export interface StudentGrade {
  studentId: number;
  studentName: string;
  classroomId: number;
  classroomName: string;
  evaluations: GradeEvaluation[];
}

export interface GradeAuditEntry {
  id: number;
  studentId: number;
  studentName: string;
  evaluationId: number;
  evaluationName: string;
  oldValue: number;
  newValue: number;
  modifiedByUsername: string;
  modificationDate: string;
}

interface UpdateGradeItemPayload {
  gradeId?: number;
  studentId?: number;
  evaluationId?: number;
  gradeValue: number;
}

interface UpdateGradesPayload {
  modifiedByUserId: number;
  grades: UpdateGradeItemPayload[];
}

@Injectable()
export class HomeService {
  constructor(private http: HttpAdapter) {}

  listClassrooms(userId: number): Observable<Classroom[]> {
    return this.http.get<Classroom[]>('/api/classrooms/user', {
      params: {
        userId: String(userId)
      }
    });
  }

  listDisciplines(userId: number, classroomId: number): Observable<Discipline[]> {
    return this.http.get<Discipline[]>('/api/classrooms/disciplines', {
      params: {
        userId: String(userId),
        classroomId: String(classroomId)
      }
    });
  }

  listGrades(classroomId: number, disciplineId: number): Observable<StudentGrade[]> {
    return this.http.get<StudentGrade[]>('/api/grades', {
      params: {
        classroomId: String(classroomId),
        disciplineId: String(disciplineId)
      }
    });
  }

  saveGradesBatch(modifiedByUserId: number, grades: UpdateGradeItemPayload[]): Observable<void> {
    const payload: UpdateGradesPayload = { modifiedByUserId, grades };
    return this.http.put<void>('/api/grades', payload);
  }

  listGradeAudit(studentId: number, disciplineId: number): Observable<GradeAuditEntry[]> {
    return this.http.get<GradeAuditEntry[]>('/api/grades/audit', {
      params: {
        studentId: String(studentId),
        disciplineId: String(disciplineId)
      }
    });
  }
}
