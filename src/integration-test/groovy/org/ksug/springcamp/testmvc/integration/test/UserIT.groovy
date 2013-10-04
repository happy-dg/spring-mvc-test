package org.ksug.springcamp.testmvc.integration.test

import geb.spock.GebReportingSpec
import org.ksug.springcamp.testmvc.integration.page.user.Form
import org.ksug.springcamp.testmvc.integration.page.user.UserList
import spock.lang.Stepwise

@Stepwise
class UserIT extends GebReportingSpec{

    def "user add"() {
        given :
        to Form

        when :
        nameField = "test";
        ageField = "24";
        sexField = "MALE";
        submitButton.click();

        then :
        at UserList

    }
}
