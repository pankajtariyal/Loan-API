package com.loandemo.Loan.API.repository;

import com.loandemo.Loan.API.modul.Document;
import com.loandemo.Loan.API.status.DocumentStatus;
import com.loandemo.Loan.API.status.DocumentType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository interface for managing {@link Document} entities.
 *
 * <p>This interface extends {@link JpaRepository} to provide
 * basic CRUD operations along with custom query methods
 * for document-related business logic.</p>
 *
 * <p>Main Responsibilities:</p>
 * <ul>
 *     <li>Fetch documents by loan and type</li>
 *     <li>Count documents based on status</li>
 *     <li>Check existence of documents with specific conditions</li>
 *     <li>Delete documents associated with a loan</li>
 * </ul>
 *
 * <p>Used in document upload, verification, and loan approval workflows.</p>
 *
 * @since 1.0
 * @author Abhishek Tadiwal
 */
@Repository
public interface DocumentRepository extends JpaRepository<Document, Long> {

    /**
     * Counts all documents for a given loan ID excluding rejected documents.
     *
     * <p>This method is typically used to check whether all required
     * documents are uploaded (ignoring rejected ones).</p>
     *
     * @param id loan ID
     * @return number of non-rejected documents
     */
    @Query("select count(d) from Document d where d.loan.id=:id and d.status <> 'REJECTED'")
    int countByLoanIdAndStatus(@Param("id") Long id);

    /**
     * Counts documents for a given loan ID with a specific status.
     *
     * <p>Used for determining verification progress such as:
     * <ul>
     *     <li>Number of VERIFIED documents</li>
     *     <li>Number of REJECTED documents</li>
     * </ul>
     * </p>
     *
     * @param id loan ID
     * @param status document status ({@link DocumentStatus})
     * @return count of documents with the given status
     */
    @Query("select count(d) from Document d where d.loan.id=:id and d.status <> :status")
    int countByLoanIdAndStatus(@Param("id") Long id,@Param("status") DocumentStatus status);

    /**
     * Finds a document by loan ID and document type.
     *
     * <p>This method is used to check if a specific document type
     * (e.g., PAN, ID_PROOF) already exists for a loan.</p>
     *
     * @param loanId loan ID
     * @param documentType type of document ({@link DocumentType})
     * @return optional document if found
     */
    Optional<Document> findByLoanIdAndDocumentType(Long loanId, DocumentType documentType);

    /**
     * Checks whether a document exists for a given loan and document type,
     * excluding documents with a specific status.
     *
     * <p>Commonly used to ensure that duplicate documents are not uploaded
     * unless the previous one is rejected.</p>
     *
     * @param loanId loan ID
     * @param type document type
     * @param status status to exclude (e.g., REJECTED)
     * @return true if such document exists, false otherwise
     */
    @Query("SELECT CASE WHEN COUNT(d) > 0 THEN true ELSE false END FROM Document d " +
            "WHERE d.loan.id = :loanId AND d.documentType = :type AND d.status <> :status")
    boolean existsByLoanIdAndStatusNot(@Param("loanId") Long loanId,
                                       @Param("type") DocumentType type,
                                       @Param("status") DocumentStatus status);


    /**
     * Finds a document by its ID and associated loan ID.
     *
     * <p>This ensures that the document belongs to the specified loan,
     * adding an extra layer of validation/security.</p>
     *
     * @param documentId document ID
     * @param loanId loan ID
     * @return optional document if found
     */
    Optional<Document> findByIdAndLoanId(Long documentId, Long loanId);

    /**
     * Deletes all documents associated with a specific loan.
     *
     * <p>Used when a loan is deleted or reset.</p>
     *
     * @param loanId loan ID
     */
    void deleteByLoanId(Long loanId);

//    @Query("SELECT new com.loandemo.Loan.API.dto.document.AllDocumentResponse(d.id,d.loan,d.status,d.rejected_reason,d.uploaded_at) FROM Document d")
//    List<AllDocumentResponse> findAllDocument();
}
