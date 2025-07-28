export default function BottomButtonContainer({
  children,
  hasShadow = true,
}: {
  children: React.ReactNode;
  hasShadow?: boolean;
}) {
  const shadowStyle = hasShadow ? 'shadow-container' : '';
  return (
    <div
      className={`bg-white w-full min-w-[320px] max-w-[430px]  h-[122px] fixed bottom-0 px-5 pt-4  rounded-t-[20px] z-100 ${shadowStyle}`}
    >
      <div className={'w-full  flex justify-center gap-x-2'}>{children}</div>
    </div>
  );
}
