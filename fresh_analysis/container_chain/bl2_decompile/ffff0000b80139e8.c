
undefined2 FUN_ffff0000b80139e8(void)

{
  int iVar1;
  undefined2 uVar2;
  undefined1 local_18 [4];
  undefined1 local_14 [4];
  
  local_18[0] = 9;
  iVar1 = FUN_ffff0000b8019bc8(10,0x55,local_18,1,1,0);
  if (iVar1 == 0) {
    DAT_ffff0000b811af88 = 0;
    local_14[0] = 8;
    iVar1 = FUN_ffff0000b8019bc8(10,0x55,local_14,1,1,0);
    if (iVar1 == 0) {
      DAT_ffff0000b811af88 = 0;
      uVar2 = CONCAT11(local_18[0],local_14[0]);
      FUN_ffff0000b804c33c
                (s__XMCHG_LK_BQ28Z610___s__vbat___d_ffff0000b808f99a,
                 s_bq28z610_get_vbat_ffff0000b8088c6c,uVar2);
    }
    else {
      FUN_ffff0000b804c33c
                (s__XMCHG_LK_BQ28Z610___s__i2c_read_ffff0000b808f96a,
                 s_bq28z610_read_byte_ffff0000b8086ff8,iVar1);
      uVar2 = 0xed8;
      DAT_ffff0000b811af88 = 1;
    }
  }
  else {
    FUN_ffff0000b804c33c
              (s__XMCHG_LK_BQ28Z610___s__i2c_read_ffff0000b808f96a,
               s_bq28z610_read_byte_ffff0000b8086ff8,iVar1);
    uVar2 = 0xed8;
    DAT_ffff0000b811af88 = 1;
  }
  return uVar2;
}


