package org.pda.announcement.stockprice.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class RedisService {

    private final RedisTemplate<String, String> redisTemplate;

    @Autowired
    public RedisService(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public String getStockRankFromRedis(String sortBy) {
        String key = getRedisKey(sortBy);
        return redisTemplate.opsForValue().get(key); // Redis에서 데이터를 읽어오기
    }


    private String getRedisKey(String sortBy) {
        return switch (sortBy) {
            case "amount" -> "거래대금순위";
            case "volume" -> "거래량순위";
            case "change_rate_up" -> "상승률순위";
            case "change_rate_down" -> "하락률순위";
            default -> throw new IllegalArgumentException("Invalid sort_by parameter");
        };
    }

    public Map<Object, Object> getStockCurrentPriceByTicker(String ticker) {
        // Redis에서 주식 정보를 해시로 가져옴
        String redisKey = "stock:" + ticker;

        return redisTemplate.opsForHash().entries(redisKey); // 'ticker'를 키로 하는 해시 맵을 가져옴
    }

    // 여러 티커의 데이터를 한 번에 가져오기
    public Map<String, Map<Object, Object>> getStockCurrentPricesByTickers(List<String> tickers) {
        HashOperations<String, Object, Object> hashOperations = redisTemplate.opsForHash();

        // Redis에서 여러 티커 데이터를 한 번에 가져오기
        return tickers.stream()
                .collect(Collectors.toMap(
                        ticker -> ticker, // 키: 티커
                        ticker -> hashOperations.entries("stock:" + ticker), // 값: Redis에서 가져온 데이터
                        (existing, replacement) -> existing // 중복 키 발생 시 기존 값 유지
                ));
    }


}


