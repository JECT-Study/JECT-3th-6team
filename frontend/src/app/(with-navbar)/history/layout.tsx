import React from 'react';

export default function HistoryLayout({
  children,
}: Readonly<{
  children: React.ReactNode;
}>) {
  return (
    <div className={'w-full h-full flex flex-col px-[20px]'}>
      <h1 className={'font-semibold text-2xl my-[26px]'}>내 방문</h1>
      {children}
    </div>
  );
}
