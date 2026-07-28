package th.mfu;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Members. The transactions that belong to a member live in TransactionController.
 */
@RestController
@RequestMapping("/api")
public class MemberController {

    @Autowired
    private MemberRepository memberRepository;

    @GetMapping("/members")
    public ResponseEntity<List<Member>> listMembers() {
        return new ResponseEntity<>((List<Member>) memberRepository.findAll(), HttpStatus.OK);
    }

    // get member  (GET /api/members/{id})
    @GetMapping("/members/{id}")
    public ResponseEntity<Member> getMember(@PathVariable Long id) {
        Optional<Member> member = memberRepository.findById(id);
        if (!member.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(member.get(), HttpStatus.OK);
    }

    @PostMapping("/members")
    public ResponseEntity<String> createMember(@RequestBody Member member) {
        if (member.getJoinDate() == null) {
            member.setJoinDate(LocalDate.now());
        }
        Member saved = memberRepository.save(member);
        return new ResponseEntity<>("Member created with ID: " + saved.getId(), HttpStatus.CREATED);
    }
}
