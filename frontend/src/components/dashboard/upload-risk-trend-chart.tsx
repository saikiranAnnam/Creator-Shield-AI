"use client";

import { mockData } from "@/lib/api";
import {
  Bar,
  BarChart,
  CartesianGrid,
  ResponsiveContainer,
  Tooltip,
  XAxis,
  YAxis,
} from "recharts";

export function UploadRiskTrendChart() {
  return (
    <div className="min-h-0 min-w-0 flex-1">
      <ResponsiveContainer width="100%" height="100%">
        <BarChart data={mockData.trend}>
          <CartesianGrid strokeDasharray="3 3" stroke="rgba(255,255,255,0.09)" />
          <XAxis dataKey="day" stroke="#9aa4af" />
          <YAxis stroke="#9aa4af" />
          <Tooltip
            contentStyle={{
              background: "#0f1319",
              border: "1px solid rgba(255,255,255,0.12)",
              borderRadius: 10,
            }}
          />
          <Bar dataKey="uploads" fill="#1ed760" radius={[6, 6, 0, 0]} />
          <Bar dataKey="suspicious" fill="#ef4444" radius={[6, 6, 0, 0]} />
        </BarChart>
      </ResponsiveContainer>
    </div>
  );
}
