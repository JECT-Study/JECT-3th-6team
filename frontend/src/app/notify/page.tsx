import NotificationCardList from '@/features/notification/ui/NotificationCardList';
import PageHeader from '@/shared/ui/header/PageHeader';

export default function NotifyPage() {
  return (
    <div>
      <PageHeader title={'알림 내역'} />
      {/*<EmptyNotificationView />*/}
      <NotificationCardList />
    </div>
  );
}
