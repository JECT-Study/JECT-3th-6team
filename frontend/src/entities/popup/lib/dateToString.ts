export default function dateToString(date: Date) {
  //"2025-10-09T04:18:45.484Z"
  const month = date!.getMonth() + 1;
  const day = date?.getDate();
  return `${month}월 ${day}일`;
}
