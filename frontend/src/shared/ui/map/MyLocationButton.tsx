import IconCurrentLocation from '@/assets/icons/Normal/Icon_Current_Location.svg';

interface MyLocationButtonProps {
  onMoveToCurrentLocation: () => void;
}

export default function MyLocationButton({
  onMoveToCurrentLocation,
}: MyLocationButtonProps) {
  const handleClickMyLocation = () => {
    onMoveToCurrentLocation();
  };

  return (
    <button
      onClick={handleClickMyLocation}
      className="absolute bottom-40 right-5 bg-white rounded-full z-10 flex justify-center items-center w-14 h-14"
    >
      <IconCurrentLocation fill={'var(--color-main)'} className="scale-200 " />
    </button>
  );
}
