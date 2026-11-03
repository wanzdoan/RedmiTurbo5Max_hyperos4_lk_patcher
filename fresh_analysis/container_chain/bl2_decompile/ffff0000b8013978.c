
ulong FUN_ffff0000b8013978(void)

{
  uint uVar1;
  uint uVar2;
  uint uVar3;
  
  uVar2 = FUN_ffff0000b8013840();
  uVar3 = FUN_ffff0000b80138dc();
  uVar1 = 0;
  if ((uVar3 & 0xffff) != 0) {
    uVar1 = ((uVar2 & 0xffff) * 10000) / (uVar3 & 0xffff);
  }
  FUN_ffff0000b804c33c
            (s__XMCHG_LK_BQ28Z610___s__raw_soc__ffff0000b8092425,
             s_bq28z610_get_batt_ssoc_ffff0000b809c2a6);
  return (ulong)(uVar1 + 0x61) / 0x62;
}


