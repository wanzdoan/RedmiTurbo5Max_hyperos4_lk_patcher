
void FUN_ffff0000b800ed78(undefined8 *param_1)

{
  undefined8 *puVar1;
  
  *param_1 = PTR_LOOP_ffff0000b80add30;
  param_1[1] = &PTR_LOOP_ffff0000b80add30;
  puVar1 = (undefined8 *)(PTR_LOOP_ffff0000b80add30 + 8);
  PTR_LOOP_ffff0000b80add30 = (undefined *)param_1;
  *puVar1 = param_1;
  return;
}


