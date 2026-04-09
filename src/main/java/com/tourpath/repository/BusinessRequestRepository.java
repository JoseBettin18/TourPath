package com.tourpath.repository;

import com.tourpath.model.BusinessRequest;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface BusinessRequestRepository extends MongoRepository<BusinessRequest, String> {
    List<BusinessRequest> findByStatusOrderByCreatedAtDesc(BusinessRequest.Status status);

    List<BusinessRequest> findAllByOrderByCreatedAtDesc();

    List<BusinessRequest> findByRequestingUserId(String userId);

    long countByStatus(BusinessRequest.Status status);
}
