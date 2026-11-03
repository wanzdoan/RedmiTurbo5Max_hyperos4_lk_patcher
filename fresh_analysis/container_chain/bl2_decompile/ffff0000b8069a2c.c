
void FUN_ffff0000b8069a2c(long param_1,long param_2,ulong param_3)

{
  ulong uVar1;
  
  if (param_3 != 0) {
    uVar1 = 0;
    do {
      *(undefined1 *)(param_1 + uVar1) = *(undefined1 *)(param_2 + uVar1);
      uVar1 = uVar1 + 1;
    } while ((uVar1 & 0xffffffff) < param_3);
  }
  return;
}


