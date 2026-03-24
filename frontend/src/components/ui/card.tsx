import { cn } from "@/lib/cn";
import type { ReactNode } from "react";

interface CardProps {
  className?: string;
  children: ReactNode;
}

export function Card({ className, children }: CardProps) {
  return <div className={cn("glass-card rounded-2xl p-5 shadow-lg", className)}>{children}</div>;
}
