
void FUN_ffff0000be022524(code *param_1)

{
  ulong uVar1;
  
  uVar1 = (*param_1)(0x13e000,0x800);
  if ((uVar1 & 1) != 0) {
    return;
  }
  FUN_ffff0000be05fd98(s__Rotfw__log_dump_fail_ffff0000be0a8f9f);
  return;
}


