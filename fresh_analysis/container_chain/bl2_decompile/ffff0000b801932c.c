
void FUN_ffff0000b801932c(code *param_1)

{
  ulong uVar1;
  
  uVar1 = (*param_1)(0x13e000,0x800);
  if ((uVar1 & 1) != 0) {
    return;
  }
  FUN_ffff0000b804c33c(s__Rotfw__log_dump_fail_ffff0000b8089acd);
  return;
}


