package com.baseballshop.service;

import com.baseballshop.dto.QnaFormDto;
import com.baseballshop.dto.SessionUser;
import com.baseballshop.entity.Member;
import com.baseballshop.entity.Qna;
import com.baseballshop.repository.MemberRepository;
import com.baseballshop.repository.QnaRepository;
import com.sun.net.httpserver.HttpsServer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpSession;
import java.security.Principal;

@Service
@RequiredArgsConstructor
@Transactional
public class QnaService {
    private final HttpSession httpSession;
    private final MemberRepository memberRepository;
    private final QnaRepository qnaRepository;

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
}
