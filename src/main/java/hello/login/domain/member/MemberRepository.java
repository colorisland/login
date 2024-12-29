package hello.login.domain.member;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.*;

@Slf4j
@Repository
public class MemberRepository {

    // static 사용. 공용 메모리로 쓰도록.
    private static Map<Long, Member> store = new HashMap<>();
    private static Long sequence = 0L;

    public Member save(Member member) {
        member.setId(++sequence);
        log.info("save memeber: {}", member);
        // store에 회원 저장.
        store.put(member.getId(), member);

        return member;
    }

    public Member findById(Long id) {
        return store.get(id);
    }

    public Optional<Member> findByLoginId(String loginId) {
        return findAll().stream().filter(m -> m.getLoginId().equals(loginId)).findFirst();
    }

    public List<Member> findAll() {
        // store.values()를 하게되면 value 들만 Collection 인터페이스에 담겨서 리턴되는데 Collection 이 List 보다 큰 인터페이스여서 오류나서 변환.
        return new ArrayList<>(store.values());
    }
}

