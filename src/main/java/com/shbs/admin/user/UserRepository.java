package com.shbs.admin.user;

import com.shbs.admin.user.model.User;
import com.shbs.common.jpa.Jpa8Repository;

public interface UserRepository extends Jpa8Repository<User, String> {
}
