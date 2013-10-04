package org.ksug.springcamp.testmvc.integration.page.user

import geb.Page

class Form extends Page{

    static url = "/user/new"
//    static at = { title == "Sample App Sign-up Page" }
    static content = {
        userForm { $("form[id=userForm]") }
        nameField { $("input[name=name]") }
        ageField { $("input[name=age]") }
        sexField { $("input[name=sex]") }
        submitButton(to: UserList) { $("button[type=submit]") }
    }
}
