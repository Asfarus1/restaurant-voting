package voting.repository.internal;


import java.time.LocalDate;

public interface HaveLunchInRepository {
    void haveLunchIn(Long restId, LocalDate date, Long userId);
}
