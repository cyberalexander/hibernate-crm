package by.leonovich.hibernatecrm.hibernate;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.FlushMode;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.SharedSessionContract;
import org.hibernate.cfg.Configuration;

import java.util.Optional;

/**
 * Created : 26/11/2020 22:03
 * Project : hibernate-crm
 * IDE : IntelliJ IDEA
 *
 * @author alexanderleonovich
 * @version 1.0
 * @deprecated Replaced by Spring JPA
 */
@Deprecated
public class HibernateUtil {
    private static final Logger log = LogManager.getLogger(HibernateUtil.class);
    private final SessionFactory factory;
    private final ThreadLocal<Session> session = new ThreadLocal<>();

    private HibernateUtil() {
        try {
            Configuration config = new Configuration().configure();
            factory = config.buildSessionFactory();
            log.trace("SessionFactory initialized : {}", factory);
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
                log.info("Session {} opened!", newSession);
                return newSession;
            });
        this.session.set(s);
        s.setHibernateFlushMode(FlushMode.AUTO);
        return s;
    }

    public void closeSession() {
        Optional.ofNullable(session.get()).ifPresentOrElse(
            s -> {
                if (s.isOpen()) {
                    s.close();
                    log.info("Session {} closed!", s);
                }
            },
            () -> log.info("Session {} was already closed!", session.get())
        );
    }
}
