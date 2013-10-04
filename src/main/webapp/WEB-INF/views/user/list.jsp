<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%@ taglib prefix="layout" tagdir="/WEB-INF/tags/layout" %>
    
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
    

<layout:body>

    <h3 class="title">목록</h3>
    <div class="row-fluid mt20">
    <div class="span12">
        <a class="btn btn-primary pull-right" style="margin-bottom: 5px;" href="<spring:url value="/user/new" />">신규등록</a>
    </div>
    </div>

    <div class="row-fluid mt20">
    <div class="span12">
        <table class="table table-bordered table-hover">
            <colgroup>
                <col style="width: 50px;">
                <col>
                <col style="width: 120px;">
                <col style="width: 120px;">
                <col style="width: 120px;">
            </colgroup>
            <thead>
                <th class="txt_c">No.</th>
                <th class="txt_c">이름</th>
                <th class="txt_c">나이</th>
                <th class="txt_c">성별</th>
                <th class="txt_c">비고</th>
            </thead>
            <tbody>
            <c:forEach items="${users}" var="user" varStatus="status">
                <tr>
                    <td class="txt_c">${status.count}</td>
                    <td>${user.name}</td>
                    <td class="txt_c">${user.age}</td>
                    <td class="txt_c">${user.sex.description}</td>
                    <td class="txt_c">
                        <a class="btn btn-mini" href="<spring:url value="/user/update/${user.id}" />">수정</a>
                        <button class="btn btn-mini delete" data-id="${user.id}" >삭제</button>
                    </td>
                </tr>
            </c:forEach>
            <c:if test="${fn:length(users) lt 1}">
                <tr>
                    <td class="txt_c" colspan="5">등록된 사용자가 없습니다.</td>
                </tr>
            </c:if>
            </tbody>
        </table>

    </div>
    </div>

    <form id="delete-form" method="post">
        <input type="hidden" name="_method" value="DELETE" />
    </form>

    <script type="text/javascript">
        $(function(){
            $(".delete").on('click', function(){
                var id = $(this).data('id');
                $("#delete-form input[name=id]").val(id);
                $("#delete-form").attr({'action':'<spring:url value="/user/" />' + id});
                $("#delete-form").submit();
            });
        });

    </script>

</layout:body>




