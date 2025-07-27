import { HTTPHeaders, HTTPMethod, HTTPParams } from '@/shared/type/api';
import API from '@/shared/lib/API';
import { cookies } from 'next/headers';

const BASE_URL = process.env.NEXT_PUBLIC_API_URL;

// API 빌더 클래스 정의
export default class APIBuilder {
  private _instance: API;

  constructor(method: HTTPMethod, url: string, data?: unknown) {
    this._instance = new API(method, url);
    this._instance.baseURL = BASE_URL;
    this._instance.data = data;
    this._instance.headers = {
      'Content-Type': 'application/json; charset=utf-8',
    };
    this._instance.timeout = 5000;
    this._instance.withCredentials = true;
    // this._instance.cache = 'no-store'; // 캐시 기본옵션 - 캐싱X
    // this._instance.next = {};
  }

  // HTTP 메서드에 따라 APIBuilder 객체를 생성하는 메서드들
  static get = (url: string) => new APIBuilder('GET', url);
  static put = (url: string, data: unknown) => new APIBuilder('PUT', url, data);
  static post = (url: string, data: unknown) =>
    new APIBuilder('POST', url, data);
  static delete = (url: string) => new APIBuilder('DELETE', url);
  static patch = (url: string, data: unknown) =>
    new APIBuilder('PATCH', url, data);

  baseURL(value: string): APIBuilder {
    this._instance.baseURL = value;
    return this;
  }

  headers(value: HTTPHeaders): APIBuilder {
    this._instance.headers = value;
    return this;
  }

  timeout(value: number): APIBuilder {
    this._instance.timeout = value;
    return this;
  }

  params(value: HTTPParams): APIBuilder {
    this._instance.params = value;
    return this;
  }

  data(value: unknown): APIBuilder {
    this._instance.data = value;
    return this;
  }

  withCredentials(value: boolean): APIBuilder {
    this._instance.withCredentials = value;
    return this;
  }

  // 캐시 옵션을 설정하는 메서드 추가
  setCache(cache: RequestCache): APIBuilder {
    this._instance.cache = cache;
    return this;
  }

  // next 옵션설정
  next(config: NextFetchRequestConfig): APIBuilder {
    this._instance.next = { ...config };
    return this;
  }

  /**
   * 서버/클라이언트 환경에 맞춰 accessToken을 자동으로 주입
   */
  async auth(): Promise<APIBuilder> {
    const isServer = () => typeof window === 'undefined';
    if (isServer()) {
      const cookieStore = await cookies();
      const token = cookieStore.get('accessToken')?.value;

      if (token) {
        this._instance.headers = {
          ...this._instance.headers,
          Cookie: `Bearer ${token}`,
        };
      }
    }
    return this;
  }

  // 구성된 API 객체를 반환
  build(): API {
    return this._instance;
  }
}
