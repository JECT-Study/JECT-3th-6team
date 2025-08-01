export interface ReservationResponse {
  waitingId: number;
  popupName: string;
  name: string;
  peopleCount: number;
  email: string;
  waitingNumber: number;
  registeredAt: string;
  location: {
    addressName: string;
    region1depthName: string;
    region2depthName: string;
    region3depthName: string;
    longitude: number;
    latitude: number;
  };
  popupImageUrl: string;
}
