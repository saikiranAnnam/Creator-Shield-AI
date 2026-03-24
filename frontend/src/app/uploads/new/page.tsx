"use client";

import { Button } from "@/components/ui/button";
import { Card } from "@/components/ui/card";
import { createUpload } from "@/lib/api";
import type { CreateUploadInput, UploadCreatedResponse } from "@/types/api";
import { useState } from "react";

const initialState: CreateUploadInput = {
  creatorId: "",
  creatorName: "",
  songTitle: "",
  artistName: "",
  albumName: "",
  labelName: "",
  releaseDate: "",
  genre: "",
  language: "",
  country: "",
  lyrics: "",
  optionalExternalClaims: "",
};

export default function NewUploadPage() {
  const [form, setForm] = useState(initialState);
  const [submitting, setSubmitting] = useState(false);
  const [result, setResult] = useState<UploadCreatedResponse | null>(null);
  const [error, setError] = useState<string | null>(null);

  async function onSubmit(e: React.FormEvent<HTMLFormElement>) {
    e.preventDefault();
    setSubmitting(true);
    setError(null);
    setResult(null);

    try {
      const payload = Object.fromEntries(
        Object.entries(form).filter(([, value]) => value !== ""),
      ) as CreateUploadInput;
      const res = await createUpload(payload);
      setResult(res);
      setForm(initialState);
    } catch (err) {
      setError(err instanceof Error ? err.message : "Failed to create upload");
    } finally {
      setSubmitting(false);
    }
  }

  return (
    <Card className="max-w-4xl">
      <h2 className="text-xl font-bold">Create Upload</h2>
      <p className="mt-1 text-sm text-[var(--muted)]">
        Submit creator metadata for asynchronous trust validation.
      </p>
      <form className="mt-5 grid gap-4 md:grid-cols-2" onSubmit={onSubmit}>
        <Field
          label="Creator ID"
          required
          value={form.creatorId}
          onChange={(v) => setForm((p) => ({ ...p, creatorId: v }))}
        />
        <Field
          label="Creator Name"
          value={form.creatorName ?? ""}
          onChange={(v) => setForm((p) => ({ ...p, creatorName: v }))}
        />
        <Field
          label="Song Title"
          required
          value={form.songTitle}
          onChange={(v) => setForm((p) => ({ ...p, songTitle: v }))}
        />
        <Field
          label="Artist Name"
          required
          value={form.artistName}
          onChange={(v) => setForm((p) => ({ ...p, artistName: v }))}
        />
        <Field label="Album Name" value={form.albumName ?? ""} onChange={(v) => setForm((p) => ({ ...p, albumName: v }))} />
        <Field label="Label Name" value={form.labelName ?? ""} onChange={(v) => setForm((p) => ({ ...p, labelName: v }))} />
        <Field label="Release Date" type="date" value={form.releaseDate ?? ""} onChange={(v) => setForm((p) => ({ ...p, releaseDate: v }))} />
        <Field label="Genre" value={form.genre ?? ""} onChange={(v) => setForm((p) => ({ ...p, genre: v }))} />
        <Field label="Language" value={form.language ?? ""} onChange={(v) => setForm((p) => ({ ...p, language: v }))} />
        <Field label="Country" value={form.country ?? ""} onChange={(v) => setForm((p) => ({ ...p, country: v }))} />
        <div className="md:col-span-2">
          <TextArea
            label="Lyrics"
            value={form.lyrics ?? ""}
            onChange={(v) => setForm((p) => ({ ...p, lyrics: v }))}
          />
        </div>
        <div className="md:col-span-2">
          <TextArea
            label="External Claims"
            value={form.optionalExternalClaims ?? ""}
            onChange={(v) => setForm((p) => ({ ...p, optionalExternalClaims: v }))}
          />
        </div>
        <div className="md:col-span-2">
          <Button type="submit" disabled={submitting}>
            {submitting ? "Submitting..." : "Create Upload"}
          </Button>
        </div>
      </form>

      {result && (
        <div className="mt-4 rounded-xl border border-emerald-400/30 bg-emerald-500/10 p-3 text-sm">
          Upload #{result.uploadId} created with status <strong>{result.status}</strong>.
        </div>
      )}
      {error && (
        <div className="mt-4 rounded-xl border border-red-400/30 bg-red-500/10 p-3 text-sm">
          {error}
        </div>
      )}
    </Card>
  );
}

function Field({
  label,
  value,
  onChange,
  type = "text",
  required = false,
}: {
  label: string;
  value: string;
  onChange: (value: string) => void;
  type?: string;
  required?: boolean;
}) {
  return (
    <label className="block">
      <span className="mb-1 block text-sm text-[var(--muted)]">
        {label}
        {required ? " *" : ""}
      </span>
      <input
        type={type}
        value={value}
        required={required}
        onChange={(e) => onChange(e.target.value)}
        className="w-full rounded-xl border border-white/10 bg-black/30 px-3 py-2 text-sm outline-none ring-[var(--spotify-green)] focus:ring-2"
      />
    </label>
  );
}

function TextArea({
  label,
  value,
  onChange,
}: {
  label: string;
  value: string;
  onChange: (value: string) => void;
}) {
  return (
    <label className="block">
      <span className="mb-1 block text-sm text-[var(--muted)]">{label}</span>
      <textarea
        value={value}
        onChange={(e) => onChange(e.target.value)}
        className="h-24 w-full rounded-xl border border-white/10 bg-black/30 px-3 py-2 text-sm outline-none ring-[var(--spotify-green)] focus:ring-2"
      />
    </label>
  );
}
