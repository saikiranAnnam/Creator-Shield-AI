"use client";

import { Button } from "@/components/ui/button";
import { Card } from "@/components/ui/card";
import { getValidation } from "@/lib/api";
import type { ValidationResult } from "@/types/api";
import { useState } from "react";

export default function ValidationsPage() {
  const [uploadId, setUploadId] = useState("");
  const [data, setData] = useState<ValidationResult | null>(null);
  const [error, setError] = useState<string | null>(null);
  const [loading, setLoading] = useState(false);

  async function fetchValidation() {
    const id = Number(uploadId);
    if (!id) return;

    setLoading(true);
    setError(null);
    setData(null);
    try {
      const result = await getValidation(id);
      setData(result);
    } catch (err) {
      setError(err instanceof Error ? err.message : "Unable to fetch validation");
    } finally {
      setLoading(false);
    }
  }

  return (
    <div className="space-y-5">
      <Card>
        <h2 className="text-xl font-bold">Validation Lookup</h2>
        <p className="text-sm text-[var(--muted)]">Enter an upload ID to inspect trust scoring output.</p>
        <div className="mt-4 flex flex-col gap-3 sm:flex-row">
          <input
            value={uploadId}
            onChange={(e) => setUploadId(e.target.value)}
            placeholder="Upload ID (e.g. 42)"
            className="h-10 flex-1 rounded-xl border border-white/10 bg-black/30 px-3 text-sm outline-none ring-[var(--spotify-green)] focus:ring-2"
          />
          <Button onClick={fetchValidation} disabled={loading}>
            {loading ? "Loading..." : "Fetch"}
          </Button>
        </div>
      </Card>

      {data && (
        <Card>
          <div className="grid gap-4 sm:grid-cols-2 lg:grid-cols-3">
            <Info label="Trust Score" value={String(data.trustScore)} />
            <Info label="Risk Level" value={data.riskLevel} />
            <Info label="Decision" value={data.decision} />
            <Info label="Duplicate Score" value={data.duplicateScore?.toFixed(2) ?? "N/A"} />
            <Info
              label="Title Similarity"
              value={data.titleSimilarityScore?.toFixed(2) ?? "N/A"}
            />
            <Info label="Validated At" value={new Date(data.validatedAt).toLocaleString()} />
          </div>
          <div className="mt-4">
            <p className="text-sm text-[var(--muted)]">Reason Codes</p>
            <div className="mt-2 flex flex-wrap gap-2">
              {data.reasonCodes.map((code) => (
                <span key={code} className="status-pill bg-white/8">
                  {code}
                </span>
              ))}
            </div>
          </div>
        </Card>
      )}

      {error && <Card className="border border-red-500/40 text-red-300">{error}</Card>}
    </div>
  );
}

function Info({ label, value }: { label: string; value: string }) {
  return (
    <div className="rounded-xl bg-black/25 p-3">
      <p className="text-xs uppercase tracking-wide text-[var(--muted)]">{label}</p>
      <p className="mt-1 font-semibold">{value}</p>
    </div>
  );
}
