# Exp8 Ansible Roles Modules

Ansible roles and modules

## Questions

- **Q25** `Q25_Ansible_Roles/` - Install Ansible on a Linux system and create three Ansible roles named prerequisites, PostgreSQL, and apache to install Git, PostgreSQL, and the Apache web server, respectively. Define the required tasks in each role, create a stack.yml playbook to execute all three roles on localhost, and verify the successful installation of the required packages.

- **Q26** `Q26_Ansible_Modules/` - Create an Ansible playbook for a simple application and use modules in Ansible.


Each question folder = ready project + `*_ALL_IN_ONE.md` (question, aim, procedure, all code, xml/yml, commands, output).


## Viva-voce

- **What is a role?** Reusable unit of tasks/handlers/vars/templates with fixed folder structure.
- **ansible-galaxy init?** Creates role skeleton (tasks, handlers, vars, defaults, meta, files, templates).
- **Module?** Unit of work Ansible runs (apt, copy, service, file, user, uri ...).
- **command vs shell module?** shell supports pipes/redirects; command does not.
- **Ad-hoc command?** One-off task: ansible all -m ping.
