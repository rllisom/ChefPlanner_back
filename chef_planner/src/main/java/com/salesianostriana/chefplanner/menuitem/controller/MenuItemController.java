package com.salesianostriana.chefplanner.menuitem.controller;

import com.salesianostriana.chefplanner.menuitem.service.MenuItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/menuitem")
@RequiredArgsConstructor
public class MenuItemController {


    private final MenuItemService menuItemService;





}
