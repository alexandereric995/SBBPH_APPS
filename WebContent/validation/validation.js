function checkUsername(whatYouTyped) {
  var fieldset = whatYouTyped.parentNode; 
  var txt = stringinput.value;
  if (txt.length > 7) {
    fieldset.className = "welldone"; 
  } else {
    fieldset.className = ""; 
  }
}