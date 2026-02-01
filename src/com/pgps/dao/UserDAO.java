package com.pgps.dao;

import com.pgps.model.User;

public interface UserDAO {
    User findById(int userId);
}
