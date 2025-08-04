import IconNormalBell from '@/assets/icons/Color/Icon_NormalAlert.svg';

export default function EmptyNotificationView() {
  return (
    <div
      className={
        'w-full h-[calc(100vh-165px)] flex justify-center items-center  '
      }
    >
      <div className={'flex flex-col items-center gap-y-[16px] '}>
        <IconNormalBell />
        <p className={'text-base font-medium'}>아직 알림이 없어요</p>
        <p className={'text-gray80 font-regular'}>
          새로운 팝업스토어를 예약해보세요!
        </p>
      </div>
    </div>
  );
}
