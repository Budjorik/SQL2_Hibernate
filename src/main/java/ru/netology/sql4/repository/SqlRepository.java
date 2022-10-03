package ru.netology.sql4.repository;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Repository;
import ru.netology.sql4.entity.City;
import ru.netology.sql4.entity.Person;
import ru.netology.sql4.entity.PersonalData;
import ru.netology.sql4.entity.enums.Gender;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

@Repository
public class SqlRepository  implements CommandLineRunner {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    @Transactional
    public void run(String... args) throws Exception {

        // Создаем города
        var cities = Stream.of("Moscow", "Novosibirsk", "Tyumen", "Kazan", "Yekaterinburg")
                .map(n -> City.builder()
                        .name(n)
                        .build())
                .collect(Collectors.toUnmodifiableList());

        // Сохраняем созданные города в Базу данных
        for (City entity : cities) {
            entityManager.persist(entity);
        }

        // Создаем мужские имена
        var maleNames = List.of("Ivan", "Petr", "Oleg");

        // Создаем мужские фамилии
        var maleSurnames = List.of("Sidorov", "Dasaev", "Nechaev");

        // Создаем женские имена
        var femaleNames = List.of("Olga", "Elena", "Tatyana");

        // Создаем женские фамилии
        var femaleSurnames = List.of("Petrova", "Ivanova", "Vyalbe");

        var random = new Random();

        // Создаем и сохраняем сущность человека, полученную на основе мужчин
        IntStream.range(0, 10)
                .forEach(i -> {
                    var malePersons = Person.builder()
                            .personalData(PersonalData.builder()
                                    .name(maleNames.get(random.nextInt(maleNames.size())))
                                    .surname(maleSurnames.get(random.nextInt(maleNames.size())))
                                    .age(random.nextInt(65))
                                    .build())
                            .gender(Gender.MALE)
                            .phoneNumber(String.valueOf(random.nextLong(999_999_999)))
                            .city(cities.get(random.nextInt(cities.size())))
                            .build();

                    entityManager.persist(malePersons);
                });

        // Создаем и сохраняем сущность человека, полученную на основе женщин
        IntStream.range(0, 10)
                .forEach(i -> {
                    var femalePersons = Person.builder()
                            .personalData(PersonalData.builder()
                                    .name(femaleNames.get(random.nextInt(femaleNames.size())))
                                    .surname(femaleSurnames.get(random.nextInt(femaleNames.size())))
                                    .age(random.nextInt(60))
                                    .build())
                            .gender(Gender.FEMALE)
                            .phoneNumber(String.valueOf(random.nextLong(999_999_999)))
                            .city(cities.get(random.nextInt(cities.size())))
                            .build();

                    entityManager.persist(femalePersons);
                });

        // Выводим на экран всех людей, созданных и сохраненных в базе данных
        Query query = entityManager.createQuery("select p from Person p order by p.personalData.name");
        List <Person> resultList = query.getResultList();
        System.out.println(resultList);

    }

    public List<Person> getPersonsByCity(String city) {
        Query query = entityManager.createQuery("select p from Person p where p.city.name = " +
                ":name order by p.personalData.name");
        query.setParameter("name", city);
        List <Person> resultList = query.getResultList();
        return resultList;
    }

    public boolean checkCity(String city) {
        Query query = entityManager.createQuery("select c from City c where c.name = " +
                ":name order by c.id");
        query.setParameter("name", city);
        List <Person> resultList = query.getResultList();
        if (resultList.size() > 0) {
            return true;
        } else return false;
    }

}
