
undefined2 FUN_ffff0000b8013840(void)

{
  int iVar1;
  undefined1 local_18 [4];
  undefined1 local_14 [4];
  
  local_14[0] = 0x10;
  iVar1 = FUN_ffff0000b8019bc8(10,0x55,local_14,1,1,0);
  if (iVar1 == 0) {
    local_18[0] = 0x11;
    iVar1 = FUN_ffff0000b8019bc8(10,0x55,local_18,1,1,0);
    if (iVar1 == 0) {
      return CONCAT11(local_18[0],local_14[0]);
    }
  }
  FUN_ffff0000b804c33c
            (s__XMCHG_LK_BQ28Z610___s__i2c_read_ffff0000b808f96a,
             s_bq28z610_read_byte_ffff0000b8086ff8,iVar1);
  return 0x2ee;
}


