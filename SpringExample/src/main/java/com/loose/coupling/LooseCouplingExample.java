package com.loose.coupling;

public class LooseCouplingExample {

    public static void main(String[] args) {
        UserDataProvider userDataProvider = new UserDatabaseProvider();
        UserManager userManagerWithDB = new UserManager(userDataProvider);
        String userInfo = userManagerWithDB.getUserInfo();
        System.out.println(userInfo);

        UserDataProvider webserviceProvider = new WebServiceDataProvider();
        UserManager userManagerWithWebservice = new UserManager(webserviceProvider);
        String userInfoFromWebservice = userManagerWithWebservice.getUserInfo();
        System.out.println(userInfoFromWebservice);

        UserDataProvider newDBProvider = new NewDatabaseProvider();
        UserManager userManagerWithNewDB = new UserManager(newDBProvider);
        String userInfoFromNewDB = userManagerWithNewDB.getUserInfo();
        System.out.println(userInfoFromNewDB);
    }
}
