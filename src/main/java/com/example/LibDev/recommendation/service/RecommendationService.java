package com.example.LibDev.recommendation.service;

import com.example.LibDev.recommendation.dto.RecommendationResponseDto;
import com.example.LibDev.recommendation.strategy.SimilarBookRecommendation;
import com.example.LibDev.recommendation.strategy.UserBaseRecommendation;
import com.example.LibDev.user.service.UserService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class RecommendationService {

    private final SimilarBookRecommendation similarBookRecommendation;
    private final UserBaseRecommendation userBaseRecommendation;
    private final UserService userService;
    private final RecommendationCacheService recommendationCacheService;

    /**
     * 도서 ID를 기준으로 유사한 도서를 추천
     * @param bookId 기준 도서 ID
     * @return 추천 도서 리스트
     */
    public List<RecommendationResponseDto> recommendSimilarBooks(Long bookId) {
        String cacheKey = "similar_books:" + bookId;
        List<RecommendationResponseDto> cachedRecommendations = recommendationCacheService.getCachedRecommendations(cacheKey);
        if (cachedRecommendations != null && !cachedRecommendations.isEmpty()) {
            return cachedRecommendations;
        }

        List<RecommendationResponseDto> recommendations = similarBookRecommendation.recommend(bookId, null);
        recommendationCacheService.cacheRecommendations(cacheKey, recommendations);
        return recommendations;
    }

    /**
     * 사용자 Email을 기준으로 개인화된 도서를 추천
     * @return 추천 도서 리스트
     */
    public List<RecommendationResponseDto> recommendUserBaseBooks() {
        String email = userService.getUserEmail();
        String cacheKey = "user_base_books:" + email;

        List<RecommendationResponseDto> cachedRecommendations = recommendationCacheService.getCachedRecommendations(cacheKey); // ✅ Redis에서 캐시 조회
        if (cachedRecommendations != null) {
            return cachedRecommendations;
        }

        List<RecommendationResponseDto> recommendations = userBaseRecommendation.recommend(null, email);
        recommendationCacheService.cacheRecommendations(cacheKey, recommendations);
        return recommendations;
    }
}
