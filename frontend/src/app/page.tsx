"use client";

import { Card } from "@/components/ui/card";
import { mockData } from "@/lib/api";
import { AlertTriangle, CheckCircle2, UploadCloud } from "lucide-react";
import dynamic from "next/dynamic";

const UploadRiskTrendChartClient = dynamic(
  () =>
    import("@/components/dashboard/upload-risk-trend-chart").then((m) => m.UploadRiskTrendChart),
  {
    ssr: false,
    loading: () => <div className="min-h-0 min-w-0 flex-1 animate-pulse rounded-xl bg-white/5" />,
  },
);

export default function Home() {
  return (
    <div className="space-y-6">
      <section className="grid gap-4 sm:grid-cols-2 xl:grid-cols-4">
        <MetricCard
          title="Uploads Today"
          value={mockData.metrics.uploadsToday.toString()}
          icon={<UploadCloud size={18} />}
          hint="ingestion active"
        />
        <MetricCard
          title="Approval Rate"
          value={`${mockData.metrics.approvalRate}%`}
          icon={<CheckCircle2 size={18} />}
          hint="last 24h"
        />
        <MetricCard
          title="Suspicious"
          value={mockData.metrics.suspiciousCount.toString()}
          icon={<AlertTriangle size={18} />}
          hint="high risk alerts"
        />
        <MetricCard
          title="Medium Risk"
          value={mockData.metrics.mediumRiskCount.toString()}
          icon={<AlertTriangle size={18} />}
          hint="manual review queue"
        />
      </section>

      <Card className="flex h-[380px] flex-col">
        <h3 className="mb-1 text-lg font-semibold">Upload Risk Trend</h3>
        <p className="mb-4 text-sm text-[var(--muted)]">
          Comparison of total uploads and suspicious detections through the week.
        </p>
        <UploadRiskTrendChartClient />
      </Card>
    </div>
  );
}

function MetricCard({
  title,
  value,
  icon,
  hint,
}: {
  title: string;
  value: string;
  icon: React.ReactNode;
  hint: string;
}) {
  return (
    <Card>
      <div className="mb-4 inline-flex rounded-xl bg-white/6 p-2">{icon}</div>
      <p className="text-sm text-[var(--muted)]">{title}</p>
      <p className="mt-2 text-3xl font-bold">{value}</p>
      <p className="mt-2 text-xs uppercase tracking-[0.14em] text-[var(--spotify-green)]">{hint}</p>
    </Card>
  );
}
