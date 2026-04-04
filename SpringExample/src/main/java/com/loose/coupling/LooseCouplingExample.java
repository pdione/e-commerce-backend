package com.loose.coupling;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class LooseCouplingExample {

    public static void main(String[] args) {

        ApplicationContext context = new ClassPathXmlApplicationContext("applicationCouplingContext.xml");
        UserManager userManagerWithDB = (UserManager) context.getBean("userManagerWithDB");
        UserManager userManagerWithWebservice = (UserManager) context.getBean("userManagerWithWebservice");
        UserManager userManagerWithNewDB = (UserManager) context.getBean("userManagerWithNewDB");
        System.out.println(userManagerWithDB.getUserInfo());
        System.out.println(userManagerWithWebservice.getUserInfo());
        System.out.println(userManagerWithNewDB.getUserInfo());

//        UserDataProvider userDataProvider = new UserDatabaseProvider();
//        UserManager userManagerWithDB = new UserManager(userDataProvider);
//        String userInfo = userManagerWithDB.getUserInfo();
//        System.out.println(userInfo);
//
//        UserDataProvider webserviceProvider = new WebServiceDataProvider();
//        UserManager userManagerWithWebservice = new UserManager(webserviceProvider);
//        String userInfoFromWebservice = userManagerWithWebservice.getUserInfo();
//        System.out.println(userInfoFromWebservice);
//
//        UserDataProvider newDBProvider = new NewDatabaseProvider();
//        UserManager userManagerWithNewDB = new UserManager(newDBProvider);
//        String userInfoFromNewDB = userManagerWithNewDB.getUserInfo();
//        System.out.println(userInfoFromNewDB);
    }
}
