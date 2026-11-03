
undefined1 FUN_ffff0000b8020398(void)

{
  long lVar1;
  undefined2 local_14 [2];
  
  local_14[0] = 0;
  if (DAT_ffff0000b811b688 == (undefined8 *)0x0) {
    local_14[0]._0_1_ = 0xda;
  }
  else {
    lVar1 = DAT_ffff0000b811b688[2];
    (*(code *)*DAT_ffff0000b811b688)
              (*(undefined2 *)(lVar1 + 0x48),local_14,*(undefined2 *)(lVar1 + 0x4a),
               *(undefined2 *)(lVar1 + 0x4c));
  }
  return (undefined1)local_14[0];
}


