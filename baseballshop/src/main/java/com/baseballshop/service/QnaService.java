package com.baseballshop.service;

import com.baseballshop.constant.QnaStatus;
import com.baseballshop.constant.Role;
import com.baseballshop.dto.QnaFormDto;
import com.baseballshop.dto.QnaListDto;
import com.baseballshop.dto.SessionUser;
import com.baseballshop.entity.Member;
import com.baseballshop.entity.Qna;
import com.baseballshop.repository.MemberRepository;
import com.baseballshop.repository.OrderRepository;
import com.baseballshop.repository.QnaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;
import javax.servlet.http.HttpSession;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class QnaService {
    private final HttpSession httpSession;
    private final MemberRepository memberRepository;
    private final QnaRepository qnaRepository;
    private final OrderRepository orderRepository;

    public Long saveQna(QnaFormDto qnaFormDto, Principal principal) {

        Member member = memberRepository.findByEmail(principal.getName());

        if (member == null) {
            SessionUser user = (SessionUser) httpSession.getAttribute("member");
            member = memberRepository.findByEmail(user.getEmail());
        }

        Qna qna = Qna.createQna(member, qnaFormDto);
        qnaRepository.save(qna);

        return qna.getId();
    }

    public Page<QnaListDto> getQnaPage(Pageable pageable) {

        List<Qna> qna = qnaRepository.findAllByOrderByIdDesc(pageable);
        Long totalQnaCount = orderRepository.countBy();

        List<QnaListDto> qnaLists = new ArrayList<>();

        for (Qna qnas : qna) {
            QnaListDto qnaListDto = new QnaListDto(qnas);
            //qnaListDto.setQnaMemberId(qnas.getMember().getEmail().split("@")[0]);
            qnaLists.add(qnaListDto);
        }

        return new PageImpl<QnaListDto>(qnaLists, pageable, totalQnaCount);
    }

    public Long qnaDone(Long qnaId) {

        Qna qna = qnaRepository.findById(qnaId).orElseThrow(EntityExistsException::new);
        qna.setAnswerType(QnaStatus.DONE);
        return qnaId;
    }

    //관리자, 작성자, 유저 비교
    public Boolean qnaWriterCheck(String loginEmail, Long qnaId) {
        Boolean result = false;
        //작성한 QNA 착기
        Qna qna = qnaRepository.findById(qnaId).orElseThrow(EntityNotFoundException::new);

        //FAQ인지 먼저 확인
        if (qna.getQnaType().getTitle().equals("FAQ")) {
            result = true;
        } else {
            //작성자 메일찾기
            String qnaEmail = qna.getMember().getEmail();

            //비교
            if (!(memberRepository.findByEmail(loginEmail).getRole() == Role.ADMIN)) {
                if (loginEmail.equals(qnaEmail)) {
                    result = true; //이메일 동일하면 진입
                } else {
                    result = false; //이메일 다르면 진입불가
                }
            } else {
                result = true; //관리자권한일 경우 진입
            }
        }
        return result;
    }

    //단순 작성자 비교 -> 수정버튼 유무에 사용됨
    public Boolean qnaMemberCheck(String loginEmail, Long qnaId) {
        Boolean result = false;
        //작성한 QNA 착기
        Qna qna = qnaRepository.findById(qnaId).orElseThrow(EntityNotFoundException::new);
        //작성자 메일찾기
        String qnaEmail = qna.getMember().getEmail();
        //비교
        if (loginEmail.equals(qnaEmail)) {
            if(qna.getAnswerType().getTitle().equals(QnaStatus.NOT.getTitle())){
                result = true; //이메일 동일, 아직 답변 전이면 수정버튼 진입
            }
            else {
                result = false; //이메일은 같지만 답변이 완료되면 수정버튼 불가
            }
        } else {
            result = false; //이메일 다르면 진입불가
        }
        return result;
    }

    public QnaListDto qnaView(Long qnaId) {
        Qna qna = qnaRepository.findById(qnaId).orElseThrow(EntityNotFoundException::new);
        QnaListDto qnaListDto = new QnaListDto(qna);
        return qnaListDto;
    }

    public Long updateQna(QnaFormDto qnaFormDto){
        Qna qna = qnaRepository.findById(qnaFormDto.getId()).orElseThrow(EntityNotFoundException::new);
        qna.updateQna(qnaFormDto);
        return qna.getId();
    }


}
