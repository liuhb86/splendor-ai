var webpack = require('webpack');
var path = require('path');

var BUILD_DIR = path.resolve(__dirname, '../public/js/react');
var APP_DIR = path.resolve(__dirname, 'src');

var config = {
  devtool: '#source-map',
  entry: APP_DIR + '/main.jsx',
  output: {
    library: "splendor",
    path: BUILD_DIR,
    filename: 'bundle.js'
  },
  module : {
    loaders : [
      {
        test : /\.jsx?/,
        include : APP_DIR,
        loader : 'babel-loader'
      }
    ]
  }
};

module.exports = config;
