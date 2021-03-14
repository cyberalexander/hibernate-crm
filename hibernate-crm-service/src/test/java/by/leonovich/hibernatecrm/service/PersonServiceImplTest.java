package by.leonovich.hibernatecrm.service;

import by.leonovich.hibernatecrm.common.random.RandomNumber;
import by.leonovich.hibernatecrm.dao.Dao;
import by.leonovich.hibernatecrm.mappings.singletable.Person;
import by.leonovich.hibernatecrm.service.person.PersonService;
import by.leonovich.hibernatecrm.service.person.PersonServiceImpl;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

/**
 * Created : 13/03/2021 18:35
 * Project : hibernate-crm
 * IDE : IntelliJ IDEA
 *
 * @author alexanderleonovich
 * @version 1.0
 */
@ExtendWith(MockitoExtension.class)
class PersonServiceImplTest {

    @InjectMocks
    private final PersonService<Person> personService = new PersonServiceImpl();

    @Mock
    private Dao<Person> dao;

    @BeforeEach
    @SneakyThrows
    void setUp() {
        Mockito.when(dao.save(Mockito.any())).thenReturn(RandomNumber.DEFAULT_L.get());
        Mockito.when(dao.get(Mockito.any())).thenReturn(Mockito.any());
    }

    @Test
    @SneakyThrows
    void testCreate() {
        Person testData = Person.init();
        personService.create(testData);
        Mockito.verify(dao).save(testData);
        Mockito.verify(dao).get(Mockito.anyLong());
    }
}
