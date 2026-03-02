package egate.digital.fasotour.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import egate.digital.fasotour.model.Avis;
import egate.digital.fasotour.model.Guide;

public interface GuideRepository extends JpaRepository<Guide, Long> {

}
