import NavbarPortal from '@/widgets/navbar/NavbarPortal';

export default function Layout({
  children,
}: Readonly<{
  children: React.ReactNode;
}>) {
  return (
    <div className={'w-full'}>
      {children}
      <NavbarPortal />
    </div>
  );
}
