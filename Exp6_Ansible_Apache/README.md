# Exp6 Ansible Apache

Ansible playbook: Apache web server + web page

## Questions

- **Q19** `Q19_Apache_WelcomePage/` - Create an Ansible playbook to install the Apache web server on a Linux system, start and enable the service, deploy a simple HTML web page displaying a welcome message, and verify the application's availability using an HTTP request.

- **Q20** `Q20_WebApp_Infrastructure/` - Create an Ansible playbook to configure a simple web application infrastructure on a Linux system by installing the Apache web server, starting and enabling the service, deploying an HTML page containing a heading and a description, and verifying that the page is accessible through http://localhost.


Each question folder = ready project + `*_ALL_IN_ONE.md` (question, aim, procedure, all code, xml/yml, commands, output).


## Viva-voce

- **What is Ansible?** Agentless configuration management tool using SSH and YAML playbooks.
- **Playbook / play / task?** Playbook = YAML file of plays; play maps hosts to tasks; task calls a module.
- **Inventory?** List of managed hosts (hosts.ini).
- **become: true?** Run tasks with sudo (root).
- **Idempotency?** Running again gives same result; no change if already in desired state.
