package com.huotu.cms.manage.controller.common;

import com.huotu.cms.manage.ManageTest;
import org.junit.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.mock.web.MockMultipartFile;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

/**
 * Created by lhx on 2016/7/21.
 */

public class UploadResourceTest extends ManageTest {

    @Test
    public void uploadResource() throws Exception {
        loginAsOwner(testOwner);
        Resource resource = new ClassPathResource("thumbnail.png");
        int code = mockMvc.perform(post("/manage/cms/resourceUpload", new MockMultipartFile("file", resource.getInputStream()))
        ).andDo(print()).andReturn().getResponse().getStatus();
        assertThat(code).isEqualTo(200);
    }

    @Test
    public void deleteResource() throws Exception {
        loginAsOwner(testOwner);
        int code = mockMvc.perform(delete("/manage/cms/deleteResource", "thumbnail.png")
        ).andDo(print()).andReturn().getResponse().getStatus();
        assertThat(code).isEqualTo(200);
    }

}
