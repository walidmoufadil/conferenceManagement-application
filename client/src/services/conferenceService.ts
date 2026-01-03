import apiClient from '@/lib/api';
import { Conference, ConferenceRequest, ReviewRequest } from '@/types/conference.types';

const BASE_PATH = '/conference-service/api/conferences';

export const conferenceService = {
  getAll: async (): Promise<Conference[]> => {
    const response = await apiClient.get<Conference[]>(BASE_PATH);
    return response.data;
  },

  getById: async (id: number): Promise<Conference> => {
    const response = await apiClient.get<Conference>(`${BASE_PATH}/${id}`);
    return response.data;
  },

  create: async (data: ConferenceRequest): Promise<void> => {
    await apiClient.post(`${BASE_PATH}/create`, data);
  },

  update: async (id: number, data: ConferenceRequest): Promise<void> => {
    await apiClient.put(`${BASE_PATH}/${id}`, data);
  },

  patch: async (id: number, data: Partial<ConferenceRequest>): Promise<void> => {
    await apiClient.patch(`${BASE_PATH}/${id}`, data);
  },

  delete: async (id: number): Promise<void> => {
    await apiClient.delete(`${BASE_PATH}/${id}`);
  },

  updateReviews: async (id: number, reviews: ReviewRequest[]): Promise<void> => {
    await apiClient.patch(`${BASE_PATH}/${id}/reviews`, reviews);
  },

  deleteReview: async (conferenceId: number, reviewId: number): Promise<void> => {
    await apiClient.delete(`${BASE_PATH}/${conferenceId}/reviews/${reviewId}`);
  },
};
