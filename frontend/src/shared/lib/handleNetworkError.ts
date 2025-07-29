import { toast } from 'sonner';

const isNetworkError = (error: Error | TypeError): error is TypeError => {
  return (
    error instanceof TypeError && error.message.includes('Failed to fetch')
  );
};

export default function handleNetworkError(error: Error | TypeError) {
  if (isNetworkError(error)) {
    toast.error('서버에 연결할 수 없습니다. 네트워크 상태를 확인해주세요.');
    return;
  }
}
