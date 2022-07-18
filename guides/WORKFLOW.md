# Documentação do workflow de desenvolvimento do Boti

O workflow basicamente será dividido em 3 tipos de branch: `main`,  `develop`, e `feature/**`.

A branch `main` funcionará como branch de produção, ou seja, o Bot de produção (Maratonou! CF Bot) deverá estar utilizando o código dessa branch.

A branch `develop` deverá funcionar como branch para testes, ou seja, caso uma feature estiver pronta e testada, é feito o merge para essa branch, e assim que estiver pronto para gerar uma `release` faça o merge dessa branch para a `main`.

Branchs do tipo `feature/**` servem para criar features, ou seja é nelas que novas funcionalidades são desenvolvidas e analisadas pelo CI, então em nenhum caso utilize outro formado para a nomeação dessas branchs, de outro modo, não será possível checar se o CI aprovou as mudanças.