package com.aomc.coop.dao;

import com.aomc.coop.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
@Transactional
public class UserDao {

    private JdbcTemplate jdbcTemplate;
    // Jdbc = Java Database Connectivity

    @Autowired
// *** 이건 왜 있는 걸까?
    public void setDataSource(DataSource dataSource) {
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public User getUser(String userId)
    {

        String sqlStatement = "SELECT * FROM user WHERE userId = ?";

        return (jdbcTemplate.queryForObject(sqlStatement,
                new Object[] { userId },
                new RowMapper<User>()
                {
// *** return() 안의 jdbcTemplate.queryForObject문을 밖으로 뺄 수는 없을까? + 코드 중복 해결 -> 코드 리팩토링
// ***** 3번째 parameter new RowMapper<User>() 이놈은 반드시 밖으로 빼야함 -> 중복 해결 -> 코드 리팩토링

//			        Query given SQL to create a prepared statement from SQL and a list of arguments to bind to the query, mapping a single result row to a Java object via a RowMapper.
//
//					<Parameters>
//					1. sql - the SQL query to execute
//					2. args - arguments to bind to the query
//					3. rowMapper - object that will map one object per row
//					<Returns>
//					the single mapped object (may be null if the given RowMapper returned null)
                    @Override
                    public User mapRow(ResultSet rs, int rowNum) throws SQLException
                    {
                        User user = new User();

                        user.setName(rs.getString("name"));
                        user.setPassword(rs.getString("password"));
                        user.setUserId(rs.getString("userId"));
                        user.setEmail(rs.getString("email"));
                        user.setSalt(rs.getString("salt"));
                        user.setReg_date(rs.getTimestamp("reg_date"));
                        user.setAccess_date(rs.getTimestamp("access_date"));
                        user.setUpdate_date(rs.getTimestamp("update_date"));
                        user.setRole(rs.getNString("role"));
                        user.setStatus(rs.getString("status"));

                        return user;
                    }
                }));
    }

    public boolean insertUser(User user) {
        String name = user.getName();
        String userId = user.getUserId();
        String password = user.getPassword();
        String email = user.getEmail();
        String salt = user.getSalt();
        String role = user.getRole();
        String status = user.getStatus();

        String sqlStatement = "INSERT INTO user (name, userId, password, email, salt, role, status) values(?,?,?,?,?,?,?)";

        return (jdbcTemplate.update(sqlStatement,
                new Object[] { name, userId, password, email, salt, role, status}) == 1);
// ***   == 1로 끝내는 return 문법을 정확히 이해하자
    }

    public List<User> getAllUsers() {
        String sqlStatement = "SELECT * FROM user";

        return jdbcTemplate.query(sqlStatement,
                new RowMapper<User>() {
// *** 이 RowMapper도 밖으로 빼기
            @Override
            public User mapRow(ResultSet rs, int rowNum) throws SQLException {

                User user = new User();

                user.setName(rs.getString("name"));
                user.setUserId(rs.getString("userId"));
                //user.setPassword(rs.getString("password"));
                user.setEmail(rs.getString("email"));
                //user.setSalt(rs.getString("salt"));
                //user.setReg_date(rs.getTimestamp("reg_date"));
                //user.setAccess_date(rs.getTimestamp("access_date"));
                //user.setUpdate_date(rs.getTimestamp("update_date"));
                //user.setRole(rs.getNString("role"));
                //user.setStatus(rs.getString("status"));

                return user;
            }
        });
    }

    public boolean deleteUser(String userId) {

        String sqlStatement = "DELETE FROM user WHERE userId = ?";
        return (jdbcTemplate.update(sqlStatement, new Object[] { userId }) == 1);
    }

    public boolean updateAccess_date(User user) {
// *** parameter : (String userId)로 바꾸자

        String sqlStatement = "UPDATE user set access_date = now() WHERE userId = ?";
        return (jdbcTemplate.update(sqlStatement, new Object[] { user.getUserId() }) == 1);
    }
}
