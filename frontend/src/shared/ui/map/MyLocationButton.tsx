import IconSearchCurrentLocation from '@/assets/icons/Color/Icon_SearchCurrentLocation.svg';

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
      className="absolute bottom-15 right-5  z-10 flex justify-center items-center w-14 h-14"
    >
      <IconSearchCurrentLocation
        fill={'var(--color-main)'}
        className="scale-200 "
      />
    </button>
  );
}
