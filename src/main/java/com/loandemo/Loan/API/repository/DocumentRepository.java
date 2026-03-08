package com.loandemo.Loan.API.repository;

import com.loandemo.Loan.API.modul.Document;
import com.loandemo.Loan.API.status.DocumentStatus;
import com.loandemo.Loan.API.status.DocumentType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DocumentRepository extends JpaRepository<Document, Long> {

    @Query("select count(d) from Document d where d.loan.id=:id and d.status <> 'REJECTED'")
    int countByLoanIdAndStatus(@Param("id") Long id);

    @Query("select count(d) from Document d where d.loan.id=:id and d.status = :status")
    int countByLoanIdAndStatus(@Param("id") Long id,@Param("status") DocumentStatus status);

    Optional<Document> findByLoanIdAndDocumentType(Long loanId, DocumentType documentType);

    @Query("SELECT CASE WHEN COUNT(d) > 0 THEN true ELSE false END FROM Document d " +
            "WHERE d.loan.id = :loanId AND d.documentType = :type AND d.status <> :status")
    boolean existsByLoanIdAndStatusNot(@Param("loanId") Long loanId,
                                       @Param("type") DocumentType type,
                                       @Param("status") DocumentStatus status);


    Optional<Document> findByIdAndLoanId(Long documentId, Long loanId);

    void deleteByLoanId(Long loanId);

//    @Query("SELECT new com.loandemo.Loan.API.dto.document.AllDocumentResponse(d.id,d.loan,d.status,d.rejected_reason,d.uploaded_at) FROM Document d")
//    List<AllDocumentResponse> findAllDocument();
}
