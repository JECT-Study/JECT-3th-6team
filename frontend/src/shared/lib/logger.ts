type ConsoleArgs = Parameters<typeof console.log>;
type levelType = 'debug' | 'warn' | 'error' | 'info';

/**
  [사용방법]
  logger.debug('개발시 디버깅하고 싶은 것들..');
  logger.warn('경고 문구');
 */

const logger = {
  debug: (...args: ConsoleArgs) => log('debug', ...args),
  warn: (...args: ConsoleArgs) => log('warn', ...args),
  error: (...args: ConsoleArgs) => log('error', ...args),
  info: (...args: ConsoleArgs) => log('info', ...args),
};

const log = (level: levelType, ...args: ConsoleArgs) => {
  if (!shouldLog(level)) return;
  const method = level === 'debug' ? 'log' : level;
  console[method](...args);
};

// NEXT_PUBLIC_LOG_LEVEL = silent, prod, dev
// NEXT_PUBLIC_ENV = PRODUCTION, DEVELOP
const LOG_LEVEL = process.env.NEXT_PUBLIC_LOG_LEVEL || 'dev';
const isProduction = process.env.NEXT_PUBLIC_ENV === 'PRODUCTION';
const shouldLog = (level: string) => {
  if (LOG_LEVEL === 'silent') {
    // 운영환경 완전 무음
    return !isProduction;
  }

  if (LOG_LEVEL === 'prod') {
    // 운영환경은 warn/error만 출력
    return !(isProduction && (level === 'debug' || level === 'info'));
  }

  // dev 모드 → 항상 로그 출력
  return true;
};
export default logger;
