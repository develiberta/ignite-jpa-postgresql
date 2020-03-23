package codesample.ignite.config;

import codesample.ignite.cache.PersonCacheStore;
import codesample.ignite.entity.Person;
import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteException;
import org.apache.ignite.Ignition;
import org.apache.ignite.cache.QueryEntity;
import org.apache.ignite.configuration.CacheConfiguration;
import org.apache.ignite.configuration.IgniteConfiguration;
import org.apache.ignite.spi.communication.CommunicationSpi;
import org.apache.ignite.spi.communication.tcp.TcpCommunicationSpi;
import org.apache.ignite.spi.discovery.DiscoverySpi;
import org.apache.ignite.spi.discovery.tcp.TcpDiscoverySpi;
import org.apache.ignite.spi.discovery.tcp.ipfinder.multicast.TcpDiscoveryMulticastIpFinder;
import org.apache.ignite.spi.discovery.tcp.ipfinder.vm.TcpDiscoveryVmIpFinder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.cache.configuration.FactoryBuilder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;

@Configuration
public class IgniteConfig {

	/* igniteConfiguration()에서 설정한대로 Ignition을 시작 */
    @Bean
    Ignite igniteClient() throws IgniteException {
        return Ignition.start(igniteConfiguration());
    }

    /* IgniteConfiguration 설정 부분 */
    @Bean
    public IgniteConfiguration igniteConfiguration() {
        IgniteConfiguration cfg = new IgniteConfiguration();

        //cfg.setClientMode(true); Do not forget to set client mode if you need to
        cfg.setPeerClassLoadingEnabled(true);
        cfg.setDiscoverySpi(discoverySpi());					// discoverySpi()는 아래에서 정의
        cfg.setCommunicationSpi(communicationSpi());			// communicationSpi()는 아래에서 정의
        cfg.setCacheConfiguration(personCacheConfiguration());	// 특정 CacheConfiguration 정의이며, 콤마로 연결해서 다수 추가 가능
        return cfg;
    }

    /* Ignite는 DiscoverySpi를 이용해서  node가 서로를 발견하는 기능 구현 */
    @Bean
    public DiscoverySpi discoverySpi() {
    	// TcpDiscoverySpi는 Ignite의 default 설정
        TcpDiscoverySpi discoverySpi = new TcpDiscoverySpi();
        discoverySpi.setIpFinder(tcpDiscoveryVmIpFinder());		

        return discoverySpi;
    }

    @Bean
    public CommunicationSpi communicationSpi() {
        TcpCommunicationSpi communicationSpi = new TcpCommunicationSpi();
        communicationSpi.setMessageQueueLimit(1024);
        return communicationSpi;
    }

    /* 각각의 Cache 별로 만들어주는 Configuration */
    @Bean public CacheConfiguration personCacheConfiguration() {
        CacheConfiguration<Long, codesample.ignite.entity.Person> cache = new CacheConfiguration<>("person");
        cache.setReadThrough(true);
        cache.setWriteThrough(true);
        cache.setCacheStoreFactory(FactoryBuilder.factoryOf(PersonCacheStore.class));
        
        cache.setIndexedTypes(Long.class, Person.class);
        
        return cache;
    }

    /* It works with pre-configured list of IP addresses specified via setAddresses(Collection) method. */
    @Bean
    public TcpDiscoveryVmIpFinder tcpDiscoveryVmIpFinder() {
        TcpDiscoveryMulticastIpFinder ipFinder = new TcpDiscoveryMulticastIpFinder();
        ipFinder.setAddresses(Arrays.asList("127.0.0.1:47500..47509", "192.168.0.99"));
        
        return ipFinder;
    }
}
