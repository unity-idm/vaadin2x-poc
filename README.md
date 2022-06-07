This is a project, which is a proof that it is possible to run many
vaadin 8 and vaadin 23 apps in one jetty server. Project for now run 3 instances of vaadin applications.
Each instance is added to server as jetty web app context. However, Vaadin is not adapted to running many vaadin applications in one server, 
so there are custom upgrade which have to be done to achieve this aim. The most important thing is custom implementation of
CustomResourceProvider, it allows us to decide that what modules should be part of vaadin apps, and also what resources should be part of particular vaadin app.
One of those modules is vaadin-security, if it will be added as one of the processing modules for vaadin application,
it automatically secures all views and adds login view and rest password view. 