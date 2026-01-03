import apiClient from '@/lib/api';
import { Keynote, KeynoteRequest } from '@/types/conference.types';

const BASE_PATH = '/keynote-service/api/keynotes';

export const keynoteService = {
  getAll: async (): Promise<Keynote[]> => {
    const response = await apiClient.get<Keynote[]>(BASE_PATH);
    return response.data;
  },

  getById: async (id: number): Promise<Keynote> => {
    const response = await apiClient.get<Keynote>(`${BASE_PATH}/${id}`);
    return response.data;
  },

  create: async (data: KeynoteRequest): Promise<void> => {
    await apiClient.post(`${BASE_PATH}/create`, data);
  },

  update: async (id: number, data: KeynoteRequest): Promise<void> => {
    await apiClient.put(`${BASE_PATH}/${id}`, data);
  },

  patch: async (id: number, data: Partial<KeynoteRequest>): Promise<void> => {
    await apiClient.patch(`${BASE_PATH}/${id}`, data);
  },

  delete: async (id: number): Promise<void> => {
    await apiClient.delete(`${BASE_PATH}/delete/${id}`);
  },
};
