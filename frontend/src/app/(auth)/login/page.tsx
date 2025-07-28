import PageHeader from '@/shared/ui/header/PageHeader';
import { Login } from '@/features/auth';

export default function LoginPage() {
  return (
    <div>
      <PageHeader title={'로그인'} />
      <Login />
    </div>
  );
}
