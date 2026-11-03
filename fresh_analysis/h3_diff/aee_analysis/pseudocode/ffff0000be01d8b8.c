
void FUN_ffff0000be01d8b8(undefined8 *param_1)

{
  undefined8 *puVar1;
  
  *param_1 = PTR_LOOP_ffff0000be0d6250;
  param_1[1] = &PTR_LOOP_ffff0000be0d6250;
  puVar1 = (undefined8 *)(PTR_LOOP_ffff0000be0d6250 + 8);
  PTR_LOOP_ffff0000be0d6250 = (undefined *)param_1;
  *puVar1 = param_1;
  return;
}


