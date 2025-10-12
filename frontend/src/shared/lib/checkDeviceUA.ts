// 기기 타입 확인
// type : MOBILE, TABLET, DESKTOP

export type DeviceType = 'MOBILE' | 'TABLET' | 'DESKTOP';
export function checkDeviceUA(ua: string = ''): DeviceType {
  const lowered = ua.toLowerCase();
  const isMobile = /(iphone|ipod|android.*mobile|windows phone)/i.test(lowered);
  const isTablet = /(ipad|android(?!.*mobile)|tablet)/i.test(lowered);

  switch (true) {
    case isMobile:
      return 'MOBILE';
    case isTablet:
      return 'TABLET';
    case !isMobile && !isTablet:
      return 'DESKTOP';
    default:
      return 'MOBILE';
  }
}
