
void FUN_ffff0000be01e9c0(undefined8 *param_1)

{
  undefined8 *puVar1;
  
  *param_1 = PTR_LOOP_ffff0000be0d63b0;
  param_1[1] = &PTR_LOOP_ffff0000be0d63b0;
  puVar1 = (undefined8 *)(PTR_LOOP_ffff0000be0d63b0 + 8);
  PTR_LOOP_ffff0000be0d63b0 = (undefined *)param_1;
  *puVar1 = param_1;
  return;
}


