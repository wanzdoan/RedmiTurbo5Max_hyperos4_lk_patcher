// ffff0000907363a8  size=88  FUN_ffff0000907363a8

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

undefined8 FUN_ffff0000907363a8(void)

{
  undefined8 uVar1;
  long lVar2;
  
  if (_DAT_ffff00009086db50 == 0) {
    uVar1 = 0xffffffda;
  }
  else {
    lVar2 = *(long *)(_DAT_ffff00009086db50 + 0x10);
    if (*(short *)(lVar2 + 0x12) == 0) {
      uVar1 = 0xffffffe8;
    }
    else {
      (**(code **)(_DAT_ffff00009086db50 + 8))
                (*(short *)(lVar2 + 0x12),1,*(undefined2 *)(lVar2 + 0x14),
                 *(undefined2 *)(lVar2 + 0x16));
      uVar1 = 0;
    }
  }
  return uVar1;
}


