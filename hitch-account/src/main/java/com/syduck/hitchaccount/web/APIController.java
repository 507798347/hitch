package com.syduck.hitchaccount.web;

import com.syduck.hitchaccount.handler.AccountHandler;
import com.syduck.hitchcommons.domain.vo.response.ResponseVO;
import com.syduck.hitchcommons.groups.Group;
import com.syduck.hitchcommons.initial.annotation.RequestInitial;
import com.syduck.hitchmodules.vo.AccountVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/")
@Api(value = "账户操作Controller", tags = {"账户管理"})
@ApiResponses(@ApiResponse(code = 200, message = "处理成功"))
@RequiredArgsConstructor
public class APIController {

    private final AccountHandler accountHandler;

    @ApiOperation(value = "用户注册接口", tags = {"账户管理"})
    @PostMapping("/register")
    @RequestInitial(groups = {Group.Create.class})
    public ResponseVO<AccountVO> register(@Validated(Group.Create.class) @RequestBody AccountVO accountVO) {
        return accountHandler.register(accountVO);
    }
}
