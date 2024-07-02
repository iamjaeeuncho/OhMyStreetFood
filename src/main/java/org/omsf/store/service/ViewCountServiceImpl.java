package org.omsf.store.service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import org.omsf.store.model.Menu;
import org.omsf.store.model.Photo;
import org.omsf.store.model.Store;
import org.omsf.store.model.StoreInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ViewCountServiceImpl implements ViewCountService{
	
	private static final Logger logger = LoggerFactory.getLogger(ViewCountService.class);
	private ObjectMapper objectMapper = new ObjectMapper();
    private final RedisTemplate<String, String> redisTemplate;
	private final StoreService storeService;
    private final MenuService menuService;
	
    private static final String DAILY_VIEW_COUNT_KEY = "store:daily:view:count:";
    private static final String POPULAR_STORES_KEY = "popular:stores";
    private static final String STORE_RANKINGS_KEY = "store:rankings";
    
    @Override
    public void incrementViewCount(Integer storeNo) {
    	//dailyviewCount + 날짜 + storeNo 저장
        String dailyKey = DAILY_VIEW_COUNT_KEY + LocalDate.now().format(DateTimeFormatter.ISO_DATE) + ":" + storeNo;

        redisTemplate.opsForValue().increment(dailyKey);
        redisTemplate.expire(dailyKey, 7, TimeUnit.DAYS);
        
        redisTemplate.opsForZSet().incrementScore("store:rankings", storeNo.toString(), 1);
    }
    
    @Override
    public void decreaseViewCount(Integer storeNo, int count) {
        redisTemplate.opsForZSet().incrementScore("store:rankings", storeNo.toString(), -count);
    }

    @Scheduled(cron = "0 0 0 * * ?") 
    @Override
    public void decreaseOldViewCounts() {
        String oldDailyKey = DAILY_VIEW_COUNT_KEY + LocalDate.now().minusDays(7).format(DateTimeFormatter.ISO_DATE) + ":*";
        Set<String> keys = redisTemplate.keys(oldDailyKey);
        for (String key : keys) {
        	//store:daily:view:count:2024-07-01:282
            String[] parts = key.split(":");
            Integer storeNo = Integer.parseInt(parts[parts.length - 1]);
            String countStr = redisTemplate.opsForValue().get(key);
            if (countStr != null) {
                int count = Integer.parseInt(countStr);
                decreaseViewCount(storeNo, count);
            }
            redisTemplate.delete(key);
        }
    }
    
    @Transactional
    @Override
    public Cookie addViewCount(HttpServletRequest request, int storeNo) {

        Cookie[] cookies = request.getCookies();
        Cookie oldCookie = this.findCookie(cookies, "View_Count");

        if (oldCookie != null) {
            if (!oldCookie.getValue().contains("[" + storeNo + "]")) {
                oldCookie.setValue(oldCookie.getValue() + "[" + storeNo + "]");
                this.incrementViewCount(storeNo);
            }
            oldCookie.setPath("/");
            return oldCookie;
        } else {
            Cookie newCookie = new Cookie("View_Count", "[" + storeNo + "]");
            newCookie.setPath("/");
            this.incrementViewCount(storeNo);
            return newCookie;
        }
    }
    
    @Override
    public Cookie findCookie(Cookie[] cookies, String name) {
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(name)) {
                    return cookie;
                }
            }
        }
        return null;
    }
	
    @Override
    @Scheduled(fixedRate = 60000)
    @Transactional(readOnly = true)
    public void updateTop10Stores() {
        Set<ZSetOperations.TypedTuple<String>> topStores = 
            redisTemplate.opsForZSet().reverseRangeWithScores(STORE_RANKINGS_KEY, 0, 9);
        logger.info("상위 10개 점포 가져오기");
        if (topStores == null || topStores.isEmpty()) {
            return;
        }

        List<StoreInfo> top10Stores = new ArrayList<>();
        for (ZSetOperations.TypedTuple<String> tuple : topStores) {
            int storeNo = Integer.parseInt(tuple.getValue());
            Store store = storeService.getStoreByNo(storeNo);
            if (store != null) {
            	Photo storePhoto = Photo.builder().build();
            	if (store.getPicture() != null) {
            		storePhoto = storeService.getPhotoByPhotoNo(store.getPicture());
            	}
            	List<Menu> menus = menuService.getMenusByStoreNo(storeNo);
            	List<Photo> gallery = storeService.getStoreGallery(storeNo);
            	StoreInfo storeInfo = setStoreInfo(store, storePhoto, gallery, menus);
            	
            	if (menus == null || menus.isEmpty()) {
                    logger.warn("Store {} has no menus", storeNo);
                }
            	
            	top10Stores.add(storeInfo);
            }
            
            if (top10Stores.size() >= 10) {
                break;  
            }
        }

        try {
            String jsonTopStores = objectMapper.writeValueAsString(top10Stores);
            String currentTopStores = redisTemplate.opsForValue().get(POPULAR_STORES_KEY);

            redisTemplate.opsForValue().set(POPULAR_STORES_KEY, jsonTopStores);
//            if (!jsonTopStores.equals(currentTopStores)) {
//            }
        } catch (JsonProcessingException e) {
            // 예외 처리
            e.printStackTrace();
        }
    }
    
    @Override
    @Transactional
    public void removeStoreRankings(int storeNo) {
        redisTemplate.opsForZSet().remove(STORE_RANKINGS_KEY, String.valueOf(storeNo));

        String jsonTopStores = redisTemplate.opsForValue().get(POPULAR_STORES_KEY);
        if (jsonTopStores != null) {
            try {
                List<StoreInfo> topStores = objectMapper.readValue(jsonTopStores, new TypeReference<List<StoreInfo>>() {});
                //없는가게 삭제
                topStores.removeIf(storeInfo -> storeInfo.getStore().getStoreNo() == storeNo);
                String updatedJsonTopStores = objectMapper.writeValueAsString(topStores);
                redisTemplate.opsForValue().set(POPULAR_STORES_KEY, updatedJsonTopStores);
            } catch (JsonProcessingException e) {
                logger.error("가게 삭제중 에러발생", e);
            }
        }
        
        //일일키 삭제
        String dailyKeyPattern = DAILY_VIEW_COUNT_KEY + "*:" + storeNo;
        Set<String> keys = redisTemplate.keys(dailyKeyPattern);
        if (keys != null && !keys.isEmpty()) {
            redisTemplate.delete(keys);
        }

        updateTop10Stores();
    }
    
    public List<StoreInfo> getTop10PopularStores() {
        String jsonTopStores = redisTemplate.opsForValue().get(POPULAR_STORES_KEY);
        if (jsonTopStores == null) {
            return Collections.emptyList();
        }

        try {
            return objectMapper.readValue(jsonTopStores, new TypeReference<List<StoreInfo>>() {});
        } catch (JsonProcessingException e) {
            // 예외 처리
            e.printStackTrace();
            return Collections.emptyList();
        }
    }
    
    
    public StoreInfo setStoreInfo(Store store, Photo photo, List<Photo> gallery, List<Menu> menus) {
		StoreInfo storeInfo = new StoreInfo();
		
    	storeInfo.setStore(store);
    	storeInfo.setPhoto(photo);
    	storeInfo.setMenus(menus);
    	storeInfo.setGallery(gallery);
    	
    	return storeInfo;
    }

    @Override
    public StoreInfo getPopularStoreInfo(int storeNo) {
        String jsonTopStores = redisTemplate.opsForValue().get(POPULAR_STORES_KEY);
        if (jsonTopStores == null) {
            return null;
        }

        try {
            List<StoreInfo> topStores = objectMapper.readValue(jsonTopStores, new TypeReference<List<StoreInfo>>() {});
            return topStores.stream()
                            .filter(store -> store.getStore().getStoreNo() == storeNo)
                            .findFirst()
                            .orElse(null);
        } catch (JsonProcessingException e) {
            logger.error("Error parsing JSON from Redis for top stores", e);
            return null;
        }
    }
}