export function Topbar() {
  return (
    <header className="mb-6 flex items-center justify-between">
      <div>
        <p className="text-sm text-[var(--muted)]">AI Content Trust & Verification</p>
        <h1 className="text-2xl font-bold">CreatorShield Control Center</h1>
      </div>
      <div className="rounded-xl border border-white/10 bg-white/5 px-3 py-2 text-sm">
        Backend: <span className="font-semibold text-[var(--spotify-green)]">Spring + Kafka</span>
      </div>
    </header>
  );
}
