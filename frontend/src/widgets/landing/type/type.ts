export type DesktopStepType =
  | 'START'
  | 'GUIDE_1'
  | 'GUIDE_2'
  | 'GUIDE_3'
  | 'GUIDE_4'
  | 'END';

export type MobileStepType = 'START' | 'GUIDE_1' | 'GUIDE_2' | 'GUIDE_3';

export type DesktopGuideStepType = Exclude<DesktopStepType, 'START' | 'END'>;
export type MobileGuideStepType = Exclude<MobileStepType, 'START'>;
