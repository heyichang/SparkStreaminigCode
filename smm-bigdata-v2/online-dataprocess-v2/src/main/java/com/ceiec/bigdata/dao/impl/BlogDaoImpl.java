package com.ceiec.bigdata.dao.impl;

import com.ceiec.bigdata.dao.BlogDao;
import com.ceiec.bigdata.dao.common.AbstractDaoImpl;
import com.ceiec.bigdata.entity.blog.Blog;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by heyichang on 2017/11/7.
 */
public class BlogDaoImpl extends AbstractDaoImpl<Blog> implements BlogDao {
    private static final Logger logger = LoggerFactory.getLogger(BlogDaoImpl.class);

    public BlogDaoImpl() {
        setClazz(Blog.class);
    }


    public BlogDaoImpl(Blog blog) {
        //this.blog = blog;
        super.obj = blog;
        setClazz(Blog.class);
    }



}
