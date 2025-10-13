import { useMemo } from 'react';

import { PopupDetailPopupDetail } from '@/entities/popup/detail/types/type';

export function useOperatingHours(
  popupDetail: PopupDetailPopupDetail
): boolean {
  return useMemo(() => {
    if (!popupDetail || !Array.isArray(popupDetail.dayOfWeeks)) {
      return false;
    }

    const dayIndexToName: Record<number, string> = {
      0: 'SUNDAY',
      1: 'MONDAY',
      2: 'TUESDAY',
      3: 'WEDNESDAY',
      4: 'THURSDAY',
      5: 'FRIDAY',
      6: 'SATURDAY',
    };

    const now = new Date();
    const todayName = dayIndexToName[now.getDay()];
    const todaySchedule = popupDetail.dayOfWeeks.find(
      d => d.dayOfWeek === todayName
    );

    if (!todaySchedule) return false;

    const [startStrRaw, endStrRaw] = todaySchedule.value.split('~');
    if (!startStrRaw || !endStrRaw) return false;
    const startStr = startStrRaw.trim();
    const endStr = endStrRaw.trim();

    const parseTimeToMinutes = (time: string) => {
      const [h, m] = time.split(':').map(v => parseInt(v, 10));
      if (Number.isNaN(h) || Number.isNaN(m)) return null;
      return h * 60 + m;
    };

    const startMin = parseTimeToMinutes(startStr);
    const endMin = parseTimeToMinutes(endStr);
    if (startMin == null || endMin == null) return false;

    const nowMin = now.getHours() * 60 + now.getMinutes();
    return nowMin >= startMin && nowMin <= endMin;
  }, [popupDetail]);
}
