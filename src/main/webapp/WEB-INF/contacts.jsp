<%--
  Created by IntelliJ IDEA.
  User: mrwen
  Date: 4/21/2023
  Time: 5:41 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" import="me.app.contacts.model.Contact" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>


<%--<html>--%>
<%--<head>--%>
<%--    <title>Address Book</title>--%>
<%--&lt;%&ndash;    <meta charset="UTF-8">&ndash;%&gt;--%>
<%--    <meta name="viewport" content="width=device-width, initial-scale=1.0">--%>
<%--    <link rel="stylesheet" href="style.css">--%>
<%--</head>--%>
<%--<body>--%>
<%--<div class="wrapper">--%>

<%--    <!-- Left side contact list -->--%>
<%--    <div class="contacts">--%>
<%--        <h2>Contacts</h2>--%>
<%--        <ul>--%>
<%--            <c:forEach items="${contacts}" var="contact">--%>
<%--                <li><a href="?id=${contact.id}"> ${contact.name} </a></li>--%>
<%--            </c:forEach>--%>
<%--        </ul>--%>
<%--    </div>--%>


<%--</div>--%>
<%--</body>--%>
<%--</html>--%>

<%--<!DOCTYPE html>--%>
<html>

<head>
    <title>Left View with Navigation Bar</title>
    <style>
        /* Styles for left and right view */
        .left-view {
            width: 55%;
            float: left;
            /* color: aqua; */
            background-color: beige;
            margin-top: 1%;
            margin-bottom: 1%;
            margin-left: 2%;
            margin-right: 1%
        }

        .right-view {
            width: 30%;
            float: right;
            color: blueviolet;
        }

        /* Styles for navigation bar */
        .navbar {
            background-color: #f2f2f2;
            padding: 10px;
            height: 100px;
        }

        /* Styles for form elements in navbar */
        .navbar form {
            display: inline-block;
            flex: auto;
            flex-direction: row;
        }

        /* Styles for search input in navbar */
        .navbar .search-input {
            width: 200px;
            padding: 4px;
            margin-right: 10px;
            border-radius: 4px;
            border: none;
        }

        .navbar .search-input-button {
            /* align-items: center; */
            bottom: 0;
            display: flex;
            justify-content: center;
            position: absolute;
            top: 0;
            /* width: 2.5rem; */
        }

        .navbar .search-input-text {
            background-color: #f0f5fb;
            border: .125rem solid transparent;
            border-radius: 4px;
            box-shadow: none;
            color: #00265d;
            display: block;
            font-family: inherit;
            font-size: 1.125rem;
            height: 3.125rem;
            margin: 0 0 .625rem;
            margin-top: 0px;
            margin-right: 0px;
            margin-left: 0px;
            outline-width: 0;
            transition-property: border-color;
            transition-duration: 0.15s, 0.15s;
            transition-timing-function: linear, linear;
            transition-delay: 0s, 0s;
            width: 100%;
            padding-left: 3rem;
            padding-right: 3rem;
        }

        /* Styles for select button in navbar */
        .navbar select {
            padding: 4px;
            margin-right: 10px;
            border-radius: 4px;
            border: none;
        }

        /* Styles for add contact button in navbar */
        .navbar .add-contact-btn {
            background-color: #008CBA;
            color: white;
            padding: 6px 12px;
            border-radius: 4px;
            border: none;
            cursor: pointer;
        }

        /* Clear floats after navbar */
        .clear {
            clear: both;
        }

        .contact-wrapper {
            height: calc(98vh - 160px);
            overflow-y: scroll;
            overflow-x: hidden;
            margin-bottom: 10px;
            flex-wrap: wrap;
        }

        .contact-list {
            display: flex;
            flex-direction: column;
        }

        .contact__table {
            height: 35px;
            width: 100%;
            background-color: #ffffff;
        }

        .contact-list li {
            color: #00265d;
            cursor: pointer;
            padding: .9375rem;
            position: relative;
            width: 100%;
            align-items: center;
            display: flex;
            flex-flow: row nowrap;
            justify-content: space-between;
        }

        .contact-list li:hover {
            background-color: rgba(255, 213, 121, .3);
        }

        .contact-list li::after {
            border-bottom: 1px solid #f0f5fb;
            bottom: 0;
            content: " ";
            left: .9375rem;
            position: absolute;
            right: .9375rem;
        }

        .contact-details {
            align-items: center;
            display: flex;
            /* flex-flow: column wrap; */
            flex-direction: column;
            flex-wrap: wrap;
            justify-content: center;
            /* list-style: none; */
            list-style-position: outside;
            list-style-image: none;
            list-style-type: none;
            /* margin: 0 0 1.25rem; */
            margin-top: 0px;
            margin-right: 0px;
            margin-bottom: 1.25rem;
            margin-left: 0px;
            /* padding: 0; */
            padding-top: 0px;
            padding-right: 0px;
            padding-bottom: 0px;
            padding-left: 0px;
        }

        .contact-details__header {
            border-bottom-color: rgb(240, 245, 251);
            border-bottom-style: solid;
            border-bottom-width: 1px;
            font-size: 1.125rem;
            line-height: 1.625rem;
            padding-bottom: 1.875rem;
            padding-top: .9375rem;
            text-align: center;
            width: 100%;
        }

        .contact-details__section {
            align-items: center;
            border-bottom-color: rgb(240, 245, 251);
            border-bottom-style: solid;
            border-bottom-width: 1px;
            display: flex;
            flex-direction: row;
            flex-wrap: nowrap;
            font-size: 1.125rem;
            line-height: 1.625rem;
            padding-top: 1.25rem;
            padding-right: 0px;
            padding-bottom: 1.25rem;
            padding-left: 0px;
            width: 100%;
        }

        .delete-contact {
            color: #e6176d;
            display: block;
            font-size: 1.125rem;
            font-weight: 700;
            margin-bottom: 1.25rem;
            text-align: center;
        }

        .panel-next {
            display: flex;
            flex-direction: column;
            flex-wrap: nowrap;
        }

        .panel-next__inner {
            box-shadow: 0 0 2px rgba(0, 0, 0, .2);
            display: flex;
            flex-direction: column;
            flex-wrap: nowrap;
            background-color: #fff;
            border-top-left-radius: 4px;
            border-top-right-radius: 4px;
            border-bottom-right-radius: 4px;
            border-bottom-left-radius: 4px;
            min-height: 0;
            overflow-x: auto;
            overflow-y: auto;
            position: relative;
        }

        .panel-next__loader {
            align-items: center;
            background: #FFFFFF;
            border-radius: 4px;
            bottom: 0;
            display: flex;
            justify-content: center;
            left: 0;
            position: absolute;
            right: 0;
            top: 0;
            z-index: 1;
        }
    </style>
</head>

<body color="#f0f5fb">
<div class="left-view">
    <!-- Add navigation bar with form elements -->
    <div class="navbar">
        <h2>All Contacts</h2>
        <form class="form">
            <input type="text" class="search-input" placeholder="Search Contacts">
            <button>search</button>
            <select>
                <!-- <option value="">Sort by:</option> -->
                <option value="name">Name</option>
                <!-- <option value="email">Email</option> -->
                <option value="phone">Phone</option>
                <option value="time">Add Time</option>
            </select>
            <button class=" add-contact-btn">Add Contact</button>
        </form>
    </div>
    <div class="clear"></div>

    <div class="fixed-panel">
        <!-- Add contact list to the left view -->
        <div class="contact-wrapper">
            <!-- <h3>Contact List</h3> -->

            <div class="contact-list">
                <c:forEach items="${contacts}" var="contact">
                    <li class="contact__table"><a href="?id=${contact.id}"> ${contact.name} </a></li>
                </c:forEach>
            </div>
        </div>
    </div>
</div>
<!-- <div class="right-view">
    <h2>Right View</h2>
    <p>This is the content for the right view.</p>
</div> -->
<div class="right-view">

</div>
</body>

</html>
