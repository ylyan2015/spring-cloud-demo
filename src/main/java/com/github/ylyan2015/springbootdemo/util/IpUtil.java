package com.github.ylyan2015.springbootdemo.util;

import lombok.extern.slf4j.Slf4j;
import org.lionsoul.ip2region.xdb.Searcher;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.io.InputStream;

@Slf4j
@Component
public class IpUtil {

    private Searcher searcher;

    public static String getClientIp(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("X-Real-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }

        if (ip != null && ip.contains(",")) {
            ip = ip.split(",")[0].trim();
        }

        return ip;
    }

    @PostConstruct
    public void init() {
        try {
            // 使用内存加载方式提高性能
            ClassPathResource v4Resource = new ClassPathResource("ip2region_v4.xdb");
            InputStream v4InputStream = v4Resource.getInputStream();
            byte[] v4Bytes = v4InputStream.readAllBytes();
            v4InputStream.close();

            searcher = Searcher.newWithBuffer(v4Bytes);
            log.info("IP地址解析器初始化成功");
        } catch (Exception e) {
            log.error("IP地址解析器初始化失败", e);
        }
    }

    public String getProvince(String ip) {
        try {
            String region = searcher.search(ip);
            if (region != null && region.contains("|")) {
                String[] parts = region.split("\\|");
                if (parts.length > 2) {
                    return parts[2];
                }
            }
            return "";
        } catch (Exception e) {
            log.error("获取省份失败, ip: {}", ip, e);
            return "";
        }
    }

    public String getCity(String ip) {
        try {
            String region = searcher.search(ip);
            if (region != null && region.contains("|")) {
                String[] parts = region.split("\\|");
                if (parts.length > 3) {
                    return parts[3];
                }
            }
            return "";
        } catch (Exception e) {
            log.error("获取城市失败, ip: {}", ip, e);
            return "";
        }
    }

    public String getDistrict(String ip) {
        try {
            String region = searcher.search(ip);
            if (region != null && region.contains("|")) {
                String[] parts = region.split("\\|");
                if (parts.length > 4) {
                    return parts[4];
                }
            }
            return "";
        } catch (Exception e) {
            log.error("获取区县失败, ip: {}", ip, e);
            return "";
        }
    }

    public String getAddress(String ip) {
        try {
            return searcher.search(ip);
        } catch (Exception e) {
            log.error("获取地址失败, ip: {}", ip, e);
            return "";
        }
    }
}
