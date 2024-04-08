
package com.jerry.springbootredis.demos;

import com.jerry.springbootredis.utils.RedisUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;


@RestController
@RequestMapping("/redis")
public class BasicController {

    @Autowired
    private RedisUtils redisUtils;

//    @GetMapping("save")
//    public String save(String key, String value) {
//        redisTemplate.opsForValue().set(key, value);
//        return "ok";
//    }

    @GetMapping("sMap")
    public String sMap(String key, String hashKey, String hashVal) {
        Map<String, String> map = new HashMap<>();
        map.put(hashKey, hashVal);
        redisUtils.add(key, map);

        return "ok";

    }

    @GetMapping("getMapString")
    public String getMapString(String key, String hasKey) {
        String mapString = redisUtils.getMapString(key, hasKey);
        return mapString;
    }

    @GetMapping("getMapInt")
    public String getMapInt(String key, String hasKey) {
        Integer mapInt = redisUtils.getMapInt(key, hasKey);
        return mapInt.toString();
    }


}
