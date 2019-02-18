package com.dyniamic.demo.mapper;

import com.dyniamic.demo.model.SaRegionInfo;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;

public interface SaRegionInfoMapper {
    int deleteByPrimaryKey(BigDecimal id);

    int insert(SaRegionInfo record);

    int insertSelective(SaRegionInfo record);

    SaRegionInfo selectByPrimaryKey(BigDecimal id);

    int updateByPrimaryKeySelective(SaRegionInfo record);

    int updateByPrimaryKey(SaRegionInfo record);

    List<SaRegionInfo> selectData(@Param("code") int code, @Param("state") int state);

    int updateById(@Param("id") int code, @Param("state") int state);

    int updateByState(@Param("id") int code, @Param("state") int state);

    int test();
}