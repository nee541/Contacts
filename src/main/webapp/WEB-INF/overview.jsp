<%--
  Created by IntelliJ IDEA.
  User: mrwen
  Date: 4/24/2023
  Time: 2:15 AM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" import="me.app.contacts.model.Contact" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<html>
<head>
    <title>Contacts</title>
    <link rel="stylesheet" href="../css/style.css" type="text/css">
    <!-- Include SweetAlert styles -->
    <link rel="stylesheet" href="../css/sweetalert.css">
    <!-- Include SweetAlert JavaScript -->
    <script src="../js/sweetalert.js"></script>
    <style>
        .modal {
            position: fixed;
            z-index: 1;
            left: 0;
            top: 0;
            width: 40%;
            height: 100%;
            overflow: auto;
            background: rgba(104, 205, 142, 0.2);
            display: none;
        }

        /*.modal .formModal {*/

        /*}*/
        #form-container {
            margin: 0 auto;
            max-width: 500px;
        }

        label {
            display: block;
            font-weight: bold;
            margin-top: 20px;
        }

        input[type="text"] {
            width: 100%;
            padding: 10px;
            /*margin-top: 5px;*/
            /*margin-bottom: 15px;*/
            border: none;
            border-bottom: 1px solid #ccc;
            font-size: 16px;

        }

        .add-user-keys {
            display: flex;
            flex-direction: row;
            height: 30px;
        }

        .add-user-keys .other-key-text {
            align-content: center;
            flex: 2;
            margin-top: 8px;
        }

        .key-value {
            display: flex;
            justify-content: space-between;
            align-items: center;
        }

        .key-value input[type="text"] {
            width: 45%;
            margin: 1em;
            flex: auto;
            min-width: fit-content;
        }

        .buttons {
            display: flex;
            flex-direction: row;
            align-items: center;
        }

        .add-remove {
            display: flex;
            justify-content: space-between;
            margin-top: 20px;
        }

        .btn {
            padding: 5px 13px;
            border: none;
            border-radius: 5px;
            color: #fff;
            font-size: 16px;
            cursor: pointer;
            align-self: center;
        }

        .btn-success {
            background-color: #4CAF50;
        }

        .btn-danger {
            background-color: #f44336;
        }

        .icon {
            cursor: pointer;
            font-size: 16px;
            margin-left: 10px;
        }

        .text-center {
            text-align: center;
        }

        .middle {
            position: absolute;
            left: 50%;
            transform: translateX(-50%);
        }
    </style>
    <script type="text/javascript" src="../js/buttons.js"></script>
</head>

<body color="#f0f5fb">
<div class="left-view">
    <!-- Add navigation bar with form elements -->
<%--    <h1>All Contacts ${contactNum}</h1>--%>
    <h1>All Contacts ${contactNum}</h1>
    <div class="navbar">
<%--        <h1>All Contacts ${contactNum}</h1>--%>
<%--        <div class="break"></div>--%>
        <form style="margin-bottom: 0px" action="/contact" method="get">
            <input id="contactSearch" type="text" class="search-input" placeholder="Search Contacts" name="q">
            <button class="search-btn">search</button>
        </form>
        <span style="margin-top: 10px">Sorted by</span>
        <select id="sortBy" onchange="addOrUpdateUrlParam('sort', this.value)">
            <c:choose>
                <c:when test="${param.sort==1}">
                    <option value="1" selected="selected">Add Time</option>
                </c:when>
                <c:otherwise>
                    <option value="1">Add Time</option>
                </c:otherwise>
            </c:choose>
            <c:choose>
                <c:when test="${param.sort==2}">
                    <option value="2" selected="selected">Name</option>
                </c:when>
                <c:otherwise>
                    <option value="2">Name</option>
                </c:otherwise>
            </c:choose>
            <c:choose>
                <c:when test="${param.sort==3}">
                    <option value="3" selected="selected">Phone</option>
                </c:when>
                <c:otherwise>
                    <option value="3">Phone</option>
                </c:otherwise>
            </c:choose>
<%--                <option value="?sort=1">Name</option>--%>
<%--                <!-- <option value="email">Email</option> -->--%>
<%--                <option value="?sort=2">Phone</option>--%>
<%--                <option value="?sort=3">Add Time</option>--%>
        </select>

        <button id="openFormModal" class="add-contact-btn" type="button">
            Add Contact
        </button>
