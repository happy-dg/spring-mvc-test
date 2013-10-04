package org.ksug.springcamp.testmvc.integration.page.user

import geb.Page

class UserList extends Page{
    static url = "/user"
    static at = {}
    static content = {
        header {$("h3").text()}
    }
}
