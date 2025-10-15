import PageHeader from '@/shared/ui/header/PageHeader';
import IconNormalBell from '@/assets/icons/Color/Icon_NormalAlert.svg';

export default function Page() {
  return (
    <div>
      <PageHeader title={'공지사항'} />

      <div
        className={
          'w-full h-[calc(100vh-180px)] flex justify-center items-center  '
        }
      >
        <div className={'flex flex-col items-center gap-y-[16px] '}>
          <IconNormalBell />
          <p className={'text-base font-medium opacity-70'}>
            아직 공지가 없어요
          </p>
          <p className={'text-gray80 font-regular opacity-70'}>
            스팟잇 소식을 준비 중이에요. 조금만 기다려주세요!{' '}
          </p>
        </div>
      </div>
    </div>
  );
}
