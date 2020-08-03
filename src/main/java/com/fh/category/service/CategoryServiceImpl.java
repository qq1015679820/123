package com.fh.category.service;

import com.alibaba.fastjson.JSONArray;
import com.fh.category.mapper.CategoryMapper;
import com.fh.category.model.Category;
import com.fh.common.ServerResponse;
import com.fh.util.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Resource
    private CategoryMapper categoryMapper;

    public ServerResponse queryList() {
        String categoryQueryList = RedisUtil.get("categoryQueryList");
        if(categoryQueryList==null){
            List<Map<String,Object>> allList = categoryMapper.queryList();
            List<Map<String,Object>> parentList = new  ArrayList<Map<String , Object>>();
            for (Map map : allList) {
                if (map.get("pid").equals(0)){
                    parentList.add(map);
                }
            }
            selectChildren(parentList,allList);
            String jsonString = JSONArray.toJSONString(parentList);
            RedisUtil.set("categoryQueryList",jsonString);
            return ServerResponse.success(parentList);
        }
        List<Map> mapList = JSONArray.parseArray(categoryQueryList, Map.class);
        return ServerResponse.success(mapList);
    }

    private void selectChildren(List<Map<String, Object>> parentList, List<Map<String, Object>> allList) {
        for (Map<String, Object> amap : parentList) {
            List<Map<String,Object>> childrenList = new  ArrayList<Map<String , Object>>();
            for (Map<String, Object> bmap : allList) {
                if (amap.get("id").equals(bmap.get("pid"))){
                    childrenList.add(bmap);
                }
            }
            if (childrenList!=null && childrenList.size()>0){
                amap.put("children",childrenList);
                selectChildren(childrenList,allList);
            }
        }
    }
}
