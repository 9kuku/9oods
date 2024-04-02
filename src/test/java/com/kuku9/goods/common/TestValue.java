package com.kuku9.goods.common;

import com.kuku9.goods.domain.product.entity.*;
import com.kuku9.goods.domain.seller.dto.request.*;
import com.kuku9.goods.domain.seller.entity.*;
import com.kuku9.goods.domain.user.entity.*;

public class TestValue {

    public final static Long TEST_USER_ID1 = 1L;
    public final static String TEST_USERNAME1 = "이메일1@이메일1.com";
    public final static String TEST_REALNAME1 = "이름";
    public final static String TEST_PASSWORD1 = "!password1";
    public final static String TEST_ADMINCODE1 = "";
    public final static UserRoleEnum TEST_ROLE1 = UserRoleEnum.USER;
    public final static User TEST_USER1 = new User(
        TEST_USER_ID1,
        TEST_USERNAME1,
        TEST_REALNAME1,
        TEST_PASSWORD1,
        TEST_ADMINCODE1,
        TEST_ROLE1);

    public final static Long TEST_USER_ID2 = 2L;
    public final static String TEST_USERNAME2 = "이메일2@이메일2.com";
    public final static String TEST_REALNAME2 = "이름이름";
    public final static String TEST_PASSWORD2 = "!password2";
    public final static String TEST_ADMINCODE2 = "";
    public final static UserRoleEnum TEST_ROLE2 = UserRoleEnum.USER;
    public final static User TEST_USER2 = new User(
        TEST_USER_ID2,
        TEST_USERNAME2,
        TEST_REALNAME2,
        TEST_PASSWORD2,
        TEST_ADMINCODE2,
        TEST_ROLE2);

    public final static Long TEST_USER_ID3 = 3L;
    public final static String TEST_USERNAME3 = "이메일3@이메일3.com";
    public final static String TEST_REALNAME3 = "이름이름이름";
    public final static String TEST_PASSWORD3 = "!password3";
    public final static String TEST_ADMINCODE3 = "";
    public final static UserRoleEnum TEST_ROLE3 = UserRoleEnum.USER;
    public final static User TEST_USER3 = new User(
        TEST_USER_ID3,
        TEST_USERNAME3,
        TEST_REALNAME3,
        TEST_PASSWORD3,
        TEST_ADMINCODE3,
        TEST_ROLE3);
    public final static User TEST_SELLER_USER = TEST_USER3;
    public final static Seller TEST_SELLER = new Seller(
        TEST_SELLER_ID1,
        TEST_SELLER_BRANDNAME1,
        TEST_SELLER_DOMAINNAME1,
        TEST_SELLER_INTRODUCE1,
        TEST_SELLER_EMAIL1,
        TEST_SELLER_PHONENEMBER,
        TEST_SELLER_USER);
    public final static Product TEST_PRODUCT = new Product(
        TEST_PRODUCT_ID1,
        TEST_SELLER,
        TEST_PRODUCT_NAME,
        TEST_PRODUCT_DESCRIPTION,
        TEST_PRODUCT_PRICE,
        true);
    public final static Long TEST_SELLER_ID1 = 1L;
    public final static String TEST_SELLER_BRANDNAME1 = "brand1";
    public final static String TEST_SELLER_DOMAINNAME1 = "domain1";
    public final static String TEST_SELLER_INTRODUCE1 = "introduce1";
    public final static String TEST_SELLER_EMAIL1 = "이메일1@이메일1.com";
    public final static String TEST_SELLER_PHONENEMBER = "00000001";
    public final static Long TEST_PRODUCT_ID1 = 1L;
    public final static String TEST_PRODUCT_NAME = "상품1";
    public final static String TEST_PRODUCT_DESCRIPTION = "상품 설명1";
    public final static Long TEST_PRODUCT_PRICE = 100000L;
    public final static Boolean TEST_PRODUCT_STATE = true;
    public final static String TEST_REQUEST_PRODUCT_NAME = TEST_PRODUCT_NAME;
    public final static String TEST_REQUEST_PRODUCT_DESCRIPTION = TEST_PRODUCT_DESCRIPTION;
    public final static Long TEST_REQUEST_PRODUCT_PRICE = TEST_PRODUCT_PRICE;
    public final static Long TEST_SELLER_ID = TEST_SELLER_ID1;
    public final static ProductRegistRequest TEST_PRODUCT_REGIST_REQUEST =
        new ProductRegistRequest(
            TEST_REQUEST_PRODUCT_NAME,
            TEST_REQUEST_PRODUCT_DESCRIPTION,
            TEST_REQUEST_PRODUCT_PRICE,
            TEST_SELLER_ID);
}