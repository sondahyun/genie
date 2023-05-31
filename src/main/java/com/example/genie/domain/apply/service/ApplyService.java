package com.example.genie.domain.apply.service;

import com.example.genie.domain.apply.entity.Apply;
import com.example.genie.domain.apply.entity.State;
import com.example.genie.domain.apply.repository.ApplyRepository;
import com.example.genie.domain.pot.entity.Pot;
import com.example.genie.domain.pot.repository.PotRepository;
import com.example.genie.domain.user.entity.User;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.awt.print.Pageable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ApplyService {
    
    ApplyRepository applyRepository;
    PotRepository potRepository;
    //Apply 생성
    public Apply createApply(User user, Long potId){
        Pot pot = potRepository.findById(potId).orElseThrow(() -> new EntityNotFoundException("Pot not found"));
        Apply apply = Apply.builder().state(State.APPLY).applicant(user).pot(pot).build();
        applyRepository.save(apply);
        return apply;
    }

    public List<User> getApplyUserList(Long potId){
        List<Apply> appyList = applyRepository.findByPot_Id(potId);
        List<User> userList = new ArrayList<>();
        for(Apply apply : appyList){
            userList.add(apply.getApplicant());
        }
        return userList;
    }


    public Apply appoveApply(Long potId, Long userId, int s){
        Apply apply = applyRepository.findByPot_IdAndApplicant_Id(potId, userId);
        State state;
        if(s==0)
            state = State.APPROVED;
        else
            state = State.REJECTED;
        apply.changeState(state);
        return applyRepository.save(apply);
    }

    public List<Apply> getApprovedApplyList(Pot pot){
        List<Apply> applyList = applyRepository.findByStateAndPot(State.REJECTED, pot);
        return applyList;
    }


}
