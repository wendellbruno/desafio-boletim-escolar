import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { finalize } from 'rxjs/operators';

import { AuthStateService } from '../../core/state/auth-state.service';
import { LoginService } from './login.service';

@Component({
  selector: 'app-login',
  standalone: false,
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss']
})
export class LoginComponent {
  username = '';
  password = '';
  error = '';
  isSubmitting = false;

  constructor(
    private loginService: LoginService,
    private router: Router,
    public authState: AuthStateService
  ) {}

  submit(): void {
    if (this.isSubmitting) {
      return;
    }

    if (!this.username.trim() || !this.password.trim()) {
      this.error = 'Preencha usuário e senha.';
      return;
    }

    this.error = '';
    this.isSubmitting = true;

    this.loginService
      .login(this.username.trim(), this.password.trim())
      .pipe(finalize(() => (this.isSubmitting = false)))
      .subscribe({
        next: () => {
          this.router.navigate(['/home']);
        },
        error: (error: Error) => {
          this.error = error.message || 'Não foi possível realizar o login.';
        }
      });
  }
}
