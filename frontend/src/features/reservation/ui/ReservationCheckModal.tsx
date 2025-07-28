import { StandardButton } from '@/shared/ui';
import { OnsiteReservationFormValue } from '@/features/reservation/hook/useForm';
import React from 'react';

interface ReservationCheckModalProps {
  data: OnsiteReservationFormValue;
  handleModalClose: () => void;
  handleSubmit: () => void;
}

interface ContentBlockProps {
  label: string;
  value: string | number;
}

const ContentBlock: React.FC<ContentBlockProps> = ({ label, value }) => (
  <div className={'w-full grid grid-cols-5'}>
    <span
      className={
        'text-gray60 font-semibold text-sm col-start-1 col-end-3 text-start'
      }
    >
      {label}
    </span>
    <span className={'col-start-3 col-end-6 text-sm font-medium'}>{value}</span>
  </div>
);

export default function ReservationCheckModal({
  data,
  handleModalClose,
  handleSubmit,
}: ReservationCheckModalProps) {
  const displayData = [
    { label: '예약자 명', value: data.name },
    { label: '예약자 수', value: data.headCount },
    { label: '예약자 이메일', value: data.email },
  ];

  return (
    <div
      className={
        'max-w-[350px] bg-white px-5 py-5 rounded-[20px] flex flex-col gap-[20px]'
      }
    >
      <h2
        className={'text-main font-semibold leading-6 text-[17px] text-center'}
      >
        확정 전 예약내용을 한번 더 확인해주세요!
      </h2>

      {/*예약내용*/}
      <div
        className={
          'w-full flex flex-col gap-y-[30px]  justify-center items-center bg-sub2 rounded-2xl py-[22px] px-3'
        }
      >
        {displayData.map(({ label, value }, index) => (
          <ContentBlock label={label} value={value} key={index} />
        ))}
      </div>

      {/*버튼*/}
      <div className={'w-full flex gap-x-2'}>
        <StandardButton
          onClick={handleModalClose}
          disabled={false}
          size={'fit'}
          className={'rounded-[10px]'}
        >
          수정
        </StandardButton>
        <StandardButton
          onClick={handleSubmit}
          disabled={false}
          size={'full'}
          color={'primary'}
        >
          웨이팅 확정
        </StandardButton>
      </div>
    </div>
  );
}
