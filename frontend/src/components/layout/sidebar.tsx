"use client";

import { cn } from "@/lib/cn";
import { AlertTriangle, BarChart3, Music2, ShieldCheck } from "lucide-react";
import Link from "next/link";
import { usePathname } from "next/navigation";

const links = [
  { href: "/", label: "Dashboard", icon: BarChart3 },
  { href: "/uploads/new", label: "New Upload", icon: Music2 },
  { href: "/validations", label: "Validations", icon: ShieldCheck },
  { href: "/alerts", label: "Suspicious Alerts", icon: AlertTriangle },
];

export function Sidebar() {
  const pathname = usePathname();

  return (
    <aside className="glass-card sticky top-6 h-[calc(100vh-3rem)] rounded-3xl p-4">
      <div className="mb-8 px-3">
        <p className="text-xs uppercase tracking-[0.22em] text-[var(--muted)]">CreatorShield</p>
        <h2 className="mt-1 text-2xl font-bold">Trust Studio</h2>
      </div>
      <nav className="space-y-1">
        {links.map((link) => {
          const Icon = link.icon;
          const active = pathname === link.href;
          return (
            <Link
              key={link.href}
              href={link.href}
              className={cn(
                "flex items-center gap-3 rounded-xl px-3 py-2.5 text-sm font-medium",
                "transition hover:bg-white/5",
                active && "spotify-border bg-[rgba(30,215,96,0.14)] text-white",
              )}
            >
              <Icon size={17} />
              {link.label}
            </Link>
          );
        })}
      </nav>
      <div className="mt-8 rounded-xl bg-black/30 p-3">
        <p className="text-xs text-[var(--muted)]">Theme</p>
        <p className="text-sm font-semibold">Spotify-inspired dark trust console</p>
      </div>
    </aside>
  );
}
