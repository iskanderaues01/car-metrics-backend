package com.energo.car_metrics.repositories;

import com.energo.car_metrics.models.Car;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CarRepository extends JpaRepository<Car, Long> {

}
