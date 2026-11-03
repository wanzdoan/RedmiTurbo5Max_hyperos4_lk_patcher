// ffff000090736508  size=156  FUN_ffff000090736508

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

ulong FUN_ffff000090736508(uint param_1)

{
  ulong uVar1;
  long lVar2;
  
  if (_DAT_ffff00009086dbe8 == 0) {
    uVar1 = 0xffffffda;
  }
  else {
    lVar2 = *(long *)(_DAT_ffff00009086dbe8 + 0x10);
    if (*(short *)(lVar2 + 0x3c) != 0) {
      (**(code **)(_DAT_ffff00009086dbe8 + 8))
                (*(short *)(lVar2 + 0x3c),0x4729,*(undefined2 *)(lVar2 + 0x3e),
                 *(undefined2 *)(lVar2 + 0x40));
    }
    lVar2 = *(long *)(_DAT_ffff00009086dbe8 + 0x10);
    (**(code **)(_DAT_ffff00009086dbe8 + 8))
              (*(undefined2 *)(lVar2 + 0x36),param_1 & 1,*(undefined2 *)(lVar2 + 0x38),
               *(undefined2 *)(lVar2 + 0x3a));
    lVar2 = *(long *)(_DAT_ffff00009086dbe8 + 0x10);
    uVar1 = (ulong)*(ushort *)(lVar2 + 0x3c);
    if (*(ushort *)(lVar2 + 0x3c) != 0) {
      (**(code **)(_DAT_ffff00009086dbe8 + 8))
                (uVar1,0,*(undefined2 *)(lVar2 + 0x3e),*(undefined2 *)(lVar2 + 0x40));
      uVar1 = 0;
    }
  }
  return uVar1;
}


