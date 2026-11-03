// ffff00009075c3a4  size=64  FUN_ffff00009075c3a4

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

void FUN_ffff00009075c3a4(long param_1)

{
  long lVar1;
  long lVar2;
  
  _DAT_ffff00009087be90 = *(undefined4 *)(param_1 + 8);
  lVar2 = 0;
  do {
    lVar1 = lVar2 + 1;
    *(undefined1 *)(lVar2 + -0xffff6f78416c) = *(undefined1 *)(param_1 + 0xc + lVar2);
    lVar2 = lVar1;
  } while (lVar1 != 0x80);
  return;
}


