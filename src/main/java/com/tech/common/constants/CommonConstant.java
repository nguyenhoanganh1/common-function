package com.tech.common.constants;

public interface CommonConstant {
    String PUBLIC_GROUP = "/public"; // API công khai, không cần xác thực
    String MEMBER_GROUP = "/member"; // API Dành cho user đã đăng nhập
    String PRIVATE_GROUP = "/private"; // API Chỉ dành cho hệ thống nội bộ
    String ADMIN_GROUP = "/admin"; // API Chỉ dành cho admin portal đã đăng nhập
    String PARTNER_GROUP = "/partner"; // API dành cho đối tác
}
