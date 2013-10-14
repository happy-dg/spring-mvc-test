package org.ksug.springcamp.testmvc.integration.page.user

import geb.Page

class UserList extends Page{
    static url = "/user"
    static at = { title == "Spring-MVC-Test" }
    static content = {
        header {}
    }
}
