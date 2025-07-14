import { Navbar } from '@/widgets';
import BellIcon from '@/assets/icons/Normal/Icon_Bell.svg';

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
          <BellIcon fill={'var(--color-main)'} width={28} height={28} />
        </div>
      </div>
      {children}
      <div
        className={'flex flex-col gap-y-2 px-5 pt-4.5 rounded-[20px] bg-white'}
      >
        {/*<KeywordFilterPreview*/}
        {/*  initialStatus={'unselect'}*/}
        {/*  onClick={() => handleOpen('keyword')}*/}
        {/*  keywords={keywords}*/}
        {/*  onDelete={({ label, type }) =>*/}
        {/*    handleDeleteKeyword(label, type, 'filter')*/}
        {/*  }*/}
        {/*/>*/}
      </div>
      <Navbar />
    </div>
  );
}
