import { cn } from "@/lib/cn";
import type { ButtonHTMLAttributes } from "react";

export function Button({
  className,
  ...props
}: ButtonHTMLAttributes<HTMLButtonElement>) {
  return (
    <button
      className={cn(
        "inline-flex h-10 items-center justify-center rounded-xl px-4 text-sm font-semibold transition",
        "bg-[var(--spotify-green)] text-black hover:bg-[var(--spotify-green-dim)]",
        "disabled:cursor-not-allowed disabled:opacity-60",
        className,
      )}
      {...props}
    />
  );
}
