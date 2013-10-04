<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%@ taglib prefix="layout" tagdir="/WEB-INF/tags/layout" %>

<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<layout:body>

    <h3 class="method-title"></h3>
    <spring:url value="/user" var="action" />


    <form:form action="${action}" modelAttribute="userForm" method="POST" ENCTYPE="UTF8">
    <c:if test="${!empty userForm.id && userForm.id gt 0}">
        <input type="hidden" name="_method" value="PUT" />
        <input type="hidden" name="id" value="${userForm.id}" />
    </c:if>

        <table class="table table-bordered">
        <colgroup>
            <col width="100px" />
            <col />
        </colgroup>
            <tr>
                <th>이름</th>
                <td>
                        <form:input path="name" cssClass="input-medium" />
                        <form:errors path="name" cssClass="error" />
                </td>
            </tr>
            <tr>
                <th>나이</th>
                <td>
                    <form:input path="age" cssClass="input-medium"/>
                    <form:errors path="age" cssClass="error" />
                </td>
            </tr>
            <tr>
                <th>성별</th>
                <td>
                    <label class="radio inline">
                        <form:radiobutton path="sex" value="MALE" />남
                    </label>
                    <label class="radio inline">
                        <form:radiobutton path="sex" value="FEMALE" /> 여
                    </label>
                    <form:errors path="sex" cssClass="error" />
                </td>
            </tr>
        </table>
        <div class="row-fluid">
            <div class="span12">
                <a class="btn" href="<spring:url value="/user" />">취소</a>
                <button class="btn btn-primary pull-right method-title" type="submit"></button>
            </div>
        </div>
    </form:form>




    <script type="text/javascript">
        $(function() {

            var title = "등록";
            <c:if test="${!empty userForm.id && userForm.id gt 0}"> title="수정";</c:if>
            $(".method-title").text(title);



        });


    </script>
</layout:body>