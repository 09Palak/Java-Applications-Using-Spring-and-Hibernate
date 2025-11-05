package com.example.config;

import java.util.Properties;

import javax.sql.DataSource;

import org.apache.commons.dbcp2.BasicDataSource;
import org.hibernate.SessionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.example.dao.StudentDAOImpl;
import com.example.dao.StudentDAO;
import com.example.service.FeeService;
import com.example.service.FeeServiceImpl;
import com.example.model.Student;
import com.example.model.Course;
import com.example.model.Payment;

@Configuration
@EnableTransactionManagement
public class AppConfig {

    @Bean
    public DataSource dataSource() {
        BasicDataSource ds = new BasicDataSource();
        // H2 in-memory
        ds.setDriverClassName("org.h2.Driver");
        ds.setUrl("jdbc:h2:mem:studentdb;DB_CLOSE_DELAY=-1;MODE=MYSQL");
        ds.setUsername("sa");
        ds.setPassword("");
        ds.setInitialSize(2);
        return ds;

        /* // For MySQL use:
        ds.setDriverClassName("com.mysql.cj.jdbc.Driver");
        ds.setUrl("jdbc:mysql://localhost:3306/studentdb?useSSL=false&serverTimezone=UTC");
        ds.setUsername("root");
        ds.setPassword("yourpassword");
        return ds;
        */
    }

    @Bean
    public LocalSessionFactoryBean sessionFactory(DataSource ds) {
        LocalSessionFactoryBean factory = new LocalSessionFactoryBean();
        factory.setDataSource(ds);
        factory.setAnnotatedClasses(Student.class, Course.class, Payment.class);
        Properties props = new Properties();
        props.put("hibernate.dialect", "org.hibernate.dialect.H2Dialect");
        props.put("hibernate.hbm2ddl.auto", "update"); // create/update schema
        props.put("hibernate.show_sql", "true");
        props.put("hibernate.format_sql", "true");
        factory.setHibernateProperties(props);
        return factory;
    }

    @Bean
    public HibernateTransactionManager transactionManager(SessionFactory sessionFactory) {
        HibernateTransactionManager tx = new HibernateTransactionManager();
        tx.setSessionFactory(sessionFactory);
        return tx;
    }

    @Bean
    public StudentDAO studentDAO(SessionFactory sf) {
        return new StudentDAOImpl(sf);
    }

    @Bean
    public FeeService feeService(StudentDAO studentDAO) {
        return new FeeServiceImpl(studentDAO);
    }
}
