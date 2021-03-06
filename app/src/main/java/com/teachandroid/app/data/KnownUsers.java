package com.teachandroid.app.data;

import android.content.Intent;

import com.teachandroid.app.LoaderApplication;
import com.teachandroid.app.api.ApiFacadeService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Created by George on 10.03.2015.
 */
public class KnownUsers {

    private static KnownUsers instance ;
    private static Map<Long,User> knownUsers;

    private KnownUsers() {
        knownUsers = new HashMap<Long, User>();
        knownUsers.put(1L,new User(1L));
    }

    public static KnownUsers getInstance(){
        if (instance==null) {instance=new KnownUsers();}
        return instance;
    }

    public static void addUser (Long id, User user){
        knownUsers.put(id,user);
    }

    public static User getUserFromId (Long id){
        User returnUser;
        returnUser = knownUsers.get(id);
        if (returnUser==null){
            returnUser = knownUsers.get(1L);

            Intent intent = new Intent(LoaderApplication.getContext(), ApiFacadeService.class);
            intent.putExtra(ApiFacadeService.EXTRA_MAIN_COMMAND, "users.get");
            HashMap<String, String> parameters = new HashMap<String, String>();
            parameters.put("user_ids", ""+id);
            parameters.put("fields", "photo_50,photo_100,photo_200");
            intent.putExtra(ApiFacadeService.EXTRA_PARAMETERS, parameters);
            intent.putExtra(ApiFacadeService.EXTRA_RETURNED_BROADCAST_MESSAGE, ApiFacadeService.BROADCAST_USER);
            intent.putExtra(ApiFacadeService.EXTRA_RETURNED_CLASS_NAME, ApiFacadeService.RETURNED_TYPE_USER);

            LoaderApplication.getContext().startService(intent);
        }
        return returnUser;
    }

    public static Set<Long> getAllKnownUsers(){
        Set<Long> resultSet = knownUsers.keySet();
        resultSet.remove(1L);
        return resultSet;
    }
}
