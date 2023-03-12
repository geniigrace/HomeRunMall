package com.baseballshop.service;

import com.baseballshop.constant.QnaStatus;
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

    public Long saveQna(QnaFormDto qnaFormDto, Principal principal){

        Member member = memberRepository.findByEmail(principal.getName());

            if(member == null){
                SessionUser user = (SessionUser) httpSession.getAttribute("member");
                member = memberRepository.findByEmail(user.getEmail());
            }

        Qna qna = Qna.createQna(member, qnaFormDto);
        qnaRepository.save(qna);

        return qna.getId();
    }

    public Page<QnaListDto> getQnaPage(Pageable pageable){

        List<Qna> qna = qnaRepository.findAllByOrderByIdDesc(pageable);
        Long totalQnaCount = orderRepository.countBy();

        List<QnaListDto> qnaLists = new ArrayList<>();

        for(Qna qnas : qna){
            QnaListDto qnaListDto = new QnaListDto(qnas);
            qnaListDto.setQnaMemberId(qnas.getMember().getEmail().split("@")[0]);
            qnaLists.add(qnaListDto);
        }

        return new PageImpl<QnaListDto>(qnaLists, pageable, totalQnaCount);
    }

    public Long qnaDone(Long qnaId){

        Qna qna = qnaRepository.findById(qnaId).orElseThrow(EntityExistsException::new);
        qna.setAnswerType(QnaStatus.DONE);
        return qnaId;
    }
}
