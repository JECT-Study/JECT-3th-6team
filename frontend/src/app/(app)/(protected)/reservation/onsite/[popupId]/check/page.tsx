'use client';

import usePostReservation from '@/features/reservation/hook/usePostReservation';
import { useReservationFormContext } from '@/features/reservation/lib/FormProvider';
import { useParams, useRouter } from 'next/navigation';
import { BottomButtonContainer, StandardButton } from '@/shared/ui';
import React, { useEffect, useRef, useState } from 'react';
import { toast } from 'sonner';

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

const SubTitle = ({ title }: { title: string }) => (
  <div
    className={
      'text-main font-semibold text-base flex gap-x-[9px] items-center'
    }
  >
    <div className={'w-[11px] h-[11px] bg-main rounded-[2px]'}></div>
    <span className={'text-main'}> {title} </span>
  </div>
);

export default function Page() {
  const { formValue } = useReservationFormContext();
  const { mutate, isPending } = usePostReservation();
  const router = useRouter();
  const params = useParams();
  const popupId = Number(params.popupId);
  const [isScrollEnd, setIsScrollEnd] = useState(false);
  const termsRef = useRef<HTMLDivElement>(null);

  const handleSubmit = async () => {
    if (!isScrollEnd) {
      toast.info('예약 안내 사항을 모두 확인해주세요.');
      return;
    }
    if (isPending) return; // 요청 중이면 중복 호출 방지

    mutate({
      popupId,
      name: formValue.name,
      peopleCount: formValue.headCount,
      email: formValue.email,
    });
  };

  const displayData = [
    { label: '예약자 명', value: formValue.name },
    { label: '예약자 수', value: formValue.headCount },
    { label: '예약자 이메일', value: formValue.email },
  ];

  const handleScroll = (e: React.UIEvent<HTMLDivElement>) => {
    if (isScrollEnd) return;

    const target = e.currentTarget;
    const isBottom =
      Math.ceil(target.scrollTop + target.clientHeight) >= target.scrollHeight;

    if (isBottom && !isScrollEnd) {
      toast.success('예약 안내 사항을 모두 확인하였습니다!');
      setIsScrollEnd(true);
    }
  };

  useEffect(() => {
    const el = termsRef.current;
    if (!el) return;

    // 스크롤 가능한 경우만 false, 그렇지 않으면 바로 true
    if (el.scrollHeight <= el.clientHeight) {
      setIsScrollEnd(true);
    }
  }, []);

  return (
    <div>
      <div className={'flex flex-col px-[20px] h-[calc(100vh-122px)] pt-8'}>
        <div
          className={'w-full bg-white rounded-[20px] flex flex-col gap-[20px]'}
        >
          <h2
            className={
              'text-main font-semibold leading-6 text-[20px] text-center'
            }
          >
            예약내용을 한번 더 확인해주세요!
          </h2>

          {/*예약내용*/}
          <div
            className={
              'w-full flex flex-col gap-y-[30px]  justify-center items-center bg-sub2 rounded-2xl py-[22px] px-[20px]'
            }
          >
            {displayData.map(({ label, value }, index) => (
              <ContentBlock label={label} value={value} key={index} />
            ))}
          </div>
        </div>

        <hr className={'my-[32px] border-gray40'} />

        {/*약관*/}
        <div
          ref={termsRef}
          onScroll={handleScroll}
          className={'overflow-y-scroll flex-1 flex flex-col gap-[32px] '}
        >
          <h3 className={'font-medium text-base'}> 웨이팅 등록 안내사항 </h3>

          <div className={'w-full flex flex-col gap-[16px]'}>
            <SubTitle title={'등록'} />
            <ul className={'list-disc pl-5'}>
              <li className={'marker:text-gray80 text-gray80'}>
                모든 고객의 공정한 이용을 위해, 동일한 스토어는 하루 1회만 이용
                가능합니다.
              </li>
            </ul>
          </div>

          <div className={'w-full flex flex-col gap-[16px]'}>
            <SubTitle title={'노쇼'} />
            <ul className={'list-disc pl-5 flex flex-col gap-[20px]'}>
              <li className={'marker:text-gray80 text-gray80'}>
                웨이팅 등록 후 방문하지 않으면 ‘노쇼’로 처리될 수 있습니다.
              </li>
              <li className={'marker:text-gray80 text-gray80'}>
                노쇼는 매장과 다른 고객에게 피해를 줄 수 있으니, 꼭 방문 가능할
                때만 등록해주세요.{' '}
              </li>
              <li className={'marker:text-gray80 text-gray80'}>
                웨이팅 등록 취소는 현재 지원되지 않으니, 예약 전 신중히 등록
                부탁드립니다.{' '}
              </li>
              <div>
                <li className={'marker:text-gray80 text-gray80'}>
                  노쇼가 누적되면 스팟잇 이용에 제한이 생깁니다.
                </li>
                <ul
                  className={
                    'list-disc pl-2 flex flex-col pt-[10px] gap-y-[10px] '
                  }
                >
                  <li
                    className={
                      'marker:text-gray60 text-gray60 ml-[20px] text-sm'
                    }
                  >
                    누적 10회 이상 시, 3일간 모든 스토어 예약 제한
                  </li>{' '}
                  <li
                    className={
                      'marker:text-gray60 text-gray60 ml-[20px] text-sm'
                    }
                  >
                    제한 기간 이후 예약 가능하며, 누적 노쇼 횟수 초기화{' '}
                  </li>
                </ul>
              </div>
            </ul>
          </div>

          <div className={'w-full flex flex-col gap-[16px]'}>
            <SubTitle title={'재예약'} />
            <ul className={'list-disc pl-5 flex flex-col gap-[20px]'}>
              <li className={'marker:text-gray80 text-gray80'}>
                불가피한 사정으로 방문하지 못했을 경우, 동일한 스토어에 대해
                재예약 1회가 허용됩니다.{' '}
              </li>
              <li className={'marker:text-gray80 text-gray80'}>
                재예약 1회까지 모두 소진한 경우, 해당 스토어는 당일 재예약이
                불가하니 신중히 등록해주세요.
              </li>
            </ul>
          </div>
        </div>
      </div>
      {/*// 하단 버튼*/}
      <BottomButtonContainer hasShadow={false}>
        <StandardButton
          onClick={() => router.push(`/reservation/onsite/${popupId}`)}
          disabled={false}
          size={'fit'}
          color={'white'}
          hasShadow={false}
          className={'rounded-[10px] w-[100px]'}
        >
          수정
        </StandardButton>

        <StandardButton
          onClick={handleSubmit}
          disabled={isPending}
          size={'full'}
          color={'primary'}
          hasShadow={false}
        >
          확인
        </StandardButton>
      </BottomButtonContainer>
    </div>
  );
}
