import { APIBuilder, logError } from '@/shared/lib';
import { ApiError } from '@/shared/type/api';
import { OnsiteReservationResponse } from '@/features/reservation/type/OnsiteReservationResponse';

export interface OnsiteReservationRequest {
  name: string;
  peopleCount: number;
  email: string;
  popupId: number;
}

export default async function postOnsiteReservationApi(
  request: OnsiteReservationRequest
): Promise<OnsiteReservationResponse> {
  const { name, peopleCount, email, popupId } = request;
  try {
    const response = await (
      await APIBuilder.post(`/popups/${popupId}/waitings`, {
        name,
        peopleCount,
        contactEmail: email,
      })
        .timeout(5000)
        .withCredentials(true)
        .auth()
        .buildAsync()
    ).call<OnsiteReservationResponse>();

    return response.data;
  } catch (error) {
    if (error instanceof ApiError) {
      logError(error, '현장 대기 예약 과정');
      console.error(error.code);
    }
    throw error;
  }
}
