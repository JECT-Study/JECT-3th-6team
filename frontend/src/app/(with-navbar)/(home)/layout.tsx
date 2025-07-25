import NotificationBell from '@/features/notification/ui/NotificationBell';

export default function HomeLayout({
  children,
}: Readonly<{
  children: React.ReactNode;
}>) {
  return (
    <div className={'w-full bg-main pt-7'}>
      <div className={'flex flex-col px-5 gap-y-4 mb-4'}>
        {/*로고*/}
        <div className={'flex items-center justify-between'}>
          <span className={'font-gangwon text-[17px] text-white font-regular'}>
            Spot it
          </span>
          <NotificationBell />
        </div>
      </div>
      {children}
    </div>
  );
}
