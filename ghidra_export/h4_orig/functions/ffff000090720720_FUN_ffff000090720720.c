// ffff000090720720  size=44  FUN_ffff000090720720

void FUN_ffff000090720720(undefined8 *param_1)

{
  undefined8 *puVar1;
  
  *param_1 = PTR_LOOP_ffff0000908183a0;
  param_1[1] = &PTR_LOOP_ffff0000908183a0;
  puVar1 = (undefined8 *)(PTR_LOOP_ffff0000908183a0 + 8);
  PTR_LOOP_ffff0000908183a0 = (undefined *)param_1;
  *puVar1 = param_1;
  return;
}


