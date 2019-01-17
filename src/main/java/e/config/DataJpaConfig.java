package e.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Bean;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import javax.sql.DataSource;
import javax.persistence.EntityManagerFactory;
import java.util.Properties;

@Configuration
@EnableTransactionManagement
public class DataJpaConfig {

    @Bean
    public DataSource dataSource() {
        try {
            SimpleDriverDataSource result = new SimpleDriverDataSource();
            Class driver = Class.forName(
                                 "org.apache.derby.jdbc.EmbeddedDriver");
            result.setDriverClass(driver);
            result.setUrl("jdbc:derby:UrlShortener;create=true");
            return result;
        } catch (ClassNotFoundException exception) {
            throw new RuntimeException(exception);
        }
    }

    @Bean
    public JpaVendorAdapter jpaVendorAdapter() {
        return new HibernateJpaVendorAdapter();
    }

    @Bean
    public Properties hibernateProperties() {
        Properties result = new Properties();
        result.put("hibernate.dialect", "org.hibernate.dialect.DerbyTenFiveDialect");
        result.put("hibernate.hbm2ddl.auto", "update");
        result.put("hibernate.show_sql", true);
        return result;
    }

    @Bean
    public EntityManagerFactory entityManagerFactory() {
        LocalContainerEntityManagerFactoryBean factoryBean =
                            new LocalContainerEntityManagerFactoryBean();
        factoryBean.setDataSource(this.dataSource());
        factoryBean.setJpaVendorAdapter(this.jpaVendorAdapter());
        factoryBean.setJpaProperties(this.hibernateProperties());
        factoryBean.setPackagesToScan("e.model");
        factoryBean.afterPropertiesSet();
        return factoryBean.getNativeEntityManagerFactory();
    }

    @Bean
    public PlatformTransactionManager transactionManager() {
      return new JpaTransactionManager(this.entityManagerFactory());
    }
}
