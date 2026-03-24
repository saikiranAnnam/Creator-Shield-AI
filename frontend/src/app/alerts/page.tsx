"use client";

import { Card } from "@/components/ui/card";
import { getAlerts } from "@/lib/api";
import type { AlertItem } from "@/types/api";
import { useEffect, useState } from "react";

const fallbackAlerts: AlertItem[] = [
  {
    alertId: 901,
    uploadId: 4442,
    creatorId: "creator_neo_18",
    alertType: "HIGH_TRUST_RISK",
    alertMessage: "EXACT_DUPLICATE_FOUND, HIGH_TITLE_SIMILARITY",
    status: "OPEN",
    createdAt: new Date().toISOString(),
  },
];

export default function AlertsPage() {
  const [alerts, setAlerts] = useState<AlertItem[]>([]);
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    getAlerts()
      .then(setAlerts)
      .catch((err) => {
        setError(err instanceof Error ? err.message : "Using fallback data");
        setAlerts(fallbackAlerts);
      });
  }, []);

  return (
    <div className="space-y-5">
      <Card>
        <h2 className="text-xl font-bold">Suspicious Alerts</h2>
        <p className="text-sm text-[var(--muted)]">
          High-risk uploads surfaced for human review and moderation workflows.
        </p>
      </Card>

      {error && <p className="text-xs text-amber-300">{error}</p>}

      <div className="grid gap-4">
        {alerts.map((alert) => (
          <Card key={alert.alertId}>
            <div className="flex items-start justify-between gap-3">
              <div>
                <p className="text-sm text-[var(--muted)]">
                  Alert #{alert.alertId} • Upload #{alert.uploadId}
                </p>
                <h3 className="mt-1 text-lg font-semibold">{alert.alertType}</h3>
              </div>
              <Status status={alert.status} />
            </div>
            <p className="mt-3 text-sm">{alert.alertMessage || "No reason message available."}</p>
            <div className="mt-3 text-xs text-[var(--muted)]">
              Creator: {alert.creatorId} • {new Date(alert.createdAt).toLocaleString()}
            </div>
          </Card>
        ))}
      </div>
    </div>
  );
}

function Status({ status }: { status: AlertItem["status"] }) {
  const classes =
    status === "OPEN"
      ? "bg-red-500/20 text-red-300"
      : status === "ACKNOWLEDGED"
        ? "bg-amber-500/20 text-amber-300"
        : "bg-emerald-500/20 text-emerald-300";
  return <span className={`status-pill ${classes}`}>{status}</span>;
}
