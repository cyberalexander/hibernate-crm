package by.leonovich.hibernatecrm.hibernate;

import org.hibernate.FlushMode;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.SharedSessionContract;
import org.hibernate.cfg.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;
import java.util.Optional;

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
    private final SessionFactory factory;
    private final ThreadLocal<Session> session = new ThreadLocal<>();

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
        Session s = Optional.ofNullable(this.session.get())
            .filter(SharedSessionContract::isOpen)
            .orElseGet(() -> {
                Session newSession = factory.openSession();
                LOG.info("Session {} opened!", newSession);
                return newSession;
            });
        this.session.set(s);
        s.setHibernateFlushMode(FlushMode.AUTO);
        return s;
    }

    public void closeSession() {
        if (Objects.nonNull(session) && session.get().isOpen()) {
            session.get().close();
            LOG.info("Session {} closed!", session.get());
        } else {
            LOG.info("Session {} was already closed!", session.get());
        }
    }
}
