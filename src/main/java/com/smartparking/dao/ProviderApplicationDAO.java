package com.smartparking.dao;

import com.smartparking.entity.ProviderApplication;
import java.util.List;
import java.util.Optional;

public interface ProviderApplicationDAO {

    ProviderApplication save(ProviderApplication application);

    Optional<ProviderApplication> findById(Long id);

    List<ProviderApplication> findByProviderId(Long providerId);

    List<ProviderApplication> findByStatus(ProviderApplication.Status status);

    ProviderApplication update(ProviderApplication application);
}