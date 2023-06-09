package com.paw.Model.UserManagementService.Services;

import com.paw.Exceptions.UnprocessableReq;
import com.paw.Exceptions.UserAlreadyExists;
import com.paw.Exceptions.UserNotFound;
import com.paw.Model.UserManagementService.Entities.Role;
import com.paw.Model.UserManagementService.Entities.UserRole;
import com.paw.Model.UserManagementService.Repositories.RoleRepository;
import com.paw.Model.UserManagementService.Repositories.UserInfoRepository;
import com.paw.Model.UserManagementService.Repositories.UserRepository;
import com.paw.Model.UserManagementService.Entities.InfoUser;
import com.paw.Model.UserManagementService.Entities.User;
import com.paw.Model.UserManagementService.Repositories.UserRolesRepository;
import com.paw.View.NewUserDTO;
import com.paw.View.RoleInputDTO;
import com.paw.View.UpdateUserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class UserService {
    @Autowired
    UserRepository userRepository;

    @Autowired
    UserInfoRepository userInfoRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    UserRolesRepository userRolesRepository;

    Integer gregorianYearStart = 1900;
    Integer gregorianMonthStart = 1;

    public User addUser(NewUserDTO newUserDTO)
    {
        if(userRepository.findUserByUsername(newUserDTO.getUsername()) != null)
        {
            throw new UserAlreadyExists("Exista deja un utilizator cu date similare!");
        }

        try {
            if(newUserDTO.getInfoUserDTO() == null)
            {
                throw new UnprocessableReq("Details about user not found!");
            }

            User newUser = new User(newUserDTO.getUsername(), newUserDTO.getPassword());
            userRepository.save(newUser);

            String[] data = newUserDTO.getInfoUserDTO().getData_nasterii().split("-");
            if(data.length < 3)
            {
                throw new UnprocessableReq("Unprocessable request body!");
            }
            Date data_nasterii = new Date(Integer.parseInt(data[0]) - gregorianYearStart,
                                        Integer.parseInt(data[1]) - gregorianMonthStart,
                                        Integer.parseInt(data[2]) );

            InfoUser infoUser = new InfoUser(
                    newUser.getId(),
                    newUserDTO.getInfoUserDTO().getNume(),
                    newUserDTO.getInfoUserDTO().getPrenume(),
                    data_nasterii,
                    newUserDTO.getInfoUserDTO().getGrupaSange(),
                    newUserDTO.getInfoUserDTO().getTelefon(),
                    newUserDTO.getInfoUserDTO().getEmail(),
                    newUserDTO.getInfoUserDTO().getInaltime(),
                    newUserDTO.getInfoUserDTO().getGreutate(),
                    0
                    );
            userInfoRepository.save(infoUser);
            newUser.setInfoUser(infoUser);

            List<Role> roles = new ArrayList<>();
            for(RoleInputDTO role:newUserDTO.getRoles())
            {
                roles.add(roleRepository.findRoleByRoleName(role.getRole_name()));
            }
            newUser.setRoles(roles);
            userRepository.save(newUser);

            return newUser;
        }
        catch (DataAccessException dataAccessException)
        {
            this.deleteUserByUsername(newUserDTO.getUsername());
            throw dataAccessException;
        }
        catch (IllegalArgumentException illegalArgumentException)
        {
            this.deleteUserByUsername(newUserDTO.getUsername());
            throw new UnprocessableReq("Unprocessable request body!");
        }
        catch (NullPointerException nullPointerException)
        {
            this.deleteUserByUsername(newUserDTO.getUsername());
            throw new UnprocessableReq("Details about user not found!");
        }
        catch (RuntimeException runtimeException)
        {
            System.out.println(runtimeException.getMessage());
            this.deleteUserByUsername(newUserDTO.getUsername());
            throw runtimeException;
        }
    }

    public User updateUser(UpdateUserDTO updateUserDTO, Integer id)
    {
        User user = userRepository.findUserById(id);
        if(user == null)
        {
            throw new UserNotFound("User not found!");
        }

        if(updateUserDTO.getInfoUserDTO() == null)
        {
            throw new UnprocessableReq("Details about user not found!");
        }

        try {
            user.setUsername(updateUserDTO.getUsername());
            user.setPassword(updateUserDTO.getPassword());

            InfoUser newUserInfo = new InfoUser(updateUserDTO.getInfoUserDTO());
            newUserInfo.setId(user.getId());

            String[] data = updateUserDTO.getInfoUserDTO().getData_nasterii().split("-");
            if(data.length < 3)
            {
                throw new UnprocessableReq("Unprocessable request body!");
            }
            Date data_nasterii = new Date(Integer.parseInt(data[0]) - gregorianYearStart,
                    Integer.parseInt(data[1]) - gregorianMonthStart,
                    Integer.parseInt(data[2]) );

            newUserInfo.setData_nasterii(data_nasterii);
            userInfoRepository.save(newUserInfo);

            user.setInfoUser(newUserInfo);
            userRepository.save(user);
            return user;
        }
        catch (DataAccessException dataAccessException)
        {
            this.deleteUserByUsername(updateUserDTO.getUsername());
            throw dataAccessException;
        }
        catch (IllegalArgumentException illegalArgumentException)
        {
            this.deleteUserByUsername(updateUserDTO.getUsername());
            throw new UnprocessableReq("Unprocessable request body!");
        }
        catch (NullPointerException nullPointerException)
        {
            this.deleteUserByUsername(updateUserDTO.getUsername());
            throw new UnprocessableReq("Details about user not found!");
        }
        catch (RuntimeException runtimeException)
        {
            System.out.println(runtimeException.getMessage());
            this.deleteUserByUsername(updateUserDTO.getUsername());
            throw runtimeException;
        }
    }

    public List<User> getUsers()
    {
        List<User> users = userRepository.findAll();
        return users;
    }

    public User getUserById(Integer id)
    {
        User user = userRepository.findUserById(id);

        if(user != null)
            return user;
        else
            throw new UserNotFound("User-ul cu id-ul " + id.toString() + " nu a fost gasit!");
    }

    public User getUserByUsername(String username)
    {
        User user = userRepository.findUserByUsername(username);
        if(user != null)
            return user;
        else
            throw new UserNotFound("User-ul cu username-ul " + username + " nu a fost gasit!");
    }

    public User deleteUserByUsername(String username)
    {
        User user = userRepository.findUserByUsername(username);
        if(user != null)
        {
            userInfoRepository.delete(user.getInfoUser());
            userRepository.delete(user);
            return user;
        }
        else
            throw new UserNotFound("User-ul cu username-ul " + username + " nu a fost gasit!");
    }

    public User deleteUserById(Integer id)
    {
        User user = userRepository.findUserById(id);
        if(user != null)
        {
            userInfoRepository.delete(user.getInfoUser());
            userRepository.delete(user);
            return user;
        }
        else
            throw new UserNotFound("User-ul cu id-ul " + id.toString() + " nu a fost gasit!");
    }

    public User loginUser(String username, String password)
    {
        User user = userRepository.findUserByUsernameAndPassword(username, password);
        if(user != null)
            return user;
        else
            throw new UserNotFound("User not found!");

    }
}
