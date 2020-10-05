
var exec = require('cordova/exec');

var PLUGIN_NAME = 'MiPlugin';

var MiPlugin = {
  mercadopago: function (name, successCallback, errorCallback){
        exec(successCallback, errorCallback, PLUGIN_NAME, "pagar", [name]);
  }
};

module.exports = MiPlugin;
