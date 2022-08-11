# WebCrawling

###Compilação e execução
A partir do diretório raiz do projeto, os seguintes comandos, executados em sequência, devem fazer a
compilação e execução da aplicação:

    docker build . -t axreng/backend
    docker run -e BASE_URL=http://hiring.axreng.com/ -e KEYWORD=four --rm axreng/backend

###Funcionamento
A interação do usuário com a aplicação é limitada ao fornecimento de argumentos de execução por
meio de variáveis de ambiente pela linha de comando (ver seções Variáveis de ambiente e Compilação
e execução, abaixo). As variáveis de ambiente fornecidas determinam a URL base do website em que a
busca deve ser feita, o termo a ser buscado e, opcionalmente, o número máximo de resultados que
devem ser obtidos antes da busca ser interrompida.

A busca deve iniciar pela URL base e seguir links (absolutos e relativos) em elementos anchor das
páginas visitadas se e somente se eles possuírem a mesma URL base que é especificada para a
inicialização da aplicação. Exemplo: se a URL base foi definida como http://ex.com/site/, um link
para http://ex.com/site/pagina.html deve ser visitado; já um link para http://ex.com/blog/ não
deve ser visitado.

Uma URL visitada é considerada um resultado da busca se e somente se sua página contiver o termo
buscado, de forma case-insensitive, em qualquer parte do conteúdo HTML (dentro ou fora de tags). Os
resultados encontrados devem ser escritos no standard output, com uma única URL absoluta por linha,
de acordo com o formato abaixo (onde o texto URL deve ser substituído pela URL do resultado). A
ordem de apresentação dos resultados é irrelevante, mas não deve haver repetições.

    Result found: URL

Cada linha de resultado deve iniciar com o texto “Result found:“. Linhas escritas no standard output
fora desse padrão serão ignoradas (o que permite logs de processamento, por exemplo).
Pode-se assumir (sem validações específicas por parte da aplicação) que todas as páginas que devem
ser visitadas são formadas por conteúdo HTML válido, e que esse conteúdo não será modificado no
servidor que hospeda o website durante a execução da aplicação.

###Variáveis de ambiente
● **BASE_URL**: Definição obrigatória. Determina a URL base do website em que a busca deve ser
feita pela aplicação. Restrições: o valor deve conter uma URL (HTTP ou HTTPS) válida e
absoluta de acordo com a implementação da classe java.net.URI.

● **KEYWORD**: Definição obrigatória. Determina o termo (sequência de caracteres) a ser buscado
no conteúdo das páginas visitadas pela aplicação. Restrições: o valor deve conter no mínimo 4
e no máximo 32 caracteres, e deve conter apenas caracteres alfanuméricos.

● **MAX_RESULTS**: Definição opcional. Determina o número máximo de resultados que devem ser
retornados por uma busca. Restrições: O valor deve representar um número inteiro igual a -1
(indicando limite não definido, ou seja, busca ilimitada) ou maior do que 0. Quando o valor não é
especificado, ou é especificado um valor inválido, a aplicação deve assumir o valor default -1.
Quando uma busca atinge o limite de resultados, ela deve ser concluída, deixando de visitar
novos links encontrados. Buscas com limite máximo definido podem retornar resultados
diferentes de acordo com a estratégia adotada para navegação entre links; diferenças desse
tipo são aceitáveis e não prejudicarão sua avaliação
