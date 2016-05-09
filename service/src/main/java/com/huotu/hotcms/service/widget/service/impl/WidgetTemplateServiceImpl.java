package com.huotu.hotcms.service.widget.service.impl;

import com.huotu.hotcms.service.widget.model.WidgetTemplateType;
import com.huotu.hotcms.service.widget.service.WidgetTemplateService;
import org.springframework.stereotype.Service;

/**
 * Created by hzbc on 2016/5/9.
 */

@Service
public class WidgetTemplateServiceImpl implements WidgetTemplateService {
    @Override
    public void initDefaultWidgetTemplate(WidgetTemplateType widgetTemplateType) {
        switch (widgetTemplateType){
            case CLOTH:
                initClothTemplate();
                break;
            case FOOD:
                initFoodTemplate();
                break;
            case MEDICINE:
                initMedicineTemplate();
                break;
            default:
                break;
        }
    }

    private void initMedicineTemplate() {

    }

    private void initFoodTemplate() {

    }

    private void initClothTemplate() {

    }
}
