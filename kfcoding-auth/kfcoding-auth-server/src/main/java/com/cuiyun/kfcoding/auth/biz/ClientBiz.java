package com.cuiyun.kfcoding.auth.biz;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.cuiyun.kfcoding.auth.dao.ClientMapper;
import com.cuiyun.kfcoding.auth.dao.ClientServiceMapper;
import com.cuiyun.kfcoding.auth.entity.Client;
import com.cuiyun.kfcoding.auth.entity.ClientService;
import com.cuiyun.kfcoding.common.base.biz.BaseBiz;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @program: kfcoding-cloud
 * @description: client业务层
 * @author: maple
 * @create: 2018-08-02 17:17
 **/
@Service
public class ClientBiz extends BaseBiz<Client, ClientMapper>{
    @Autowired
    private ClientServiceMapper clientServiceMapper;
    @Autowired
    private ClientServiceBiz clientServiceBiz;

    public List<Client> getClientServices(int id) {
        return this.baseMapper.selectAuthorityServiceInfo(id);
    }

    public void modifyClientServices(int id, String clients) {
        clientServiceMapper.delete(new EntityWrapper<ClientService>().eq("service_id", id));
        if (!StrUtil.isEmpty(clients)) {
            String[] mem = clients.split(",");
            for (String m : mem) {
                ClientService clientService = new ClientService();
                clientService.setServiceId(m);
                clientService.setClientId(id+"");
                clientServiceBiz.insert(clientService);
            }
        }
    }
}
