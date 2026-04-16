import { inject, Injectable } from '@angular/core';
import { HttpClient, HttpErrorResponse, HttpHeaders, HttpParams } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { catchError } from 'rxjs/operators';

export type HttpMethod = 'GET' | 'POST' | 'PUT' | 'PATCH' | 'DELETE';

export interface HttpRequestOptions {
  headers?: HttpHeaders | Record<string, string | string[]>;
  params?: HttpParams | Record<string, string | string[]>;
}

@Injectable({ providedIn: 'root' })
export class HttpAdapter {
  private readonly http = inject(HttpClient);

  get<T>(url: string, options?: HttpRequestOptions): Observable<T> {
    return this.http
      .get<T>(url, this.buildOptions(options))
      .pipe(catchError(this.handleError));
  }

  post<T>(url: string, body: unknown, options?: HttpRequestOptions): Observable<T> {
    return this.http
      .post<T>(url, body, this.buildOptions(options))
      .pipe(catchError(this.handleError));
  }

  put<T>(url: string, body: unknown, options?: HttpRequestOptions): Observable<T> {
    return this.http
      .put<T>(url, body, this.buildOptions(options))
      .pipe(catchError(this.handleError));
  }

  patch<T>(url: string, body: unknown, options?: HttpRequestOptions): Observable<T> {
    return this.http
      .patch<T>(url, body, this.buildOptions(options))
      .pipe(catchError(this.handleError));
  }

  delete<T>(url: string, options?: HttpRequestOptions): Observable<T> {
    return this.http
      .delete<T>(url, this.buildOptions(options))
      .pipe(catchError(this.handleError));
  }

  private buildOptions(options?: HttpRequestOptions) {
    return {
      headers: options?.headers,
      params: options?.params
    };
  }

  private handleError(error: HttpErrorResponse) {
    const apiError = error.error as { message?: string; msg?: string } | string | null;
    const messageFromApi =
      typeof apiError === 'string' ? apiError : apiError?.message || apiError?.msg;
    const fallbackMessage = error.message || 'Erro ao processar requisicao HTTP';
    const message = messageFromApi || fallbackMessage;

    return throwError(() => new Error(message));
  }
}
