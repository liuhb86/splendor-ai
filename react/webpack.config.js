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
    rules : [
      {
        test : /\.jsx?/,
        include : APP_DIR,
        use : {
          loader : 'babel-loader',
          options: {
            plugins: ['transform-runtime', "transform-object-rest-spread", 
              "transform-class-properties"],
            presets: ['es2015', 'es2017', 'react'],
          }
        }
      }
    ]
  }
};

module.exports = config;
