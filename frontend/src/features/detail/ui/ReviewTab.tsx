import { TabsContent } from '@/components/ui/tabs';
import { Star } from 'lucide-react';

export function ReviewTab() {
  return (
    <TabsContent value="reviews" className="px-5 py-6">
      <div className="space-y-4">
        <div className="flex items-center justify-between">
          <h3 className="text-lg font-semibold">리뷰</h3>
          <div className="flex items-center gap-1">
            <Star className="w-4 h-4 fill-orange-400 text-orange-400" />
            <span className="text-sm font-medium">4.0</span>
            <span className="text-sm text-gray-500">(25개)</span>
          </div>
        </div>

        <div className="space-y-4">
          <div className="border-b pb-4">
            <div className="flex items-center gap-2 mb-2">
              <div className="w-8 h-8 bg-gray-200 rounded-full flex items-center justify-center">
                <span className="text-sm font-medium">김</span>
              </div>
              <div>
                <p className="text-sm font-medium">김**</p>
                <div className="flex items-center gap-1">
                  {[1, 2, 3, 4, 5].map(star => (
                    <Star
                      key={star}
                      className="w-3 h-3 fill-orange-400 text-orange-400"
                    />
                  ))}
                </div>
              </div>
            </div>
            <p className="text-sm text-gray-600">
              정말 멋진 선글라스들이 많아요! 직접 착용해볼 수 있어서 좋았습니다.
            </p>
          </div>

          <div className="border-b pb-4">
            <div className="flex items-center gap-2 mb-2">
              <div className="w-8 h-8 bg-gray-200 rounded-full flex items-center justify-center">
                <span className="text-sm font-medium">이</span>
              </div>
              <div>
                <p className="text-sm font-medium">이**</p>
                <div className="flex items-center gap-1">
                  {[1, 2, 3, 4].map(star => (
                    <Star
                      key={star}
                      className="w-3 h-3 fill-orange-400 text-orange-400"
                    />
                  ))}
                  <Star className="w-3 h-3 text-gray-300" />
                </div>
              </div>
            </div>
            <p className="text-sm text-gray-600">
              디자인이 독특하고 품질도 좋네요. 가격대가 조금 높지만 만족합니다.
            </p>
          </div>
        </div>
      </div>
    </TabsContent>
  );
}
