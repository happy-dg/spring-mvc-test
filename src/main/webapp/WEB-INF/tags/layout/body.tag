<%@ tag language="java" pageEncoding="UTF-8" %>

<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>


<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="utf-8">
    <title>Spring-MVC-Test</title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta name="description" content="">
    <meta name="author" content="">

    <!-- Le styles -->
    <link href="<spring:url value="/resources/css/bootstrap.css" />" rel="stylesheet">
    <link href="<spring:url value="/resources/css/default.css" />" rel="stylesheet">

    <script src="<spring:url value="/resources/js/jquery-2.0.3.min.js" />"></script>
    <script src="<spring:url value="/resources/js/jquery.tmpl.min.js" />"></script>

    <script src="<spring:url value="/resources/js/bootstrap.min.js" />"></script>

</head>
<body>


<div class="navbar navbar-inverse navbar-fixed-top">
    <div class="navbar-inner">
        <div class="container">
            <button type="button" class="btn btn-navbar" data-toggle="collapse" data-target=".nav-collapse">
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
            </button>
            <a class="brand" href="#">
                Spring MVC TEST
            </a>
            <div class="nav-collapse collapse">
                <ul class="nav">
                    <li class="active"><a href="#">Home</a></li>
                </ul>
            </div><!--/.nav-collapse -->
        </div>
    </div>
</div>

<div class="container-fluid">
    <div class="row-fluid">
        <div class="span2">


        </div>

        <div class="span8">

            <div id="message-holder">
                <c:if test="${!empty feedbackMessage}">
                    <div class="messageblock hidden">${feedbackMessage}</div>
                </c:if>
                <c:if test="${!empty errorMessage}">
                    <div class="errorblock hidden">${errorMessage}</div>
                </c:if>
            </div>

            <div id="view-holder">

                <jsp:doBody />

            </div>

        </div>

        <div class="span2">


        </div>
    </div>


</div> <!-- /container -->


<script id="template-alert-message" type="text/x-jquery-tmpl">
    <div id="alert-message" class="alert alert-success fade in">
        <a class="close" data-dismiss="alert">&times;</a>
        {{= message}}
    </div>
</script>
<script id="template-alert-message-error" type="text/x-jquery-tmpl">
    <div id="alert-message-error" class="alert alert-error fade in">
        <a class="close" data-dismiss="alert">&times;</a>
        {{= message}}
    </div>
</script>

<script type="text/javascript">
$(function(){

    $("span.error").removeClass('error')
            .addClass('help-inline')
            .closest('td')
            .addClass('control-group')
            .addClass('error');

    $("input, select").on('click focus', function(){
        var parent = $(this).closest('td');
        if( parent.hasClass('control-group') ) {
            parent.removeClass('control-group').removeClass('error').find('.help-inline').remove();
        }
    });


    var feedbackmessage = $(".messageblock");
    if( feedbackmessage.length > 0 )
        Message.addMessage(feedbackmessage.text());

    var errormessage = $(".errorblock");
    if( errormessage.length > 0 )
        Message.addMessageError(errormessage.text());
});

    var Message = {
        addMessage : function (message) {
            var alertTemplate = $("#template-alert-message").tmpl({'message':message});
            $("#message-holder").html(alertTemplate);
            $("#alert-message").alert().delay(5000).fadeOut("fast", function(){$(this).remove();});
        },
        addMessageError : function (message) {
            var alertTemplate = $("#template-alert-message-error").tmpl({'message':message});
            $("#message-holder").html(alertTemplate);
            $("#alert-message-error").alert().delay(5000).fadeOut("fast", function(){$(this).remove();});
        }
    };
</script>
</body>
</html>