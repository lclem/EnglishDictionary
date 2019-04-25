var ANIMATION_DURATION = 400;
var oldFocus = null;

$(document).ready (function() {
	//addAudioButtons();
	listenForClicksOnUnfocusedLinks();
});

function addAudioButtons () {
	$('audio').each(function(i) {
		$(this).after(
			"<a href='hmaudio://" + 
			$(this).attr('word') + "/" + 
			$(this).attr('translit') + 
         "' class='audio_link'><img src='SoundButton.png'/></a>");
	});
}

function listenForClicksOnUnfocusedLinks() {
	$('A').click(handleClickOnUnfocusedLink);
	$('x').click(handleClickOnUnfocusedLink);
}

function handleClickOnUnfocusedLink(event) {
	if (oldFocus) unfocusElement(oldFocus);
	if (!$(this).hasClass('audio_link')) {
		focusElement(this);
		listenForNextClickOnUnlinkedSpace(handleClickOnUnlinkedSpace);
		event.stopPropagation();
	}
}

function unfocusElement(element) {
	animateToUnfocusedState(element);
	removeFocusedLinkListener(element);
	replaceUnfocusedLinkClickHandlerFor(element);
}

function focusElement(element) {
	animateToFocusedState(element);
	oldFocus = element;
	listenForNextClickOnFocusedElement(element, handleClickOnFocusedElement);
	removeUnfocusedLinkHandlerFor(element);
}

function removeUnfocusedLinkHandlerFor(element) {
	$(element).unbind('click', handleClickOnUnfocusedLink);
}

function replaceUnfocusedLinkClickHandlerFor(element) {
	$(element).click(handleClickOnUnfocusedLink);
}

function listenForNextClickOnFocusedElement(focus, fn) {
	$(focus).click(fn);
}

function handleClickOnFocusedElement(event) {
	if (elementHasRefURL(this)) {
		window.location.href = refURLFor(this);
	}
	else {
		window.location.href = wordURLFor(this);
	}
	event.stopPropagation();
}

function refURLFor(element) {
	var ancestor = $(element).parent();
	if (ancestor.is('x')) ancestor = ancestor.parent();	
	return "hmoawtlink://refurl/" + ancestor.attr('refurl');
}

function wordURLFor(element) {
	return "hmoawtlink://word/" + $(element).html();
	//return $(element).html();
}

function elementHasRefURL(element) {
	var ancestor = $(element).parent();
	if (ancestor.is('x')) ancestor = ancestor.parent();
	if (ancestor.attr('refurl') && !(ancestor.attr('refurl') == '0') && !(ancestor.attr('refurl') == 'x0')) {
		return true;
	}
	else {
		return false;
	}
}

function animateToFocusedState (element) {
	$(element).animate({fontSize: '200%'}, {queue: false, duration: ANIMATION_DURATION});
	$(element).css('border-bottom-style', 'solid');
}

function animateToUnfocusedState (element) {
	$(element).animate({fontSize: '100%'}, {queue: false, duration: ANIMATION_DURATION});
	$(element).css('border-bottom-style', 'dotted');
}

function listenForNextClickOnUnlinkedSpace(fn) {
	$('dictionary').one('click', fn);
}

function handleClickOnUnlinkedSpace(event) {
	disableLinkMode();
	oldFocus = null;
}

function disableLinkMode() {
	unfocusElement(oldFocus);
}

function removeFocusedLinkListener(element) {
	$(element).unbind('click', handleClickOnFocusedElement);
}

function showDictionary() {
   $('body').addClass('dictionary_shown');
   $('body').removeClass('thesaurus_shown');
}

function showThesaurus() {
   $('body').addClass('thesaurus_shown');
   $('body').removeClass('dictionary_shown');
}
