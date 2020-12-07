package by.leonovich.hibernatecrm.hibernate;

import org.hibernate.FlushMode;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created : 26/11/2020 22:03
 * Project : hibernate-crm
 * IDE : IntelliJ IDEA
 *
 * @author alexanderleonovich
 * @version 1.0
 */
public class HibernateUtil {
    private static final Logger LOG = LoggerFactory.getLogger(HibernateUtil.class);
    private static final HibernateUtil instance = new HibernateUtil();
    private final SessionFactory factory;
    private final ThreadLocal<Session> sessions = new ThreadLocal<>();

    private HibernateUtil() {
        try {
            Configuration config = new Configuration().configure();
            config.setPhysicalNamingStrategy(new CustomPhysicalNamingStrategy());
            factory = config.buildSessionFactory();
            LOG.trace("SessionFactory initialized : {}", factory);
        } catch (Exception e) {
            throw new HibernateException("Hibernate Session Factory creation failed.", e);
            /*System.exit(0);*/
        }
    }

    public Session getSession() {
        Session session = sessions.get();
        if (session == null) {
            session = factory.openSession();
            sessions.set(session);
        }
        session.setHibernateFlushMode(FlushMode.AUTO);
        return session;
    }

    public static synchronized HibernateUtil getInstance(){
        return instance;
    }
}
