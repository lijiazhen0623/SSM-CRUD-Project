import com.github.pagehelper.PageInfo;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

//声明此注解才能拿到ioc容器
@WebAppConfiguration
@RunWith(SpringJUnit4ClassRunner.class)
//Spring和SpringMVC的配置文件都要加载
@ContextConfiguration(value ={"classpath:applicationContext.xml","classpath:SpringMVC-config.xml"} )
public class MVCtest {

    //自动注入ioc容器
    @Autowired
    private WebApplicationContext context;

    private MockMvc mockMvc;
    //在测试方法执行前执行，对mockMvc初始化
    @Before
    public void init(){
        //建立一个虚拟请求
        mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
    }
    @Test
    public void test() throws Exception {
        //模拟请求拿到返回值
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/").param("pn", "2")).andReturn();
        //获取请求域
        MockHttpServletRequest request = result.getRequest();
        PageInfo pageInfo = (PageInfo) request.getAttribute("pageInfo");
        System.out.println("当前页"+pageInfo.getPageNum());
        System.out.println("总页数"+pageInfo.getPages());
        System.out.println("数据个数"+pageInfo.getTotal());
        boolean isFirstPage = pageInfo.isIsFirstPage();
        boolean isLastPage = pageInfo.isIsLastPage();
        int navigateLastPage = pageInfo.getNavigateLastPage();
        for (int navigatepageNum : pageInfo.getNavigatepageNums()) {
            System.out.println(navigatepageNum);
        }

        for (Object o : pageInfo.getList()) {
            System.out.println(o);
        }

    }
}
