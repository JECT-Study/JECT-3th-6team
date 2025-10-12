import { DeviceType } from '@/shared/lib/checkDeviceUA';
import DesktopLanding from '@/widgets/landing/ui/DesktopLanding';
import MobileLanding from '@/widgets/landing/ui/MobileLanding';

export default async function LandingPage({
  searchParams,
}: {
  searchParams?: Promise<{ device: DeviceType }>;
}) {
  const params = await searchParams;
  const device = params?.device;

  if (device === 'DESKTOP') return <DesktopLanding />;
  if (device === 'MOBILE') return <MobileLanding />;
}
