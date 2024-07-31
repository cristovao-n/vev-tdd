Deve-se implementar um sistema de venda de ingressos para shows.

Um show tem data, artista, cachê, total de despesas de infraestrutura, lotes de ingressos e se o show será realizado em data especial ou não. De 20 a 30% dos ingressos devem ser do tipo VIP, 10% dos ingressos devem ser reservados para MEIA_ENTRADA, enquanto o restante é NORMAL. Um ingresso contém id, tipo e status. O status pode ser vendido ou não. Um ingresso VIP deve custar o dobro de um ingresso NORMAL e um ingresso MEIA_ENTRADA deve custar a metade do NORMAL.

Deve ser possível marcar um ingresso como vendido ou não.

Ingressos são vendidos em lotes, cada lote tem um id, um conjunto de ingressos e um desconto aplicável. Assim, um lote pode oferecer ou não um desconto em ingressos, sendo o desconto máximo permitido de 25%. O desconto só é válido para ingressos VIP e NORMAL.

Deve ser possível gerar um relatório do show. O relatório contém o número de ingressos vendidos de cada tipo, a receita líquida e o status financeiro do show. A receita líquida do show é calculada como sendo a soma dos valores de todos os ingressos vendidos, considerando os descontos aplicáveis, deduzidas as despesas de infraestrutura e o cachê do artista. Se o show for realizado em data especial, as despesas de infraestrutura têm um valor adicional de 15%. Por fim, o status financeiro é LUCRO quando a receita líquida é positiva, ESTÁVEL se for zero, e PREJUÍZO se a receita for negativa.

Exemplo: um show em data especial com um artista com cachê de R$ 1.000,00, custos de infraestrutura de R$ 2.000,00 e um lote de ingressos à venda com 20% dos ingressos sendo VIP, e o preço do ingresso NORMAL é R$ 10,00. O lote tem 500 ingressos à venda com 15% de desconto, dos quais todos foram vendidos. Assim, a receita dos ingressos corresponde a R$ 4.925,00. Os custos correspondem a R$ 3.300,00. Portanto, o status financeiro do show é LUCRO, com lucro de R$ 1.625,00.
