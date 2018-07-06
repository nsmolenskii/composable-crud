# composable-crud

Set of Java interfaces with default methods (Traits).
Those traits allow you to implement service with simple traits compositions.


# Example

*User.java*
```java
@Data
@Entity
public class User implements Identifiable<String>, Archivable {
    @Id
    private String id;

    private boolean archived;

    @Override
    public void archive() {
        archived = true;
    }
}
```

*UserRepository.java*

```java
@Repository
public interface UserRepository extends
        Repository<User, String>,
        PersistableRepository<User, String>,
        IdentifiableRepository<User, String>,
        RemovableRepository<User, String>,
        QueryDslPredicateExecutor<User>, 
        Repository<User, String> {
}
```

*UserService.java*
```java
@Service
@AllArgsConstructor
public class UserService implements
        IdentifiableTrait<User, String, UserRepository>,
        PersistableTrait<User, String, UserRepository>,
        ArchivableTrait<User, String, UserRepository>,
        RemovableTrait<User, String, UserRepository>,
        QueryableTrait<User, String, UserRepository> {

    @Getter
    private final UserRepository repository;

    @Override
    public Supplier<EntityNotFoundProblem> notFoundById(final String id) {
        return EntityNotFoundProblem.withId("user", id);
    }

}
``` 
