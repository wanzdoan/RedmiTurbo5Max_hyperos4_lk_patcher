// ffff0000907366e8  size=88  FUN_ffff0000907366e8

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

undefined1 FUN_ffff0000907366e8(void)

{
  long lVar1;
  undefined2 local_14 [2];
  
  local_14[0] = 0;
  if (_DAT_ffff00009086dbe8 == (undefined8 *)0x0) {
    local_14[0]._0_1_ = 0xda;
  }
  else {
    lVar1 = _DAT_ffff00009086dbe8[2];
    (*(code *)*_DAT_ffff00009086dbe8)
              (*(undefined2 *)(lVar1 + 0x48),local_14,*(undefined2 *)(lVar1 + 0x4a),
               *(undefined2 *)(lVar1 + 0x4c));
  }
  return (undefined1)local_14[0];
}


