Feature: Belly

@tag


  Scenario: a few cukes 
  Given I have 42 cukes in my belly 
  When I wait 1 hour 
  And I wait another 30 mins
  Then my belly should growl
