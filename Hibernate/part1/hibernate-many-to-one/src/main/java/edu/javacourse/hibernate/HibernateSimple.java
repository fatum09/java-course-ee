package edu.javacourse.hibernate;

import java.util.ArrayList;
import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.criterion.Restrictions;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.service.internal.SessionFactoryServiceRegistryFactoryImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Простой пример работы со связанными таблицами
 * 
 * @author ASaburov
 */
public class HibernateSimple {

    private static final Logger log = LoggerFactory.getLogger( HibernateSimple.class );

    public static void main( String[] args )
    {
        Configuration configuration = new Configuration();
        configuration.configure( "hibernate.cfg.xml" );
        StandardServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder().applySettings( configuration.getProperties() ).build();
        SessionFactory sessionFactory = configuration.buildSessionFactory( serviceRegistry );

        Session s = sessionFactory.getCurrentSession();
        s.beginTransaction();

        Criteria criteria = s.createCriteria( City.class );
        List< City > cityList = criteria.list();

        for ( City city : cityList )
        {
            log.debug( "city id: {}", city.getCityId());
            log.debug("city name: {}", city.getCityName());
            log.debug("city region id: {}", city.getRegion().getRegionId());
            log.debug("city region name: {}", city.getRegion().getRegionName());
            log.debug("");
        }

        log.debug("====================================================");
        
        List<Region> regionList = s.createQuery("from Region").list();
        
        for(Region r : regionList) {
            log.debug("Region name: {}", r);
            for(City c : r.getCityList()) {
                log.debug("     City name: {}", c);
            }
        }

        Region spb = new Region("SPb");
        s.save(spb);

        City gatchina = new City();
        gatchina.setCityName("Gatchina");
        gatchina.setRegion(spb);
        s.save(gatchina);

        City pushkin = new City();
        pushkin.setCityName("Pushkin");
        pushkin.setRegion(spb);
        s.save(pushkin);

        s.save(spb);

        s.getTransaction().commit();
    }

}
