<%-- 
    Document   : index
    Created on : 29/11/2021, 2:20:45 a. m.
    Author     : od948
--%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
    </head>
    <body>
        <h1>Hola mundo JPA</h1>
        <form actio="ServletJPA" method="POST">
            <input type="submit" name="accion" value="Listar">
        </form>
        </br>
        <table>
            <c:if test="${lista!=null}">
            <thead>
                <tr>
                    <th>Dni</th>
                    <th>Nombres</th>
                    <th>Direccion</th>
                </tr>
            </thead>
            <tbody>
                <c:forEach var="item" items="${lista}">
                    <tr>
                        <td>${item.getDni()}</td>
                        <td>${item.getNombres()}</td>
                        <td>${item.getDireccion()}</td>
                    </tr>
            </c:forEach>
            </tbody>
        </table>
            </c:if>
        <!--<a href="ServletJPA?accion=Listar">Listar datos Cliente</a>-->
    </body>
</html>
