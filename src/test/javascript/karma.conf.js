module.exports = function(config){
  config.set({

    basePath : '../../../',

    files : [
      'src/main/webapp/vendor/angular**/**.min.js',
      'src/main/webapp/vendor/angular-mocks/angular-mocks.js',
      'src/main/javascript/**/*.js',
      'src/test/javascript/**/*Spec.js',
      'src/test/javascript/**/!(karma.conf).js'
    ],

    autoWatch : true,

    frameworks: ['jasmine'],

      browsers: ['Chrome', 'Chrome_without_security'], // You may use 'ChromeCanary', 'Chromium' or any other supported browser

      // you can define custom flags
      customLaunchers: {
          Chrome_without_security: {
              base: 'Chrome',
              flags: ['--disable-web-security']
          }
      },

    plugins : [
            'karma-chrome-launcher',
            'karma-firefox-launcher',
            'karma-phantomjs-launcher',
            "karma-safari-launcher",
            'karma-jasmine',
            'karma-junit-reporter'
            ],

    junitReporter : {
      outputFile: 'target/test_out/unit.xml',
        suite: 'src/test/javascript'
    }

  });
};