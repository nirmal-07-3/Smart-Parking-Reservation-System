package com.smartparking.dao;

import com.smartparking.config.JPAUtil;
import com.smartparking.entity.Vehicle;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import java.util.List;

public class VehicleDAOImpl implements VehicleDAO {

    @Override
    public Vehicle save(Vehicle vehicle) {
        EntityManager em = JPAUtil.getEntityManager();
        EntityTransaction transaction = em.getTransaction();

        try {
            transaction.begin();
            em.persist(vehicle);
            transaction.commit();
            return vehicle;
        } catch (RuntimeException e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            throw e;
        } finally {
            em.close();
        }
    }

    @Override
    public Vehicle findById(Long id) {
        EntityManager em = JPAUtil.getEntityManager();

        try {
            return em.find(Vehicle.class, id);
        } finally {
            em.close();
        }
    }

    @Override
    public List<Vehicle> findByUserId(Long userId) {
        EntityManager em = JPAUtil.getEntityManager();

        try {
            return em.createQuery(
                            "SELECT v FROM Vehicle v WHERE v.userId = :userId ORDER BY v.id DESC",
                            Vehicle.class)
                    .setParameter("userId", userId)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    @Override
    public boolean existsByVehicleNumber(String vehicleNumber) {
        EntityManager em = JPAUtil.getEntityManager();

        try {
            Long count = em.createQuery(
                            "SELECT COUNT(v) FROM Vehicle v WHERE UPPER(v.vehicleNumber) = :number",
                            Long.class)
                    .setParameter("number", vehicleNumber.trim().toUpperCase())
                    .getSingleResult();

            return count > 0;
        } finally {
            em.close();
        }
    }

    @Override
    public void update(Vehicle vehicle) {
        EntityManager em = JPAUtil.getEntityManager();
        EntityTransaction transaction = em.getTransaction();

        try {
            transaction.begin();
            em.merge(vehicle);
            transaction.commit();
        } catch (RuntimeException e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            throw e;
        } finally {
            em.close();
        }
    }

    @Override
    public boolean delete(Long id, Long userId) {
        EntityManager em = JPAUtil.getEntityManager();
        EntityTransaction transaction = em.getTransaction();

        try {
            transaction.begin();

            List<Vehicle> vehicles = em.createQuery(
                            "SELECT v FROM Vehicle v WHERE v.id = :id AND v.userId = :userId",
                            Vehicle.class)
                    .setParameter("id", id)
                    .setParameter("userId", userId)
                    .setMaxResults(1)
                    .getResultList();

            if (vehicles.isEmpty()) {
                transaction.rollback();
                return false;
            }

            em.remove(vehicles.get(0));
            transaction.commit();
            return true;
        } catch (RuntimeException e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            throw e;
        } finally {
            em.close();
        }
    }
}