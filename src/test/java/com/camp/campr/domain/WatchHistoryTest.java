package com.camp.campr.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.camp.campr.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class WatchHistoryTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(WatchHistory.class);
        WatchHistory watchHistory1 = new WatchHistory();
        watchHistory1.setId(1L);
        WatchHistory watchHistory2 = new WatchHistory();
        watchHistory2.setId(watchHistory1.getId());
        assertThat(watchHistory1).isEqualTo(watchHistory2);
        watchHistory2.setId(2L);
        assertThat(watchHistory1).isNotEqualTo(watchHistory2);
        watchHistory1.setId(null);
        assertThat(watchHistory1).isNotEqualTo(watchHistory2);
    }
}
