// DTOs for keynote-service (based on keynote-service-api-docs.json)
export interface KeynoteRequestDTO {
  nom?: string;
  prenom?: string;
  email?: string;
  fonction?: string;
}

export interface KeynoteResponseDTO extends KeynoteRequestDTO {
  id?: number;
}

import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class KeynoteService {
  private readonly baseUrl = 'http://localhost:8888/keynote-service/api';

  constructor(private http: HttpClient) {}

  getKeynotes(): Observable<KeynoteResponseDTO[]> {
    return this.http.get<KeynoteResponseDTO[]>(`${this.baseUrl}/keynotes`);
  }

  getKeynoteById(id: number): Observable<KeynoteResponseDTO> {
    return this.http.get<KeynoteResponseDTO>(`${this.baseUrl}/keynotes/${id}`);
  }

  createKeynote(payload: KeynoteRequestDTO): Observable<any> {
    return this.http.post<any>(`${this.baseUrl}/keynotes/create`, payload);
  }

  updateKeynote(id: number, payload: KeynoteRequestDTO): Observable<any> {
    return this.http.put<any>(`${this.baseUrl}/keynotes/${id}`, payload);
  }

  patchKeynote(id: number, payload: KeynoteRequestDTO): Observable<any> {
    return this.http.patch<any>(`${this.baseUrl}/keynotes/${id}`, payload);
  }

  deleteKeynote(id: number): Observable<any> {
    // note: OpenAPI shows delete endpoint as /api/keynotes/delete/{id}
    return this.http.delete<any>(`${this.baseUrl}/keynotes/delete/${id}`);
  }
}
