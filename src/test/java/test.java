import com.zhen.SSM.dao.DepartmentMapper;
import com.zhen.SSM.dao.EmployeeMapper;
import com.zhen.SSM.pojo.Department;
import com.zhen.SSM.pojo.Employee;
import org.apache.ibatis.session.SqlSession;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mybatis.generator.api.MyBatisGenerator;
import org.mybatis.generator.config.Configuration;
import org.mybatis.generator.config.xml.ConfigurationParser;
import org.mybatis.generator.exception.XMLParserException;
import org.mybatis.generator.internal.DefaultShellCallback;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RunWith(SpringJUnit4ClassRunner.class)
/**
 * 引入spring-test依赖才有
 * 加载Spring配置文件
 *
 * 配合@RunWith(SpringJUnit4ClassRunner.class)使用
 */
@ContextConfiguration(value = "classpath:applicationContext.xml")
public class test {
    @Test
    public void test1() throws Exception {
        List<String> warnings = new ArrayList<String>();
        boolean overwrite = true;
        File configFile = new File("mbg.xml");
        ConfigurationParser cp = new ConfigurationParser(warnings);
        Configuration config = cp.parseConfiguration(configFile);
        DefaultShellCallback callback = new DefaultShellCallback(overwrite);
        MyBatisGenerator myBatisGenerator = new MyBatisGenerator(config, callback, warnings);
        myBatisGenerator.generate(null);
    }
    @Autowired
    private DepartmentMapper departmentMapper;
    @Autowired
    private SqlSession sqlSession;
    @Autowired
    private EmployeeMapper employeeMapper;
    @Test
    public void test2(){
        //因为员工表和部门表建立了外键，所以得先创建部门信息
//        departmentMapper.insert(new Department(null,"开发部"));
//        departmentMapper.insert(new Department(null,"测试部"));
        EmployeeMapper mapper = sqlSession.getMapper(EmployeeMapper.class);
        for(int i = 0;i<1000;i++){
            String uuid = UUID.randomUUID().toString().substring(0, 5);
            mapper.insert(new Employee(null,uuid,"m",uuid+"@qq.com",5));
        }
        System.out.println("添加成功");
    }
}
