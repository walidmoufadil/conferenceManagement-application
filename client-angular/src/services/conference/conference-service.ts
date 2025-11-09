// DTOs (basics, based on provided OpenAPI documentation)
export interface ReviewRequestDTO {
  date?: string;
  commentaire?: string;
}

export interface ReviewResponseDTO {
  id?: number;
  date?: string;
  commentaire?: string;
}

export interface Keynote {
  id?: number;
  nom?: string;
  prenom?: string;
  email?: string;
  fonction?: string;
}

export interface ConferenceRequestDTO {
  titre?: string;
  type?: 'Academic' | 'commercial' | string;
  date?: string;
  duree?: number;
  nombreInscrits?: number;
  score?: number;
  reviews?: ReviewRequestDTO[];
  keynoteId?: number;
}

export interface ConferenceResponseDTO extends ConferenceRequestDTO {
  id?: number;
  reviews?: ReviewResponseDTO[];
  keynote?: Keynote;
}

import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class ConferenceService {
  private readonly baseUrl = 'http://localhost:8888/conference-service/api';

  constructor(private http: HttpClient) {}

  // Methods
  getConferences(): Observable<ConferenceResponseDTO[]> {
    return this.http.get<ConferenceResponseDTO[]>(`${this.baseUrl}/conferences`);
  }

  getConferenceById(id: number): Observable<ConferenceResponseDTO> {
    return this.http.get<ConferenceResponseDTO>(`${this.baseUrl}/conferences/${id}`);
  }

  createConference(payload: ConferenceRequestDTO): Observable<any> {
    return this.http.post<any>(`${this.baseUrl}/conferences/create`, payload);
  }

  updateConference(id: number, payload: ConferenceRequestDTO): Observable<any> {
    return this.http.put<any>(`${this.baseUrl}/conferences/${id}`, payload);
  }

  patchConference(id: number, payload: ConferenceRequestDTO): Observable<any> {
    return this.http.patch<any>(`${this.baseUrl}/conferences/${id}`, payload);
  }

  deleteConference(id: number): Observable<any> {
    return this.http.delete<any>(`${this.baseUrl}/conferences/${id}`);
  }

  patchConferenceReviews(id: number, reviews: ReviewRequestDTO[]): Observable<any> {
    return this.http.patch<any>(`${this.baseUrl}/conferences/${id}/reviews`, reviews);
  }

  deleteReviewFromConference(conferenceId: number, reviewId: number): Observable<any> {
    return this.http.delete<any>(`${this.baseUrl}/conferences/${conferenceId}/reviews/${reviewId}`);
  }
}
