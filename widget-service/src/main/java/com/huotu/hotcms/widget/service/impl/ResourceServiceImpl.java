package com.huotu.hotcms.widget.service.impl;

import me.jiangcai.lib.resource.Resource;
import me.jiangcai.lib.resource.service.ResourceService;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by lhx on 2016/6/27.
 */
@Service
public class ResourceServiceImpl implements ResourceService {
    @Override
    public Resource uploadResource(String path, InputStream data) throws IOException {
        return null;
    }

    @Override
    public Resource getResource(String path) {
        return null;
    }

    @Override
    public void deleteResource(String path) throws IOException {

    }
}
