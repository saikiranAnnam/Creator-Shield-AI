import type {
  AlertItem,
  CreateUploadInput,
  UploadCreatedResponse,
  UploadDetail,
  ValidationResult,
} from "@/types/api";

const API_BASE = process.env.NEXT_PUBLIC_API_BASE_URL ?? "http://localhost:8080/api/v1";

async function request<T>(path: string, init?: RequestInit): Promise<T> {
  const res = await fetch(`${API_BASE}${path}`, {
    ...init,
    headers: {
      "Content-Type": "application/json",
      ...(init?.headers ?? {}),
    },
    cache: "no-store",
  });

  if (!res.ok) {
    const text = await res.text();
    throw new Error(text || `Request failed: ${res.status}`);
  }

  return (await res.json()) as T;
}

export async function createUpload(input: CreateUploadInput) {
  return request<UploadCreatedResponse>("/uploads", {
    method: "POST",
    body: JSON.stringify(input),
  });
}

export async function getUpload(uploadId: number) {
  return request<UploadDetail>(`/uploads/${uploadId}`);
}

export async function getValidation(uploadId: number) {
  return request<ValidationResult>(`/validations/${uploadId}`);
}

export async function getAlerts() {
  return request<AlertItem[]>("/alerts/suspicious");
}

export const mockData = {
  metrics: {
    uploadsToday: 147,
    suspiciousCount: 19,
    mediumRiskCount: 33,
    approvalRate: 81,
  },
  trend: [
    { day: "Mon", uploads: 91, suspicious: 11 },
    { day: "Tue", uploads: 110, suspicious: 12 },
    { day: "Wed", uploads: 125, suspicious: 15 },
    { day: "Thu", uploads: 98, suspicious: 9 },
    { day: "Fri", uploads: 147, suspicious: 19 },
  ],
};
