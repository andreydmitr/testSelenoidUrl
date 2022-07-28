package models;

import io.qameta.allure.internal.shadowed.jackson.annotation.JsonIgnoreProperties;
import io.qameta.allure.internal.shadowed.jackson.annotation.JsonProperty;
import org.json.JSONPropertyIgnore;

public class ReqresModels {

    /*
    "data": {
        "id": 2,
        "email": "janet.weaver@reqres.in",
        "first_name": "Janet",
        "last_name": "Weaver",
        "avatar": "https://reqres.in/img/faces/2-image.jpg"
    },
     */

    @JsonIgnoreProperties(ignoreUnknown=true)
    public class User{
        private Long id;
        private String email;
        @JsonProperty("firstName")
        private String firstName;
        @JsonProperty("lastName")
        private String lastName;

        public Long getId(){return this.id;}
        public void setId(Long id){this.id=id;}
        public String getEmail(){return this.email;}
        public void setEmail(String email){this.email=email;}
        public String getFirstName(){return this.firstName;}
        public void setFirstName(String firstName){this.firstName=firstName;}
        public String getLastName(){return this.lastName;}
        public void setLastName(String lastName){this.lastName=lastName;}
    }

    @JsonIgnoreProperties(ignoreUnknown=true)
    public class UserData{
        private User data;

        public User getData(){return this.data;}
        public void setData(User data){this.data=data;}

    }
}
