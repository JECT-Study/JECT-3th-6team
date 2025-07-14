import OptionTitle from '@/features/filtering/ui/OptionTitle';

export default function Page() {
  return (
    <>
      <h1> 캘린더 컴포넌트 </h1>
      <OptionTitle openType={'date'} />
      <OptionTitle openType={'location'} />
      <OptionTitle openType={'keyword'} />
    </>
  );
}
