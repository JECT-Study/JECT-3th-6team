// src/lib/ua.ts
export function isDesktopUA(ua: string = ''): boolean {
  const lowered = ua.toLowerCase();
  const isMobile = /(iphone|ipod|android.*mobile|windows phone)/i.test(lowered);
  const isTablet = /(ipad|android(?!.*mobile)|tablet)/i.test(lowered);
  return !(isMobile || isTablet);
}
