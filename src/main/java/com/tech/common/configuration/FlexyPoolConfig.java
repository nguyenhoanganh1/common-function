package com.tech.common.configuration;

//import com.p6spy.engine.common.ConnectionInformation;
//import com.p6spy.engine.event.JdbcEventListener;

import lombok.RequiredArgsConstructor;

@org.springframework.context.annotation.Configuration
//@EnableConfigurationProperties(HikariConfig.class)
@RequiredArgsConstructor
public class FlexyPoolConfig {

//    private final HikariConfig hikariConfig;

//    @Bean
//    public HikariDataSource hikariDataSource() {
//        return new HikariDataSource(hikariConfig);
//    }
//
//    @Bean
//    public JdbcEventListener myListener() {
//        return new JdbcEventListener() {
//            @Override
//            public void onAfterGetConnection(ConnectionInformation connectionInformation, SQLException e) {
//                System.out.println("got connection");
//            }
//
//            @Override
//            public void onAfterConnectionClose(ConnectionInformation connectionInformation, SQLException e) {
//                System.out.println("connection closed");
//            }
//        };
//    }


}
