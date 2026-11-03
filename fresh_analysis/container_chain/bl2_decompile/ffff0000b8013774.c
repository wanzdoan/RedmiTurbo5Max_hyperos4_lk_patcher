
uint FUN_ffff0000b8013774(void)

{
  uint uVar1;
  int iVar2;
  undefined1 local_8 [4];
  undefined1 local_4 [4];
  
  local_8[0] = 7;
  iVar2 = FUN_ffff0000b8019bc8(10,0x55,local_8,1,1,0);
  if (iVar2 == 0) {
    local_4[0] = 6;
    iVar2 = FUN_ffff0000b8019bc8(10,0x55,local_4,1,1,0);
    if (iVar2 == 0) {
      uVar1 = CONCAT11(local_8[0],local_4[0]) / 10 - 0x111;
      FUN_ffff0000b804c33c
                (s__XMCHG_LK_BQ28Z610___s__temp___d_ffff0000b808ceb2,
                 s_bq28z610_get_tbat_ffff0000b8088c5a,uVar1 & 0xffff);
      return uVar1;
    }
  }
  FUN_ffff0000b804c33c
            (s__XMCHG_LK_BQ28Z610___s__i2c_read_ffff0000b808f96a,
             s_bq28z610_read_byte_ffff0000b8086ff8,iVar2);
  return 0x19;
}


