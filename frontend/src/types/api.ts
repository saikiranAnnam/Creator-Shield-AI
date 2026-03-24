export type UploadStatus =
  | "PENDING_VALIDATION"
  | "VALIDATED_APPROVED"
  | "VALIDATED_REVIEW"
  | "VALIDATED_SUSPICIOUS";

export interface UploadDetail {
  uploadId: number;
  creatorId: string;
  songTitle: string;
  artistName: string;
  albumName?: string;
  labelName?: string;
  releaseDate?: string;
  genre?: string;
  language?: string;
  uploadStatus: UploadStatus;
  createdAt: string;
}

export interface UploadCreatedResponse {
  uploadId: number;
  status: UploadStatus;
  createdAt: string;
}

export interface ValidationResult {
  uploadId: number;
  duplicateScore: number;
  titleSimilarityScore: number;
  trustScore: number;
  riskLevel: "LOW" | "MEDIUM" | "HIGH";
  decision: "AUTO_APPROVED" | "REVIEW_REQUIRED" | "SUSPICIOUS_ALERT";
  reasonCodes: string[];
  validatedAt: string;
}

export interface AlertItem {
  alertId: number;
  uploadId: number;
  creatorId: string;
  alertType: string;
  alertMessage: string;
  status: "OPEN" | "ACKNOWLEDGED" | "RESOLVED";
  createdAt: string;
}

export interface CreateUploadInput {
  creatorId: string;
  creatorName?: string;
  songTitle: string;
  artistName: string;
  albumName?: string;
  labelName?: string;
  releaseDate?: string;
  genre?: string;
  language?: string;
  lyrics?: string;
  optionalExternalClaims?: string;
  country?: string;
}
