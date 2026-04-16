import { Injectable } from '@angular/core';
import { Router } from '@angular/router';
import { Observable } from 'rxjs';
import { tap } from 'rxjs/operators';

import { HttpAdapter } from '../../core/infra/libs/http';
import { AuthStateService, LoggedUser } from '../../core/state/auth-state.service';

interface LoginPayload {
  username: string;
  password: string;
}

interface LoginResponse {
  id: number;
  username: string;
  token: string;
}

@Injectable()
export class LoginService {
  constructor(
    private http: HttpAdapter,
    private authState: AuthStateService,
    private router: Router
  ) {}

  login(username: string, password: string): Observable<LoggedUser> {
    const payload: LoginPayload = {
      username,
      password
    };

    return this.http.post<LoginResponse>('/api/user/login', payload).pipe(
      tap((response) => {
        this.authState.setUser({
          id: response.id,
          username: response.username,
          token: response.token
        });
      })
    );
  }

  logout(): void {
    this.authState.clearUser();
    this.router.navigate(['/login']);
  }
}
