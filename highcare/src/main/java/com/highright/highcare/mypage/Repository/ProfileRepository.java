package com.highright.highcare.mypage.Repository;

import com.highright.highcare.mypage.dto.MyProfileDTO;
import com.highright.highcare.mypage.entity.MyProfile;
import com.highright.highcare.mypage.entity.MyProfileFile;
import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface ProfileRepository extends JpaRepository<MyProfile, Integer> {

   List<MyProfile> findByEmpNo(int empNo);
}
