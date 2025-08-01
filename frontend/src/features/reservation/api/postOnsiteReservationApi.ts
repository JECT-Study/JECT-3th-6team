import { APIBuilder, logError } from '@/shared/lib';
import { ApiError } from '@/shared/type/api';
import { ReservationResponse } from '@/features/reservation/type/response';

export interface OnsiteReservationRequest {
  name: string;
  peopleCount: number;
  email: string;
  popupId: number;
}

export default async function postOnsiteReservationApi(
  request: OnsiteReservationRequest
): Promise<ReservationResponse> {
  const { name, peopleCount, email, popupId } = request;
  try {
    const response = await (
      await APIBuilder.post(`/popups/${popupId}/waitings`, {
        name,
        peopleCount,
        email,
      })
        .timeout(5000)
        .withCredentials(true)
        .auth()
        .buildAsync()
    ).call<ReservationResponse>();

    return response.data;
  } catch (error) {
    if (error instanceof ApiError) {
      logError(error, '현장 대기 예약 과정');
    }
    throw error;
  }
}
