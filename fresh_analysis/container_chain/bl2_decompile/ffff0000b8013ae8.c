
int FUN_ffff0000b8013ae8(void)

{
  int iVar1;
  byte local_8 [4];
  byte local_4 [4];
  
  local_8[0] = 0xd;
  iVar1 = FUN_ffff0000b8019bc8(10,0x55,local_8,1,1,0);
  if (iVar1 == 0) {
    local_4[0] = 0xc;
    iVar1 = FUN_ffff0000b8019bc8(10,0x55,local_4,1,1,0);
    if (iVar1 == 0) {
      iVar1 = (uint)local_8[0] * -0x100 - (uint)local_4[0];
      FUN_ffff0000b804c33c
                (s__XMCHG_LK_BQ28Z610___s__ibat___d_ffff0000b809a7d8,
                 s_bq28z610_get_ibat_ffff0000b809d558,(int)(short)iVar1);
      return iVar1;
    }
  }
  FUN_ffff0000b804c33c
            (s__XMCHG_LK_BQ28Z610___s__i2c_read_ffff0000b808f96a,
             s_bq28z610_read_byte_ffff0000b8086ff8,iVar1);
  return 0;
}


