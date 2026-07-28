package th.mfu;

import org.springframework.data.repository.CrudRepository;

/**
 * Spring Data repository for Member.
 */
public interface MemberRepository extends CrudRepository<Member, Long> {
}
