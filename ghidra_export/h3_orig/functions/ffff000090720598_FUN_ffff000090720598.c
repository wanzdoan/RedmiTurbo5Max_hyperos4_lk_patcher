// ffff000090720598  size=44  FUN_ffff000090720598

void FUN_ffff000090720598(undefined8 *param_1)

{
  undefined8 *puVar1;
  
  *param_1 = PTR_LOOP_ffff000090818390;
  param_1[1] = &PTR_LOOP_ffff000090818390;
  puVar1 = (undefined8 *)(PTR_LOOP_ffff000090818390 + 8);
  PTR_LOOP_ffff000090818390 = (undefined *)param_1;
  *puVar1 = param_1;
  return;
}


