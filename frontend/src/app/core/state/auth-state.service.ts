import { Injectable, WritableSignal, signal } from '@angular/core';

export interface LoggedUser {
  id: number;
  username: string;
  token: string;
}

@Injectable({ providedIn: 'root' })
export class AuthStateService {
  private readonly storageKey = 'boletim-auth';
  readonly user: WritableSignal<LoggedUser | null> = signal<LoggedUser | null>(null);

  constructor() {
    this.restoreUser();
  }

  get currentUser(): LoggedUser | null {
    return this.user();
  }

  setUser(user: LoggedUser): void {
    this.user.set(user);
    localStorage.setItem(this.storageKey, JSON.stringify(user));
  }

  clearUser(): void {
    this.user.set(null);
    localStorage.removeItem(this.storageKey);
  }

  isAuthenticated(): boolean {
    return !!this.user();
  }

  private restoreUser(): void {
    const storedUser = localStorage.getItem(this.storageKey);

    if (!storedUser) {
      return;
    }

    try {
      this.user.set(JSON.parse(storedUser) as LoggedUser);
    } catch {
      localStorage.removeItem(this.storageKey);
    }
  }
}
