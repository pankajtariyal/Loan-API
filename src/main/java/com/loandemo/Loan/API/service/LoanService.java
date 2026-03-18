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

/**
 * Service responsible for managing loan-related business logic.
 *
 * <p>This service handles the complete lifecycle of a loan, including:</p>
 * <ul>
 *     <li>Applying for a new loan</li>
 *     <li>Fetching all loans for a user</li>
 *     <li>Fetching details of a specific loan by ID</li>
 *     <li>Fetching all loans in the system</li>
 * </ul>
 *
 * <p>The service interacts with {@link LoanRepository} and {@link UserRepository}
 * to persist and retrieve loan and user data from the database.</p>
 *
 * @implNote Transactions are managed using {@link Transactional} where necessary.
 * @since 1.0
 */
@Service
public class LoanService {

    private final LoanRepository loanRepository;
    private final UserRepository userRepository;

    /**
     * Constructs a new {@code LoanService} with the given repositories.
     *
     * @param loanRepository repository for Loan entities
     * @param userRepository repository for User entities
     */
    @Autowired
    public LoanService(LoanRepository loanRepository, UserRepository userRepository){
        this.loanRepository = loanRepository;
        this.userRepository = userRepository;
    }

    /**
     * Processes a new loan application for the currently authenticated user.
     *
     * <p>This method performs the following steps:</p>
     * <ol>
     *     <li>Retrieve the currently authenticated user.</li>
     *     <li>Check if the user has exceeded the maximum allowed active loans.</li>
     *     <li>Create a new {@link Loan} entity.</li>
     *     <li>Save the loan to the database.</li>
     *     <li>Return a {@link LoanApplyResponse} with loan details.</li>
     * </ol>
     *
     * @param request the loan application request containing amount, interest rate, tenure, and PAN number
     * @return {@link LoanApplyResponse} containing the applied loan details
     * @throws RuntimeException if the user exceeds loan limit or user is not found
     */
    @Transactional
    public LoanApplyResponse applyLoan(LoanApplyRequest request){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User user = userRepository.findByUsername(username)
                .orElseThrow(()->new UsernameNotFoundException("User not found."));
        if(isLoanLimitExceed(user.getUserId())){
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

    /**
     * Checks if a user has exceeded the maximum allowed active loans.
     *
     * @param userId the ID of the user
     * @return {@code true} if the user has 3 or more active loans, {@code false} otherwise
     */
    private boolean isLoanLimitExceed(Long userId){
        int loanCount = loanRepository.countLoanByUserId(userId, LoanStatus.REJECTED);
        return loanCount >= 3;
    }

    /**
     * Retrieves all loans for the currently authenticated user.
     *
     * @return {@link GetLoanByUserResponse} containing user details and a list of loans
     * @throws UsernameNotFoundException if the user is not found
     */
    public GetLoanByUserResponse getLoan(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User user = userRepository.findByUsername(username)
                .orElseThrow(()->new UsernameNotFoundException("User not found"));

        List<LoanApplyResponse> loanList = loanRepository.findAllByUserId(user.getUserId());

        return GetLoanByUserResponse.builder()
                .username(user.getUsername())
                .email(user.getEmail())
                .loanList(loanList)
                .build();
    }

    /**
     * Retrieves detailed information of a loan by its ID.
     *
     * @param id the ID of the loan
     * @return {@link GetLoanByIdResponse} containing detailed loan and user information
     * @throws RuntimeException if the loan is not found or an internal error occurs
     */
    public GetLoanByIdResponse getLoanById(Long id){
        try {
            Loan loan = loanRepository.findById(id)
                    .orElseThrow(()->new IllegalArgumentException("Loan not found"));
            User user = loan.getUser();
            UserResponse userResponse = UserResponse.builder()
                    .id(user.getUserId())
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
            throw new RuntimeException("Internal error occurred");
        }
    }

    /**
     * Retrieves a list of all loans in the system.
     *
     * @return a list of {@link GetAllLoan} representing all loans
     */
    public List<GetAllLoan> getAllLoan() {
        return loanRepository.findAllLoan();
    }
}
