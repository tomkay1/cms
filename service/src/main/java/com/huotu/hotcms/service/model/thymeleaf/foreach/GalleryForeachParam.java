package com.huotu.hotcms.service.model.thymeleaf.foreach;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 *     图库参数模型
 * </p>
 * @author xhl
 *
 * @since 1.2
 *
 * @time 2016/04/28
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class GalleryForeachParam extends BaseForeachParam {
    /**
     * 所属图库的id
     */
    @Rename("galleryid")
    public Long galleryId;
}
