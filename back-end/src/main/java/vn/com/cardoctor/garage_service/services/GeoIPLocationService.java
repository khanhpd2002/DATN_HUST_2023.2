package vn.com.cardoctor.garage_service.services;

import com.maxmind.geoip2.DatabaseReader;
import com.maxmind.geoip2.exception.GeoIp2Exception;
import com.maxmind.geoip2.model.CityResponse;
import org.springframework.core.io.InputStreamResource;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;
import vn.com.cardoctor.garage_service.models.GeoIP;

import java.io.*;
import java.net.InetAddress;

@Service
public class GeoIPLocationService {
    private final DatabaseReader dbReader;

    public GeoIPLocationService() throws IOException {
        try (InputStream database = getClass().getClassLoader().getResourceAsStream("GeoLite2-City.mmdb")) {
            if (database == null) {
                throw new IOException("Database file not found in resources folder");
            }
            dbReader = new DatabaseReader.Builder(database).build();
        }
    }

    public GeoIP getLocation(String ip)
            throws IOException, GeoIp2Exception {
        InetAddress ipAddress = InetAddress.getByName(ip);
        CityResponse response = dbReader.city(ipAddress);

        String cityName = response.getCity().getName();
        String latitude =
                response.getLocation().getLatitude().toString();
        String longitude =
                response.getLocation().getLongitude().toString();
        return new GeoIP(ip, cityName, latitude, longitude);
    }
}
