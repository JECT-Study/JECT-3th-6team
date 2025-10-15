'use client';

import Link from 'next/link';
import { useState } from 'react';
import IconBracketRight from '@/assets/icons/Normal/Icon_Bracket_Right.svg';
import { MenuType } from '@/entities/settings/constants/menu';

import WithdrawalConfirmModal from '@/features/auth/ui/WithdrawalConfirmModal';
import { useWithdrawalConfirm } from '@/features/auth/hook/useWithdrawalConfirm';

export default function MenuList({ menu }: { menu: MenuType[] }) {
  const [isWithdrawalModalOpen, setIsWithdrawalModalOpen] = useState(false);
  const withdrawalConfirm = useWithdrawalConfirm();

  const handleWithdrawalConfirmClick = (menu: MenuType) => {
    if (menu.modalType === 'withdraw') {
      setIsWithdrawalModalOpen(true);
    }
  };

  const handleWithdrawalConfirm = async () => {
    withdrawalConfirm();
    setIsWithdrawalModalOpen(false);
  };

  // 일반 메뉴와 탈퇴하기 메뉴를 분리
  const regularMenus = menu.filter(menu => !menu.modalType);
  const withdrawalMenu = menu.find(menu => menu.modalType === 'withdraw');

  return (
    <>
      <div className={'mt-[35px] flex flex-col flex-1'}>
        {/* 일반 메뉴들 */}
        <div>
          {regularMenus.map((menu, index) => (
            <Link
              href={menu.link!}
              key={index}
              className={
                'flex justify-between py-4 border-b-1 border-gray20 group'
              }
            >
              <span
                className={
                  'text-black font-medium text-base group-hover:text-main transition-all duration-200 '
                }
              >
                {menu.title}
              </span>
              {menu.title === '고객센터' ? (
                <span
                  className={
                    'text-gray60 text-sm underline flex justify-center items-center '
                  }
                >
                  스팟잇 카카오톡 채널
                </span>
              ) : (
                <IconBracketRight
                  width={20}
                  height={20}
                  fill={'var(--color-gray80)'}
                />
              )}
            </Link>
          ))}
        </div>

        {/* 탈퇴하기 메뉴 - 맨 하단에 고정 */}
        {withdrawalMenu && (
          <div className="mt-auto">
            <button
              onClick={() => handleWithdrawalConfirmClick(withdrawalMenu)}
              className={
                'flex justify-between py-4 border-b-1 border-gray20 group w-full text-left cursor-pointer'
              }
            >
              <span
                className={
                  'text-gray60 font-medium text-base group-hover:text-main transition-all duration-200 '
                }
              >
                {withdrawalMenu.title}
              </span>
              <IconBracketRight
                width={20}
                height={20}
                fill={'var(--color-gray60)'}
              />
            </button>
          </div>
        )}
      </div>
      <WithdrawalConfirmModal
        isOpen={isWithdrawalModalOpen}
        onClose={() => setIsWithdrawalModalOpen(false)}
        onConfirm={handleWithdrawalConfirm}
      />
    </>
  );
}
