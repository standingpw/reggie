package com.nervous.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.nervous.entity.AddressBook;
import com.nervous.mapper.AddressBookMapper;
import com.nervous.service.AddressBookService;
import org.springframework.stereotype.Service;

@Service
public class AddressBookServiceImpl extends ServiceImpl<AddressBookMapper, AddressBook> implements AddressBookService {
}
