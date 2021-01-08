package by.leonovich.hibernatecrm;

import by.leonovich.hibernatecrm.annotation.Author;
import by.leonovich.hibernatecrm.annotation.Book;
import by.leonovich.hibernatecrm.annotation.Library;
import by.leonovich.hibernatecrm.annotation.Portfolio;
import by.leonovich.hibernatecrm.dao.AuthorDao;
import by.leonovich.hibernatecrm.dao.BookDao;
import by.leonovich.hibernatecrm.dao.Dao;
import by.leonovich.hibernatecrm.dao.LibraryDao;
import by.leonovich.hibernatecrm.dao.PortfolioDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.util.Locale;

/**
 * Hello world!
 *
 */
public class App {
    private static final Logger LOGGER = LoggerFactory.getLogger(App.class);
    private static final Dao<Author> authorDao = new AuthorDao();
    private static final Dao<Book> bookDao = new BookDao();
    private static final Dao<Library> libraryDao = new LibraryDao();
    private static final Dao<Portfolio> portfolioDao = new PortfolioDao();


    public static void main( String[] args ) throws Exception {
        Serializable authorId = authorDao.save(new Author().populate());
        LOGGER.info("AUTHOR : {}", authorDao.get(authorId));

        Serializable bookId = bookDao.save(new Book().populate());
        LOGGER.info("BOOK : {}", bookDao.get(bookId));

        Serializable libraryId = libraryDao.save(new Library().populate());
        LOGGER.info("LIBRARY : {}", libraryDao.get(libraryId));

        Serializable portfolioId = portfolioDao.save(new Portfolio().populate());
        LOGGER.info("PORTFOLIO : {}", portfolioDao.get(portfolioId));
    }

    @SuppressWarnings("PMD.SystemPrintln")
    public static void printLocales() {
        String[] locales = Locale.getISOCountries();
        for (String countryCode : locales) {
            Locale obj = new Locale("", countryCode);
            System.out.println("obj = " + obj);
            System.out.println("Country Code = " + obj.getCountry()
                + ", Country Name = " + obj.getDisplayCountry());
        }
    }
}
