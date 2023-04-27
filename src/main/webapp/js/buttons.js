function addOrUpdateUrlParam(name, value)
{
    var href = window.location.href;
    var regex = new RegExp("[&\\?]" + name + "=");
    if(regex.test(href))
    {
        regex = new RegExp("([&\\?])" + name + "=\\d+");
        window.location.href = href.replace(regex, "$1" + name + "=" + value);
    }
    else
    {
        if(href.indexOf("?") > -1)
            window.location.href = href + "&" + name + "=" + value;
        else
            window.location.href = href + "?" + name + "=" + value;
    }
}

function searchContact() {
    var searchTerm = document.getElementById("contactSearch").value;

}

function addKeyValue(containerId, keyId) {
    var userKeysContainer = document.getElementById(containerId);

    // Create new key-value pair elements
    var selectedKey = document.getElementById(keyId);

    var keyInput = document.createElement("input");
    keyInput.type = "text";
    console.log(selectedKey.value)
    if (selectedKey.value !== "new") {
        keyInput.value = selectedKey.options[selectedKey.selectedIndex].text;
    } else {
        keyInput.placeholder = "Key";
    }

    var valueInput = document.createElement("input");
    valueInput.type = "text";
    valueInput.placeholder = "Value";

    var removeIcon = document.createElement("span");
    removeIcon.classList.add("icon");
    removeIcon.innerHTML = "&#x2715"; // X symbol to indicate removal

    var keyValue = document.createElement("div");
    keyValue.classList.add("key-value");
    keyValue.appendChild(keyInput);
    keyValue.appendChild(valueInput);
    keyValue.appendChild(removeIcon);

    // Add new key-value pair to the form
    userKeysContainer.appendChild(keyValue);

    // Attach click event to the remove icon
    removeIcon.addEventListener("click", function () {
        userKeysContainer.removeChild(keyValue);
    });
}

function addKeyValueWhenAdd() {
    addKeyValue("user-keys", "newKey");
}

function addKeyValueWhenUpdate() {
    addKeyValue("updateUserKeys", "updateNewKey");
}

function submitNewContact() {
    let url = "/contact?action=add";
    let nameInput = document.getElementById("newContactName");
    if (!nameInput.value) {
        swal("Error", "Please fill all the blanket", "error");
    }
    let phoneNumberInput = document.getElementById("newContactPhoneNumber");
    if (!phoneNumberInput.value) {
        swal("Error", "Please fill all the blanket", "error");
    }
    let data = {
        "name": nameInput.value,
        "phoneNumber": phoneNumberInput.value
    };
    let extraInfo = {};
    let userKeyValues = document.querySelectorAll("#user-keys .key-value")
    for (const keyValue of userKeyValues) {
        let keyInput = keyValue.childNodes[0];
        let valueInput = keyValue.childNodes[1];
        if (!keyInput.value || !valueInput.value) {
            swal("Error", "Please fill all the blanket", "error");
        }
        extraInfo[keyInput.value] = valueInput.value;
    }
    data['extraInfo'] = extraInfo;

    console.log(JSON.stringify(data));
    // swal("Success!", "You have successfully add a new contact!", "success");

    fetch(url, {
        method: "PUT",
        body: JSON.stringify(data),
        headers: {"Content-Type": "application/json"}
    }).then(response => {
        if(!response.ok) {
            throw Error(response.statusText);
        }
        swal("Success!", "You have successfully add a new contact!", "success");
    }).catch(error => {
        swal("Error!", "Error Code: ", "error");
    })
}

function updateContactForm(contactId) {
    let getUrl = `/contact?type=json&id=${contactId}`;
    fetch(getUrl)
        .then(response => {
            response.json().then(data => {
                // display modal
                let updateFormModal = document.getElementById("updateFormModal");
                updateFormModal.style.display = "block";
                // set initial value
                // name
                let updateContactName = document.getElementById("updateContactName");
                updateContactName.value = data.name;
                // phone number
                let updateContactPhoneNumber = document.getElementById("updateContactPhoneNumber");
                updateContactPhoneNumber.value = data.phoneNumber;
                // added keys
                let keyValueContainer = document.getElementById("updateUserKeys");
                for (const [key, value] of Object.entries(data.extraInfo)) {
                    let keyInput = document.createElement("input");
                    keyInput.type = "text";
                    keyInput.value = key;
                    let valueInput = document.createElement("input");
                    valueInput.type = "text";
                    valueInput.value = value;
                    var removeIcon = document.createElement("span");
                    removeIcon.classList.add("icon");
                    removeIcon.innerHTML = "&#x2715"; // X symbol to indicate removal

                    var keyValue = document.createElement("div");
                    keyValue.classList.add("key-value");
                    keyValue.appendChild(keyInput);
                    keyValue.appendChild(valueInput);
                    keyValue.appendChild(removeIcon);

                    // Add new key-value pair to the form
                    keyValueContainer.appendChild(keyValue);

                    // Attach click event to the remove icon
                    removeIcon.addEventListener("click", function () {
                        keyValueContainer.removeChild(keyValue);
                    });
                }
            })
        })
        .catch(error => {
            swal("Error", `Error when fetch contact ${contactId}`, "error");
        })
}

function sendUpdatedContact(id) {
    let url = `/contact?action=update&id=${id}`;
    let nameInput = document.getElementById("updateContactName");
    if (!nameInput.value) {
        swal("Error", "Please fill all the blanket", "error");
    }
    let phoneNumberInput = document.getElementById("updateContactPhoneNumber");
    if (!phoneNumberInput.value) {
        swal("Error", "Please fill all the blanket", "error");
    }
    let data = {
        "name": nameInput.value,
        "phoneNumber": phoneNumberInput.value
    };
    let extraInfo = {};
    let userKeyValues = document.querySelectorAll("#updateUserkeys .key-value");
    for (const keyValue of userKeyValues) {
        let keyInput = keyValue.childNodes[0];
        let valueInput = keyValue.childNodes[1];
        if (!keyInput.value || !valueInput.value) {
            swal("Error", "Please fill all the blanket", "error");
        }
        extraInfo[keyInput.value] = valueInput.value;
    }
    data['extraInfo'] = extraInfo;

    console.log(JSON.stringify(data));
    // swal("Success!", "You have successfully add a new contact!", "success");

    fetch(url, {
        method: "PUT",
        body: JSON.stringify(data),
        headers: {"Content-Type": "application/json"}
    }).then(response => {
        if(!response.ok) {
            throw Error(response.statusText);
        }
        swal("Success!", "You have successfully update contact!", "success");
    }).catch(error => {
        swal("Error!", `Some error occurs`, "error");
    })
}

function deleteContact(id) {
    const url = `/contact?action=delete&id=${id}`;
    fetch(url, {
        method: "PUT"
    }).then(response => {
        if(!response.ok) {
            throw Error(response.statusText);
        }
        swal("Success!", "You have successfully delete contact!", "success");
    }).catch(error => {
        swal("Error!", `Some error occurs`, "error");
    })
}