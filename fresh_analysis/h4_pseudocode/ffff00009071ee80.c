
void FUN_ffff00009071ee80(undefined8 *param_1)

{
  undefined8 *puVar1;
  
  *param_1 = PTR_LOOP_ffff000090818240;
  param_1[1] = &PTR_LOOP_ffff000090818240;
  puVar1 = (undefined8 *)(PTR_LOOP_ffff000090818240 + 8);
  PTR_LOOP_ffff000090818240 = (undefined *)param_1;
  *puVar1 = param_1;
  return;
}


