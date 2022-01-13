package com.camp.campr.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.camp.campr.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class UserUploadTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(UserUpload.class);
        UserUpload userUpload1 = new UserUpload();
        userUpload1.setId(1L);
        UserUpload userUpload2 = new UserUpload();
        userUpload2.setId(userUpload1.getId());
        assertThat(userUpload1).isEqualTo(userUpload2);
        userUpload2.setId(2L);
        assertThat(userUpload1).isNotEqualTo(userUpload2);
        userUpload1.setId(null);
        assertThat(userUpload1).isNotEqualTo(userUpload2);
    }
}
