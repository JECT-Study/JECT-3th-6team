import { Toaster } from '@/shared/ui';

export default function AppShellLayout({
  children,
}: {
  children: React.ReactNode;
}) {
  return (
    <div className="min-h-screen bg-gray40">
      <div className="w-full min-w-[320px] max-w-[430px] mx-auto bg-white min-h-screen">
        <Toaster position="top-center" richColors />
        <main className="flex-1">{children}</main>
      </div>
    </div>
  );
}
