'use strict';

var welcomeForm = document.querySelector('#welcomeForm');
var dialogueForm = document.querySelector('#dialogueForm');

var btnOn = document.querySelector('#btnOn');
var btnOff = document.querySelector('#btnOff');
var btnReset = document.querySelector('#btnReset');

welcomeForm.addEventListener('submit', connect, true);

btnOn.addEventListener('click', switchOn, true)
btnOff.addEventListener('click', switchOff, true)
btnReset.addEventListener('click', reset, true)

var stompClient = null;
var name = null;


function connect(event) {
	name = document.querySelector('#name').value.trim();

	if (name) {
		document.querySelector('#welcome-page').classList.add('hidden');
		document.querySelector('#dialogue-page').classList.remove('hidden');

		var socket = new SockJS('/garden');
		stompClient = Stomp.over(socket);

		stompClient.connect({}, connectionSuccess);
		stompClient.onclose = function() {
		    console.log('close');
		    stompClient.disconnect();
		};
	}
	event.preventDefault();
}



function connectionSuccess() {
	stompClient.subscribe('/topic/status', onMessageReceived);

	stompClient.send("/app/admin.newUser", {}, JSON.stringify({
		name : "new admin",
		command : 'newLogin'
	}))
}

function switchOn(event) {
	btnOn.classList.add('disabled'); 
	if (stompClient) {
		var device = {
			name : 'on',
			command : 'on'
		};

		stompClient.send("/app/device.instruction", {}, JSON
				.stringify(device));
	}
	event.preventDefault();
}

function switchOff(event) {
	btnOff.classList.add('disabled'); 
	if (stompClient) {
		var device = {
			name : 'off',
			command : 'off'
		};

		stompClient.send("/app/device.instruction", {}, JSON
				.stringify(device));
	}
	event.preventDefault();
}

function reset(event) {
	btnReset.classList.add('disabled'); 
	btnOn.classList.add('disabled');
	btnOff.classList.add('disabled');
	if (stompClient) {
		var device = {
			name : 'reset',
			command : 'reset'
		};

		stompClient.send("/app/device.instruction", {}, JSON
				.stringify(device));
	}
	event.preventDefault();
}

function onMessageReceived(payload) {
	var message = JSON.parse(payload.body);
	var command = message.command;
	command = command.toUpperCase();
	
	if('ON' === command){
		btnOn.classList.add('disabled'); // Disables visually
		btnOff.classList.remove('disabled'); 
		btnReset.classList.remove('disabled');
	} else if ('OFF' === command){
		btnOff.classList.add('disabled'); 
		btnOn.classList.remove('disabled'); 
		btnReset.classList.remove('disabled');
	} else if ('resetting..' !== message.command){
		btnOn.classList.remove('disabled');
		btnOff.classList.remove('disabled');
	}
	
	document.querySelector('#deviceStatus').innerHTML = ">>" + command + "<<";
	console.log(message);
}
