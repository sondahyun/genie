package com.example.genie.domain.interest.service;

import com.example.genie.common.util.UserUtils;
import com.example.genie.domain.interest.entity.Interest;
import com.example.genie.domain.interest.mapper.InterestMapper;
import com.example.genie.domain.interest.repository.InterestRepository;
import com.example.genie.domain.pot.entity.Pot;
import com.example.genie.domain.pot.service.PotService;
import com.example.genie.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class InterestService {

    private final UserUtils userUtils;
    private final PotService potService;
    private final InterestRepository interestRepository;

    public Interest saveInterest(Authentication authentication, Long potId) {
        User user = userUtils.getUser(authentication);
        Pot pot = potService.getPotEntity(potId);
        Interest interest = InterestMapper.mapToPotWithUser(user,pot);
        return interestRepository.save(interest);
    }

    public void deleteInterest(Authentication authentication, Long potId){
        User user = userUtils.getUser(authentication);
        Pot pot = potService.getPotEntity(potId);
        Interest interest = interestRepository.findByPot_IdAndUser_Id(potId, user.getId());
        interestRepository.delete(interest);
    }

    public List<Interest> getUserInterestList(Authentication authentication){
        User user = userUtils.getUser(authentication);
        return interestRepository.findByUser_Id(user.getId());
    }

}
