package com.camp.campr.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.camp.campr.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class UserFavoritesTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(UserFavorites.class);
        UserFavorites userFavorites1 = new UserFavorites();
        userFavorites1.setId(1L);
        UserFavorites userFavorites2 = new UserFavorites();
        userFavorites2.setId(userFavorites1.getId());
        assertThat(userFavorites1).isEqualTo(userFavorites2);
        userFavorites2.setId(2L);
        assertThat(userFavorites1).isNotEqualTo(userFavorites2);
        userFavorites1.setId(null);
        assertThat(userFavorites1).isNotEqualTo(userFavorites2);
    }
}
