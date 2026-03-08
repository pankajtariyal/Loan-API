package com.loandemo.Loan.API.service;

import com.loandemo.Loan.API.dto.loan.apply.LoanApplyRequest;
import com.loandemo.Loan.API.dto.loan.apply.LoanApplyResponse;
import com.loandemo.Loan.API.dto.loan.get.GetAllLoan;
import com.loandemo.Loan.API.dto.loan.get.GetLoanByIdResponse;
import com.loandemo.Loan.API.dto.loan.get.GetLoanByUserResponse;
import com.loandemo.Loan.API.dto.user.UserResponse;
import com.loandemo.Loan.API.modul.Loan;
import com.loandemo.Loan.API.modul.User;
import com.loandemo.Loan.API.repository.LoanRepository;
import com.loandemo.Loan.API.repository.UserRepository;
import com.loandemo.Loan.API.status.LoanStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class LoanService {

    private final LoanRepository loanRepository;
    private final UserRepository userRepository;
    @Autowired
    public LoanService(LoanRepository loanRepository, UserRepository userRepository){
        this.loanRepository = loanRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public LoanApplyResponse applyLoan(LoanApplyRequest request){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User user = userRepository.findByUsername(username)
                .orElseThrow(()->new UsernameNotFoundException("User not found."));
        if(isLoanLimitExceed(user.getUser_id())){
            throw new RuntimeException("Loan Limit exceed");
        }
        Loan loan = Loan.builder()
                .user(user)
                .amount(request.getAmount())
                .interestRate(request.getInterestRate())
                .tenureMonths(request.getTenureMonths())
                .panNumber(request.getPanNumber())
                .build();
        Loan loanApplied = loanRepository.save(loan);
        return LoanApplyResponse.builder()
                .loan_id(loanApplied.getId())
                .amount(loanApplied.getAmount())
                .interestRate(loanApplied.getInterestRate())
                .tenureMonths(loanApplied.getTenureMonths())
                .status(loanApplied.getStatus())
                .createdAt(loanApplied.getCreatedAt())
                .build();
    }

    private boolean isLoanLimitExceed(Long id){
        int loanCount = loanRepository.countLoanByUserId(id, LoanStatus.REJECTED);
        return loanCount >= 3;
    }

    public GetLoanByUserResponse getLoan(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User user = userRepository.findByUsername(username)
                .orElseThrow(()->new UsernameNotFoundException("User not found"));

        List<LoanApplyResponse> loanList = loanRepository.findAllByUserId(user.getUser_id());

        GetLoanByUserResponse userLoans = GetLoanByUserResponse.builder()
                .username(user.getUsername())
                .email(user.getEmail())
                .loanList(loanList)
                .build();

        return userLoans;
    }

    public GetLoanByIdResponse getLoanById(Long id){
        try {
            Loan loan = loanRepository.findById(id)
                    .orElseThrow(()->new IllegalArgumentException("Found error"));
            User user = loan.getUser();
            UserResponse userResponse = UserResponse.builder()
                    .id(user.getUser_id())
                    .username(user.getUsername())
                    .email(user.getEmail())
                    .role(user.getRole())
                    .active(user.isActive())
                    .created_at(user.getCreated_at().toString())
                    .build();
            return GetLoanByIdResponse.builder()
                    .id(loan.getId())
                    .user(userResponse)
                    .amount(loan.getAmount())
                    .interestRate(loan.getInterestRate())
                    .tenureMonths(loan.getTenureMonths())
                    .panNumber(loan.getPanNumber())
                    .status(loan.getStatus())
                    .rejected_reason(loan.getRejected_reason())
                    .approvedBy(loan.getApprovedBy())
                    .approvedAt(loan.getApprovedAt())
                    .createdAt(loan.getCreatedAt())
                    .build();
        }catch (Exception e){
            throw new RuntimeException("Internal error occur");
        }
    }

    public List<GetAllLoan> getAllLoan() {
        return loanRepository.findAllLoan();
    }
}
