Feature: Abrir UI buscar casas amarillas

  @test
     Scenario: Ingresar a Google
    Given Abrir Google
    When busco casas amarillas
    And pulso buscar
    Then aparece respuesta
    And Cierro Google