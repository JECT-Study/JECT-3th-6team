import Link from 'next/link';
import IconBracketRight from '@/assets/icons/Normal/Icon_Bracket_Right.svg';
import { MenuType } from '@/entities/settings/constants/menu';

export default function MenuList({ menu }: { menu: MenuType[] }) {
  return (
    <div className={'mt-[35px]'}>
      {menu.map((menu, index) => {
        return (
          <Link
            href={menu.link}
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
            <IconBracketRight
              width={20}
              height={20}
              fill={'var(--color-gray80)'}
            />
          </Link>
        );
      })}
    </div>
  );
}