<%--            <div class="add-contact-btn">--%>
<%--                <a href="#popup-form">Add Contact</a>--%>
<%--            </div>--%>
    </div>
    <div class="clear"></div>

    <div id="formModal" class="modal middle">
        <div id="form-container">
            <span id="closeFormModal" class="close-cta">
                <img src="../resources/images/icon_close.svg" alt="">
            </span>
            <h1 style="text-align: center;">Add Contact</h1>
            <form>
                <label>Name:</label>
                <input id="newContactName" type="text">

                <label>Phone Number:</label>
                <input id="newContactPhoneNumber" type="text">

                <div class="add-user-keys">
                    <h4 class="other-key-text">Other Keys:</h4>
                    <select id="newKey">
                        <option value="new" selected="selected">Self-defined</option>
                        <option value="email">Email</option>
                        <option value="address">Address</option>
                    </select>
                    <button type="button" class="btn btn-success" onclick="addKeyValueWhenAdd()">Add</button>
                </div>

                <div id="user-keys">

                </div>

                <div class="buttons">
                    <button type="button" onclick="submitNewContact()" class="btn btn-success text-center middle" style="margin-top: 8em;">Submit</button>
                </div>
            </form>
        </div>
    </div>

    <div id="updateFormModal" class="modal middle">
        <div id="updateFormContainer">
            <span id="updateCloseFormModal" class="close-cta">
                <img src="../resources/images/icon_close.svg" alt="">
            </span>
            <h1 style="text-align: center;">Update Contact</h1>
            <form>
                <label>Name:</label>
                <input id="updateContactName" type="text">

                <label>Phone Number:</label>
                <input id="updateContactPhoneNumber" type="text">

                <div class="add-user-keys">
                    <h4 class="other-key-text">Other Keys:</h4>
                    <select id="updateNewKey">
                        <option value="new" selected="selected">Self-defined</option>
                        <option value="email">Email</option>
                        <option value="address">Address</option>
                    </select>
                    <button type="button" class="btn btn-success" onclick="addKeyValueWhenUpdate()">Add</button>
                </div>

                <div id="updateUserKeys">

                </div>

                <div class="buttons">
                    <button type="button" onclick="sendUpdatedContact(${param.id})" class="btn btn-success text-center middle" style="margin-top: 8em;">Update</button>
                </div>
            </form>
        </div>
    </div>

    <script>
        // Open or Close the popup box
        let formModal = document.getElementById("formModal");
        let openFormModal = document.getElementById("openFormModal");
        let closeFormModal = document.getElementById("closeFormModal");

        openFormModal.onclick = function () {
            console.log("Open form modal");
            formModal.style.display = "block";
        }
        closeFormModal.onclick = function () {
            console.log("Close form modal");
            formModal.style.display = "none";
        }

        let updateFormModal = document.getElementById("updateFormModal")
        let updateCloseFormModal = document.getElementById("updateCloseFormModal");
        updateCloseFormModal.onclick = function () {
            updateFormModal.style.display = "none";
        }
        // when user click anywhere outside of the modal, close it
        // window.onclick = function (event) {
        //     if (event.target == formModal) {
        //         console.log("Close form modal");
        //         formModal.style.display = "none";
        //     }
        // }
    </script>

    <div class="fixed-panel">
        <!-- Add contact list to the left view -->
        <div class="contact-wrapper">
            <!-- <h3>Contact List</h3> -->

            <div class="contact-list">
                <c:forEach items="${contacts}" var="contact">
                    <li class="contact" onclick="addOrUpdateUrlParam('id', ${contact.id})"><a>${contact.name} </a></li>
                </c:forEach>
            </div>
        </div>
    </div>
</div>

<div class="right-view">
    <c:if test="${!empty param.id}">
        <a class="close-cta">
            <img src="../resources/images/icon_close.svg" alt="">
        </a>
        <h2 class="contact-name">${name}</h2>
        <div class="button-container">
<%--            <button class="edit-button" type="submit" onclick="location.href='?id=${param.id}&edit=1'">Edit</button>--%>
<%--            <form class="edit-button" action="https://google.com" style="display: inline">--%>
<%--                <input type="submit" value="Edit" style="display: inline">--%>
<%--            </form>--%>
            <button class="update-button" type="button" onclick="updateContactForm(${param.id})">Update</button>
            <button class="delete-button" type="button" onclick="deleteContact(${param.id})">Delete</button>
        </div>
        <div class="contact-wrapper">
            <div class="key-value-pairs">
                <c:forEach var="infoPair" items="${extraInfo}">
                    <div class="kv-pair">
                        <div class="key">${infoPair.key}</div>
                        <div class="value">${infoPair.value}</div>
                    </div>
                </c:forEach>
                <!-- Add more key-value pairs dynamically as needed -->
            </div>
        </div>
    </c:if>
</div>
</body>
</html>
