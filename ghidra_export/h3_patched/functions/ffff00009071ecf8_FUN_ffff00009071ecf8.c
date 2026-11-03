// ffff00009071ecf8  size=44  FUN_ffff00009071ecf8

void FUN_ffff00009071ecf8(undefined8 *param_1)

{
  undefined8 *puVar1;
  
  *param_1 = PTR_LOOP_ffff000090818230;
  param_1[1] = &PTR_LOOP_ffff000090818230;
  puVar1 = (undefined8 *)(PTR_LOOP_ffff000090818230 + 8);
  PTR_LOOP_ffff000090818230 = (undefined *)param_1;
  *puVar1 = param_1;
  return;
}


