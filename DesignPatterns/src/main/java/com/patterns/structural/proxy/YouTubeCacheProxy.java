package com.patterns.structural.proxy;

import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;

@Slf4j
public class YouTubeCacheProxy implements ThirdPartyYouTubeLib {

    private ThirdPartyYouTubeLib youTubeService;
    private HashMap<String, Video> cachePopular = new HashMap<String, Video>();
    private HashMap<String, Video> cacheAll = new HashMap<String, Video>();

    public YouTubeCacheProxy() {
        this.youTubeService = new ThirdPartyYouTubeClass();
    }

    @Override
    public HashMap<String, Video> popularVideos() {
        if(cachePopular.isEmpty()) {
            cachePopular = youTubeService.popularVideos();
        } else {
            log.info("Retrieving list from cache.");
        }
        return cachePopular;
    }
    @Override
    public Video getVideo(String videoId) {
        Video video = cacheAll.get(videoId);

        if(video == null) {
            video = youTubeService.getVideo(videoId);
            cacheAll.put(videoId, video);
        } else {
            log.info("Retrieved video '{}' from cache.", videoId);
        }

        return video;
    }

    public void reset() {
        cachePopular.clear();
        cacheAll.clear();
    }
}
