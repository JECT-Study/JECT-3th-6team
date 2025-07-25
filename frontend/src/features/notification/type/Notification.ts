export type RelatedResourceType = {
  type: 'POPUP' | 'WAITING' | 'MY_VISIT_HISTORY';
  id: number | null;
};

export type NotificationCodeType =
  | 'WAITING_CONFIRMED' // 웨이팅 완료, 대기번호 확인 -> 대기순번 페이지
  | 'ENTER_3TEAMS_BEFORE' // 3팀 남음, 입장 대기 -> 내 방문 페이지
  | 'ENTER_NOW' // 입장 -> 웨이팅 상세 페이지
  | 'ENTER_TIME_OVER' // 입장 시간이 초과 -> 웨이팅 상세
  | 'REVIEW_REQUEST'; // MVP 제외

export default interface NotificationType {
  notificationId: number;
  notificationCode: NotificationCodeType;
  message: string;
  createdAt: string;
  isRead: boolean;
  relatedResource: RelatedResourceType;
  // TODO : 백엔드와 논의 후 필드명 수정
  notificationTitle: string;
}
