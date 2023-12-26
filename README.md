# compentency-tool

1.Create User:
	POST localhost:8080/api/user
	   request body = {
	    "name":"Ankit",
	    "email":"Ankit@tarento.com",
	    "phone_num":"9677896757"
	}

2.GetAllUser:
   GET localhost:8080/api/user
   
3.UpdateUser
  PUT localhost:8080/api/user/1
	  requestbody = {
	        "id": 1,
	        "name": "samynathan",
	        "email": "samynathan@gmail.com",
	        "phone_num": 321
	    }

4.Delete User
    DELETE localhost:8080/api/user/1
    
    
