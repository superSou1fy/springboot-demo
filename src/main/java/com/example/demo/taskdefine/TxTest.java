package com.example.demo.taskdefine;

import com.example.demo.mapper.SaRegionInfoMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Component
@Slf4j
public class TxTest {

    @Autowired
    private SaRegionInfoMapper saRegionInfoMapper;

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public int update(int id, int state) {
        int r = saRegionInfoMapper.updateById(id, state);
        log.info("effiction rows {}", r);
        return r;
    }
}
