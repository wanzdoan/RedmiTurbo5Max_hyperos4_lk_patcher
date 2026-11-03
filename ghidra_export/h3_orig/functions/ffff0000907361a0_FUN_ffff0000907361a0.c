// ffff0000907361a0  size=24  FUN_ffff0000907361a0

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

ulong FUN_ffff0000907361a0(void)

{
  long lVar1;
  ushort local_14 [2];
  
  if (_DAT_ffff00009086db50 != (undefined8 *)0x0) {
    lVar1 = _DAT_ffff00009086db50[2];
    local_14[0] = 0;
    (*(code *)*_DAT_ffff00009086db50)
              (*(undefined2 *)(lVar1 + 0x1e),local_14,*(undefined2 *)(lVar1 + 0x20),
               *(undefined2 *)(lVar1 + 0x22));
    return (ulong)local_14[0];
  }
  return 0xffffffda;
}


