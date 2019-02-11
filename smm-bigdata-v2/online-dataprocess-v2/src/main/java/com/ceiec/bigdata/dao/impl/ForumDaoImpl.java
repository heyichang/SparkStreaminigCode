package com.ceiec.bigdata.dao.impl;

import com.ceiec.bigdata.dao.ForumDao;
import com.ceiec.bigdata.dao.common.AbstractDaoImpl;
import com.ceiec.bigdata.entity.forum.Forum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by heyichang on 2017/11/7.
 */
public class ForumDaoImpl extends AbstractDaoImpl<Forum> implements ForumDao {

    private static final Logger logger = LoggerFactory.getLogger(ForumDaoImpl.class);

    public ForumDaoImpl(Forum forum) {
        super.obj = forum;
        setClazz(Forum.class);
    }

    public ForumDaoImpl() {
        setClazz(Forum.class);
    }



}
