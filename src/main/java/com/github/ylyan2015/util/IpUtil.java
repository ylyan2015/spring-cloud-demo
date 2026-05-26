package com.github.ylyan2015.util;

import lombok.extern.slf4j.Slf4j;
import org.lionsoul.ip2region.xdb.Searcher;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

/**
 * IP地址解析工具类
 */
@Component
@Slf4j
public class IpUtil {

    private Searcher v4Searcher;
    private Searcher v6Searcher;

    @PostConstruct
    public void init() {
        try {
            // 初始化IPv4数据库
            initV4Searcher();
            
            // 初始化IPv6数据库（可选）
            initV6Searcher();
            
            log.info("IP地址解析库初始化成功");
        } catch (Exception e) {
            log.error("IP地址解析库初始化失败", e);
        }
    }

    /**
     * 初始化IPv4搜索引擎
     */
    private void initV4Searcher() throws IOException {
        ClassPathResource resource = new ClassPathResource("ip2region_v4.xdb");
        InputStream inputStream = resource.getInputStream();
        
        Path tempFile = Files.createTempFile("ip2region_v4", ".xdb");
        Files.copy(inputStream, tempFile, StandardCopyOption.REPLACE_EXISTING);
        
        String dbPath = tempFile.toString();
        v4Searcher = Searcher.newWithFileOnly(dbPath);
        
        log.info("IPv4地址解析库初始化成功");
    }

    /**
     * 初始化IPv6搜索引擎
     */
    private void initV6Searcher() {
        try {
            ClassPathResource resource = new ClassPathResource("ip2region_v6.xdb");
            InputStream inputStream = resource.getInputStream();
            
            Path tempFile = Files.createTempFile("ip2region_v6", ".xdb");
            Files.copy(inputStream, tempFile, StandardCopyOption.REPLACE_EXISTING);
            
            String dbPath = tempFile.toString();
            v6Searcher = Searcher.newWithFileOnly(dbPath);
            
            log.info("IPv6地址解析库初始化成功");
        } catch (IOException e) {
            log.warn("IPv6地址解析库初始化失败，将仅支持IPv4: {}", e.getMessage());
            v6Searcher = null;
        }
    }

    /**
     * 解析IP地址
     * 
     * @param ip IP地址
     * @return 地理位置信息，格式：国家|区域|省份|城市|ISP
     */
    public String searchIp(String ip) {
        if (ip == null || ip.isEmpty()) {
            return "";
        }
        
        try {
            // 判断IP版本
            if (isIPv6(ip)) {
                if (v6Searcher != null) {
                    return v6Searcher.search(ip);
                } else {
                    log.warn("IPv6地址但未加载IPv6数据库: {}", ip);
                    return "";
                }
            } else {
                if (v4Searcher != null) {
                    return v4Searcher.search(ip);
                } else {
                    log.error("IPv4数据库未初始化");
                    return "";
                }
            }
        } catch (Exception e) {
            log.error("解析IP地址失败: {}", ip, e);
            return "";
        }
    }

    /**
     * 判断是否为IPv6地址
     */
    private boolean isIPv6(String ip) {
        try {
            InetAddress address = InetAddress.getByName(ip);
            return address instanceof java.net.Inet6Address;
        } catch (Exception e) {
            // 如果解析失败，简单判断是否包含冒号
            return ip.contains(":");
        }
    }

    /**
     * 获取省份
     */
    public String getProvince(String ip) {
        String region = searchIp(ip);
        if (region == null || region.isEmpty()) {
            return "";
        }
        
        String[] parts = region.split("\\|");
        if (parts.length >= 3) {
            return parts[2];
        }
        return "";
    }

    /**
     * 获取城市
     */
    public String getCity(String ip) {
        String region = searchIp(ip);
        if (region == null || region.isEmpty()) {
            return "";
        }
        
        String[] parts = region.split("\\|");
        if (parts.length >= 4) {
            return parts[3];
        }
        return "";
    }

    /**
     * 获取区县
     */
    public String getDistrict(String ip) {
        String region = searchIp(ip);
        if (region == null || region.isEmpty()) {
            return "";
        }
        
        String[] parts = region.split("\\|");
        if (parts.length >= 5) {
            return parts[4];
        }
        return "";
    }

    /**
     * 获取详细地址
     */
    public String getAddress(String ip) {
        String region = searchIp(ip);
        if (region == null || region.isEmpty()) {
            return "";
        }
        
        // 去掉最后的ISP信息，只保留地理位置
        int lastPipeIndex = region.lastIndexOf('|');
        if (lastPipeIndex > 0) {
            return region.substring(0, lastPipeIndex);
        }
        return region;
    }

    /**
     * 销毁方法
     */
    public void destroy() {
        if (v4Searcher != null) {
            try {
                v4Searcher.close();
            } catch (IOException e) {
                log.error("关闭IPv4地址解析器失败", e);
            }
        }
        if (v6Searcher != null) {
            try {
                v6Searcher.close();
            } catch (IOException e) {
                log.error("关闭IPv6地址解析器失败", e);
            }
        }
    }
}
