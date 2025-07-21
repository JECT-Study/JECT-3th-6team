import Image from 'next/image';
import KakaoLoginImage from '/public/images/kakao-login-large.png';

export default function KakaoLoginButton({
  onClick,
  disabled,
}: {
  onClick: () => void;
  disabled: boolean;
}) {
  return (
    <button onClick={onClick} disabled={disabled} className={'cursor-pointer'}>
      <Image
        src={KakaoLoginImage}
        alt="카카오 로그인 버튼"
        width={300}
        height={50}
      />
    </button>
  );
}
