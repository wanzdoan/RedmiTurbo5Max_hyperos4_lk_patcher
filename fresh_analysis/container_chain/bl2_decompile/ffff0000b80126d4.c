
void FUN_ffff0000b80126d4(undefined8 *param_1)

{
  undefined8 *puVar1;
  
  *param_1 = PTR_LOOP_ffff0000b80adf20;
  param_1[1] = &PTR_LOOP_ffff0000b80adf20;
  puVar1 = (undefined8 *)(PTR_LOOP_ffff0000b80adf20 + 8);
  PTR_LOOP_ffff0000b80adf20 = (undefined *)param_1;
  *puVar1 = param_1;
  return;
}


