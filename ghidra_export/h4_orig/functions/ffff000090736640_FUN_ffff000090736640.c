// ffff000090736640  size=88  FUN_ffff000090736640

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

undefined8 FUN_ffff000090736640(void)

{
  undefined8 uVar1;
  long lVar2;
  
  if (_DAT_ffff00009086dbe8 == 0) {
    uVar1 = 0xffffffda;
  }
  else {
    lVar2 = *(long *)(_DAT_ffff00009086dbe8 + 0x10);
    if (*(short *)(lVar2 + 0x12) == 0) {
      uVar1 = 0xffffffe8;
    }
    else {
      (**(code **)(_DAT_ffff00009086dbe8 + 8))
                (*(short *)(lVar2 + 0x12),1,*(undefined2 *)(lVar2 + 0x14),
                 *(undefined2 *)(lVar2 + 0x16));
      uVar1 = 0;
    }
  }
  return uVar1;
}


