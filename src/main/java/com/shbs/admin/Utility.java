package com.shbs.admin;

import org.springframework.ui.Model;

public final class Utility {

    private Utility() {
    }

    public static void addAttributesToFormModel(Model model, String pageHeader, String formActionUrl, String buttonValue) {
        model.addAttribute("pageHeader", pageHeader);
        model.addAttribute("formAction", formActionUrl);
        model.addAttribute("buttonValue", buttonValue);
    }
}